package com.nxin.framework.converter.kettle.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxgraph.model.mxCell;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.kettle.ShellService;
import org.pentaho.di.job.JobMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JobConvertChain {

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected JobConvertChain next;

    private ShellService shellService;

    private FtpService ftpService;

    private Map<String, Object> jobVariable = new HashMap<>();

    public void setNext(JobConvertChain next) {
        this.next = next;
    }

    public ShellService getShellService() {
        return shellService;
    }

    public void setShellService(ShellService shellService) {
        this.shellService = shellService;
    }

    public FtpService getFtpService() {
        return ftpService;
    }

    public void setFtpService(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    public Map<String, Object> getJobVariable() {
        return jobVariable;
    }

    /**
     * mxgraph生成kettle组件
     *
     * @param cell
     * @param jobMeta
     * @return
     * @throws IOException
     */
    public abstract ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException;

    public abstract void callback(JobMeta jobMeta);
}
