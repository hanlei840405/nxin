package com.nxin.framework.controller.basic;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.base.ProjectConverter;
import com.nxin.framework.dto.basic.ProjectDto;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Project;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.basic.ProjectVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('PROJECT')")
@RestController
@RequestMapping
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;

    private static final BeanConverter<ProjectVo, Project> projectConverter = new ProjectConverter();

    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectVo> one(@PathVariable Long id, Principal principal) {
        User loginUser = userService.one(principal.getName());
        Project project = projectService.one(id);
        if (project != null && Objects.equals(project.getUserId(), loginUser.getId())) {
            return ResponseEntity.ok(projectConverter.convert(project));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/projects")
    public ResponseEntity<List<ProjectVo>> projects(@RequestBody ProjectDto projectDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> projectIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).distinct().collect(Collectors.toList());
        List<Project> projects = projectService.search(projectIdList, projectDto.getPayload());
        List<ProjectVo> projectsVo = projectConverter.convert(projects);
        return ResponseEntity.ok(projectsVo);
    }

    @PostMapping("/project")
    public ResponseEntity<ProjectVo> save(@RequestBody ProjectDto projectDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        if (projectDto.getId() != null) {
            List<User> members = userService.findByResource(projectDto.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        projectService.save(project);
        return ResponseEntity.ok(projectConverter.convert(project));
    }

    @DeleteMapping("/project/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Project persisted = projectService.one(id);
        if (persisted != null && Objects.equals(loginUser.getId(), persisted.getUserId())) { // 登陆人为工程拥有人
            List<User> members = userService.findByResource(persisted.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.size() == 1 && Objects.equals(members.get(0).getId(), loginUser.getId())) { // 项目成员仅剩负责人自己
                projectService.delete(persisted);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_OWNER).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/project/{projectId}/owner/{userId}")
    public ResponseEntity transferProject(@PathVariable("projectId") Long projectId, @PathVariable("userId") Long userId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Project persisted = projectService.one(projectId);
        if (persisted != null && Objects.equals(loginUser.getId(), persisted.getUserId())) { // 登陆人为工程拥有人
            persisted.setUserId(userId);
            projectService.save(persisted);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/project/quit/{projectId}")
    public ResponseEntity quitProject(@PathVariable("projectId") Long projectId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Project persisted = projectService.one(projectId);
        if (persisted != null && !Objects.equals(loginUser.getId(), persisted.getUserId())) { // 退出人不能为工程拥有人
            projectService.deleteMember(persisted, Collections.singletonList(loginUser.getId()));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_OWNER).build();
    }
}
