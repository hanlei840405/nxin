package com.nxin.framework.controller.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.base.DictionaryConverter;
import com.nxin.framework.converter.bean.base.DictionaryItemConverter;
import com.nxin.framework.dto.basic.DictionaryDto;
import com.nxin.framework.entity.basic.Dictionary;
import com.nxin.framework.entity.basic.DictionaryItem;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.basic.DictionaryItemService;
import com.nxin.framework.service.basic.DictionaryService;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.basic.DictionaryItemVo;
import com.nxin.framework.vo.basic.DictionaryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private DictionaryItemService dictionaryItemService;

    private final BeanConverter<DictionaryVo, Dictionary> dictionaryConverter = new DictionaryConverter();
    private final BeanConverter<DictionaryItemVo, DictionaryItem> dictionaryItemConverter = new DictionaryItemConverter();

    @PreAuthorize("hasAuthority('ROOT')")
    @GetMapping("/dictionary/{id}")
    public ResponseEntity<DictionaryVo> one(@PathVariable Long id) {
        LambdaQueryWrapper<Dictionary> dictionaryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictionaryLambdaQueryWrapper.eq(Dictionary::getStatus, Constant.ACTIVE);
        dictionaryLambdaQueryWrapper.eq(Dictionary::getId, id);
        Dictionary dictionary = dictionaryService.getOne(dictionaryLambdaQueryWrapper);
        if (dictionary != null) {
            LambdaQueryWrapper<DictionaryItem> dictionaryItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getStatus, Constant.ACTIVE);
            dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getDictionaryId, id);
            List<DictionaryItem> dictionaryItems = dictionaryItemService.list(dictionaryItemLambdaQueryWrapper);
            DictionaryVo dictionaryVo = dictionaryConverter.convert(dictionary);
            dictionaryVo.setDictionaryItemList(dictionaryItemConverter.convert(dictionaryItems));
            return ResponseEntity.ok(dictionaryVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @GetMapping("/dictionaryItems/{code}")
    public ResponseEntity<List<DictionaryItemVo>> dictionaryItems(@PathVariable String code) {
        LambdaQueryWrapper<Dictionary> dictionaryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dictionaryLambdaQueryWrapper.eq(Dictionary::getStatus, Constant.ACTIVE);
        dictionaryLambdaQueryWrapper.eq(Dictionary::getCode, code);
        Dictionary dictionary = dictionaryService.getOne(dictionaryLambdaQueryWrapper);
        if (dictionary != null) {
            LambdaQueryWrapper<DictionaryItem> dictionaryItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getStatus, Constant.ACTIVE);
            dictionaryItemLambdaQueryWrapper.eq(DictionaryItem::getDictionaryId, dictionary.getId());
            List<DictionaryItem> dictionaryItems = dictionaryItemService.list(dictionaryItemLambdaQueryWrapper);
            return ResponseEntity.ok(dictionaryItemConverter.convert(dictionaryItems));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PreAuthorize("hasAuthority('ROOT')")
    @PostMapping("/dictionaries")
    public ResponseEntity<PageVo<DictionaryVo>> page(@RequestBody DictionaryDto dictionaryDto) {
        IPage<Dictionary> dictionaryIPage = dictionaryService.search(dictionaryDto.getPayload(), dictionaryDto.getPageNo(), dictionaryDto.getPageSize());
        return ResponseEntity.ok(new PageVo<>(dictionaryIPage.getTotal(), dictionaryConverter.convert(dictionaryIPage.getRecords())));
    }

    @PreAuthorize("hasAuthority('ROOT')")
    @PostMapping("/dictionary")
    public ResponseEntity<DictionaryVo> save(@RequestBody DictionaryDto dictionaryDto) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDto, dictionary);
        List<DictionaryItem> dictionaryItems = dictionaryDto.getDictionaryItemList().stream().map(dto -> {
            DictionaryItem dictionaryItem = new DictionaryItem();
            BeanUtils.copyProperties(dto, dictionaryItem, "id");
            return dictionaryItem;
        }).collect(Collectors.toList());
        dictionaryService.save(dictionary, dictionaryItems);
        return ResponseEntity.ok(dictionaryConverter.convert(dictionary));
    }

    @PreAuthorize("hasAuthority('ROOT')")
    @DeleteMapping("/dictionary/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        dictionaryService.delete(id);
        return ResponseEntity.ok().build();
    }
}
