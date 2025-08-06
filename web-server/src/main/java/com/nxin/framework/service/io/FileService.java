package com.nxin.framework.service.io;

import com.google.common.io.Files;
import com.qcloud.cos.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.BytesIdentityInfo;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEnv(String env) {
        try {
            FileObject dir = VFS.getManager().resolveFile(baseUrl + env, getOptions());
            if (!dir.exists()) {
                dir.createFolder();
            }
        } catch (FileSystemException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String createFile(String env, String path, String text) {
        try {
            // 创建临时文件
            File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
            Files.write(text.getBytes(StandardCharsets.UTF_8), tempFile);
            FileObject source = VFS.getManager().resolveFile(tempFile.getAbsolutePath());
            FileObject target = VFS.getManager().resolveFile(baseUrl + env + File.separator + path, getOptions());
            target.copyFrom(source, Selectors.SELECT_SELF);
            tempFile.delete();
            return DigestUtils.md5DigestAsHex(text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void createFolder(String env, String path) {
        try {
            String remotePath = baseUrl + env + File.separator + path;
            FileObject dir = VFS.getManager().resolveFile(remotePath, getOptions());
            if (!dir.exists()) {
                dir.createFolder();
            }
        } catch (FileSystemException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String content(String env, String path) {
        try {
            // 这里替换成你的文件路径
            FileObject file = VFS.getManager().resolveFile(baseUrl + env + File.separator + path, getOptions());
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
}
