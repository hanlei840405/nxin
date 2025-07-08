package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Layout;
import com.nxin.framework.vo.bi.LayoutVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class LayoutConverter extends BeanConverter<LayoutVo, Layout> {

    @Override
    public LayoutVo convert(Layout layout, String... ignores) {
        LayoutVo layoutVo = new LayoutVo();
        BeanUtils.copyProperties(layout, layoutVo, ignores);
        return layoutVo;
    }

    @Override
    public List<LayoutVo> convert(List<Layout> layoutList, String... ignores) {
        return layoutList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
