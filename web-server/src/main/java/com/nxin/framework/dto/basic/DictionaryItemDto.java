package com.nxin.framework.dto.basic;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

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

    private Long dictionaryId;
    @NotNull
    private String name;
    @NotNull
    private String value;
    private String description;
}
