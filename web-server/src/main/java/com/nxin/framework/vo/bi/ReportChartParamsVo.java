package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportChartParamsVo extends BaseVo {

    private Long reportId;
    private Long chartParamsId;
    private String script;
}
