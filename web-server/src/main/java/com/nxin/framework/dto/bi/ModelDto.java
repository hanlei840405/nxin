package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ModelDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String description;
    private Long datasourceId;
    private Long projectId;
    private List<MetadataDto> metadataList;
}
