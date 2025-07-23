package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ModelDto extends CrudDto implements Serializable {

    @NotNull
    private String code;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Long datasourceId;
    @NotNull
    private Long projectId;
    private List<MetadataDto> metadataList;
}
