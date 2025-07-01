package com.nxin.framework.message.sender;

import com.nxin.framework.utils.SpringContextUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisSender implements Sender {

    @Override
    public void send(String topic, String payload) {
        StringRedisTemplate stringRedisTemplate = SpringContextUtils.getBean(StringRedisTemplate.class);
        stringRedisTemplate.convertAndSend(topic, payload);
    }
}
