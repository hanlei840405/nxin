package com.nxin.framework.service.task;

import com.alibaba.fastjson2.JSON;
import com.nxin.framework.dto.CronTriggerDto;
import com.nxin.framework.dto.ResponseDto;
import com.nxin.framework.interfaces.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@DubboService
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private Scheduler scheduler;

    @Override
    public ResponseDto create(String group, String id, String description, String cron, Integer misfire, String data) {
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

    @Transactional
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
}
