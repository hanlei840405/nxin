package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Project;
import com.nxin.framework.vo.basic.ProjectVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectConverter extends BeanConverter<ProjectVo, Project> {

    @Override
    public ProjectVo convert(Project project) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo, "manager");
        if (project.getManager() != null) {
            projectVo.setManager(project.getManager().getName());
        }
        return projectVo;
    }

    @Override
    public List<ProjectVo> convert(List<Project> projects) {
        return projects.stream().map(this::convert).collect(Collectors.toList());
    }
}
