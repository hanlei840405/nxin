package com.nxin.framework.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.kettle.RunningProcessService;
import org.pentaho.di.job.Job;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TopicShutdownListener {
    @Autowired
    private RunningProcessService runningProcessService;

    @Transactional
    public void onMessage(String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        String name = jsonObject.getString("name");
        String instanceId = jsonObject.getString("instanceId");
        String category = jsonObject.getString("category");
        RunningProcess runningProcess = runningProcessService.instanceId(instanceId);
        CarteObjectEntry carteObjectEntry = new CarteObjectEntry(name, instanceId);
        if (Constant.JOB.equals(category)) {
            Job job = CarteSingleton.getInstance().getJobMap().getJob(carteObjectEntry);
            if (job != null) {
                job.stopAll();
                CarteSingleton.getInstance().getJobMap().removeJob(carteObjectEntry);
            }
        } else {
            Trans trans = CarteSingleton.getInstance().getTransformationMap().getTransformation(carteObjectEntry);
            if (trans != null) {
                trans.stopAll();
                CarteSingleton.getInstance().getTransformationMap().removeTransformation(carteObjectEntry);
            }
        }
        if (runningProcess != null) {
            runningProcessService.delete(runningProcess);
        }
    }
}
