
package com.nxin.framework.controller.kettle;

import com.enterprisedt.net.ftp.FTPClient;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.base.FtpConverter;
import com.nxin.framework.dto.basic.FtpDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.basic.FtpVo;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.sftp.SFTPClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@PreAuthorize("hasAuthority('ROOT') or hasAuthority('FTP')")
@RestController
@RequestMapping
public class ShellStorageController {
    @Autowired
    private FtpService ftpService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Value("${dev.dir}")
    private String devDir;
    private final BeanConverter<FtpVo, Ftp> ftpConverter = new FtpConverter();
    private final static String FTP = "FTP";

    @GetMapping("/ftp/{id}")
    public ResponseEntity<FtpVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Ftp ftp = ftpService.one(id);
        if (ftp != null && ftp.getProjectId() != null) {
            List<User> members = userService.findByResource(ftp.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                return ResponseEntity.ok(ftpConverter.convert(ftp));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/ftpList")
    public ResponseEntity<List<FtpVo>> list(@RequestBody FtpDto ftpDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(ftpDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            return ResponseEntity.ok(ftpConverter.convert(ftpService.all(ftpDto.getProjectId(), ftpDto.getCategory())));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/ftp")
    public ResponseEntity save(@RequestBody FtpDto ftpDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(ftpDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            Ftp ftp = new Ftp();
            BeanUtils.copyProperties(ftpDto, ftp);
            ftpService.save(ftp);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/ftp/test")
    public ResponseEntity<Boolean> test(@RequestBody FtpDto ftpDto) {
        try {
            JobMeta jobMeta = new JobMeta();
            if (FTP.equals(ftpDto.getCategory())) {
                FTPClient ftpClient = new FTPClient();
                ftpClient.setRemoteAddr(InetAddress.getByName(ftpDto.getHost()));
                ftpClient.setRemotePort(ftpDto.getPort());
                if (!Utils.isEmpty(ftpDto.getProxyHost())) {
                    ftpClient.setRemoteAddr(InetAddress.getByName(ftpDto.getProxyHost()));
                    int port = ftpDto.getProxyPort();
                    if (port != 0) {
                        ftpClient.setRemotePort(port);
                    }
                }
                // login to ftp host ...
                ftpClient.connect();
                String realUsername = ftpDto.getUsername() + (StringUtils.hasLength(ftpDto.getProxyHost()) ? "@" + ftpDto.getHost() : "") + (StringUtils.hasLength(ftpDto.getProxyUsername()) ? " " + ftpDto.getProxyUsername() : "");
                String realPassword = Utils.resolvePassword(jobMeta, ftpDto.getPassword()) + (StringUtils.hasLength(ftpDto.getProxyHost()) ? " " + Utils.resolvePassword(jobMeta, ftpDto.getProxyPassword()) : "");
                // login now ...
                ftpClient.login(realUsername, realPassword);
                ftpClient.pwd();
            } else {
                if (ftpDto.getUsePrivateKey()) {
                    String sshFilePath = devDir.concat(File.separator).concat(Constant.SSH_PATH).concat(File.separator).concat(ftpDto.getProjectId().toString());
                    File sshFileFolder = new File(sshFilePath);
                    if (!sshFileFolder.exists()) {
                        sshFileFolder.mkdirs();
                    }
                    String uuid = UUID.randomUUID().toString();
                    File ssh = new File(sshFilePath.concat(File.separator).concat(uuid));
                    Files.write(ssh.toPath(), ftpDto.getPrivateKey().getBytes(Charset.defaultCharset()));
                    ftpDto.setPrivateKey(ssh.getPath());
                }
                SFTPClient sftpclient;
                try {
                    sftpclient = new SFTPClient(InetAddress.getByName(ftpDto.getHost()), ftpDto.getPort() != null ? ftpDto.getPort() : 22, ftpDto.getUsername(), ftpDto.getPrivateKey(), ftpDto.getPrivateKeyPassword());
                } catch (Throwable e) {
                    throw new Exception(e);
                } finally {
                    if (ftpDto.getUsePrivateKey()) {
                        File ssh = new File(ftpDto.getPrivateKey());
                        ssh.delete();
                    }
                }
                if (StringUtils.hasLength(ftpDto.getProxyHost())) {
                    sftpclient.setProxy(ftpDto.getProxyHost(), String.valueOf(ftpDto.getProxyPort()), ftpDto.getProxyUsername(), Utils.resolvePassword(jobMeta, ftpDto.getProxyPassword()), ftpDto.getProxyCategory());
                }
                sftpclient.login(Utils.resolvePassword(jobMeta, ftpDto.getPassword()));
            }
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }

    @DeleteMapping("/ftp/{id}")
    public ResponseEntity<FtpVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Ftp persisted = ftpService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                ftpService.delete(persisted.getProjectId(), Collections.singletonList(persisted.getId()));
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }
}
