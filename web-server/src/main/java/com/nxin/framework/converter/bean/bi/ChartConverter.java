package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Chart;
import com.nxin.framework.vo.bi.ChartVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ChartConverter extends BeanConverter<ChartVo, Chart> {

    @Override
    public ChartVo convert(Chart chart, String... ignores) {
        ChartVo chartVo = new ChartVo();
        BeanUtils.copyProperties(chart, chartVo, ignores);
        return chartVo;
    }

    @Override
    public List<ChartVo> convert(List<Chart> chartList, String... ignores) {
        return chartList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
