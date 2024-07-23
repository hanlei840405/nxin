package com.nxin.framework.controller;

import com.nxin.framework.request.QueryReq;
import com.nxin.framework.request.StreamingReq;
import com.nxin.framework.request.TaskReq;
import com.nxin.framework.response.CronTriggerRes;
import com.nxin.framework.service.task.ScheduleService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<Date> createBatch(@RequestBody TaskReq taskReq) {
        try {
            Date date = scheduleService.createJob(taskReq);
            return ResponseEntity.ok(date);
        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/findAllCronTrigger")
    public ResponseEntity<List<CronTriggerRes>> findAllCronTrigger(@RequestBody QueryReq queryReq) {
        return ResponseEntity.ok(scheduleService.findAllCronTrigger(queryReq.getGroupList()));
    }

    @PostMapping("/pause")
    public ResponseEntity<Boolean> pause(@RequestBody TaskReq taskReq) {
        boolean value = scheduleService.pause(taskReq);
        if (value) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/resume")
    public ResponseEntity<Boolean> resume(@RequestBody TaskReq taskReq) {
        boolean value = scheduleService.resume(taskReq);
        if (value) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/stop")
    public ResponseEntity<Boolean> stop(@RequestBody TaskReq taskReq) {
        boolean value = scheduleService.stop(taskReq);
        if (value) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/modify")
    public ResponseEntity<Date> modify(@RequestBody TaskReq taskReq) {
        try {
            Date date = scheduleService.modify(taskReq);
            return ResponseEntity.ok(date);
        } catch (SchedulerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/createStreaming")
    public ResponseEntity<Boolean> createStreaming(@RequestBody StreamingReq streamingReq) {
        boolean value = scheduleService.createStreaming(streamingReq.getPayload());
        if (value) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
