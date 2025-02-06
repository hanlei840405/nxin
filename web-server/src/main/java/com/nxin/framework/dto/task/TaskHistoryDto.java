package com.nxin.framework.dto.task;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskHistoryDto extends CrudDto {

    private Long shellPublishId;
    private String name;
    private String beginTime;
    private String endTime;
}
