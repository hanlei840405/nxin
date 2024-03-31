package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

@Data
public class ResourceDto extends CrudDto {

    private String code;

    private String name;

    private String category;

    private String level;
}
