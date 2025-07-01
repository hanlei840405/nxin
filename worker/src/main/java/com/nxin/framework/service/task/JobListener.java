package com.nxin.framework.service.task;

import com.alibaba.fastjson.JSONObject;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.message.sender.SenderUtils;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
public class JobListener extends JobListenerSupport {

    /**
     * job监听器名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "EtlJobListener";
    }

    /**
     * 任务调度被拒了
     *
     * @param context
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.error("{}拒绝", context.getJobDetail());
    }

    /**
     * 任务被调度后
     *
     * @param context
     * @param jobException
     */
    @Transactional
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info("{}完成调度", context.getJobDetail());
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        long id = jobDataMap.getLong("id");
        String name = jobDataMap.getString("name");
        String logChannelId = jobDataMap.getString("logChannelId");
        String uuid = jobDataMap.getString("uuid");
        RunningProcessService runningProcessService = SpringContextUtils.getBean(RunningProcessService.class);
        RunningProcess runningProcess = runningProcessService.instanceId(uuid);
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setShellPublishId(id);
        taskHistory.setBeginTime(LocalDateTime.now());
        String topic;
        if (jobException != null) {
            topic = Constant.TOPIC_TASK_FAILURE;
            taskHistory.setStatus(Constant.INACTIVE);
            runningProcess.setInstanceName(name);
            runningProcessService.updateById(runningProcess);
        } else {
            topic = Constant.TOPIC_TASK_SUCCESS;
            taskHistory.setStatus(Constant.ACTIVE);
            taskHistory.setLogChannelId(logChannelId);
            taskHistory.setRunningProcessId(runningProcess.getId());
            runningProcessService.delete(runningProcess);
        }
        taskHistory.setEndTime(LocalDateTime.now());
        TaskHistoryService taskHistoryService = SpringContextUtils.getBean(TaskHistoryService.class);
        taskHistoryService.save(taskHistory);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("taskHistoryId", taskHistory.getId());
        jsonObject.put("fireTime", context.getFireTime());
        SenderUtils.getSender().send(topic, jsonObject.toString());
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("{}准备调度", context.getJobDetail());
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        long id = jobDataMap.getLong("id");
        long shellId = jobDataMap.getLong("shellId");
        long projectId = jobDataMap.getLong("projectId");
        String uuid = UUID.randomUUID().toString();
        RunningProcess runningProcess = new RunningProcess();
        runningProcess.setProd("1");
        runningProcess.setOwner(Constant.OWNER_TASK);
        runningProcess.setShellPublishId(id);
        runningProcess.setShellId(shellId);
        runningProcess.setProjectId(projectId);
        runningProcess.setInstanceId(uuid);
        runningProcess.setCategory(Constant.JOB);
        runningProcess.setVersion(1);
        RunningProcessService runningProcessService = SpringContextUtils.getBean(RunningProcessService.class);
        runningProcessService.save(runningProcess);
        jobDataMap.put("uuid", uuid);
    }
}
