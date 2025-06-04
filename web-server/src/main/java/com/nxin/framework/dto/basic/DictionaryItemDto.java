package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
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
@Builder
@Data
public class DictionaryItemDto extends CrudDto {

    private Long id;
    private Long dictionaryId;
    private String name;
    private String value;
    private String description;
}
