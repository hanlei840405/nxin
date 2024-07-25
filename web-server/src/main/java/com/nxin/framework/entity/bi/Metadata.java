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
 * 
 * </p>
 *
 * @author jesse han
 * @since 2024-07-25
 */
@TableName("bi_metadata")
@ApiModel(value = "Metadata对象", description = "")
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Boolean autoIncrement;

    private String columnCategory;

    private String columnCode;

    private Integer columnDecimal;

    private Integer columnLength;

    private String columnName;

    private LocalDateTime createTime;

    private String creator;

    private String modifier;

    private LocalDateTime modifyTime;

    private Boolean notNull;

    private Boolean primaryKey;

    private String status;

    private Integer version;

    private Long modelId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public String getColumnCategory() {
        return columnCategory;
    }

    public void setColumnCategory(String columnCategory) {
        this.columnCategory = columnCategory;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public Integer getColumnDecimal() {
        return columnDecimal;
    }

    public void setColumnDecimal(Integer columnDecimal) {
        this.columnDecimal = columnDecimal;
    }

    public Integer getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(Integer columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
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

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    @Override
    public String toString() {
        return "Metadata{" +
            "id = " + id +
            ", autoIncrement = " + autoIncrement +
            ", columnCategory = " + columnCategory +
            ", columnCode = " + columnCode +
            ", columnDecimal = " + columnDecimal +
            ", columnLength = " + columnLength +
            ", columnName = " + columnName +
            ", createTime = " + createTime +
            ", creator = " + creator +
            ", modifier = " + modifier +
            ", modifyTime = " + modifyTime +
            ", notNull = " + notNull +
            ", primaryKey = " + primaryKey +
            ", status = " + status +
            ", version = " + version +
            ", modelId = " + modelId +
        "}";
    }
}
