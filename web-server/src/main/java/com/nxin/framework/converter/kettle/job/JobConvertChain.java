package com.nxin.framework.converter.kettle.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxgraph.model.mxCell;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.kettle.ShellService;
import org.pentaho.di.job.JobMeta;

import java.io.IOException;

public abstract class JobConvertChain {

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected JobConvertChain next;

    protected DatasourceService datasourceService;

    private ShellService shellService;

    public void setNext(JobConvertChain next) {
        this.next = next;
    }

    public DatasourceService getDatasourceService() {
        return datasourceService;
    }

    public void setDatasourceService(DatasourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

    public ShellService getShellService() {
        return shellService;
    }

    public void setShellService(ShellService shellService) {
        this.shellService = shellService;
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
