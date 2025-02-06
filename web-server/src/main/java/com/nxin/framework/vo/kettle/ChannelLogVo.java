package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class ChannelLogVo extends BaseVo {
    private Integer idBatch;
    private String channelId;
    private String loggingObjectType;
    private String objectName;
    private String objectCopy;
    private String repositoryDirectory;
    private String filename;
    private String objectId;
    private String objectRevision;
    private String parentChannelId;
    private String rootChannelId;
    private Date logDate;
}