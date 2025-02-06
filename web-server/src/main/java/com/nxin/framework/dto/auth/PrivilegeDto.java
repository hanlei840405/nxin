package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrivilegeDto extends CrudDto {

    private String name;

    private String category;
}
