package com.nxin.framework.entity.kettle;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author jesse han
 * @since 2024-01-04
 */
@TableName("kettle_shell")
@ApiModel(value = "Shell对象", description = "")
public class Shell implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Long projectId;

    private Long parentId;

    private String category;

    private Boolean executable;

    private String reference;

    private String streaming;

    private String md5Graph;

    private String md5Xml;

    private String description;

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

    @TableField(exist = false)
    private String content;

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getExecutable() {
        return executable;
    }

    public void setExecutable(Boolean executable) {
        this.executable = executable;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStreaming() {
        return streaming;
    }

    public void setStreaming(String streaming) {
        this.streaming = streaming;
    }

    public String getMd5Graph() {
        return md5Graph;
    }

    public void setMd5Graph(String md5Graph) {
        this.md5Graph = md5Graph;
    }

    public String getMd5Xml() {
        return md5Xml;
    }

    public void setMd5Xml(String md5Xml) {
        this.md5Xml = md5Xml;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Shell{" +
                "id = " + id +
                ", name = " + name +
                ", projectId = " + projectId +
                ", parentId = " + parentId +
                ", category = " + category +
                ", executable = " + executable +
                ", reference = " + reference +
                ", streaming = " + streaming +
                ", md5_xml = " + md5Xml +
                ", md5_graph = " + md5Graph +
                ", description = " + description +
                ", status = " + status +
                ", creator = " + creator +
                ", modifier = " + modifier +
                ", createTime = " + createTime +
                ", modifyTime = " + modifyTime +
                ", version = " + version +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shell shell = (Shell) o;
        return getId().equals(shell.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
