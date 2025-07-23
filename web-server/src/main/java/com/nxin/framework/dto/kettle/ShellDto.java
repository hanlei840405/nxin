package com.nxin.framework.dto.kettle;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShellDto extends CrudDto {
    @NotNull
    private String name;
    private String description;
    @NotNull
    private String category;
    private String reference;
    private String content;
    private String streaming;
    private String xml;
    private boolean executable = false;
    @NotNull
    private Long parentId;
    @NotNull
    private Long projectId;
}
