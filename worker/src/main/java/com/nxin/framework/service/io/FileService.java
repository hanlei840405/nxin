package com.nxin.framework.service.io;

import com.nxin.framework.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.filter.NameFileFilter;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.BytesIdentityInfo;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FileService {
    @Value("${vfs.lang}")
    private String lang;
    @Value("${vfs.host}")
    private String host;
    @Value("${vfs.path}")
    private String path;
    @Value("${vfs.schema}")
    private String schema;
    @Value("${vfs.username}")
    private String username;
    @Value("${vfs.password}")
    private String password;
    @Value("${vfs.privateKey}")
    private String privateKey;
    @Value("${vfs.passPhrase}")
    private String passPhrase;
    private String baseUrl;

    private static final String MODE_FTP = "ftp";

    @PostConstruct
    public void init() {
        try {
            if (!StringUtils.hasLength(privateKey)) {
                baseUrl = String.format("%s://%s:%s@%s%s", schema, username, URLEncoder.encode(password, "utf-8"), host, path);
            } else {
                baseUrl = String.format("%s://%s@%s%s", schema, username, host, path);
            }
            FileObject dir = VFS.getManager().resolveFile(baseUrl + "log/", getOptions());
            if (!dir.exists()) {
                dir.createFolder();
            }
        } catch (UnsupportedEncodingException | FileSystemException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void createFile(String path, FileObject source) {
        try {
            FileObject target = VFS.getManager().resolveFile(baseUrl + path, getOptions());
            target.copyFrom(source, Selectors.SELECT_SELF);
            source.delete();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public synchronized String downloadFile(String env, String localRootPath, Map<String, String> pathMap) {
        try {
            for (Map.Entry<String, String> entry : pathMap.entrySet()) {
                String[] array = entry.getValue().split(",");
                String ossPath = array[0];
                String nativePath = array[1];
                String directory = localRootPath + nativePath;
                String rename = directory + entry.getKey();
                FileObject renameFileObject = VFS.getManager().resolveFile(rename, getOptions());
                if (!renameFileObject.exists()) {
                    FileObject localDirectory = VFS.getManager().resolveFile(directory, getOptions());
                    FileObject fileToCopy = VFS.getManager().resolveFile(baseUrl + env + File.separator + ossPath, getOptions());
                    NameFileFilter nameFileFilter = new NameFileFilter(Collections.singletonList(fileToCopy.getName().getBaseName()));
                    FileSelector fileSelector = new FileFilterSelector(nameFileFilter);
                    localDirectory.copyFrom(fileToCopy.getParent(), fileSelector);
                    String oldName = directory + fileToCopy.getName().getBaseName();
                    FileObject oldFileObject = VFS.getManager().resolveFile(oldName, getOptions());
                    if (oldFileObject.canRenameTo(renameFileObject)) {
                        oldFileObject.moveTo(renameFileObject);
                        modifyFileContent(rename, localRootPath);
                    }
                }
                return rename;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private FileSystemOptions getOptions() {
        FileSystemOptions opts = new FileSystemOptions();
        if (MODE_FTP.equals(schema)) {
            FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
            //解决中文乱码
            FtpFileSystemConfigBuilder.getInstance().setServerLanguageCode(opts, lang);
            //builder.setControlEncoding(options, "UTF-8");
            FtpFileSystemConfigBuilder.getInstance().setAutodetectUtf8(opts, true);
            //设置超时时间
            FtpFileSystemConfigBuilder.getInstance().setConnectTimeout(opts, 30 * 1000);
            //设置 被动模式，防止 由于防火墙导致连接不上
            FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
        } else {
            try {
                SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no"); // 可选：关闭主机密钥检查以避免首次连接时的警告
                SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true); // 根目录为用户的家目录
                if (StringUtils.hasLength(privateKey)) {
                    BytesIdentityInfo identityInfo = new BytesIdentityInfo(privateKey.getBytes(StandardCharsets.UTF_8), StringUtils.hasLength(passPhrase) ? passPhrase.getBytes(StandardCharsets.UTF_8) : null);
                    SftpFileSystemConfigBuilder.getInstance().setIdentityProvider(opts, identityInfo);
                }
            } catch (FileSystemException e) {
                throw new RuntimeException(e);
            }
        }
        return opts;
    }

    private void modifyFileContent(String file, String target) {
        target = target.substring(0, target.lastIndexOf(File.separator));
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(file));
            Element rootElement = document.getRootElement();
            if (file.endsWith(Constant.DOT + Constant.JOB_SUFFIX)) {
                List<Element> entries = rootElement.element("entries").elements();
                for (Element entry : entries) {
                    Element filenameElement = entry.element("filename");
                    if (filenameElement != null) {
                        Element nameElement = entry.element("name");
                        if (nameElement != null && StringUtils.hasLength(nameElement.getText())) {
                            String value = filenameElement.getTextTrim();
                            if (StringUtils.hasLength(value)) {
                                filenameElement.setText(target + value);
                            }
                        }
                    }
                    Element keyfilenameElement = entry.element("keyfilename");
                    if (keyfilenameElement != null) {
                        Element nameElement = entry.element("name");
                        if (nameElement != null && StringUtils.hasLength(nameElement.getText())) {
                            String value = keyfilenameElement.getTextTrim();
                            if (StringUtils.hasLength(value)) {
                                keyfilenameElement.setText(target + value);
                            }
                        }
                    }
                }
            } else {
                List<Element> steps = rootElement.element("step").elements();
                for (Element step : steps) {
                    if ("transformationPath".equals(step.getName())) {
                        if (StringUtils.hasLength(step.getText())) {
                            String value = step.getTextTrim();
                            if (StringUtils.hasLength(value)) {
                                step.setText(target + value);
                            }
                        }
                    }
                }
            }
            XMLWriter writer = new XMLWriter(new FileWriter(file));
            //写入数据
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
