package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.basic.DictionaryItem;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.DictionaryItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典项 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-05-29
 */
@Service
public class DictionaryItemService extends ServiceImpl<DictionaryItemMapper, DictionaryItem> {

    public List<DictionaryItem> all(Long dictionaryId) {
        LambdaQueryWrapper<DictionaryItem> dictionaryItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getStatus, Constant.ACTIVE);
        dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getDictionaryId, dictionaryId);
        return getBaseMapper().selectList(dictionaryItemLambdaQueryWrapper);
    }
}
