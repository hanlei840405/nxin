package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 元数据字段索引
 * </p>
 *
 * @author jesse han
 * @since 2025-05-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetadataIndexDto extends CrudDto implements Serializable {

    private String indexCode;
    private String indexName;
    private String indexCategory;
    private String metadataIdArray;
}
