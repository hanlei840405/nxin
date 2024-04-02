package com.nxin.framework.service.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

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
        log.error("{}调度失败", context.getJobDetail());
    }

    /**
     * 任务被调度后
     *
     * @param context
     * @param jobException
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.info("{}完成调度", context.getJobDetail());
    }

    @Override
    public void jobToBeExecuted (JobExecutionContext context) {
        log.info("{}准备调度", context.getJobDetail());
    }
}
