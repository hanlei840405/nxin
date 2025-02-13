package com.nxin.framework.converter.kettle.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxgraph.model.mxCell;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.kettle.ShellService;
import org.pentaho.di.trans.TransMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TransformConvertChain {
    public final Map<String, Object> callbackMap = new ConcurrentHashMap<>(0);

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected TransformConvertChain next;

    protected DatasourceService datasourceService;

    private ShellService shellService;

    private FtpService ftpService;

    public void setNext(TransformConvertChain next) {
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

    public FtpService getFtpService() {
        return ftpService;
    }

    public void setFtpService(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    public abstract ResponseMeta parse(mxCell cell, TransMeta transMeta) throws JsonProcessingException;

    public abstract void callback(TransMeta transMeta, Map<String, String> idNameMapping);
}
