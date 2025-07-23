package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChartDto extends CrudDto implements Serializable {

    @NotNull
    private String code;
    @NotNull
    private String name;
    @NotNull
    private String category;
    @NotNull
    private String options;
    @NotNull
    private String data;
    private String description;
    private List<ChartParamsDto> chartParamsList;
}
