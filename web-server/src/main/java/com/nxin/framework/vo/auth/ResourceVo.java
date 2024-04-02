package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResourceVo extends BaseVo {

    private String code;
    private String name;
    private String category;
    private String level;
}
