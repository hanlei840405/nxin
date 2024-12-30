package com.nxin.framework.event.listener;

import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.event.JobExecuteEvent;
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
public class JobExecuteListener {
    @Autowired
    private LogService logService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RunningProcessService runningProcessService;
    @Value("${etl.log.send-delay}")
    private Integer sendDelay = 5;

    @Async
    @EventListener(JobExecuteEvent.class)
    public void action(JobExecuteEvent jobExecuteEvent) {
        RunningProcess runningProcess = (RunningProcess) jobExecuteEvent.getSource();
        LoggingRegistry loggingRegistry = LoggingRegistry.getInstance();
        try {
            CarteSingleton.getInstance().getJobMap().addJob(jobExecuteEvent.getJob().getName(), jobExecuteEvent.getInstanceId(), jobExecuteEvent.getJob(), jobExecuteEvent.getJobConfiguration());
            // 空参调用
            jobExecuteEvent.getJob().setLogLevel(LogLevel.BASIC);
            KettleLoggingEventListener kettleLoggingEventListener = kettleLoggingEvent -> {
                LogMessageInterface logMessageInterface = (LogMessageInterface) kettleLoggingEvent.getMessage();
                List<String> childrenLogChannelIds = loggingRegistry.getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
                log.info("current: {}", logMessageInterface.getLogChannelId());
                if (childrenLogChannelIds.contains(logMessageInterface.getLogChannelId())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("log", logMessageInterface.toString());
                    response.put("steps", Collections.EMPTY_LIST);
                    if (jobExecuteEvent.getJob().isFinished() || jobExecuteEvent.getJob().isStopped()) {
                        response.put("running", false);
                    } else {
                        response.put("running", true);
                    }
                    simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), response);
                }
            };
            KettleLogStore.getAppender().addLoggingEventListener(kettleLoggingEventListener);
            jobExecuteEvent.getJob().start();
            jobExecuteEvent.getJob().waitUntilFinished();
            KettleLogStore.getAppender().removeLoggingEventListener(kettleLoggingEventListener);
            CarteSingleton.getInstance().getJobMap().removeJob(new CarteObjectEntry(jobExecuteEvent.getJob().getName(), jobExecuteEvent.getInstanceId()));
            runningProcessService.delete(runningProcess);
            List<String> logChannelIds = LoggingRegistry.getInstance().getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
            Map<String, Object> response = logService.fetchLogs(logChannelIds);
            response.put("log", KettleLogStore.getAppender().getBuffer(jobExecuteEvent.getJob().getLogChannelId(), true).toString());
            if (jobExecuteEvent.getJob().isFinished() || jobExecuteEvent.getJob().isStopped()) {
                response.put("running", false);
            } else {
                response.put("running", true);
            }
            simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), response);
            KettleLogStore.discardLines(jobExecuteEvent.getJob().getLogChannelId(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            List<String> logChannelIds = loggingRegistry.getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
//            Map<String, Object> error = logService.fetchLogs(logChannelIds);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e + "\r\n" + e.getStackTrace()[0].toString());
            simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), error);
        }
    }

//    @Async
//    @EventListener(JobExecuteEvent.class)
//    public void action(JobExecuteEvent jobExecuteEvent) {
//        RunningProcess runningProcess = (RunningProcess) jobExecuteEvent.getSource();
//        LoggingRegistry loggingRegistry = LoggingRegistry.getInstance();
//        try {
//            CarteSingleton.getInstance().getJobMap().addJob(jobExecuteEvent.getJob().getName(), jobExecuteEvent.getInstanceId(), jobExecuteEvent.getJob(), jobExecuteEvent.getJobConfiguration());
//            jobExecuteEvent.getJob().start();
//            Map<String, Object> response;
//            while (!jobExecuteEvent.getJob().isStopped() && !jobExecuteEvent.getJob().isFinished()) {
//                List<String> logChannelIds = loggingRegistry.getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
//                StringBuffer buffer = KettleLogStore.getAppender().getBuffer(jobExecuteEvent.getJob().getLogChannelId(), true);
//                KettleLogStore.discardLines(jobExecuteEvent.getJob().getLogChannelId(), false);
////                loggingRegistry.removeIncludingChildren(jobExecuteEvent.getJob().getLogChannelId());
//                response = logService.fetchLogs(logChannelIds);
//                response.put("log", buffer);
//                response.put("running", true);
//                simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), response);
//                TimeUnit.SECONDS.sleep(sendDelay);
//            }
//            CarteSingleton.getInstance().getTransformationMap().removeTransformation(new CarteObjectEntry(jobExecuteEvent.getJob().getName(), jobExecuteEvent.getInstanceId()));
//            jobExecuteEvent.getJob().waitUntilFinished();
//            runningProcessService.delete(runningProcess);
//            List<String> logChannelIds = LoggingRegistry.getInstance().getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
//            response = logService.fetchLogs(logChannelIds);
//            StringBuffer buffer = KettleLogStore.getAppender().getBuffer(jobExecuteEvent.getJob().getLogChannelId(), true);
//            KettleLogStore.discardLines(jobExecuteEvent.getJob().getLogChannelId(), true);
//            response.put("log", buffer);
//            response.put("running", false);
//            simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), response);
//        } catch (Exception e) {
////            List<String> logChannelIds = loggingRegistry.getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
////            Map<String, Object> error = logService.fetchLogs(logChannelIds);
//            Map<String, Object> error = new HashMap<>();
//            error.put("error", e + "\r\n" + e.getStackTrace()[0].toString());
//            simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), error);
//        }
//    }
}
