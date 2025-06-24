package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.vo.bi.ModelVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter extends BeanConverter<ModelVo, Model> {

    @Override
    public ModelVo convert(Model model, String... ignores) {
        ModelVo modelVo = new ModelVo();
        BeanUtils.copyProperties(model, modelVo, ignores);
        return modelVo;
    }

    @Override
    public List<ModelVo> convert(List<Model> modelList, String... ignores) {
        return modelList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
