package com.nxin.framework.message.sender;

public interface Sender {
    void send(String topic, String payload);
}
