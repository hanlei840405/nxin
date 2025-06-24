package com.nxin.framework.converter.bean.base;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.basic.Project;
import com.nxin.framework.vo.basic.ProjectVo;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectConverter extends BeanConverter<ProjectVo, Project> {

    @Override
    public ProjectVo convert(Project project, String... ignores) {
        ProjectVo projectVo = new ProjectVo();
        Arrays.append(ignores, "manager");
        BeanUtils.copyProperties(project, projectVo, ignores);
        if (project.getManager() != null) {
            projectVo.setManager(project.getManager().getName());
        }
        return projectVo;
    }

    @Override
    public List<ProjectVo> convert(List<Project> projects, String... ignores) {
        return projects.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
