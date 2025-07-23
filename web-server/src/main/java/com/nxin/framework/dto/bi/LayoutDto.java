package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LayoutDto extends CrudDto implements Serializable {

    @NotNull
    private String name;
    private Boolean authenticate;
    private String description;

    private List<LayoutReportDto> layoutReportList;
}
