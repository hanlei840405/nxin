package com.nxin.framework.service.task;

import com.alibaba.fastjson2.JSON;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.request.TaskReq;
import com.nxin.framework.response.CronTriggerRes;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.RunningProcessService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.job.JobConfiguration;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.www.CarteSingleton;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class ScheduleService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private RunningProcessService runningProcessService;
    @Autowired
    private TaskHistoryService taskHistoryService;
    @Autowired
    private FileService fileService;
    @Value("${production.dir}")
    private String productionDir;

    public Date createJob(TaskReq taskReq) throws SchedulerException {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskReq.getCron());
        if (taskReq.getMisfire() == -1) {
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (taskReq.hashCode() == 1) {
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else {
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }
        CronTrigger kettleTrigger = TriggerBuilder.newTrigger().withIdentity(taskReq.getId(), taskReq.getGroup()).withDescription(taskReq.getDescription()).withSchedule(cronScheduleBuilder).build();
        JobDetail jobDetail = JobBuilder.newJob(EtlTaskComp.class).withIdentity(taskReq.getId(), taskReq.getGroup()).withDescription(taskReq.getDescription()).setJobData(new JobDataMap(JSON.parseObject(taskReq.getData()))).build();
        try {
            return scheduler.scheduleJob(jobDetail, kettleTrigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public List<CronTriggerRes> findAllCronTrigger(List<String> groupList) {
        List<CronTriggerRes> cronTriggerResList = new ArrayList<>();
        for (String group : groupList) {
            GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(group);
            try {
                Set<JobKey> jobKeys = scheduler.getJobKeys(jobKeyGroupMatcher);
                for (JobKey jobKey : jobKeys) {
                    String shellId = scheduler.getJobDetail(jobKey).getJobDataMap().getString("id");
                    List<CronTrigger> triggers = (List<CronTrigger>) scheduler.getTriggersOfJob(jobKey);
                    if (!triggers.isEmpty()) {
                        String state = scheduler.getTriggerState(triggers.get(0).getKey()).name();
                        CronTriggerRes cronTriggerRes = new CronTriggerRes();
                        cronTriggerRes.setTriggerKey(triggers.get(0).getKey().toString());
                        cronTriggerRes.setCron(triggers.get(0).getCronExpression());
                        cronTriggerRes.setDescription(triggers.get(0).getDescription());
                        cronTriggerRes.setMisfire(triggers.get(0).getMisfireInstruction());
                        cronTriggerRes.setName(triggers.get(0).getDescription());
                        cronTriggerRes.setState(state);
                        cronTriggerRes.setNextFireTime(triggers.get(0).getNextFireTime());
                        cronTriggerRes.setPreviousFireTime(triggers.get(0).getPreviousFireTime());
                        cronTriggerRes.setStartTime(triggers.get(0).getStartTime());
                        cronTriggerRes.setShellId(Long.valueOf(shellId));
                        cronTriggerResList.add(cronTriggerRes);
                    }
                }
            } catch (SchedulerException e) {
                log.error(e.getMessage(), e);
                return Collections.emptyList();
            }
        }
        return cronTriggerResList;
    }

    public boolean pause(TaskReq taskReq) {
        try {
            scheduler.pauseJob(JobKey.jobKey(taskReq.getId(), taskReq.getGroup()));
            scheduler.pauseTrigger(TriggerKey.triggerKey(taskReq.getId(), taskReq.getGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean resume(TaskReq taskReq) {
        try {
            scheduler.resumeJob(JobKey.jobKey(taskReq.getId(), taskReq.getGroup()));
            scheduler.resumeTrigger(TriggerKey.triggerKey(taskReq.getId(), taskReq.getGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean stop(TaskReq taskReq) {
        try {
            if (scheduler.checkExists(TriggerKey.triggerKey(taskReq.getId(), taskReq.getGroup()))) {
                TriggerKey triggerKey = TriggerKey.triggerKey(taskReq.getId(), taskReq.getGroup());
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                if (scheduler.checkExists(JobKey.jobKey(taskReq.getId(), taskReq.getGroup()))) {
                    scheduler.deleteJob(JobKey.jobKey(taskReq.getId(), taskReq.getGroup()));
                }
            }
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public Date modify(TaskReq taskReq) throws SchedulerException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(taskReq.getId(), taskReq.getGroup());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskReq.getCron());
            if (taskReq.getMisfire() == -1) {
                cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            } else if (taskReq.getMisfire() == 1) {
                cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            } else {
                cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            }
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(cronTrigger.getDescription()).withSchedule(cronScheduleBuilder).build();
            return scheduler.rescheduleJob(triggerKey, cronTrigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public boolean createStreaming(String shellPathMap) {
        Map<String, Object> jobDataMap = JSON.parseObject(shellPathMap);
        Number id = (Number) jobDataMap.get("id");
        Number shellId = (Number) jobDataMap.get("shellId");
        Number jobName = (Number) jobDataMap.get("jobName");
        Number projectId = (Number) jobDataMap.get("projectId");
        String rootPath = (String) jobDataMap.get("rootPath");
        List<Map<String, String>> referencePathList = (List<Map<String, String>>) jobDataMap.get("referencePathList");
        SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject("SPOON", LoggingObjectType.SPOON, null);
        String uuid = UUID.randomUUID().toString();
        spoonLoggingObject.setContainerObjectId(uuid);
        JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
        jobExecutionConfiguration.setLogLevel(LogLevel.BASIC);

        try {
            String entryJobPath = null;
            for (Map<String, String> referencePathMap : referencePathList) {
                String path = fileService.downloadFile(Constant.ENV_PUBLISH, productionDir + rootPath, referencePathMap);
                if (referencePathMap.containsKey(jobName + Constant.DOT + Constant.JOB_SUFFIX)) {
                    entryJobPath = path;
                }
            }
            if (StringUtils.hasLength(entryJobPath)) {
                JobMeta jobMeta = new JobMeta(entryJobPath, null);
                JobConfiguration jobConfiguration = new JobConfiguration(jobMeta, jobExecutionConfiguration);
                spoonLoggingObject.setLogLevel(jobExecutionConfiguration.getLogLevel());
                org.pentaho.di.job.Job job = new org.pentaho.di.job.Job(null, jobMeta, spoonLoggingObject);
                job.injectVariables(jobConfiguration.getJobExecutionConfiguration().getVariables());
                job.setGatheringMetrics(true);
                job.start();
                CarteSingleton.getInstance().getJobMap().addJob(job.getName(), uuid, job, jobConfiguration);
                RunningProcess runningProcess = new RunningProcess();
                runningProcess.setProd("1");
                runningProcess.setOwner(Constant.OWNER_TASK);
                runningProcess.setShellPublishId(id.longValue());
                runningProcess.setShellId(shellId.longValue());
                runningProcess.setProjectId(projectId.longValue());
                runningProcess.setInstanceId(uuid);
                runningProcess.setInstanceName(job.getName());
                runningProcess.setCategory(Constant.JOB);
                runningProcess.setVersion(1);
                runningProcessService.save(runningProcess);

                TaskHistory taskHistory = new TaskHistory();
                taskHistory.setShellPublishId(id.longValue());
                taskHistory.setBeginTime(LocalDateTime.now());
                taskHistory.setLogChannelId(job.getLogChannelId());
                taskHistory.setRunningProcessId(runningProcess.getId());
                taskHistory.setStatus(Constant.ACTIVE);
                taskHistoryService.save(taskHistory);
            }
            return true;
        } catch (KettleXMLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
