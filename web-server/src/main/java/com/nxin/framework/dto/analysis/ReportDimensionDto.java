package com.nxin.framework.dto.analysis;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReportDimensionDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String expr;
    private String graph;
    private String anchor;
    private String category;
    private String description;
    private Long reportId;
}
