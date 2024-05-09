package com.nxin.framework.entity.kettle;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author jesse han
 * @since 2024-02-09
 */
@TableName("kettle_running_process")
@ApiModel(value = "RunningProcess对象", description = "")
public class RunningProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String category;

    private String instanceId;

    private String instanceName;

    private String owner;

    private String prod;

    private Long projectId;

    private Long shellId;

    private Long shellPublishId;

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

    private String status;

    public static String PROJECT_ID_COLUMN = "project_id";

    public static String INSTANCE_ID_COLUMN = "instance_id";

    public static String SHELL_ID_COLUMN = "shell_id";

    public static String SHELL_PUBLISH_ID_COLUMN = "shell_publish_id";

    public static String PROD_COLUMN = "prod";

    public static String STATUS_COLUMN = "status";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getShellId() {
        return shellId;
    }

    public void setShellId(Long shellId) {
        this.shellId = shellId;
    }

    public Long getShellPublishId() {
        return shellPublishId;
    }

    public void setShellPublishId(Long shellPublishId) {
        this.shellPublishId = shellPublishId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RunningProcess{" +
                "id = " + id +
                ", category = " + category +
                ", createTime = " + createTime +
                ", instanceId = " + instanceId +
                ", instanceName = " + instanceName +
                ", owner = " + owner +
                ", prod = " + prod +
                ", projectId = " + projectId +
                ", shellId = " + shellId +
                ", shellPublishId = " + shellPublishId +
                ", version = " + version +
                ", creator = " + creator +
                ", modifier = " + modifier +
                ", modifyTime = " + modifyTime +
                ", status = " + status +
                "}";
    }
}
