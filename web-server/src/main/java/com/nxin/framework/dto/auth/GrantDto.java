package com.nxin.framework.dto.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GrantDto implements Serializable {

    private Long userId;

    private Long privilegeId;

    private String resourceCode;

    private String resourceCategory;

    private String resourceLevel;

    private List<UserPrivilegeDto> userPrivileges;
}
