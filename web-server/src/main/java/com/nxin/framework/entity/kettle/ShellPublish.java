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
 * @since 2024-02-10
 */
@TableName("kettle_shell_publish")
@ApiModel(value = "ShellPublish对象", description = "")
public class ShellPublish implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private LocalDateTime deployTime;

    private String description;

    private String prod;

    private String reference;

    private String streaming;

    private String taskId;

    private String md5Graph;

    private String md5Xml;

    private Long projectId;

    private Long shellId;

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

    @TableField(exist = false)
    private Shell shell;

    public static String SHELL_ID_COLUMN = "shell_id";

    public static String ID_COLUMN = "id";

    public static String NAME_COLUMN = "name";

    public static String STREAMING_COLUMN = "streaming";

    public static String STATUS_COLUMN = "status";

    public static String CREATE_TIME_COLUMN = "create_time";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(LocalDateTime deployTime) {
        this.deployTime = deployTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Shell getShell() {
        return shell;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    @Override
    public String toString() {
        return "ShellPublish{" +
                "id = " + id +
                ", content = " + content +
                ", createTime = " + createTime +
                ", creator = " + creator +
                ", deployTime = " + deployTime +
                ", description = " + description +
                ", prod = " + prod +
                ", name = " + name +
                ", reference = " + reference +
                ", streaming = " + streaming +
                ", taskId = " + taskId +
                ", md5Graph = " + md5Graph +
                ", md5Xml = " + md5Xml +
                ", projectId = " + projectId +
                ", shellId = " + shellId +
                ", modifier = " + modifier +
                ", modifyTime = " + modifyTime +
                ", status = " + status +
                ", version = " + version +
                "}";
    }
}
