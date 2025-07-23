package com.nxin.framework.dto.auth;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class ApplyDto extends CrudDto implements Serializable {

    @NotNull
    private Long privilegeId;
    private String reason;
    private LocalDateTime expireDate;
    private String auditStatus;
    private Boolean searchAudit;

    private List<Long> applyIdList;
}
