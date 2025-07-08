package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.LayoutReport;
import com.nxin.framework.vo.bi.LayoutReportVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class LayoutReportConverter extends BeanConverter<LayoutReportVo, LayoutReport> {

    @Override
    public LayoutReportVo convert(LayoutReport layoutReport, String... ignores) {
        LayoutReportVo layoutReportVo = new LayoutReportVo();
        BeanUtils.copyProperties(layoutReport, layoutReportVo, ignores);
        return layoutReportVo;
    }

    @Override
    public List<LayoutReportVo> convert(List<LayoutReport> layoutReportList, String... ignores) {
        return layoutReportList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
