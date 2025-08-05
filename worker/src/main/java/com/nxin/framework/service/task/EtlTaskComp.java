package com.nxin.framework.service.task;

import com.alibaba.fastjson2.util.DateUtils;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;
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
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class EtlTaskComp extends QuartzJobBean {

    /**
     * 可以选择将任务发布到kettle集群执行
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        FileService fileUtils = SpringContextUtils.getBean(FileService.class);
        FtpService ftpService = SpringContextUtils.getBean(FtpService.class);
        Environment environment = SpringContextUtils.getBean(Environment.class);
        String productionDir = environment.getProperty("production.dir");
        String logDir = environment.getProperty("log.dir");
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String uuid = jobDataMap.getString("uuid");
        long shellId = jobDataMap.getLong("shellId");
        long projectId = jobDataMap.getLong("projectId");
        String rootPath = jobDataMap.getString("rootPath");
        List<Map<String, String>> referencePathList = (List<Map<String, String>>) jobDataMap.get("referencePathList");
        List<String> attachmentOrDownloadDirList = (List<String>) jobDataMap.get("attachmentOrDownloadDirList");
        SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject("SPOON", LoggingObjectType.SPOON, null);
        spoonLoggingObject.setLogLevel(LogLevel.BASIC);
        spoonLoggingObject.setContainerObjectId(uuid);
        JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
        jobExecutionConfiguration.setLogLevel(spoonLoggingObject.getLogLevel());
        try {
            List<Ftp> ftps = ftpService.all(projectId, "SFTP");
            for (Ftp ftp : ftps) {
                if (ftp.getUsePrivateKey()) {
                    assert productionDir != null;
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
                String path = fileUtils.downloadFile(Constant.ENV_PUBLISH, productionDir + rootPath, referencePathMap);
                if (referencePathMap.containsKey(shellId + Constant.DOT + Constant.JOB_SUFFIX)) {
                    entryJobPath = path;
                }
            }
            if (StringUtils.hasLength(entryJobPath)) {
                JobMeta jobMeta = new JobMeta(entryJobPath, null);
                JobConfiguration jobConfiguration = new JobConfiguration(jobMeta, jobExecutionConfiguration);
                Job job = new Job(null, jobMeta, spoonLoggingObject);
                job.injectVariables(jobConfiguration.getJobExecutionConfiguration().getVariables());
                job.setGatheringMetrics(true);

                FileLoggingEventListener fileLoggingEventListener = new FileLoggingEventListener(job.getLogChannelId(), logDir + job.getLogChannelId() + ".out", true);
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
            Job job = CarteSingleton.getInstance().getJobMap().findJob(uuid);
            if (job != null) {
                CarteObjectEntry carteObjectEntry = CarteSingleton.getInstance().getJobMap().getFirstCarteObjectEntry(job.getObjectName());
                CarteSingleton.getInstance().getJobMap().removeJob(carteObjectEntry);
                FileLoggingEventListener fileLoggingEventListener = Constant.logMapping.get(uuid);
                Constant.logMapping.remove(uuid);
                KettleLogStore.getAppender().removeLoggingEventListener(fileLoggingEventListener);
                try {
                    fileLoggingEventListener.close();
                } catch (KettleException e) {
                    log.error(e.getMessage(), e);
                }
                fileUtils.createFile("log" + File.separator + DateUtils.format(new Date(), "yyyy-MM-dd") + File.separator + job.getLogChannelId() + ".out", fileLoggingEventListener.getFile());
            }
        }
    }
}
