package com.nxin.framework.entity.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 权限操作日志
 * </p>
 *
 * @author jesse han
 * @since 2025-07-23
 */
@TableName("auth_log")
@ApiModel(value = "Log对象", description = "权限操作日志")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("权限拥有者")
    private Long userId;

    @ApiModelProperty("权限")
    private Long privilegeId;

    @ApiModelProperty("动作:A:增加,D:删除,E:失效")
    private String action;

    @ApiModelProperty("发生时间")
    private LocalDateTime actionTime;

    @ApiModelProperty("操作者")
    private String operator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Log{" +
            "id = " + id +
            ", userId = " + userId +
            ", privilegeId = " + privilegeId +
            ", action = " + action +
            ", actionTime = " + actionTime +
            ", operator = " + operator +
        "}";
    }
}
