package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.DictionaryItem;
import com.nxin.framework.vo.basic.DictionaryItemVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryItemConverter extends BeanConverter<DictionaryItemVo, DictionaryItem> {

    @Override
    public DictionaryItemVo convert(DictionaryItem dictionaryItem) {
        DictionaryItemVo dictionaryItemVo = new DictionaryItemVo();
        BeanUtils.copyProperties(dictionaryItem, dictionaryItemVo);
        return dictionaryItemVo;
    }

    @Override
    public List<DictionaryItemVo> convert(List<DictionaryItem> dictionaryItems) {
        return dictionaryItems.stream().map(this::convert).collect(Collectors.toList());
    }
}
