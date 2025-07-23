package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class DatasourceDto extends CrudDto {
    @NotNull
    private String name;
    @NotNull
    private String category;
    @NotNull
    private Boolean generic;
    private String host;
    private Integer port;
    private String schemaName;
    @NotNull
    private String username;
    @NotNull
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
    @NotNull
    private Long projectId;
    private String driver;
    private String url;
    @NotNull
    private String charset;
}
