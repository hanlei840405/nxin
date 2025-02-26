package com.nxin.framework.converter.kettle.job.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.job.JobConvertChain;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.UnExecutableException;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.ftp.JobEntryFTP;
import org.pentaho.di.job.entries.ftpput.JobEntryFTPPUT;
import org.pentaho.di.job.entries.sftp.SFTPClient;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.util.StringUtils;

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
            Number server = (Number) formAttributes.get("server");
            Ftp ftp = getFtpService().one(server.longValue());
            String serverName = ftp.getHost();
            String serverPort = String.valueOf(ftp.getPort());
            String userName = ftp.getUsername();
            String password = ftp.getPassword();
            String proxyHost = ftp.getProxyHost();
            String proxyPort = String.valueOf(ftp.getProxyPort());
            String proxyUsername = ftp.getProxyUsername();
            String proxyPassword = ftp.getProxyPassword();
            String proxyCategory = ftp.getProxyCategory();
            String attachmentDir = (String) formAttributes.get("attachmentDir");
            String filename = (String) formAttributes.get("filename");
            String wildcard = (String) formAttributes.get("wildcard");
            String remoteDirectory = (String) formAttributes.get("remoteDirectory");
            boolean binaryMode = (boolean) formAttributes.get("binaryMode");
            boolean remove = (boolean) formAttributes.get("remove");
            boolean onlyPuttingNewFiles = (boolean) formAttributes.get("onlyPuttingNewFiles");
            String controlEncoding = (String) formAttributes.get("controlEncoding");
            int timeout = (int) formAttributes.get("timeout");
            JobEntryFTPPUT jobEntryFTPPUT = new JobEntryFTPPUT();
            jobEntryFTPPUT.setName(name);
            jobEntryFTPPUT.setServerName(serverName);
            jobEntryFTPPUT.setServerPort(serverPort);
            jobEntryFTPPUT.setUserName(userName);
            jobEntryFTPPUT.setPassword(password);

            // proxy
            if (StringUtils.hasLength(proxyHost)) {
                if (SFTPClient.PROXY_TYPE_SOCKS5.equals(proxyCategory)) {
                    jobEntryFTPPUT.setSocksProxyHost(proxyHost);
                    jobEntryFTPPUT.setSocksProxyPort(proxyPort);
                    jobEntryFTPPUT.setSocksProxyUsername(proxyUsername);
                    jobEntryFTPPUT.setSocksProxyPassword(proxyPassword);
                } else {
                    jobEntryFTPPUT.setProxyHost(proxyHost);
                    jobEntryFTPPUT.setProxyPort(proxyPort);
                    jobEntryFTPPUT.setProxyUsername(proxyUsername);
                    jobEntryFTPPUT.setProxyPassword(proxyPassword);
                }
            }

            jobEntryFTPPUT.setLocalDirectory(attachmentDir);
            jobEntryFTPPUT.setWildcard(wildcard);
            jobEntryFTPPUT.setRemoteDirectory(remoteDirectory);
            jobEntryFTPPUT.setBinaryMode(binaryMode);
            jobEntryFTPPUT.setRemove(remove);
            jobEntryFTPPUT.setOnlyPuttingNewFiles(onlyPuttingNewFiles);
            // 默认false
            jobEntryFTPPUT.setActiveConnection(false);
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
            return new ResponseMeta(cell.getId(), jobEntryCopy, null, null);
        } else {
            return next.parse(cell, jobMeta);
        }
    }

    @Override
    public void callback(JobMeta jobMeta) {

    }
}
