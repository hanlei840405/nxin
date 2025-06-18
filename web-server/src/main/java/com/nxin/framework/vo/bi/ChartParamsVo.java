package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChartParamsVo extends BaseVo {
    private String field;
    private String category;
    private String description;
    private Long parentId;
    private String path;
    private Long chartId;

}
