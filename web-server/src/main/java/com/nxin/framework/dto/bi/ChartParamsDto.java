package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChartParamsDto extends CrudDto implements Serializable {
    private String field;
    private String category;
    private String description;
    private Long parentId;
    private String path;
    private Long chartId;
}
