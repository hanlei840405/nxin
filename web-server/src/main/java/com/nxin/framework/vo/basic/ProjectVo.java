package com.nxin.framework.vo.basic;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectVo extends BaseVo {
    private String name;
    private String description;
    private Long userId;
    private String manager;
}
