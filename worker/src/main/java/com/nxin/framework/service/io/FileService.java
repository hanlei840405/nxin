package com.nxin.framework.service.io;

import com.google.common.io.Files;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        try {
            baseUrl = String.format("%s://%s:%s@%s%s", schema, username, URLEncoder.encode(password, "utf-8"), host, path);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String content(String env, String path) {
        try (StandardFileSystemManager fileSystemManager = new StandardFileSystemManager()) {
            fileSystemManager.init();
            // 这里替换成你的文件路径
            FileObject file = fileSystemManager.resolveFile(baseUrl + env + File.separator + path, getOptions());
            if (file.exists()) {
                // 打开文件输入流
                try (InputStream is = file.getContent().getInputStream()) {
                    return IOUtils.toString(is);
                }
            }
            return null;
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
                try {
                    String text = content(env, ossPath + entry.getKey());
                    FileUtils.write(entryFile, text, Charset.defaultCharset());
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
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
}
