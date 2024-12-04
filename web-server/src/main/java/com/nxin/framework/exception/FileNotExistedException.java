package com.nxin.framework.exception;

public class FileNotExistedException extends RuntimeException {
    public FileNotExistedException(String message) {
        super(message);
    }
}
