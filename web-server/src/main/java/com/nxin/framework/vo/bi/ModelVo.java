package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;

import java.util.List;

@Data
public class ModelVo extends BaseVo {
    private String code;
    private String description;
    private String name;
    private Long datasourceId;
    private Long projectId;
    private List<MetadataVo> metadataList;
}
