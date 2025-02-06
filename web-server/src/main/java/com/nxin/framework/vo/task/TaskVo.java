package com.nxin.framework.vo.task;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskVo extends BaseVo {

    private String name;
    private String description;
    private String cron;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;
    private String state;
    private Integer misfire;
}
