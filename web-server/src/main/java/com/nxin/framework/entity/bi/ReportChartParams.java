package com.nxin.framework.entity.bi;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 报表模型图形参数定义
 * </p>
 *
 * @author jesse han
 * @since 2025-06-16
 */
@TableName("bi_report_chart_params")
@ApiModel(value = "ReportChartParams对象", description = "报表模型图形参数定义")
public class ReportChartParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("报表ID")
    private Long reportId;

    @ApiModelProperty("参数ID")
    private Long chartParamsId;

    @ApiModelProperty("数据源ID")
    private Long datasourceId;

    @ApiModelProperty("类型")
    private String category;

    @ApiModelProperty("脚本")
    private String script;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("创建者")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    @ApiModelProperty("修改者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifier;

    @ApiModelProperty("版本")
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getChartParamsId() {
        return chartParamsId;
    }

    public void setChartParamsId(Long chartParamsId) {
        this.chartParamsId = chartParamsId;
    }

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
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
        return "ReportChartParams{" +
            "id = " + id +
            ", reportId = " + reportId +
            ", chartParamsId = " + chartParamsId +
            ", datasourceId = " + datasourceId +
            ", category = " + category +
            ", script = " + script +
            ", status = " + status +
            ", createTime = " + createTime +
            ", creator = " + creator +
            ", modifier = " + modifier +
            ", modifyTime = " + modifyTime +
            ", version = " + version +
        "}";
    }
}
