package com.nxin.framework.controller.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.task.RunningProcessConverter;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.ResponseDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Project;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.service.kettle.ShellPublishService;
import com.nxin.framework.service.task.TaskHistoryService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.task.RunningProcessVo;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('BATCH') or hasAuthority('STREAMING')")
@RestController
public class ProcessController {
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
    @Autowired
    private ProjectService projectService;

    private BeanConverter<RunningProcessVo, RunningProcess> runningProcessConverter = new RunningProcessConverter();

    @PostMapping("/process/running")
    public ResponseEntity<PageVo<RunningProcessVo>> running(@RequestBody CrudDto crudDto) {
        User user = userService.one(LoginUtils.getUsername());
        List<Project> projects = projectService.search(null, user.getId());
        if (!projects.isEmpty()) {
            IPage<RunningProcess> runningProcessIPage = runningProcessService.page(projects.stream().map(Project::getId).collect(Collectors.toList()), null, crudDto.getPageNo(), crudDto.getPageSize());
            List<RunningProcessVo> runningProcessVos = runningProcessConverter.convert(runningProcessIPage.getRecords());
            PageVo<RunningProcessVo> pageVo = new PageVo<>(runningProcessIPage.getTotal(), runningProcessVos);
            return ResponseEntity.ok(pageVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/process/shutdown")
    public ResponseEntity shutdown(@RequestBody CrudDto crudDto) {
        TaskHistory taskHistory = taskHistoryService.getById(crudDto.getId());
        Long shellPublishId;
        RunningProcess runningProcess = null;
        if (taskHistory != null) {
            shellPublishId = taskHistory.getShellPublishId();
        } else {
            runningProcess = runningProcessService.getById(crudDto.getId());
            shellPublishId = runningProcess.getShellPublishId();
        }
        ShellPublish persisted = shellPublishService.one(shellPublishId);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                if (runningProcess == null) {
                    runningProcess = runningProcessService.getById(taskHistory.getRunningProcessId());
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("taskHistoryId", String.valueOf(crudDto.getId()));
                jsonObject.put("name", runningProcess.getInstanceName());
                jsonObject.put("instanceId", runningProcess.getInstanceId());
                stringRedisTemplate.convertAndSend(Constant.TOPIC_SHUTDOWN, jsonObject.toJSONString());
                // 将关联脚本也一并下线
                List<ShellPublish> deployedShellPublishes = new ArrayList<>(0);
                String reference = persisted.getReference();
                if (StringUtils.hasLength(reference)) {
                    List<Long> referenceIds = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
                    deployedShellPublishes.addAll(shellPublishService.listByIds(referenceIds));
                }
                deployedShellPublishes.add(persisted);
                deployedShellPublishes.forEach(deployed -> deployed.setProd(Constant.INACTIVE));
                shellPublishService.updateBatchById(deployedShellPublishes); // 将正在执行的脚本更新为下线状态
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}