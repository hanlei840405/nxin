package com.nxin.framework.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BaseVo implements Serializable {
    private Long id;
    private LocalDateTime createTime;
    private String creator;
    private LocalDateTime modifyTime;
    private String modifier;
}
