package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportChartParamsDto extends CrudDto implements Serializable {

    private Long reportId;
    private Long chartParamsId;
    private String script;
}
