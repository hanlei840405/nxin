package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends CrudDto {

    private String name;
    private String mobile;
    private String email;
    private String gender;
    private String wechat;
    private String password;
    private Date birthDate;

}
