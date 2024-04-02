package com.nxin.framework.dto.analysis;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MetadataDto extends CrudDto implements Serializable {

    private Long id;
    private String columnCode;
    private String columnName;
    private String columnCategory;
    private int columnLength;
    private int columnDecimal;
    private boolean primaryKey;
    private boolean notNull;
    private boolean autoIncrement;
    private Long modelId;
    private String status;
    private Date createTime;
    private Date modifyTime;
    private String creator;
    private String modifier;
    private int version;
}
