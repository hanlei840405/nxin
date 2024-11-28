package com.nxin.framework.service.kettle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.job.JobConvertChain;
import com.nxin.framework.converter.kettle.job.JobConvertFactory;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.converter.kettle.transform.TransformConvertChain;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KettleGeneratorService {
    @Value("${setting.trans.records-size}")
    private int recordsSize;
    @Value("${setting.trans.row-size}")
    private int rowSize;
    @Value("${setting.trans.feedback}")
    private boolean feedback;
    @Value("${setting.trans.thread-manage-priority}")
    private boolean threadManagePriority;
    @Value("${etl.log.datasource.name}")
    private String etlLogDatasourceName;
    @Value("${etl.log.datasource.type}")
    private String etlLogDatasourceType;
    @Value("${etl.log.datasource.access}")
    private String etlLogDatasourceAccess;
    @Value("${etl.log.datasource.host}")
    private String etlLogDatasourceHost;
    @Value("${etl.log.datasource.port}")
    private String etlLogDatasourcePort;
    @Value("${etl.log.datasource.schema}")
    private String etlLogDatasourceSchema;
    @Value("${etl.log.datasource.username}")
    private String etlLogDatasourceUsername;
    @Value("${etl.log.datasource.password}")
    private String etlLogDatasourcePassword;
    @Value("${dev.dir}")
    private String devDir;
    @Value("${attachment.dir}")
    private String attachmentDir;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShellService shellService;

    public Map<String, Object> getTransMeta(Shell shell, boolean prod) {
        String content = ZipUtils.unCompress(shell.getContent()), name = shell.getName();
        try {
            StringBuilder builder = new StringBuilder(attachmentDir);
//            String env = prod ? Constant.ENV_PRODUCTION : Constant.ENV_DEV;
//            builder.append(shell.getProjectId()).append(File.separator).append(shell.getParentId()).append(File.separator).append(shell.getId()).append(File.separator).append(env).append(File.separator);
            builder.append(shell.getProjectId()).append(File.separator).append(shell.getParentId()).append(File.separator).append(shell.getId()).append(File.separator); // 转换中如果有需要存储上传文件的目录 todo 是否需要考虑上传文件
//            fileService.createFolder(env, builder.toString());
            ConvertFactory.getVariable().put(Constant.VAR_ATTACHMENT_DIR, builder.toString());
            Document document = XMLHandler.loadXMLString(content);
            mxCodec codec = new mxCodec();
            mxGraph graph = new mxGraph();
            codec.decode(document.getDocumentElement(), graph.getModel());
            Object[] elements = graph.getChildCells(graph.getDefaultParent());
            TransformConvertChain transformConvertChain = TransformConvertFactory.getInstance();
            TransMeta transMeta = new TransMeta();
            transMeta.setFeedbackSize(rowSize);
            transMeta.setFeedbackShown(feedback);
            transMeta.setSizeRowset(recordsSize);
            transMeta.setTransformationType(TransMeta.TransformationType.Normal);
            transMeta.setUsingThreadPriorityManagment(threadManagePriority);
            DatabaseMeta databaseMeta = new DatabaseMeta(etlLogDatasourceName, etlLogDatasourceType, etlLogDatasourceAccess, etlLogDatasourceHost, etlLogDatasourceSchema, etlLogDatasourcePort, etlLogDatasourceUsername, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(etlLogDatasourcePassword));
            transMeta.addDatabase(databaseMeta);
            transMeta.setClearingLog(true);
            TransLogTable transLogTable = transMeta.getTransLogTable();
            StepLogTable stepLogTable = transMeta.getStepLogTable();
            ChannelLogTable channelLogTable = transMeta.getChannelLogTable();
//            MetricsLogTable metricsLogTable = transMeta.getMetricsLogTable();
//            PerformanceLogTable performanceLogTable = transMeta.getPerformanceLogTable();
            transLogTable.setConnectionName(etlLogDatasourceName);
            transLogTable.setTableName("log_etl_transform");
            stepLogTable.setConnectionName(etlLogDatasourceName);
            stepLogTable.setTableName("log_etl_transform_step");
            channelLogTable.setConnectionName(etlLogDatasourceName);
            channelLogTable.setTableName("log_etl_transform_channel");
//            metricsLogTable.setConnectionName(etlLogDatasourceName);
//            metricsLogTable.setTableName("log_etl_transform_metrics");
//            performanceLogTable.setConnectionName(etlLogDatasourceName);
//            performanceLogTable.setTableName("log_etl_transform_performance");
            transMeta.setLogLevel(LogLevel.DETAILED);
            String dir = prod ? File.separator : devDir;
            ConvertFactory.getVariable().put("dir", dir);
            Set<Long> referenceIds = new HashSet<>(0);
            Map<String, String> idNameMapping = new HashMap<>(0);
            for (Object element : elements) {
                mxCell cell = (mxCell) element;
                ResponseMeta responseMeta = transformConvertChain.parse(cell, transMeta);
                if (responseMeta.getStepMeta() != null) {
                    idNameMapping.put(responseMeta.getId(), responseMeta.getStepMeta().getName());
                }
                if (cell.isVertex()) {
                    transMeta.addStep(responseMeta.getStepMeta());
                    if (responseMeta.getDatabaseMeta() != null && Arrays.stream(transMeta.getDatabaseNames()).noneMatch(databaseName -> databaseName.equals(responseMeta.getDatabaseMeta().getName()))) {
                        transMeta.addDatabase(responseMeta.getDatabaseMeta());
                    }
                } else {
                    transMeta.addTransHop(responseMeta.getTransHopMeta());
                }
            }
            for (TransformConvertChain chain : TransformConvertFactory.getTransformConvertChains()) {
                chain.callback(transMeta, idNameMapping);
            }
            if (ConvertFactory.getVariable().containsKey(Constant.VAR_REFERENCES)) {
                referenceIds = (Set<Long>) ConvertFactory.getVariable().get(Constant.VAR_REFERENCES);
                List<Shell> shells = shellService.listByIds(referenceIds);
                shells.forEach(item -> {
                    String suffix = item.getCategory().equals(Constant.JOB) ? Constant.JOB_SUFFIX : Constant.TRANS_SUFFIX;
                    File folder = new File(dir + item.getProjectId() + File.separator + item.getParentId());
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    String path = dir + item.getProjectId() + File.separator + item.getParentId() + File.separator + item.getId() + Constant.DOT + suffix;
                    File file = new File(path);
                    boolean download = true;
                    if (file.exists()) {
                        try {
                            String text = Files.asCharSource(file, Charset.defaultCharset()).read();
                            String md5 = DigestUtils.md5DigestAsHex(text.getBytes(StandardCharsets.UTF_8));
                            if (md5.equals(item.getMd5Xml())) { // 本地文件md5与最新脚本记录的md5相等，无需从oss中获取最新文件
                                download = false;
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    if (download) {
                        InputStream inputStream = fileService.inputStream(Constant.ENV_DEV, item.getProjectId() + File.separator + item.getParentId() + File.separator + item.getId() + Constant.DOT + suffix);
                        try {
                            FileUtils.copyInputStreamToFile(inputStream, file);
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        } finally {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                });
            }
            TransformConvertFactory.destroyTransformConvertChains();
            TransformConvertFactory.destroyVariable();
            transMeta.setName(name);
            return ImmutableMap.of("transMeta", transMeta, "referenceIds", String.join(",", referenceIds.stream().map(x -> x + "").collect(Collectors.toList())));
        } catch (JsonProcessingException | KettleException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public Map<String, Object> getJobMeta(Shell shell, boolean prod) {
        String content = ZipUtils.unCompress(shell.getContent()), name = shell.getName();
        try {
            StringBuilder builder = new StringBuilder(attachmentDir);
            ConvertFactory.getVariable().put(Constant.VAR_ATTACHMENT_DIR, builder.toString());
            Document document = XMLHandler.loadXMLString(content);
            mxCodec codec = new mxCodec();
            mxGraph graph = new mxGraph();
            codec.decode(document.getDocumentElement(), graph.getModel());
            Object[] elements = graph.getChildCells(graph.getDefaultParent());
            JobConvertChain jobConvertChain = JobConvertFactory.getInstance();
            JobMeta jobMeta = new JobMeta();

            DatabaseMeta databaseMeta = new DatabaseMeta(etlLogDatasourceName, etlLogDatasourceType, etlLogDatasourceAccess, etlLogDatasourceHost, etlLogDatasourceSchema, etlLogDatasourcePort, etlLogDatasourceUsername, Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(etlLogDatasourcePassword));
            jobMeta.addDatabase(databaseMeta);
            jobMeta.setClearingLog(true);
            JobLogTable jobLogTable = jobMeta.getJobLogTable();
            JobEntryLogTable jobEntryLogTable = jobMeta.getJobEntryLogTable();
            ChannelLogTable channelLogTable = jobMeta.getChannelLogTable();
            jobLogTable.setConnectionName(etlLogDatasourceName);
            jobLogTable.setTableName("log_etl_job");
            jobEntryLogTable.setConnectionName(etlLogDatasourceName);
            jobEntryLogTable.setTableName("log_etl_job_entry");
            channelLogTable.setConnectionName(etlLogDatasourceName);
            channelLogTable.setTableName("log_etl_transform_channel");
            jobMeta.setLogLevel(LogLevel.DETAILED);
            String dir = prod ? File.separator : devDir;
            ConvertFactory.getVariable().put("dir", dir);

            Set<Long> referenceIds = new HashSet<>(0);
            for (Object element : elements) {
                mxCell cell = (mxCell) element;
                ResponseMeta responseMeta = jobConvertChain.parse(cell, jobMeta);
                if (cell.isVertex()) {
                    jobMeta.addJobEntry(responseMeta.getJobEntryCopy());
                    if (responseMeta.getDatabaseMeta() != null && Arrays.stream(jobMeta.getDatabaseNames()).noneMatch(databaseName -> databaseName.equals(responseMeta.getDatabaseMeta().getName()))) {
                        jobMeta.addDatabase(responseMeta.getDatabaseMeta());
                    }
                } else {
                    jobMeta.addJobHop(responseMeta.getJobHopMeta());
                }
            }
            if (ConvertFactory.getVariable().containsKey(Constant.VAR_REFERENCES)) {
                referenceIds = (Set<Long>) ConvertFactory.getVariable().get(Constant.VAR_REFERENCES);
                List<Shell> shells = shellService.listByIds(referenceIds);
                shells.forEach(item -> {
                    String suffix = item.getCategory().equals(Constant.JOB) ? Constant.JOB_SUFFIX : Constant.TRANS_SUFFIX;
                    File folder = new File(dir + item.getProjectId() + File.separator + item.getParentId());
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    String path = dir + item.getProjectId() + File.separator + item.getParentId() + File.separator + item.getId() + Constant.DOT + suffix;
                    File file = new File(path);
                    boolean download = true;
                    if (file.exists()) {
                        try {
                            String text = Files.asCharSource(file, Charset.defaultCharset()).read();
                            String md5 = DigestUtils.md5DigestAsHex(text.getBytes(StandardCharsets.UTF_8));
                            if (md5.equals(item.getMd5Xml())) { // 本地文件md5与最新脚本记录的md5相等，无需从oss中获取最新文件
                                download = false;
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    if (download) {
                        try {
                            String text = fileService.content(Constant.ENV_DEV, item.getProjectId() + File.separator + item.getParentId() + File.separator + item.getId() + Constant.DOT + suffix);
                            FileUtils.write(file, text, Charset.defaultCharset());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                });
            }
            JobConvertFactory.destroyVariable();
            jobMeta.setName(name);
            return ImmutableMap.of("jobMeta", jobMeta, "referenceIds", String.join(",", referenceIds.stream().map(x -> x + "").collect(Collectors.toList())));
        } catch (KettleException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
