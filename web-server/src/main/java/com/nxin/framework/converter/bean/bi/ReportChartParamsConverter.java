package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.ReportChartParams;
import com.nxin.framework.vo.bi.ReportChartParamsVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ReportChartParamsConverter extends BeanConverter<ReportChartParamsVo, ReportChartParams> {

    @Override
    public ReportChartParamsVo convert(ReportChartParams reportChartParams, String... ignores) {
        ReportChartParamsVo reportChartParamsVo = new ReportChartParamsVo();
        BeanUtils.copyProperties(reportChartParams, reportChartParamsVo, ignores);
        return reportChartParamsVo;
    }

    @Override
    public List<ReportChartParamsVo> convert(List<ReportChartParams> reportChartParams, String... ignores) {
        return reportChartParams.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
