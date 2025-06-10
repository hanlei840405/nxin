package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.basic.Dictionary;
import com.nxin.framework.entity.basic.DictionaryItem;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.DictionaryMapper;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-05-29
 */
@Service
public class DictionaryService extends ServiceImpl<DictionaryMapper, Dictionary> {

    @Autowired
    private DictionaryItemService dictionaryItemService;

    public IPage<Dictionary> search(String payload, int pageNo, int pageSize) {
        Page<Dictionary> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(payload)) {
            queryWrapper.likeRight(Dictionary::getName, payload);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Dictionary dictionary, List<DictionaryItem> dictionaryItems) {
        int upsert;
        if (dictionary.getId() != null) {
            Dictionary persisted = getBaseMapper().selectById(dictionary.getId());
            BeanUtils.copyProperties(dictionary, persisted, "version");
            dictionary.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(persisted);
        } else {
            dictionary.setStatus(Constant.ACTIVE);
            dictionary.setVersion(1);
            dictionary.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(dictionary);
        }
        LambdaQueryWrapper<DictionaryItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictionaryItem::getDictionaryId, dictionary.getId());
        dictionaryItemService.remove(queryWrapper);
        dictionaryItems.forEach(dictionaryItem -> {
            dictionaryItem.setDictionaryId(dictionary.getId());
            dictionaryItem.setStatus(Constant.ACTIVE);
            dictionaryItem.setCreator(LoginUtils.getUsername());
            dictionaryItem.setVersion(1);
        });
        dictionaryItemService.saveBatch(dictionaryItems);
        return upsert > 0;
    }

    @Transactional
    public void delete(Long id) {
        LambdaQueryWrapper<Dictionary> dictionaryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictionaryLambdaQueryWrapper.eq(Dictionary::getId, id);
        getBaseMapper().delete(dictionaryLambdaQueryWrapper);
        LambdaQueryWrapper<DictionaryItem> dictionaryItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getDictionaryId, id);
        dictionaryItemService.remove(dictionaryItemLambdaQueryWrapper);
    }
}
