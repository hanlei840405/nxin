package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportChartParamsVo extends BaseVo {

    private Long reportId;
    private Long chartParamsId;
    private String category;
    private String script;

    // chartParams字段
    private String field;
    private String fieldCategory;
    private String description;
}
