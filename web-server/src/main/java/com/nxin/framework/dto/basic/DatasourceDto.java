package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class DatasourceDto extends CrudDto {
    private String name;
    private String category;
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
}
