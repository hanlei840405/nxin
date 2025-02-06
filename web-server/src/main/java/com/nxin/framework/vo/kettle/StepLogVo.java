package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class StepLogVo extends BaseVo {
    private String name;
    private String logDate;
    private Integer stepCopy;
    private Long read;
    private Long written;
    private Long updated;
    private Long input;
    private Long output;
    private Long rejected;
    private Long errors;
}