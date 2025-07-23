package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceDto extends CrudDto {

    @NotNull
    private String code;

    @NotNull
    private String name;

    private String category;

    private String level;
}
