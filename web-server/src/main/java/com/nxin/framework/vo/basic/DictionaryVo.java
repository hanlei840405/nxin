package com.nxin.framework.vo.basic;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 字典
 * </p>
 *
 * @author jesse han
 * @since 2025-05-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryVo extends BaseVo {

    private Long id;
    private String code;
    private String name;
    private String description;
    private List<DictionaryItemVo> dictionaryItemList;
}
