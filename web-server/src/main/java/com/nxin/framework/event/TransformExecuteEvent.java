package com.nxin.framework.event;

import com.nxin.framework.entity.kettle.RunningProcess;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransConfiguration;
import org.springframework.context.ApplicationEvent;

public class TransformExecuteEvent extends ApplicationEvent {
    private String instanceId;
    private Long shellId;
    private Trans trans;
    private TransConfiguration transConfiguration;

    public TransformExecuteEvent(RunningProcess runningProcess, String instanceId, Long shellId, Trans trans, TransConfiguration transConfiguration) {
        super(runningProcess);
        this.instanceId = instanceId;
        this.trans = trans;
        this.transConfiguration = transConfiguration;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Long getShellId() {
        return shellId;
    }

    public void setShellId(Long shellId) {
        this.shellId = shellId;
    }

    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    public TransConfiguration getTransConfiguration() {
        return transConfiguration;
    }

    public void setTransConfiguration(TransConfiguration transConfiguration) {
        this.transConfiguration = transConfiguration;
    }
}
