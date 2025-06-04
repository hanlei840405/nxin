package com.nxin.framework.entity.bi;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 模型元数据
 * </p>
 *
 * @author jesse han
 * @since 2025-05-27
 */
@TableName("bi_metadata")
@ApiModel(value = "Metadata对象", description = "模型元数据")
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("列名")
    private String columnName;

    @ApiModelProperty("列编码")
    private String columnCode;

    @ApiModelProperty("列类型")
    private String columnCategory;

    @ApiModelProperty("列长度")
    private Integer columnLength;

    @ApiModelProperty("列小数长度")
    private Integer columnDecimal;

    @ApiModelProperty("是否为空")
    private Boolean columnNotNull;

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

    @ApiModelProperty("列绑定的外键模型ID")
    private Long columnForeignModelId;

    @ApiModelProperty("模型ID")
    private Long modelId;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("版本")
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnCategory() {
        return columnCategory;
    }

    public void setColumnCategory(String columnCategory) {
        this.columnCategory = columnCategory;
    }

    public Integer getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
    }

    public Integer getColumnDecimal() {
        return columnDecimal;
    }

    public void setColumnDecimal(Integer columnDecimal) {
        this.columnDecimal = columnDecimal;
    }

    public Boolean getColumnNotNull() {
        return columnNotNull;
    }

    public void setColumnNotNull(Boolean columnNotNull) {
        this.columnNotNull = columnNotNull;
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

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Long getColumnForeignModelId() {
        return columnForeignModelId;
    }

    public void setColumnForeignModelId(Long columnForeignModelId) {
        this.columnForeignModelId = columnForeignModelId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
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

    @Override
    public String toString() {
        return "Metadata{" +
                "id = " + id +
                ", columnName = " + columnName +
                ", columnCode = " + columnCode +
                ", columnCategory = " + columnCategory +
                ", columnLength = " + columnLength +
                ", columnDecimal = " + columnDecimal +
                ", columnNotNull = " + columnNotNull +
                ", createTime = " + createTime +
                ", creator = " + creator +
                ", modifyTime = " + modifyTime +
                ", modifier = " + modifier +
                ", columnForeignModelId = " + columnForeignModelId +
                ", modelId = " + modelId +
                ", status = " + status +
                ", version = " + version +
                "}";
    }
}
