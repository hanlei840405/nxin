package com.nxin.framework.vo.task;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RunningProcessVo extends BaseVo {

    private String category;

    private String instanceId;

    private String instanceName;

    private String owner;

    private String prod;

    private Long shellId;

    private Long shellPublishId;
}
