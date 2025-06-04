package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MetadataVo extends BaseVo {

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
