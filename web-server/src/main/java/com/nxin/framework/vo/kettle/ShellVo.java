package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;

@Data
public class ShellVo extends BaseVo {

    private String name;
    private String description;
    private String category;
    private String reference;
    private String content;
    private String streaming;
    private String xml;
    private boolean executable;
    private Long parentId;
    private Long projectId;
}
