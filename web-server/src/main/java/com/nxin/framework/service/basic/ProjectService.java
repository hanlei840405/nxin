package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Project;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.ProjectMapper;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService extends ServiceImpl<ProjectMapper, Project> {
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private FileService fileService;

    public Project one(Long id) {
        LambdaQueryWrapper<Project> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Project::getId, id);
        queryWrapper.eq(Project::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public List<Project> search(List<Long> projectIdList, String name) {
        if (projectIdList.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Project> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Project::getStatus, Constant.ACTIVE);
        lambdaQueryWrapper.in(Project::getId, projectIdList);
        if (StringUtils.hasLength(name)) {
            lambdaQueryWrapper.likeRight(Project::getName, name);
        }
        List<Project> projects = getBaseMapper().selectList(lambdaQueryWrapper);
        List<Long> userIds = projects.stream().map(Project::getUserId).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(User::getStatus, Constant.ACTIVE);
            userQueryWrapper.in(User::getId, userIds);
            List<User> users = userService.list(userQueryWrapper);
            Map<Long, User> userMap = users.stream().collect((Collectors.toMap(User::getId, user -> user)));
            for (Project project : projects) {
                project.setManager(userMap.get(project.getUserId()));
            }
        }
        return projects;
    }

    @Transactional
    public boolean save(Project project) {
        int upsert;
        if (project.getId() != null) {
            Project existed = getBaseMapper().selectById(project.getId());
            BeanUtils.copyProperties(project, existed, "version");
            existed.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(existed);
        } else {
            User user = userService.one(LoginUtils.getUsername());
            if (user != null) {
                project.setUserId(user.getId());
            }
            project.setVersion(1);
            project.setStatus(Constant.ACTIVE);
            project.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(project);
            resourceService.registryBusinessResource(String.valueOf(project.getId()), project.getName(), Constant.RESOURCE_CATEGORY_PROJECT, user);
        }
        Arrays.stream(Constant.ENV_BUCKET).forEach(env -> fileService.createFolder(env, project.getId() + File.separator));
        return upsert > 0;
    }

    @Transactional
    public void delete(Project persisted) {
        persisted.setStatus(Constant.INACTIVE);
        getBaseMapper().updateById(persisted);
        resourceService.delete(persisted.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS);
    }

    public void deleteMember(Project project, List<Long> users) {
        if (!users.isEmpty()) {
            privilegeService.deletePrivilegesByResourceAndUser(project.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, users, null);
        }
    }
}
