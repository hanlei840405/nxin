package com.nxin.framework.service.io;

import com.google.common.io.Files;
import com.nxin.framework.enums.Constant;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    @Value("${dev.dir}")
    private String devDir;
    @Value("${ftp.lang}")
    private String lang;
    @Value("${ftp.prefix-url}")
    private String prefixUrl;

    public InputStream inputStream(String env, String path) {
        try (StandardFileSystemManager fileSystemManager = new StandardFileSystemManager()) {
            fileSystemManager.init();
            // 这里替换成你的文件路径
            FileObject file = fileSystemManager.resolveFile(prefixUrl + env + File.separator + path, getOptions());
            return file.getContent().getInputStream();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String downloadFile(String env, String localRootPath, Map<String, String> pathMap) {
        for (Map.Entry<String, String> entry : pathMap.entrySet()) {
            String[] array = entry.getValue().split(",");
            String ossPath = array[0];
            String nativePath = array[1];
            File entryFolder = new File(localRootPath + nativePath);
            if (!entryFolder.exists()) {
                entryFolder.mkdirs();
            }
            String productionPath = localRootPath + nativePath + entry.getKey();
            File entryFile = new File(productionPath);
            if (!entryFile.exists()) {
                InputStream inputStream = inputStream(env, ossPath);
                try {
                    FileUtils.copyInputStreamToFile(inputStream, entryFile);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                modifyFileContent(productionPath, localRootPath);
            }
            return productionPath;
        }
        return null;
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
//                                    String[] path = value.split(File.separator);
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
//                                    String[] path = value.split(File.separator);
                                step.setText(target + value);
                            }
                        }
                    }
                }
            }
            //格式化为缩进格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            //设置编码格式
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(new FileWriter(file), format);
            //写入数据
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
