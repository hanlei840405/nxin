package com.nxin.framework.message.sender;

import com.nxin.framework.utils.SpringContextUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SenderUtils {
    private static final String PROPERTY_SENDER = "message.sender";
    private static final String REDIS = "redis";
    public static final Map<String, Sender> SENDER_MAP = new HashMap<>();

    public static void init() {
        SENDER_MAP.put(REDIS, new RedisSender());
    }

    public static Sender getSender() {
        Environment environment = SpringContextUtils.getBean(Environment.class);
        return SENDER_MAP.get(environment.getProperty(PROPERTY_SENDER));
    }
}
