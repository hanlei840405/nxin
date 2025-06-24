package com.nxin.framework.controller.kettle;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.kettle.ShellConverter;
import com.nxin.framework.converter.bean.kettle.ShellPublishConverter;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.kettle.ShellPublishDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.ShellPublishService;
import com.nxin.framework.service.kettle.ShellService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.kettle.ShellPublishVo;
import com.nxin.framework.vo.kettle.ShellVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class ShellPublishController {
    @Autowired
    private ShellService shellService;
    @Autowired
    private ShellPublishService shellPublishService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private FileService fileService;
    private static final BeanConverter<ShellVo, Shell> shellConverter = new ShellConverter();
    private static final BeanConverter<ShellPublishVo, ShellPublish> shellPublishConverter = new ShellPublishConverter();

    @PostMapping("/publishes")
    public ResponseEntity<PageVo<ShellPublishVo>> publishes(@RequestBody CrudDto crudDto) {
        Shell shell = shellService.one(crudDto.getId());
        if (shell != null) {
            List<User> members = userService.findByResource(shell.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                IPage<ShellPublish> shellPublishPage = shellPublishService.findHistories(crudDto.getId(), crudDto.getPageNo(), crudDto.getPageSize());
                PageVo<ShellPublishVo> shellPublishVoPageVo = new PageVo<>(shellPublishPage.getTotal(), shellPublishConverter.convert(shellPublishPage.getRecords()));
                return ResponseEntity.ok(shellPublishVoPageVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @GetMapping("/publish/content/{id}")
    public ResponseEntity<String> content(@PathVariable Long id) {
        ShellPublish persisted = shellPublishService.one(id);
        if (persisted != null) {
            Shell shell = shellService.one(persisted.getShellId());
            List<User> members = userService.findByResource(shell.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                return ResponseEntity.ok(fileService.content(Constant.ENV_PUBLISH, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + persisted.getId() + Constant.DOT + Constant.GRAPH_SUFFIX));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/references")
    public ResponseEntity<List<ShellPublishVo>> references(@RequestBody ShellPublishDto shellPublishDto) {
        ShellPublish persisted = shellPublishService.one(shellPublishDto.getId());
        if (persisted != null) {
            Shell shell = shellService.one(persisted.getShellId());
            List<User> members = userService.findByResource(shell.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                if (StringUtils.hasLength(persisted.getReference())) {
                    List<Long> ids = Arrays.stream(persisted.getReference().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    List<ShellPublish> records = shellPublishService.listByIds(ids);
                    ids = records.stream().map(ShellPublish::getShellId).collect(Collectors.toList());
                    List<Shell> shells = shellService.listByIds(ids);
                    List<ShellPublishVo> shellPublishVos = new ArrayList<>();
                    records.forEach(item -> {
                        Shell one = shells.stream().filter(s -> s.getId().equals(item.getShellId())).findFirst().orElse(null);
                        ShellPublishVo shellPublishVo = shellPublishConverter.convert(item);
                        if (one != null) {
                            shellPublishVo.setShell(shellConverter.convert(one));
                        }
                        shellPublishVos.add(shellPublishVo);
                    });
                    return ResponseEntity.ok(shellPublishVos);
                }
                return ResponseEntity.status(Constant.EXCEPTION_ETL_GRAMMAR).build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    /**
     * 上线新版并下线旧版
     *
     * @param shellPublishDto 脚本发布对象
     */
    @PostMapping("/deploy")
    public ResponseEntity<ShellPublishVo> deploy(@RequestBody ShellPublishDto shellPublishDto) {
        ShellPublish persisted = shellPublishService.one(shellPublishDto.getId());
        if (persisted != null) {
            Shell shell = shellService.one(persisted.getShellId());
            persisted.setShell(shell);
            List<User> members = userService.findByResource(shell.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                persisted.setProd(Constant.ACTIVE);
                persisted.setDeployTime(LocalDateTime.now());
                if (Constant.BATCH.equals(persisted.getStreaming())) {
                    shellPublishService.deploySchedule(persisted, shellPublishDto.getCron(), shellPublishDto.getMisfire());
                } else {
                    shellPublishService.deployStreaming(persisted);
                }
                return ResponseEntity.ok(shellPublishConverter.convert(persisted));
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }
}
