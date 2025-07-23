package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChartParamsDto extends CrudDto implements Serializable {
    @NotNull
    private String field;
    @NotNull
    private String category;
    private String description;
    private Long parentId;
    private String path;
    private Long chartId;
}
