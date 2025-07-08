package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LayoutVo extends BaseVo {

    private String name;
    private Boolean authenticate;
    private String description;

    private List<LayoutReportVo> layoutReportList;
}
