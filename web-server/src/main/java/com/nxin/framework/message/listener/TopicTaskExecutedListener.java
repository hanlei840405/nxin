package com.nxin.framework.message.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class TopicTaskExecutedListener {

    @Transactional
    public void onSuccess(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        JSONObject jsonObject = JSON.parseObject(message);
        String name = jsonObject.getString("name");
        String taskHistoryId = jsonObject.getString("taskHistoryId");
        Date fireTime = jsonObject.getDate("fireTime");
        log.info("{}, {}, {}", name, taskHistoryId, sdf.format(fireTime));
    }

    @Transactional
    public void onFailure(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        JSONObject jsonObject = JSON.parseObject(message);
        String name = jsonObject.getString("name");
        String taskHistoryId = jsonObject.getString("taskHistoryId");
        Date fireTime = jsonObject.getDate("fireTime");
        log.info("{}, {}, {}", name, taskHistoryId, sdf.format(fireTime));
    }
}
