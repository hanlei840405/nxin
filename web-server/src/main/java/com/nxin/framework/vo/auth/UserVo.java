package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVo extends BaseVo {

    private boolean master;
    private String name;
    private String mobile;
    private String email;
    private String gender;
    private String wechat;
    private String password;
    private Date birthDate;
    /**
     * 权限
     */
    private String rw;
}
