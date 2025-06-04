package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
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
@Builder
@Data
public class DictionaryDto extends CrudDto {

    private Long id;
    private String code;
    private String name;
    private String description;
    private List<DictionaryItemDto> dictionaryItemList;
}
