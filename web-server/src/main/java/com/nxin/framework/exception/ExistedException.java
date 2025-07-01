package com.nxin.framework.exception;

public class ExistedException extends RuntimeException {
    public ExistedException() {
    }

    public ExistedException(String message) {
        super(message);
    }
}
