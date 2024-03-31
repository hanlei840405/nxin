package com.nxin.framework.controller.task;

import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.service.kettle.ShellPublishService;
import com.nxin.framework.service.task.TaskHistoryService;
import com.nxin.framework.utils.LoginUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('BATCH') or hasAuthority('STREAMING')")
@RestController
public class ShutdownController {
    @Autowired
    private UserService userService;
    @Autowired
    private ShellPublishService shellPublishService;
    @Autowired
    private TaskHistoryService taskHistoryService;
    @Autowired
    private RunningProcessService runningProcessService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/running/shutdown")
    public ResponseEntity shutdown(@RequestBody CrudDto crudDto) {
        TaskHistory taskHistory = taskHistoryService.getById(crudDto.getId());
        ShellPublish persisted = shellPublishService.one(taskHistory.getShellPublishId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                RunningProcess runningProcess = runningProcessService.getById(taskHistory.getRunningProcessId());
                if (runningProcess != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("taskHistoryId", String.valueOf(crudDto.getId()));
                    jsonObject.put("name", runningProcess.getInstanceName());
                    jsonObject.put("instanceId", runningProcess.getInstanceId());
                    stringRedisTemplate.convertAndSend(Constant.TOPIC_SHUTDOWN, jsonObject.toJSONString());
                }
                return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}