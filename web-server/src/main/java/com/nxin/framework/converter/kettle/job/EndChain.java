package com.nxin.framework.converter.kettle.job;

import com.mxgraph.model.mxCell;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import org.pentaho.di.job.JobMeta;

import java.io.IOException;

public class EndChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        return null;
    }

    @Override
    public void callback(JobMeta jobMeta) {

    }
}
