package com.nxin.framework.vo.basic;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FtpVo extends BaseVo {
    private String name;
    private String category;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Long projectId;
    private Boolean usePrivateKey;
    private String privateKey;
    private String privateKeyPassword;
    private String proxyCategory;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;
    private String proxyPassword;
}
