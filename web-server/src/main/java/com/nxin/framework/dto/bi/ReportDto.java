package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.entity.bi.ReportChartParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportDto extends CrudDto implements Serializable {

    @NotNull
    private String code;
    @NotNull
    private String name;
    @NotNull
    private Long projectId;
    @NotNull
    private Long chartId;
    @NotNull
    private Long modelId;
    /**
     * 预留字段
     */
    private String script;
    /**
     * 预留字段
     */
    private String mapping;
    private String description;
    private List<ReportChartParamsDto> reportChartParamsList;

    // 拓展参数，用于在列表页面直接预览报表时的控制参数
    private Boolean edit;
}
