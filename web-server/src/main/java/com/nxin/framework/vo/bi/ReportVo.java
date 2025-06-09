package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReportVo extends BaseVo {

    private String code;
    private String name;
    private Long projectId;
    private Long chartId;
    private String script;
    private String mapping;
    private String description;
    private List<ModelVo> modelList;
}
