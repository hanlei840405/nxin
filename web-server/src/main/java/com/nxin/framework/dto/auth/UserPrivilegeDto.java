package com.nxin.framework.dto.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPrivilegeDto implements Serializable {

    private Long userId;

    private String rw;

}
