package com.nxin.framework.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class CrudDto implements Serializable {
    private Long id;
    private String payload;
    private int pageNo;
    private int pageSize;
    private String status;
    private Date createTime;
    private String creator;
    private String modifier;
    private List<Long> idList;
}
