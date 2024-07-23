package com.nxin.framework.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskReq implements Serializable {
    private String group;
    private String id;
    private String description;
    private String cron;
    private Integer misfire;
    private String data;
}
