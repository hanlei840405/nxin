package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.entity.bi.ReportChartParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private Long projectId;
    private Long chartId;
    private Long modelId;
    private String script;
    private String mapping;
    private String description;
    private List<ReportChartParamsDto> reportChartParamsList;

    // 拓展参数，用于在列表页面直接预览报表时的控制参数
    private Boolean edit;
}
