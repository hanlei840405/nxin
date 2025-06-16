package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.Resource;
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
        return getBaseMapper().selectById(id);
    }

    public List<Project> search(String name, Long userId) {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Privilege.STATUS_COLUMN, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Privilege.NAME_COLUMN, name);
        }
        if (!resourceService.isRoot(userId)) {
            List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(userId, Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS);
            if (resources.isEmpty()) {
                return Collections.emptyList();
            }
            queryWrapper.in(Project.ID_COLUMN, resources.stream().map(resource -> Integer.valueOf(resource.getCode())).collect(Collectors.toList()));
        }
        List<Project> projects = getBaseMapper().selectList(queryWrapper);
        if (projects.isEmpty()) {
            return projects;
        }
        List<Long> userIds = projects.stream().map(Project::getUserId).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(User.STATUS_COLUMN, Constant.ACTIVE);
        userQueryWrapper.in(User.ID_COLUMN, userIds);
        List<User> users = userService.list(userQueryWrapper);
        Map<Long, User> userMap = users.stream().collect((Collectors.toMap(User::getId, user -> user)));
        for (Project project : projects) {
            project.setManager(userMap.get(project.getUserId()));
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
    public void deleteProject(Project project) {
        datasourceService.delete(project.getId(), Collections.emptyList());
        privilegeService.deletePrivilegesByResourceAndUser(project.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        resourceService.delete(project.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS);
        getBaseMapper().deleteById(project);
    }

    public void deleteMember(Project project, List<Long> users) {
        if (!users.isEmpty()) {
            privilegeService.deletePrivilegesByResourceAndUser(project.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, users);
        }
    }
}
