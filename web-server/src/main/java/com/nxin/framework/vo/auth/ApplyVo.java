package com.nxin.framework.vo.auth;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限申请
 * </p>
 *
 * @author jesse han
 * @since 2025-06-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyVo extends BaseVo {

    private Long privilegeId;
    private String auditStatus;
    private String reason;
    private LocalDateTime expireDate;

    private String privilegeName;
    private String privilegeCategory;
}
