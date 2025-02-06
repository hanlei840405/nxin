package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserPrivilegeVo extends BaseVo {

    private Long privilegeId;
    private Long userId;
}
