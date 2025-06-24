package com.nxin.framework.entity.auth;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 权限申请
 * </p>
 *
 * @author jesse han
 * @since 2025-06-23
 */
@TableName("auth_apply")
@ApiModel(value = "Apply对象", description = "权限申请")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("权限ID")
    private Long privilegeId;

    @ApiModelProperty("申请原因")
    private String reason;

    @ApiModelProperty("失效日期")
    private LocalDateTime expireDate;

    @ApiModelProperty("审核状态")
    private String auditStatus;

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

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
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
        return "Apply{" +
            "id = " + id +
            ", privilegeId = " + privilegeId +
            ", reason = " + reason +
            ", expireDate = " + expireDate +
            ", auditStatus = " + auditStatus +
            ", status = " + status +
            ", createTime = " + createTime +
            ", creator = " + creator +
            ", modifier = " + modifier +
            ", modifyTime = " + modifyTime +
            ", version = " + version +
        "}";
    }
}
