package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectDto extends CrudDto {
    private String name;
    private String description;
    private String scope;
}
