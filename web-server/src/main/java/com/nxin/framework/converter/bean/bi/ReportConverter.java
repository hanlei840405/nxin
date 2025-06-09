package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Report;
import com.nxin.framework.vo.bi.ReportVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter extends BeanConverter<ReportVo, Report> {

    @Override
    public ReportVo convert(Report report) {
        ReportVo reportVo = new ReportVo();
        BeanUtils.copyProperties(report, reportVo);
        return reportVo;
    }

    @Override
    public List<ReportVo> convert(List<Report> reportList) {
        return reportList.stream().map(this::convert).collect(Collectors.toList());
    }
}
