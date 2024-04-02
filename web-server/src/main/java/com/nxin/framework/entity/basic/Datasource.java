package com.nxin.framework.entity.basic;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author jesse han
 * @since 2024-01-04
 */
@TableName("basic_datasource")
@ApiModel(value = "Datasource对象", description = "")
public class Datasource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String category;

    private Long projectId;

    private Boolean useCursor;

    private Boolean usePool;

    private String username;

    private String password;

    private String parameter;

    private Integer poolInitial;

    private Integer poolInitialSize;

    private Integer poolMaxActive;

    private Integer poolMaxIdle;

    private Integer poolMaxSize;

    private Integer poolMaxWait;

    private Integer poolMinIdle;

    private String schemaName;

    private String host;

    private Integer port;

    private String dataSpace;

    private String indexSpace;

    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    @Version
    private Integer version;

    public static String ID_COLUMN = "id";

    public static String STATUS_COLUMN = "status";

    public static String NAME_COLUMN = "name";

    public static String PROJECT_ID_COLUMN = "project_id";

    public static String MODIFIER_COLUMN = "modifier";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getUseCursor() {
        return useCursor;
    }

    public void setUseCursor(Boolean useCursor) {
        this.useCursor = useCursor;
    }

    public Boolean getUsePool() {
        return usePool;
    }

    public void setUsePool(Boolean usePool) {
        this.usePool = usePool;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Integer getPoolInitial() {
        return poolInitial;
    }

    public void setPoolInitial(Integer poolInitial) {
        this.poolInitial = poolInitial;
    }

    public Integer getPoolInitialSize() {
        return poolInitialSize;
    }

    public void setPoolInitialSize(Integer poolInitialSize) {
        this.poolInitialSize = poolInitialSize;
    }

    public Integer getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(Integer poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    public Integer getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(Integer poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public Integer getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(Integer poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }

    public Integer getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(Integer poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public Integer getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(Integer poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDataSpace() {
        return dataSpace;
    }

    public void setDataSpace(String dataSpace) {
        this.dataSpace = dataSpace;
    }

    public String getIndexSpace() {
        return indexSpace;
    }

    public void setIndexSpace(String indexSpace) {
        this.indexSpace = indexSpace;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Datasource{" +
            "id = " + id +
            ", name = " + name +
            ", category = " + category +
            ", projectId = " + projectId +
            ", useCursor = " + useCursor +
            ", usePool = " + usePool +
            ", username = " + username +
            ", password = " + password +
            ", parameter = " + parameter +
            ", poolInitial = " + poolInitial +
            ", poolInitialSize = " + poolInitialSize +
            ", poolMaxActive = " + poolMaxActive +
            ", poolMaxIdle = " + poolMaxIdle +
            ", poolMaxSize = " + poolMaxSize +
            ", poolMaxWait = " + poolMaxWait +
            ", poolMinIdle = " + poolMinIdle +
            ", schemaName = " + schemaName +
            ", host = " + host +
            ", port = " + port +
            ", dataSpace = " + dataSpace +
            ", indexSpace = " + indexSpace +
            ", status = " + status +
            ", createTime = " + createTime +
            ", creator = " + creator +
            ", modifier = " + modifier +
            ", modifyTime = " + modifyTime +
            ", version = " + version +
        "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Datasource datasource = (Datasource) o;
        return getId().equals(datasource.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
