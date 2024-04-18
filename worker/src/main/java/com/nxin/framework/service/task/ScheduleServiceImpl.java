package com.nxin.framework.service.task;

import com.alibaba.fastjson2.JSON;
import com.nxin.framework.dto.CronTriggerDto;
import com.nxin.framework.dto.ResponseDto;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.interfaces.ScheduleService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.RunningProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobConfiguration;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.www.CarteSingleton;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@DubboService
public class ScheduleServiceImpl implements ScheduleService {
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

    @Override
    public ResponseDto createBatch(String group, String id, String description, String cron, Integer misfire, String data) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        if (misfire == -1) {
            cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (misfire == 1) {
            cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else {
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        }
        CronTrigger kettleTrigger = TriggerBuilder.newTrigger().withIdentity(id, group).withDescription(description).withSchedule(cronScheduleBuilder).build();
        JobDetail jobDetail = JobBuilder.newJob(EtlTaskComp.class).withIdentity(id, group).withDescription(description).setJobData(new JobDataMap(JSON.parseObject(data))).build();
        try {
            scheduler.scheduleJob(jobDetail, kettleTrigger);
            return ResponseDto.builder().success(true).build();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }

    @Override
    public ResponseDto<List<CronTriggerDto>> findAllCronTrigger(List<String> groupList) {
        List<CronTriggerDto> cronTriggerDtoList = new ArrayList<>();
        for (String group : groupList) {
            GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(group);
            try {
                Set<JobKey> jobKeys = scheduler.getJobKeys(jobKeyGroupMatcher);
                for (JobKey jobKey : jobKeys) {
                    String shellId = scheduler.getJobDetail(jobKey).getJobDataMap().getString("id");
                    List<CronTrigger> triggers = (List<CronTrigger>) scheduler.getTriggersOfJob(jobKey);
                    if (!triggers.isEmpty()) {
                        String state = scheduler.getTriggerState(triggers.get(0).getKey()).name();
                        CronTriggerDto cronTriggerDto = new CronTriggerDto();
                        cronTriggerDto.setTriggerKey(triggers.get(0).getKey().toString());
                        cronTriggerDto.setCron(triggers.get(0).getCronExpression());
                        cronTriggerDto.setDescription(triggers.get(0).getDescription());
                        cronTriggerDto.setMisfire(triggers.get(0).getMisfireInstruction());
                        cronTriggerDto.setName(triggers.get(0).getDescription());
                        cronTriggerDto.setState(state);
                        cronTriggerDto.setNextFireTime(triggers.get(0).getNextFireTime());
                        cronTriggerDto.setPreviousFireTime(triggers.get(0).getPreviousFireTime());
                        cronTriggerDto.setStartTime(triggers.get(0).getStartTime());
                        cronTriggerDto.setShellId(Long.valueOf(shellId));
                        cronTriggerDtoList.add(cronTriggerDto);
                    }
                }
            } catch (SchedulerException e) {
                log.error(e.getMessage(), e);
                return ResponseDto.<List<CronTriggerDto>>builder().success(false).message(e.getMessage()).build();
            }
        }
        return ResponseDto.<List<CronTriggerDto>>builder().success(true).data(cronTriggerDtoList).build();
    }

    @Override
    public ResponseDto pause(String group, String id) {
        try {
            scheduler.pauseJob(JobKey.jobKey(id, group));
            scheduler.pauseTrigger(TriggerKey.triggerKey(id, group));
            return ResponseDto.builder().success(true).build();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }

    @Override
    public ResponseDto resume(String group, String id) {
        try {
            scheduler.resumeJob(JobKey.jobKey(id, group));
            scheduler.resumeTrigger(TriggerKey.triggerKey(id, group));
            return ResponseDto.builder().success(true).build();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }

    @Override
    public ResponseDto stop(String group, String id) {
        try {
            if (scheduler.checkExists(TriggerKey.triggerKey(id, group))) {
                TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                if (scheduler.checkExists(JobKey.jobKey(id, group))) {
                    scheduler.deleteJob(JobKey.jobKey(id, group));
                }
            }
            return ResponseDto.builder().success(true).build();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }

    @Override
    public ResponseDto modify(String group, String id, String cron, Integer misfire) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(id, group);
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            if (misfire == -1) {
                cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
            } else if (misfire == 1) {
                cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
            } else {
                cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            }
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(cronTrigger.getDescription()).withSchedule(cronScheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, cronTrigger);
            return ResponseDto.builder().success(true).build();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return ResponseDto.builder().success(false).message(e.getMessage()).build();
        }
    }

    @Override
    public ResponseDto createStreaming(String shellPathMap) {
        Map<String, Object> jobDataMap = JSON.parseObject(shellPathMap);
        Number id = (Number) jobDataMap.get("id");
        Number shellId = (Number) jobDataMap.get("shellId");
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
                if (referencePathMap.containsKey(shellId + Constant.DOT + Constant.JOB_SUFFIX)) {
                    entryJobPath = path;
                }
            }
            if (StringUtils.hasLength(entryJobPath)) {
                JobMeta jobMeta = new JobMeta(entryJobPath, null);
                JobConfiguration jobConfiguration = new JobConfiguration(jobMeta, jobExecutionConfiguration);
                spoonLoggingObject.setLogLevel(jobExecutionConfiguration.getLogLevel());
                Job job = new Job(null, jobMeta, spoonLoggingObject);
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
            return ResponseDto.builder().success(true).build();
        } catch (KettleXMLException e) {
            log.error(e.getMessage(), e);
            return ResponseDto.builder().success(false).build();
        }
    }
}
