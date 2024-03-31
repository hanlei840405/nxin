package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserPrivilegeVo extends BaseVo {

    private Long privilegeId;
    private Long userId;
}
