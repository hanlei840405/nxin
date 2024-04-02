package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

@Data
public class PrivilegeDto extends CrudDto {

    private String name;

    private String category;
}
