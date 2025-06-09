package com.nxin.framework.entity.bi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 报表设计
 * </p>
 *
 * @author jesse han
 * @since 2025-06-06
 */
@TableName("bi_report")
@ApiModel(value = "Report对象", description = "报表设计")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("报表编码")
    private String code;

    @ApiModelProperty("报表名称")
    private String name;

    @ApiModelProperty("报表脚本")
    private String script;

    @ApiModelProperty("工程ID")
    private Long projectId;

    @ApiModelProperty("展示图表ID")
    private Long chartId;

    @ApiModelProperty("图表结构映射字段")
    private String mapping;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建者")
    private String creator;

    @ApiModelProperty("修改者")
    private String modifier;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty("版本")
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
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
        return "Report{" +
            "id = " + id +
            ", code = " + code +
            ", name = " + name +
            ", script = " + script +
            ", projectId = " + projectId +
            ", chartId = " + chartId +
            ", mapping = " + mapping +
            ", description = " + description +
            ", status = " + status +
            ", createTime = " + createTime +
            ", creator = " + creator +
            ", modifier = " + modifier +
            ", modifyTime = " + modifyTime +
            ", version = " + version +
        "}";
    }
}
