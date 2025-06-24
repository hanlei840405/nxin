package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.ChartParams;
import com.nxin.framework.vo.bi.ChartParamsVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ChartParamsConverter extends BeanConverter<ChartParamsVo, ChartParams> {

    @Override
    public ChartParamsVo convert(ChartParams chartParams, String... ignores) {
        ChartParamsVo chartParamsVo = new ChartParamsVo();
        BeanUtils.copyProperties(chartParams, chartParamsVo, ignores);
        return chartParamsVo;
    }

    @Override
    public List<ChartParamsVo> convert(List<ChartParams> chartParamsList, String... ignores) {
        return chartParamsList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
