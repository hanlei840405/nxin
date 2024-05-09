package com.nxin.framework.controller.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.task.TaskHistoryConverter;
import com.nxin.framework.dto.CronTriggerDto;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.ResponseDto;
import com.nxin.framework.dto.kettle.ShellPublishDto;
import com.nxin.framework.dto.task.TaskHistoryDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Project;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.interfaces.ScheduleService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.kettle.LogService;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.service.kettle.ShellPublishService;
import com.nxin.framework.service.kettle.ShellService;
import com.nxin.framework.service.task.TaskHistoryService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.task.TaskHistoryVo;
import com.nxin.framework.vo.task.TaskVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('BATCH') or hasAuthority('STREAMING') or hasAuthority('PROCESS') or hasAuthority('LOG')")
@RestController
public class TaskController {
    @Autowired
    private UserService userService;
    @DubboReference
    private ScheduleService scheduleService;
    @Autowired
    private ShellService shellService;
    @Autowired
    private ShellPublishService shellPublishService;
    @Autowired
    private LogService logService;
    @Autowired
    private TaskHistoryService taskHistoryService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RunningProcessService runningProcessService;

    private BeanConverter<TaskHistoryVo, TaskHistory> taskHistoryConverter = new TaskHistoryConverter();

    @PostMapping("/task/batches")
    public ResponseEntity<PageVo<TaskVo>> runningBatchTasks(@RequestBody CrudDto crudDto) {
        User user = userService.one(LoginUtils.getUsername());
        List<Project> projects = projectService.search(null, user.getId());
        if (!projects.isEmpty()) {
            ResponseDto<List<CronTriggerDto>> responseDto = scheduleService.findAllCronTrigger(projects.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.toList()));
            PageVo<TaskVo> taskVoPageVo;
            if (responseDto.isSuccess()) {
                if (responseDto.getData().isEmpty()) {
                    taskVoPageVo = new PageVo<>(0, Collections.EMPTY_LIST);
                } else {
                    List<CronTriggerDto> cronTriggerDtoList = responseDto.getData();
                    Map<Long, CronTriggerDto> shellMap = cronTriggerDtoList.stream().collect(Collectors.toMap(CronTriggerDto::getShellId, item -> item));
                    IPage<ShellPublish> shellPublishIPage = shellPublishService.online(crudDto.getPayload(), Constant.BATCH, shellMap.keySet(), crudDto.getPageNo(), crudDto.getPageSize());
                    List<TaskVo> taskVos = shellPublishIPage.getRecords().stream().map(shellPublish -> {
                        TaskVo taskVo = new TaskVo();
                        taskVo.setName(shellPublish.getName());
                        taskVo.setDescription(shellPublish.getDescription());
                        CronTriggerDto cronTriggerDto = shellMap.get(shellPublish.getId());
                        taskVo.setCron(cronTriggerDto.getCron());
                        taskVo.setNextFireTime(cronTriggerDto.getNextFireTime());
                        taskVo.setPreviousFireTime(cronTriggerDto.getPreviousFireTime());
                        taskVo.setStartTime(cronTriggerDto.getStartTime());
                        taskVo.setState(cronTriggerDto.getState());
                        taskVo.setMisfire(cronTriggerDto.getMisfire());
                        taskVo.setId(shellPublish.getId());
                        return taskVo;
                    }).collect(Collectors.toList());
                    taskVoPageVo = new PageVo<>(shellPublishIPage.getTotal(), taskVos);
                }
                return ResponseEntity.ok(taskVoPageVo);
            } else {
                taskVoPageVo = new PageVo<>(0, Collections.EMPTY_LIST);
            }
            return ResponseEntity.ok(taskVoPageVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/task/streaming")
    public ResponseEntity<PageVo<TaskVo>> runningStreamingTasks(@RequestBody CrudDto crudDto) {
        User user = userService.one(LoginUtils.getUsername());
        List<Project> projects = projectService.search(null, user.getId());
        if (!projects.isEmpty()) {
            IPage<RunningProcess> runningProcessIPage = runningProcessService.page(projects.stream().map(Project::getId).collect(Collectors.toList()), Constant.STREAMING, crudDto.getPageNo(), crudDto.getPageSize());
            List<TaskVo> taskVos = runningProcessIPage.getRecords().stream().map(runningProcess -> {
                TaskVo taskVo = new TaskVo();
                taskVo.setName(runningProcess.getInstanceName());
                taskVo.setStartTime(Date.from(runningProcess.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
                taskVo.setId(runningProcess.getId());
                return taskVo;
            }).collect(Collectors.toList());
            PageVo<TaskVo> taskVoPageVo = new PageVo<>(runningProcessIPage.getTotal(), taskVos);
            return ResponseEntity.ok(taskVoPageVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/task/batch/history")
    public ResponseEntity<PageVo<TaskHistoryVo>> runningProcesses(@RequestBody TaskHistoryDto taskHistoryDto) {
        ShellPublish persisted = shellPublishService.one(taskHistoryDto.getShellPublishId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                LocalDate begin = null;
                if (StringUtils.hasLength(taskHistoryDto.getBeginTime())) {
                    begin = LocalDate.parse(taskHistoryDto.getBeginTime());
                }
                LocalDate end = null;
                if (StringUtils.hasLength(taskHistoryDto.getEndTime())) {
                    end = LocalDate.parse(taskHistoryDto.getEndTime());
                }
                IPage<TaskHistory> page = taskHistoryService.allByShellPublish(taskHistoryDto.getShellPublishId(), begin, end, taskHistoryDto.getPageNo(), taskHistoryDto.getPageSize());
                PageVo<TaskHistoryVo> taskHistoryPageVo = new PageVo<>(page.getTotal(), taskHistoryConverter.convert(page.getRecords()));
                return ResponseEntity.ok(taskHistoryPageVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/task/pause")
    public ResponseEntity pause(@RequestBody ShellPublishDto shellPublishDto) {
        ShellPublish persisted = shellPublishService.one(shellPublishDto.getId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                ResponseDto responseDto = scheduleService.pause(persisted.getProjectId().toString(), persisted.getTaskId());
                if (responseDto.isSuccess()) {
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(Constant.EXCEPTION_ADD_SCHEDULE).build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/task/resume")
    public ResponseEntity resume(@RequestBody ShellPublishDto shellPublishDto) {
        ShellPublish persisted = shellPublishService.one(shellPublishDto.getId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                ResponseDto responseDto = scheduleService.resume(persisted.getProjectId().toString(), persisted.getTaskId());
                if (responseDto.isSuccess()) {
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(Constant.EXCEPTION_ADD_SCHEDULE).build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/task/stop")
    public ResponseEntity stop(@RequestBody ShellPublishDto shellPublishDto) {
        ShellPublish persisted = shellPublishService.one(shellPublishDto.getId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                shellPublishService.stop(persisted);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/task/modify")
    public ResponseEntity modify(@RequestBody ShellPublishDto shellPublishDto) {
        ShellPublish persisted = shellPublishService.one(shellPublishDto.getId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                scheduleService.modify(persisted.getProjectId().toString(), persisted.getTaskId(), shellPublishDto.getCron(), shellPublishDto.getMisfire());
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @GetMapping("/task/batch/logs/{id}")
    public ResponseEntity<String> batchLogs(@PathVariable("id") Long id) {
        TaskHistory taskHistory = taskHistoryService.getById(id);
        ShellPublish persisted = shellPublishService.one(taskHistory.getShellPublishId());
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                return ResponseEntity.ok(logService.jobLog(taskHistory.getLogChannelId()));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

//    @GetMapping("/task/batch/log/{id}/{channelId}")
//    public ResponseEntity<JobLogVo> batchLog(@PathVariable("id") Long id, @PathVariable("channelId") String channelId, Principal principal) {
//        User loginUser = userService.one(principal.getName());
//        ShellPublish sp = shellPublishService.one(id);
//        if (sp.getShell().getProject().getUsers().contains(loginUser)) {
//            List<JobLogVo> jobLogs = logService.jobLog(Arrays.asList(channelId));
//            if (!jobLogs.isEmpty()) {
//                return ResponseEntity.ok(jobLogs.get(0));
//            }
//            return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
//        }
//        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
//    }
//
//    @GetMapping("/task/streaming/log/{id}")
//    public ResponseEntity<String> streamingLog(@PathVariable("id") Long id, Principal principal) {
//        User loginUser = userService.one(principal.getName());
//        ShellPublish existed = shellPublishService.one(id);
//        if (existed.getShell().getProject().getUsers().contains(loginUser)) {
//            List<String> logChannelIds = shellPublishLogService.allByShellPublish(existed.getId()).stream().map(shellPublishLog -> shellPublishLog.getLogChannelId()).collect(Collectors.toList());
//            StringBuilder builder = new StringBuilder();
//            List<TransformLogVo> transformLogs = logService.transformLog(logChannelIds);
//            transformLogs.forEach(log -> builder.append(log.getLogField()));
//            return ResponseEntity.ok(builder.toString());
//        }
//        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
//    }
}
