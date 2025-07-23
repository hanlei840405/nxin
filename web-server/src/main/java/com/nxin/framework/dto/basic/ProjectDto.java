package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectDto extends CrudDto {
    @NotNull
    private String name;
    private String description;
    @Deprecated
    private String scope;
}
