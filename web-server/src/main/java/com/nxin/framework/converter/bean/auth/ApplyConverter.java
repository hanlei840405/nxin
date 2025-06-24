package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.Apply;
import com.nxin.framework.vo.auth.ApplyVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ApplyConverter extends BeanConverter<ApplyVo, Apply> {

    @Override
    public ApplyVo convert(Apply apply, String... ignores) {
        ApplyVo chartVo = new ApplyVo();
        BeanUtils.copyProperties(apply, chartVo, ignores);
        return chartVo;
    }

    @Override
    public List<ApplyVo> convert(List<Apply> applyList, String... ignores) {
        return applyList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
