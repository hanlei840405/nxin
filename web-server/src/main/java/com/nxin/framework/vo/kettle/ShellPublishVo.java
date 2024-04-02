package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;

import java.util.Date;

@Data
public class ShellPublishVo extends BaseVo {

    private String name;
    private String businessId;
    private String description;
    private String reference;
    private String prod;
    private String content;
    private String streaming;
    private String taskId;
    private Date deployTime;
    private ShellVo shell;
    private String cron;
    private String shellName;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;
    private String state;
}
