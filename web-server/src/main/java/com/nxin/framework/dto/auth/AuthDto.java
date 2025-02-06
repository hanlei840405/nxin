package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthDto extends CrudDto {
    private String username;
    private String password;
    private String email;
    private String name;
    private List<ResourceDto> resources = new ArrayList<>();
    /**
     * 找回密码所依赖的验证码
     */
    private String code;
}
