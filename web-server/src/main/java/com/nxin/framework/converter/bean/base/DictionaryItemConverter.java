package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.DictionaryItem;
import com.nxin.framework.vo.basic.DictionaryItemVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryItemConverter extends BeanConverter<DictionaryItemVo, DictionaryItem> {

    @Override
    public DictionaryItemVo convert(DictionaryItem dictionaryItem, String... ignores) {
        DictionaryItemVo dictionaryItemVo = new DictionaryItemVo();
        BeanUtils.copyProperties(dictionaryItem, dictionaryItemVo, ignores);
        return dictionaryItemVo;
    }

    @Override
    public List<DictionaryItemVo> convert(List<DictionaryItem> dictionaryItems, String... ignores) {
        return dictionaryItems.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
