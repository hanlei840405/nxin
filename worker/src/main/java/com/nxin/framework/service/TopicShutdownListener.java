package com.nxin.framework.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.service.task.TaskHistoryService;
import org.pentaho.di.job.Job;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class TopicShutdownListener {
    @Autowired
    private TaskHistoryService taskHistoryService;
    @Autowired
    private RunningProcessService runningProcessService;

    @Transactional
    public void onMessage(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        String taskHistoryId = jsonObject.getString("taskHistoryId");
        String name = jsonObject.getString("name");
        String instanceId = jsonObject.getString("instanceId");
        CarteObjectEntry carteObjectEntry = new CarteObjectEntry(name, instanceId);
        Job job = CarteSingleton.getInstance().getJobMap().getJob(carteObjectEntry);
        if (job != null) {
            job.stopAll();
            CarteSingleton.getInstance().getJobMap().removeJob(carteObjectEntry);
            TaskHistory taskHistory = taskHistoryService.getById(Long.valueOf(taskHistoryId));
            if (taskHistory != null) {
                taskHistory.setEndTime(LocalDateTime.now());
                taskHistoryService.updateById(taskHistory);
            }
        }
        runningProcessService.delete(runningProcessService.instanceId(instanceId));
    }
}
