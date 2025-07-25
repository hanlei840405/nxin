package com.nxin.framework.vo.bi;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChartVo extends BaseVo {

    private String code;
    private String name;
    private String category;
    private String options;
    private String data;
    private String description;
    private Boolean publish;
    private LocalDateTime publishTime;
    private List<ChartParamsVo> chartParamsList;
}
