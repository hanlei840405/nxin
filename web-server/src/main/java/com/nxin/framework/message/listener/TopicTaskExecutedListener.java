package com.nxin.framework.message.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.kettle.ShellPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TopicTaskExecutedListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ShellPublishService shellPublishService;

    @Transactional
    public void onSuccess(String message) {
        log.info("message on success: {}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        Long shellPublishId = jsonObject.getLong("shellPublishId");
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "task_completed");
        ShellPublish shellPublish = shellPublishService.one(shellPublishId);
        simpMessagingTemplate.convertAndSendToUser(shellPublish.getModifier(), Constant.WEB_SOCKET_DESTINATION_MESSAGE, jsonObject, headers);
    }

    @Transactional
    public void onFailure(String message) {
        log.info("message on failure: {}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        Long shellPublishId = jsonObject.getLong("shellPublishId");
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "task_failure");
        ShellPublish shellPublish = shellPublishService.one(shellPublishId);
        simpMessagingTemplate.convertAndSendToUser(shellPublish.getModifier(), Constant.WEB_SOCKET_DESTINATION_MESSAGE, jsonObject, headers);
    }
}
