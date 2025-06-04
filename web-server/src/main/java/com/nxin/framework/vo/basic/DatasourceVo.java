package com.nxin.framework.vo.basic;

import com.nxin.framework.vo.BaseVo;
import com.nxin.framework.vo.basic.ProjectVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DatasourceVo extends BaseVo {
    private String name;
    private String category;
    private Boolean generic;
    private String host;
    private Integer port;
    private String schemaName;
    private String username;
    private String password;
    private String dataSpace;
    private String indexSpace;
    private String parameter;
    private Boolean usePool;
    private Boolean useCursor;
    private Integer poolInitialSize;
    private Integer poolMaxSize;
    private Integer poolInitial;
    private Integer poolMaxActive;
    private Integer poolMaxIdle;
    private Integer poolMinIdle;
    private Integer poolMaxWait;
    private Long projectId;
    private String driver;
    private String url;
    private String charset;
}
