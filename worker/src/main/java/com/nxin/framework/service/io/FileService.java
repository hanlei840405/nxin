package com.nxin.framework.service.io;

import com.google.common.io.Files;
import com.nxin.framework.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.filter.NameFileFilter;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
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
import java.util.UUID;

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
    private String baseUrl;

    @PostConstruct
    public void init() {
        try (StandardFileSystemManager fileSystemManager = new StandardFileSystemManager()) {
            fileSystemManager.init();
            baseUrl = String.format("%s://%s:%s@%s%s", schema, username, URLEncoder.encode(password, "utf-8"), host, path);
            FileObject dir = fileSystemManager.resolveFile(baseUrl + "log/", getOptions());
            if (!dir.exists()) {
                dir.createFolder();
            }
        } catch (UnsupportedEncodingException | FileSystemException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void createFile(String path, FileObject source) {
        try (StandardFileSystemManager fileSystemManager = new StandardFileSystemManager()) {
            fileSystemManager.init();
            FileObject target = fileSystemManager.resolveFile(baseUrl + path, getOptions());
            target.copyFrom(source, Selectors.SELECT_SELF);
            source.delete();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public synchronized String downloadFile(String env, String localRootPath, Map<String, String> pathMap) {
        try (StandardFileSystemManager fileSystemManager = new StandardFileSystemManager()) {
            fileSystemManager.init();
            for (Map.Entry<String, String> entry : pathMap.entrySet()) {
                String[] array = entry.getValue().split(",");
                String ossPath = array[0];
                String nativePath = array[1];
                String directory = localRootPath + nativePath;
                String rename = directory + entry.getKey();
                FileObject renameFileObject = fileSystemManager.resolveFile(rename, getOptions());
                if (!renameFileObject.exists()) {
                    FileObject localDirectory = fileSystemManager.resolveFile(directory, getOptions());
                    FileObject fileToCopy = fileSystemManager.resolveFile(baseUrl + env + File.separator + ossPath, getOptions());
                    NameFileFilter nameFileFilter = new NameFileFilter(Collections.singletonList(fileToCopy.getName().getBaseName()));
                    FileSelector fileSelector = new FileFilterSelector(nameFileFilter);
                    localDirectory.copyFrom(fileToCopy.getParent(), fileSelector);
                    String oldName = directory + fileToCopy.getName().getBaseName();
                    FileObject oldFileObject = fileSystemManager.resolveFile(oldName, getOptions());
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
        FtpFileSystemConfigBuilder builder = FtpFileSystemConfigBuilder.getInstance();
        FileSystemOptions options = new FileSystemOptions();
        //解决中文乱码
        builder.setServerLanguageCode(options, lang);
//        builder.setControlEncoding(options, "UTF-8");
        builder.setAutodetectUtf8(options, true);
        //设置超时时间
        builder.setConnectTimeout(options, 30 * 1000);
        //设置 被动模式，防止 由于防火墙导致连接不上
        builder.setPassiveMode(options, true);
        return options;
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
