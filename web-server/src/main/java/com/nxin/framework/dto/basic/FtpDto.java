package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class FtpDto extends CrudDto {
    @NotNull
    private String name;
    @NotNull
    private String category;
    @NotNull
    private String host;
    @NotNull
    private Integer port;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
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
