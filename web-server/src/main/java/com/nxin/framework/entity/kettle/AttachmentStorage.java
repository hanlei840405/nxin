package com.nxin.framework.entity.kettle;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 脚本运行时生成的文件存放位置
 * </p>
 *
 * @author jesse han
 * @since 2025-02-17
 */
@TableName("kettle_attachment_storage")
@ApiModel(value = "AttachmentStorage对象", description = "脚本运行时生成的文件存放位置")
public class AttachmentStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private Long shellId;

    private String shellName;

    private Long shellParentId;

    private String component;

    private String componentId;

    private Integer category;

    private String storageDir;

    private String storageDirRelative;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getShellName() {
        return shellName;
    }

    public void setShellName(String shellName) {
        this.shellName = shellName;
    }

    public Long getShellParentId() {
        return shellParentId;
    }

    public void setShellParentId(Long shellParentId) {
        this.shellParentId = shellParentId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getStorageDir() {
        return storageDir;
    }

    public void setStorageDir(String storageDir) {
        this.storageDir = storageDir;
    }

    public String getStorageDirRelative() {
        return storageDirRelative;
    }

    public void setStorageDirRelative(String storageDirRelative) {
        this.storageDirRelative = storageDirRelative;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
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
        return "AttachmentStorage{" +
                "id=" + id +
                ", shellId=" + shellId +
                ", shellParentId=" + shellParentId +
                ", component='" + component + '\'' +
                ", componentId='" + componentId + '\'' +
                ", category=" + category +
                ", storageDir='" + storageDir + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", creator='" + creator + '\'' +
                ", modifier='" + modifier + '\'' +
                ", modifyTime=" + modifyTime +
                ", version=" + version +
                '}';
    }
}
