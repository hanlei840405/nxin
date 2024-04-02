package com.nxin.framework.event;

import com.nxin.framework.entity.kettle.RunningProcess;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobConfiguration;
import org.springframework.context.ApplicationEvent;

public class JobExecuteEvent extends ApplicationEvent {
    private String instanceId;
    private Job job;
    private JobConfiguration jobConfiguration;

    public JobExecuteEvent(RunningProcess runningProcess, String instanceId, Job job, JobConfiguration jobConfiguration) {
        super(runningProcess);
        this.instanceId = instanceId;
        this.job = job;
        this.jobConfiguration = jobConfiguration;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public JobConfiguration getJobConfiguration() {
        return jobConfiguration;
    }

    public void setJobConfiguration(JobConfiguration jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }
}
