package com.nxin.framework.vo.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class LayoutVo extends CrudDto implements Serializable {

    private String code;
    private String name;
    private String resourceCode;
    private String description;
    private String arrange;
}
