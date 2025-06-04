package com.nxin.framework.entity.bi;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 元数据字段索引
 * </p>
 *
 * @author jesse han
 * @since 2025-05-27
 */
@TableName("bi_metadata_index")
@ApiModel(value = "MetadataIndex对象", description = "元数据字段索引")
public class MetadataIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("索引系统名")
    private String indexCode;

    @ApiModelProperty("索引展示名")
    private String indexName;

    @ApiModelProperty("索引类型")
    private String indexCategory;

    @ApiModelProperty("索引对应元数据ID集合")
    private String metadataIdArray;

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

    @ApiModelProperty("状态")
    private String status;

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexCategory() {
        return indexCategory;
    }

    public void setIndexCategory(String indexCategory) {
        this.indexCategory = indexCategory;
    }

    public String getMetadataIdArray() {
        return metadataIdArray;
    }

    public void setMetadataIdArray(String metadataIdArray) {
        this.metadataIdArray = metadataIdArray;
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
        return "MetadataIndex{" +
            "id = " + id +
            ", indexCode = " + indexCode +
            ", indexName = " + indexName +
            ", indexCategory = " + indexCategory +
            ", metadataIdArray = " + metadataIdArray +
            ", createTime = " + createTime +
            ", creator = " + creator +
            ", modifyTime = " + modifyTime +
            ", modifier = " + modifier +
            ", status = " + status +
            ", version = " + version +
        "}";
    }
}
