package com.nxin.framework.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CronTriggerRes implements Serializable {

    private String triggerKey;
    private String name;
    private String description;
    private String cron;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;
    private String state;
    private Integer misfire;
    private Long shellId;
}
