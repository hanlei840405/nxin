package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrivilegeVo extends BaseVo {

    private Long resourceId;

    private String name;

    private String category;

    private String description;
}
