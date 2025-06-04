package com.nxin.framework.vo.basic;

import com.nxin.framework.vo.BaseVo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典项
 * </p>
 *
 * @author jesse han
 * @since 2025-05-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryItemVo extends BaseVo {

    private Long id;
    private Long dictionaryId;
    private String name;
    private String value;
    private String description;
}
