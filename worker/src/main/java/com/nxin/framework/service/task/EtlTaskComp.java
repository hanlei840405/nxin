package com.nxin.framework.service.task;

import com.alibaba.fastjson2.util.DateUtils;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.RunningProcessService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobConfiguration;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.io.File;
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
    private TaskHistoryService taskHistoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Value("${production.dir}")
    private String productionDir;
    @Value("${log.dir}")
    private String logDir;

    /**
     * 可以选择将任务发布到kettle集群执行
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String id = jobDataMap.getString("id");
        String shellId = jobDataMap.getString("shellId");
        String projectId = jobDataMap.getString("projectId");
        String rootPath = jobDataMap.getString("rootPath");
        List<Map<String, String>> referencePathList = (List<Map<String, String>>) jobDataMap.get("referencePathList");
        LoggingRegistry loggingRegistry = LoggingRegistry.getInstance();
        SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject("SPOON", LoggingObjectType.SPOON, null);
        String uuid = UUID.randomUUID().toString();
        spoonLoggingObject.setContainerObjectId(uuid);
        JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
        jobExecutionConfiguration.setLogLevel(LogLevel.BASIC);
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setShellPublishId(Long.valueOf(id));
        taskHistory.setBeginTime(LocalDateTime.now());
        CarteObjectEntry carteObjectEntry = null;
        try {
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
                carteObjectEntry = new CarteObjectEntry(job.getName(), uuid);
                job.injectVariables(jobConfiguration.getJobExecutionConfiguration().getVariables());
                job.setGatheringMetrics(true);
                FileLoggingEventListener fileLoggingEventListener = new FileLoggingEventListener(logDir + job.getLogChannelId() + ".out", true);
//                String logFilePath = fileService.getBaseUrl().concat("log/").concat(job.getLogChannelId()).concat(".out");
//                FileLoggingEventListener fileLoggingEventListener = new FileLoggingEventListener(logFilePath, true);
                KettleLogStore.getAppender().addLoggingEventListener(fileLoggingEventListener);
                CarteSingleton.getInstance().getJobMap().addJob(job.getName(), uuid, job, jobConfiguration);
                job.start();
                RunningProcess runningProcess = new RunningProcess();
                runningProcess.setProd("1");
                runningProcess.setOwner(Constant.OWNER_TASK);
                runningProcess.setShellPublishId(Long.valueOf(id));
                runningProcess.setShellId(Long.valueOf(shellId));
                runningProcess.setProjectId(Long.valueOf(projectId));
                runningProcess.setInstanceId(uuid);
                runningProcess.setInstanceName(job.getName());
                runningProcess.setCategory(Constant.JOB);
                runningProcess.setVersion(1);
                runningProcessService.save(runningProcess);
                taskHistory.setLogChannelId(job.getLogChannelId());
                taskHistory.setRunningProcessId(runningProcess.getId());
                job.waitUntilFinished();
                fileService.createFile("log" + File.separator + DateUtils.format(new Date(), "yyyy-MM-dd")  + File.separator + job.getLogChannelId() + ".out", fileLoggingEventListener.getFile());
                KettleLogStore.getAppender().removeLoggingEventListener(fileLoggingEventListener);
                runningProcessService.delete(runningProcess);
                if (job.getErrors() > 0) {
                    taskHistory.setStatus(Constant.INACTIVE);
                    log.error("执行" + entryJobPath + "发生异常, {}", job.getResult().getLogText());
                } else {
                    taskHistory.setStatus(Constant.ACTIVE);
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            taskHistory.setStatus(Constant.INACTIVE);
        } finally {
            if (carteObjectEntry != null && CarteSingleton.getInstance().getJobMap().getJob(carteObjectEntry) != null) {
                CarteSingleton.getInstance().getJobMap().removeJob(carteObjectEntry);
            }
        }
        taskHistory.setEndTime(LocalDateTime.now());
        taskHistoryService.save(taskHistory);
    }
}
