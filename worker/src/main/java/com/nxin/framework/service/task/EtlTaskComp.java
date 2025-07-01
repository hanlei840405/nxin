package com.nxin.framework.service.task;

import com.alibaba.fastjson2.util.DateUtils;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.RunningProcessService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleJobException;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobConfiguration;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@Transactional
public class EtlTaskComp extends QuartzJobBean {
    @Autowired
    private RunningProcessService runningProcessService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private FtpService ftpService;
    @Value("${production.dir}")
    private String productionDir;
    @Value("${attachment.dir}")
    private String attachmentDir;
    @Value("${download.dir}")
    private String downloadDir;
    @Value("${log.dir}")
    private String logDir;

    /**
     * 可以选择将任务发布到kettle集群执行
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String uuid = jobDataMap.getString("uuid");
        long shellId = jobDataMap.getLong("shellId");
        long projectId = jobDataMap.getLong("projectId");
        String rootPath = jobDataMap.getString("rootPath");
        List<Map<String, String>> referencePathList = (List<Map<String, String>>) jobDataMap.get("referencePathList");
        List<String> attachmentOrDownloadDirList = (List<String>) jobDataMap.get("attachmentOrDownloadDirList");
        SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject("SPOON", LoggingObjectType.SPOON, null);
        spoonLoggingObject.setContainerObjectId(uuid);
        JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
        jobExecutionConfiguration.setLogLevel(LogLevel.BASIC);
        CarteObjectEntry carteObjectEntry = null;
        FileLoggingEventListener fileLoggingEventListener = null;
        try {
            List<Ftp> ftps = ftpService.all(projectId, "SFTP");
            for (Ftp ftp : ftps) {
                if (ftp.getUsePrivateKey()) {
                    String sshFilePath = productionDir.concat(rootPath).concat(Constant.SSH_PATH).concat(File.separator).concat(String.valueOf(projectId));
                    File sshFileFolder = new File(sshFilePath);
                    if (!sshFileFolder.exists()) {
                        sshFileFolder.mkdirs();
                    }
                    File ssh = new File(sshFilePath.concat(File.separator).concat(ftp.getId().toString()));
                    Files.write(ssh.toPath(), ftp.getPrivateKey().getBytes(Charset.defaultCharset()));
                }
            }
            for (String attachmentOrDownloadDir : attachmentOrDownloadDirList) {
                File folder = new File(attachmentOrDownloadDir);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            }
            String entryJobPath = null;
            for (Map<String, String> referencePathMap : referencePathList) {
                String path = fileService.downloadFile(Constant.ENV_PUBLISH, productionDir + rootPath, referencePathMap);
                if (referencePathMap.containsKey(shellId + Constant.DOT + Constant.JOB_SUFFIX)) {
                    entryJobPath = path;
                }
            }
            if (StringUtils.hasLength(entryJobPath)) {
                JobMeta jobMeta = new JobMeta(entryJobPath, null);
                JobConfiguration jobConfiguration = new JobConfiguration(jobMeta, jobExecutionConfiguration);
                spoonLoggingObject.setLogLevel(jobExecutionConfiguration.getLogLevel());
                Job job = new Job(null, jobMeta, spoonLoggingObject);
                carteObjectEntry = new CarteObjectEntry(job.getJobMeta().getName(), uuid);
                job.injectVariables(jobConfiguration.getJobExecutionConfiguration().getVariables());
                job.setGatheringMetrics(true);

                fileLoggingEventListener = new FileLoggingEventListener(job.getLogChannelId(), logDir + job.getLogChannelId() + ".out", true);
                Constant.logMapping.put(uuid, fileLoggingEventListener);
                KettleLogStore.getAppender().addLoggingEventListener(fileLoggingEventListener);
                CarteSingleton.getInstance().getJobMap().addJob(job.getJobMeta().getName(), uuid, job, jobConfiguration);
                job.start();
                job.waitUntilFinished();
                // 将参数放入jobDataMap
                jobDataMap.put("name", job.getJobMeta().getName());
                jobDataMap.put("logChannelId()", job.getLogChannelId());
                if (job.getErrors() > 0) {
                    log.error("执行{}发生异常, {}", entryJobPath, job.getResult().getLogText());
                    throw new JobExecutionException();
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new JobExecutionException();
        } finally {
            if (carteObjectEntry != null) {
                Job job = CarteSingleton.getInstance().getJobMap().getJob(carteObjectEntry);
                if (job != null) {
                    CarteSingleton.getInstance().getJobMap().removeJob(carteObjectEntry);
                    Constant.logMapping.remove(carteObjectEntry.getId());
                    if (fileLoggingEventListener != null) {
                        KettleLogStore.getAppender().removeLoggingEventListener(fileLoggingEventListener);
                        try {
                            fileLoggingEventListener.close();
                        } catch (KettleException e) {
                            log.error(e.getMessage(), e);
                        }
                        fileService.createFile("log" + File.separator + DateUtils.format(new Date(), "yyyy-MM-dd") + File.separator + job.getLogChannelId() + ".out", fileLoggingEventListener.getFile());
                    }
                }
            }
        }
    }
}
