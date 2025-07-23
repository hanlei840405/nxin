package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends CrudDto {

    @NotNull
    private String name;
    private String mobile;
    @NotNull
    private String email;
    private String gender;
    private String wechat;
    @NotNull
    private String password;
    private Date birthDate;

}
