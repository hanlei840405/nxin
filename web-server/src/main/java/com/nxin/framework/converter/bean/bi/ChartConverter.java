package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Chart;
import com.nxin.framework.vo.bi.ChartVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ChartConverter extends BeanConverter<ChartVo, Chart> {

    @Override
    public ChartVo convert(Chart chart) {
        ChartVo chartVo = new ChartVo();
        BeanUtils.copyProperties(chart, chartVo);
        return chartVo;
    }

    @Override
    public List<ChartVo> convert(List<Chart> chartList) {
        return chartList.stream().map(this::convert).collect(Collectors.toList());
    }
}
