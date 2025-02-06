package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class TransformLogVo extends BaseVo {
    private Integer idBatch;
    private String channelId;
    private String transName;
    private String status;
    private Long linesRead;
    private Long linesWritten;
    private Long linesUpdated;
    private Long linesInput;
    private Long linesOutput;
    private Long linesRejected;
    private Long errors;
    private Date startDate;
    private Date endDate;
    private Date logDate;
    private Date depDate;
    private Date replayDate;
    private String logField;
}