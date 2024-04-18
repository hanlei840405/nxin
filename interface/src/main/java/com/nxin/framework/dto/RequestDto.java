package com.nxin.framework.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class RequestDto<T> implements Serializable {

    private T data;
}
