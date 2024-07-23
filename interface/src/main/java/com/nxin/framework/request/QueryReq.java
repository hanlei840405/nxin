package com.nxin.framework.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QueryReq implements Serializable {
    private List<String> groupList;
}
