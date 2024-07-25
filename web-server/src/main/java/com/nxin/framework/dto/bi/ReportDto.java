package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReportDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String script;
    private String chart;
    private String mode;
    private String description;
    private Long projectId;
    private Long modelId;
}
