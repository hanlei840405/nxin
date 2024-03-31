package com.nxin.framework.event.listener;

import com.alibaba.fastjson.JSON;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.event.TransformExecuteEvent;
import com.nxin.framework.service.kettle.LogService;
import com.nxin.framework.service.kettle.RunningProcessService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TransformExecuteListener {
    @Autowired
    private LogService logService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RunningProcessService runningProcessService;
    @Value("${etl.log.send-delay}")
    private Integer sendDelay = 5;

    @Async
    @EventListener(TransformExecuteEvent.class)
    public void action(TransformExecuteEvent transformExecuteEvent) {
        RunningProcess runningProcess = (RunningProcess) transformExecuteEvent.getSource();
        LoggingRegistry loggingRegistry = LoggingRegistry.getInstance();
        try {
            CarteSingleton.getInstance().getTransformationMap().addTransformation(transformExecuteEvent.getTrans().getName(), transformExecuteEvent.getInstanceId(), transformExecuteEvent.getTrans(), transformExecuteEvent.getTransConfiguration());
            // 空参调用
            transformExecuteEvent.getTrans().setLogLevel(LogLevel.BASIC);
            transformExecuteEvent.getTrans().execute(new String[]{});
            KettleLoggingEventListener kettleLoggingEventListener = kettleLoggingEvent -> {
                LogMessageInterface logMessageInterface = (LogMessageInterface) kettleLoggingEvent.getMessage();
                List<String> childrenLogChannelIds = loggingRegistry.getLogChannelChildren(transformExecuteEvent.getTrans().getLogChannelId());
                log.info("current: {}", logMessageInterface.getLogChannelId());
                if (childrenLogChannelIds.contains(logMessageInterface.getLogChannelId())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("log", logMessageInterface.toString());
                    response.put("steps", Collections.EMPTY_LIST);
                    if (transformExecuteEvent.getTrans().isFinished() || transformExecuteEvent.getTrans().isStopped() || transformExecuteEvent.getTrans().isPaused()) {
                        response.put("running", false);
                    } else {
                        response.put("running", true);
                    }
                    simpMessagingTemplate.convertAndSend(transformExecuteEvent.getInstanceId(), response);
                }
            };
            KettleLogStore.getAppender().addLoggingEventListener(kettleLoggingEventListener);
            transformExecuteEvent.getTrans().waitUntilFinished();
            KettleLogStore.getAppender().removeLoggingEventListener(kettleLoggingEventListener);
            CarteSingleton.getInstance().getTransformationMap().removeTransformation(new CarteObjectEntry(transformExecuteEvent.getTrans().getName(), transformExecuteEvent.getInstanceId()));
            runningProcessService.delete(runningProcess);
            List<String> logChannelIds = LoggingRegistry.getInstance().getLogChannelChildren(transformExecuteEvent.getTrans().getLogChannelId());
            Map<String, Object> response = logService.fetchLogs(logChannelIds);
            response.put("log", KettleLogStore.getAppender().getBuffer(transformExecuteEvent.getTrans().getLogChannelId(), true).toString());
            if (transformExecuteEvent.getTrans().isFinished() || transformExecuteEvent.getTrans().isStopped() || transformExecuteEvent.getTrans().isPaused()) {
                response.put("running", false);
            } else {
                response.put("running", true);
            }
            simpMessagingTemplate.convertAndSend(transformExecuteEvent.getInstanceId(), response);
            KettleLogStore.discardLines(transformExecuteEvent.getTrans().getLogChannelId(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            List<String> logChannelIds = loggingRegistry.getLogChannelChildren(transformExecuteEvent.getTrans().getLogChannelId());
//            Map<String, Object> error = logService.fetchLogs(logChannelIds);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e + "\r\n" + e.getStackTrace()[0].toString());
            simpMessagingTemplate.convertAndSend(transformExecuteEvent.getInstanceId(), error);
        }
    }
}
