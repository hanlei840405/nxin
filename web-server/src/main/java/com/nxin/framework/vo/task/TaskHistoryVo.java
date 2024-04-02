package com.nxin.framework.vo.task;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskHistoryVo implements Serializable {
    private Long id;

    private String logChannelId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long shellPublishId;

    private String status;
}
