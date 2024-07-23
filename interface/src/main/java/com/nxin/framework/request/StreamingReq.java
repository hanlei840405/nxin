package com.nxin.framework.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class StreamingReq implements Serializable {
    private String payload;
}
