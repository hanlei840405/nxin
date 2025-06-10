package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChartDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String category;
    private String option;
    private String data;
    private String description;
}
