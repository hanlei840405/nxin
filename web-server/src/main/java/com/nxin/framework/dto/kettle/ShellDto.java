package com.nxin.framework.dto.kettle;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

@Data
public class ShellDto extends CrudDto {
    private String name;
    private String description;
    private String category;
    private String reference;
    private String content;
    private String streaming;
    private String xml;
    private boolean executable = false;
    private Long parentId;
    private Long projectId;
}
