package com.nxin.framework.dto.kettle;

import com.nxin.framework.dto.CrudDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class AttachmentStorageDto extends CrudDto {
    private Long projectId;
    private Long shellParentId;
    private String category;
}
