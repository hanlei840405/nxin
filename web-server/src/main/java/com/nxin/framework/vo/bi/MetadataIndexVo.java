package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class MetadataIndexVo extends BaseVo {

    private String indexCode;
    private String indexName;
    private String indexCategory;
    private String metadataIdArray;
}
