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
import com.nxin.framework.entity.kettle.AttachmentStorage;
import com.nxin.framework.enums.Constant;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.ftp.JobEntryFTP;
import org.pentaho.di.job.entries.sftp.SFTPClient;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class JobEntryFTPChain extends JobConvertChain {

    @Override
    public ResponseMeta parse(mxCell cell, JobMeta jobMeta) throws IOException {
        if (cell.isVertex() && "JobEntryFTP".equalsIgnoreCase(cell.getStyle())) {
            DeferredElementImpl value = (DeferredElementImpl) cell.getValue();
            Map<String, Object> formAttributes = objectMapper.readValue(value.getAttribute("form"), new TypeReference<Map<String, Object>>() {
            });
            // basic
            String name = (String) formAttributes.get("name");
            // 本地目录，路径：~/attachment/{projectId}/{脚本所在目录ID}/{脚本ID}
            Shell shell = JSON.parseObject(jobMeta.getVariable(Constant.SHELL_INFO), Shell.class);
            String localDirectory = ConvertFactory.getVariable().get(Constant.VAR_DOWNLOAD_DIR).toString() + shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId();
            LambdaQueryWrapper<AttachmentStorage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AttachmentStorage::getShellId, shell.getId());
            queryWrapper.eq(AttachmentStorage::getComponent, cell.getStyle());
            queryWrapper.eq(AttachmentStorage::getComponentName, name);
            AttachmentStorage attachmentStorage = getAttachmentStorageService().getOne(queryWrapper);
            if (attachmentStorage == null) {
                attachmentStorage = new AttachmentStorage();
                attachmentStorage.setShellId(shell.getId());
                attachmentStorage.setShellParentId(shell.getParentId());
                attachmentStorage.setComponent(cell.getStyle());
                attachmentStorage.setComponentName(name);
                attachmentStorage.setCategory(Constant.ATTACHMENT_CATEGORY_DOWNLOAD);
                attachmentStorage.setStorageDir(localDirectory);
                attachmentStorage.setStatus(Constant.ACTIVE);
                attachmentStorage.setVersion(1);
            } else if (attachmentStorage.getShellParentId().equals(shell.getParentId())) {
                attachmentStorage.setShellParentId(shell.getParentId());
                attachmentStorage.setStorageDir(localDirectory);
            }
            getAttachmentStorageService().saveOrUpdate(attachmentStorage);
            boolean binaryMode = (boolean) formAttributes.get("binaryMode");
            // remote
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
            String wildcard = (String) formAttributes.get("wildcard");
            String remoteDirectory = (String) formAttributes.get("remoteDirectory");
            String remoteTargetDirectory = (String) formAttributes.get("remoteTargetDirectory");
            boolean remove = (boolean) formAttributes.get("remove");
            String controlEncoding = (String) formAttributes.get("controlEncoding");
            int timeout = (int) formAttributes.get("timeout");

            // local
            String existFile = (String) formAttributes.get("existFile");
            boolean includeDate = (boolean) formAttributes.get("includeDate");
            boolean includeTime = (boolean) formAttributes.get("includeTime");
            String dateFormat = (String) formAttributes.get("dateFormat");
            boolean includeDateInExtension = (boolean) formAttributes.get("includeDateInExtension");
            boolean appendFilename = (boolean) formAttributes.get("appendFilename");

            JobEntryFTP jobEntryFTP = new JobEntryFTP();
            jobEntryFTP.setName(name);
            jobEntryFTP.setBinaryMode(binaryMode);
            // 默认false
            jobEntryFTP.setActiveConnection(false);

            jobEntryFTP.setServerName(serverName);
            jobEntryFTP.setPort(serverPort);
            jobEntryFTP.setUserName(userName);
            jobEntryFTP.setPassword(password);
            if (StringUtils.hasLength(proxyHost)) {
                if (SFTPClient.PROXY_TYPE_SOCKS5.equals(proxyCategory)) {
                    jobEntryFTP.setSocksProxyHost(proxyHost);
                    jobEntryFTP.setSocksProxyPort(proxyPort);
                    jobEntryFTP.setSocksProxyUsername(proxyUsername);
                    jobEntryFTP.setSocksProxyPassword(proxyPassword);
                } else {
                    jobEntryFTP.setProxyHost(proxyHost);
                    jobEntryFTP.setProxyPort(proxyPort);
                    jobEntryFTP.setProxyUsername(proxyUsername);
                    jobEntryFTP.setProxyPassword(proxyPassword);
                }
            }
            jobEntryFTP.setFtpDirectory(remoteDirectory);
            jobEntryFTP.setWildcard(wildcard);
            jobEntryFTP.setTargetDirectory(localDirectory);
            if (StringUtils.hasLength(remoteTargetDirectory)) {
                jobEntryFTP.setMoveFiles(true);
                jobEntryFTP.setCreateMoveFolder(true);
            }
            jobEntryFTP.setMoveToDirectory(remoteTargetDirectory);
            jobEntryFTP.setRemove(remove);
            jobEntryFTP.setControlEncoding(controlEncoding);
            jobEntryFTP.setTimeout(timeout);
            if (StringUtils.hasLength(existFile)) {
                jobEntryFTP.setOnlyGettingNewFiles(true);
                int strategy = Integer.parseInt(existFile);
                if ( strategy == 0 ) {
                    jobEntryFTP.ifFileExists = jobEntryFTP.ifFileExistsSkip;
                    jobEntryFTP.SifFileExists = jobEntryFTP.SifFileExistsSkip;
                } else if ( strategy == 1 ) {
                    jobEntryFTP.ifFileExists = jobEntryFTP.ifFileExistsCreateUniq;
                    jobEntryFTP.SifFileExists = jobEntryFTP.SifFileExistsCreateUniq;
                } else {
                    jobEntryFTP.ifFileExists = jobEntryFTP.ifFileExistsFail;
                    jobEntryFTP.SifFileExists = jobEntryFTP.SifFileExistsFail;
                }
            }
            jobEntryFTP.setDateInFilename(includeDate);
            jobEntryFTP.setTimeInFilename(includeTime);
            if (!includeDate && !includeTime) {
                jobEntryFTP.setSpecifyFormat(true);
                jobEntryFTP.setDateTimeFormat(dateFormat);
            }
            jobEntryFTP.setAddDateBeforeExtension(includeDateInExtension);
            jobEntryFTP.setAddToResult(appendFilename);
            JobEntryCopy jobEntryCopy = new JobEntryCopy(jobEntryFTP);
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
