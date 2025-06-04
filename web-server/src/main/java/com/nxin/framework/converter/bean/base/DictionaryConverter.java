package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Dictionary;
import com.nxin.framework.vo.basic.DictionaryVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryConverter extends BeanConverter<DictionaryVo, Dictionary> {

    @Override
    public DictionaryVo convert(Dictionary dictionary) {
        DictionaryVo dictionaryVo = new DictionaryVo();
        BeanUtils.copyProperties(dictionary, dictionaryVo);
        return dictionaryVo;
    }

    @Override
    public List<DictionaryVo> convert(List<Dictionary> dictionaries) {
        return dictionaries.stream().map(this::convert).collect(Collectors.toList());
    }
}
