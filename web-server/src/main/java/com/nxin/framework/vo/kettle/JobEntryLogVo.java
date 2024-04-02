package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class JobEntryLogVo extends BaseVo {
    private Integer idBatch;
    private String channelId;
    private Date logDate;
    private String transName;
    private String stepName;
    private Long linesRead;
    private Long linesWritten;
    private Long linesUpdated;
    private Long linesInput;
    private Long linesOutput;
    private Long linesRejected;
    private Long errors;
    private String result;
    private Long nrResultRows;
    private Long nrResultFiles;
}