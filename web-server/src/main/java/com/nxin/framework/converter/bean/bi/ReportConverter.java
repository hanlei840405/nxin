package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Report;
import com.nxin.framework.vo.bi.ReportVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter extends BeanConverter<ReportVo, Report> {

    @Override
    public ReportVo convert(Report report, String... ignores) {
        ReportVo reportVo = new ReportVo();
        BeanUtils.copyProperties(report, reportVo, ignores);
        return reportVo;
    }

    @Override
    public List<ReportVo> convert(List<Report> reportList, String... ignores) {
        return reportList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
