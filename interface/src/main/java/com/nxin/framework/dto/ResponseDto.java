package com.nxin.framework.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class ResponseDto<T> implements Serializable {
    private boolean success;

    private T data;

    private String message;
}
