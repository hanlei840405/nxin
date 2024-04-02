package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

@Data
public class ProjectDto extends CrudDto {
    private String name;
    private String description;
    private String scope;
}
