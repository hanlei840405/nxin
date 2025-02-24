package com.nxin.framework.vo.kettle;

import com.nxin.framework.vo.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttachmentStorageVo extends BaseVo {

    private Long projectId;
    private Long shellId;
    private String shellName;
    private Long shellParentId;
    private String component;
    private String componentName;
    private Integer category;
    private String storageDir;
    private String storageDirRelative;
    private String status;
    private LocalDateTime createTime;
    private String creator;
    private String modifier;
    private LocalDateTime modifyTime;
}
