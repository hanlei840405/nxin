package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class LayoutReportDto extends CrudDto implements Serializable {

    private Long layoutId;

    private Long reportId;

    private Integer frequency;

    private Integer x;

    private Integer y;

    private Integer w;

    private Integer h;
}
