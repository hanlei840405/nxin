package com.nxin.framework.converter.kettle.job.transfer;

import com.alibaba.fastjson.JSON;
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
import org.pentaho.di.job.entries.sftp.SFTPClient;
import org.pentaho.di.job.entries.sftpput.JobEntrySFTPPUT;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JobEntrySFTPPutChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntrySFTPPUT".equalsIgnoreCase(cell.getStyle())) {
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
            Boolean usePrivateKey = ftp.getUsePrivateKey();
            String privateKeyPassword = ftp.getPrivateKeyPassword();
            String proxyHost = ftp.getProxyHost();
            String proxyPort = String.valueOf(ftp.getProxyPort());
            String proxyUsername = ftp.getProxyUsername();
            String proxyPassword = ftp.getProxyPassword();
            String proxyCategory = ftp.getProxyCategory();
            String remoteDirectory = (String) formAttributes.get("remoteDirectory");
            String compression = (String) formAttributes.get("compression");
            boolean successWhenNoFile = (boolean) formAttributes.get("successWhenNoFile");
            boolean createRemoteFolder = (boolean) formAttributes.get("createRemoteFolder");
            String attachmentDir = (String) formAttributes.get("attachmentDir");
            String wildcard = (String) formAttributes.get("wildcard");
            JobEntrySFTPPUT jobEntrySFTPPUT = new JobEntrySFTPPUT();
            jobEntrySFTPPUT.setName(name);
            jobEntrySFTPPUT.setServerName(serverName);
            jobEntrySFTPPUT.setServerPort(serverPort);
            jobEntrySFTPPUT.setUserName(userName);
            jobEntrySFTPPUT.setPassword(password);
            if (usePrivateKey) {
                Shell shell = JSON.parseObject(jobMeta.getVariable(Constant.SHELL_INFO), Shell.class);
                String privateKeyFile = jobMeta.getVariable(Constant.SHELL_STORAGE_DIR).concat(File.separator).concat(Constant.SSH_PATH).concat(File.separator).concat(shell.getProjectId().toString()).concat(File.separator).concat(ftp.getId().toString());
                jobEntrySFTPPUT.setKeyFilename(privateKeyFile);
                jobEntrySFTPPUT.setKeyPassPhrase(privateKeyPassword);
                jobEntrySFTPPUT.setUseKeyFile(usePrivateKey);
            }
            jobEntrySFTPPUT.setCompression(compression);
            // proxy
            if (StringUtils.hasLength(proxyHost)) {
                if (SFTPClient.PROXY_TYPE_SOCKS5.equals(proxyCategory)) {
                    jobEntrySFTPPUT.setProxyType(SFTPClient.PROXY_TYPE_SOCKS5);
                } else {
                    jobEntrySFTPPUT.setProxyType(SFTPClient.PROXY_TYPE_HTTP);
                }
                jobEntrySFTPPUT.setProxyHost(proxyHost);
                jobEntrySFTPPUT.setProxyPort(proxyPort);
                jobEntrySFTPPUT.setProxyUsername(proxyUsername);
                jobEntrySFTPPUT.setProxyPassword(proxyPassword);
            }
            jobEntrySFTPPUT.setLocalDirectory(attachmentDir);
            jobEntrySFTPPUT.setWildcard(wildcard);
            jobEntrySFTPPUT.setSuccessWhenNoFile(successWhenNoFile);
            jobEntrySFTPPUT.setAfterFTPS(JobEntrySFTPPUT.AFTER_FTPSPUT_DELETE);
            jobEntrySFTPPUT.setScpDirectory(remoteDirectory);
            jobEntrySFTPPUT.setCreateRemoteFolder(createRemoteFolder);
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntrySFTPPUT);
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
