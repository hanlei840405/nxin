package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class LayoutDto extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String resourceCode;
    private String description;
    private String arrange;
}
