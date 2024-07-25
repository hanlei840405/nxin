package com.nxin.framework.dto.bi;

import com.nxin.framework.dto.CrudDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class MetadataDto extends CrudDto implements Serializable {

    private String columnCode;
    private String columnName;
    private String columnCategory;
    private int columnLength;
    private int columnDecimal;
    private boolean primaryKey;
    private boolean notNull;
    private boolean autoIncrement;
    private Long modelId;
}
