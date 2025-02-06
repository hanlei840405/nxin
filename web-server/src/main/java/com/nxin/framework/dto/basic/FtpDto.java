package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class FtpDto extends CrudDto {
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
