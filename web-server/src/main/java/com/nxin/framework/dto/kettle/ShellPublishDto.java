package com.nxin.framework.dto.kettle;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.util.Date;

@Data
public class ShellPublishDto extends CrudDto {

    private String name;
    private String businessId;
    private String description;
    private String reference;
    private String prod;
    private String content;
    private String streaming;
    private String taskId;
    private Long shellId;
    private Date deployTime;
    private String cron;
    private Integer misfire;
}
