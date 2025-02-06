package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceVo extends BaseVo {

    private String code;
    private String name;
    private String category;
    private String level;
}
