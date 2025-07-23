package com.nxin.framework.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GrantDto implements Serializable {

    @NotNull
    private Long userId;

    @NotNull
    private Long privilegeId;

    private String resourceCode;

    private String resourceCategory;

    private String resourceLevel;

    private LocalDateTime expireDate;

    private List<UserPrivilegeDto> userPrivileges;
}
