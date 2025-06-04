package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ModelVo extends BaseVo {

    private String code;
    private String name;
    private String description;
    private Long datasourceId;
    private Long projectId;
    private List<MetadataVo> metadataList;
}
