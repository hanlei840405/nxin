package com.nxin.framework.dto.analysis;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ModelDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String description;
    private Long datasourceId;
    private Long projectId;
}
