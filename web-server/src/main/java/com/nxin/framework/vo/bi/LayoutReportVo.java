package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LayoutReportVo extends BaseVo {

    private Long layoutId;

    private Long reportId;

    private Integer frequency;

    private Integer x;

    private Integer y;

    private Integer w;

    private Integer h;
}
