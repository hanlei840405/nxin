package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class PrivilegeVo extends BaseVo {
    private String name;

    private String category;
}
