package com.nxin.framework.converter.kettle.job.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.job.JobConvertChain;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.UnExecutableException;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.ftp.JobEntryFTP;
import org.pentaho.di.job.entries.ftpput.JobEntryFTPPUT;
import org.pentaho.di.job.entry.JobEntryCopy;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class JobEntryFTPPutChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntryFTPPUT".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            String name = (String) formAttributes.get("name");
            String serverName = (String) formAttributes.get("serverName");
            String serverPort = (String) formAttributes.get("serverPort");
            String userName = (String) formAttributes.get("userName");
            String password = (String) formAttributes.get("password");
            Number shellId = (Number) formAttributes.get("shellId");
            // 需要上传的文件目录，{环境目录}/工程ID/根目录ID/文件ID
            Shell transformShell = getShellService().one(shellId.longValue());
            String localDirectory;
            if (transformShell.getExecutable()) {
                localDirectory = getJobVariable().get(Constant.VAR_ATTACHMENT_DIR).toString() + transformShell.getProjectId() + File.separator + transformShell.getParentId() + File.separator + transformShell.getId();
            } else {
                throw new UnExecutableException();
            }
            String wildcard = (String) formAttributes.get("wildcard");
            String remoteDirectory = (String) formAttributes.get("remoteDirectory");
            boolean binaryMode = (boolean) formAttributes.get("binaryMode");
            boolean remove = (boolean) formAttributes.get("remove");
            boolean onlyPuttingNewFiles = (boolean) formAttributes.get("onlyPuttingNewFiles");
            boolean activeConnection = (boolean) formAttributes.get("activeConnection");
            String controlEncoding = (String) formAttributes.get("controlEncoding");
            int timeout = (int) formAttributes.get("timeout");
            JobEntryFTPPUT jobEntryFTPPUT = new JobEntryFTPPUT();
            jobEntryFTPPUT.setName(name);
            jobEntryFTPPUT.setServerName(serverName);
            jobEntryFTPPUT.setServerPort(serverPort);
            jobEntryFTPPUT.setUserName(userName);
            jobEntryFTPPUT.setPassword(password);
            jobEntryFTPPUT.setLocalDirectory(localDirectory);
            jobEntryFTPPUT.setWildcard(wildcard);
            jobEntryFTPPUT.setRemoteDirectory(remoteDirectory);
            jobEntryFTPPUT.setBinaryMode(binaryMode);
            jobEntryFTPPUT.setRemove(remove);
            jobEntryFTPPUT.setOnlyPuttingNewFiles(onlyPuttingNewFiles);
            jobEntryFTPPUT.setActiveConnection(activeConnection);
            jobEntryFTPPUT.setControlEncoding(controlEncoding);
            jobEntryFTPPUT.setTimeout(timeout);
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntryFTPPUT);
            mxGeometry geometry = cell.getGeometry();
            jobEntryCopy.setLocation(new Double(geometry.getX()).intValue(), new Double(geometry.getY()).intValue());
            jobEntryCopy.setDrawn();
            if (formAttributes.containsKey(Constant.ETL_PARALLEL)) {
                boolean parallel = (boolean) formAttributes.get(Constant.ETL_PARALLEL);
                jobEntryCopy.setLaunchingInParallel(parallel);
            }
            return new ResponseMeta(cell.getId(), jobEntryCopy, null);
        } else {
            return next.parse(cell, jobMeta);
        }
    }

    @Override
    public void callback(JobMeta jobMeta) {

    }
}
