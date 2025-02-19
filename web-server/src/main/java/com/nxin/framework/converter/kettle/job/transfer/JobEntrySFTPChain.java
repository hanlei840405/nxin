package com.nxin.framework.converter.kettle.job.transfer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.job.JobConvertChain;
import com.nxin.framework.converter.kettle.transform.ResponseMeta;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.entity.kettle.ShellStorage;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.UnExecutableException;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.sftp.JobEntrySFTP;
import org.pentaho.di.job.entries.sftp.SFTPClient;
import org.pentaho.di.job.entries.sftpput.JobEntrySFTPPUT;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class JobEntrySFTPChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntrySFTP".equalsIgnoreCase(cell.getStyle())) {
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
            boolean remove = (boolean) formAttributes.get("remove");
            boolean isAddResult = (boolean) formAttributes.get("isAddResult");
            String wildcard = (String) formAttributes.get("wildcard");
            // 需要上传的文件目录，{环境目录}/工程ID/根目录ID/文件ID
            Shell shell = JSON.parseObject(jobMeta.getVariable(Constant.SHELL_INFO), Shell.class);
            String localDirectory = ConvertFactory.getVariable().get(Constant.VAR_DOWNLOAD_DIR).toString() + shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId();
            LambdaQueryWrapper<ShellStorage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShellStorage::getShellId, shell.getId());
            queryWrapper.eq(ShellStorage::getComponent, cell.getStyle());
            queryWrapper.eq(ShellStorage::getComponentName, name);
            ShellStorage shellStorage = getShellStorageService().getOne(queryWrapper);
            if (shellStorage == null) {
                shellStorage = new ShellStorage();
                shellStorage.setShellId(shell.getId());
                shellStorage.setShellParentId(shell.getParentId());
                shellStorage.setComponent(cell.getStyle());
                shellStorage.setComponentName(name);
                shellStorage.setStorageDir(localDirectory);
                shellStorage.setStatus(Constant.ACTIVE);
                shellStorage.setVersion(1);
            } else if (shellStorage.getShellParentId().equals(shell.getParentId())) {
                shellStorage.setShellParentId(shell.getParentId());
                shellStorage.setStorageDir(localDirectory);
            }
            getShellStorageService().saveOrUpdate(shellStorage);
            JobEntrySFTP jobEntrySFTP = new JobEntrySFTP();
            jobEntrySFTP.setName(name);
            jobEntrySFTP.setServerName(serverName);
            jobEntrySFTP.setServerPort(serverPort);
            jobEntrySFTP.setUserName(userName);
            jobEntrySFTP.setPassword(password);
            if (usePrivateKey) {
                String privateKeyFile = jobMeta.getVariable(Constant.SHELL_STORAGE_DIR).concat(File.separator).concat(Constant.SSH_PATH).concat(File.separator).concat(shell.getProjectId().toString()).concat(File.separator).concat(ftp.getId().toString());
                jobEntrySFTP.setKeyFilename(privateKeyFile);
                jobEntrySFTP.setKeyPassPhrase(privateKeyPassword);
                jobEntrySFTP.setUseKeyFile(usePrivateKey);
            }
            jobEntrySFTP.setCompression(compression);
            // proxy
            if (StringUtils.hasLength(proxyHost)) {
                if (SFTPClient.PROXY_TYPE_SOCKS5.equals(proxyCategory)) {
                    jobEntrySFTP.setProxyType(SFTPClient.PROXY_TYPE_SOCKS5);
                } else {
                    jobEntrySFTP.setProxyType(SFTPClient.PROXY_TYPE_HTTP);
                }
                jobEntrySFTP.setProxyHost(proxyHost);
                jobEntrySFTP.setProxyPort(proxyPort);
                jobEntrySFTP.setProxyUsername(proxyUsername);
                jobEntrySFTP.setProxyPassword(proxyPassword);
            }
            jobEntrySFTP.setScpDirectory(remoteDirectory);
            jobEntrySFTP.setWildcard(wildcard);
            jobEntrySFTP.setRemove(remove);
            jobEntrySFTP.setTargetDirectory(localDirectory);
            jobEntrySFTP.setCreateTargetFolder(false);
            jobEntrySFTP.setAddToResult(isAddResult);
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntrySFTP);
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
