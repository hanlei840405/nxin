package com.nxin.framework.dto.task;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.util.Date;

@Data
public class TaskHistoryDto extends CrudDto {

    private Long shellPublishId;
    private String name;
    private String beginTime;
    private String endTime;
}
