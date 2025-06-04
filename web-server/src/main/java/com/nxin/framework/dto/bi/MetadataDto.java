package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetadataDto extends CrudDto implements Serializable {

    private String columnName;
    private String columnCode;
    private String columnCategory;
    private Integer columnLength;
    private Integer columnDecimal;
    private Boolean columnNotNull;
    private Long columnForeignModelId;
    private Long modelId;
    private Integer version;
}
