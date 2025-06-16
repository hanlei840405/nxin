package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Report;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.bi.ReportMapper;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 报表设计 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-06-06
 */
@Service
public class ReportService extends ServiceImpl<ReportMapper, Report> {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    public Report one(Long id) {
        return getBaseMapper().selectById(id);
    }

    public IPage<Report> search(Long projectId, String name, int pageNo, int pageSize) {
        Page<Report> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Report::getProjectId, projectId);
        queryWrapper.eq(Report::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Report::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Report report) {
        int upsert;
        if (report.getId() != null) {
            Report persisted = one(report.getId());
            BeanUtils.copyProperties(report, persisted, "version");
            report.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(persisted);
        } else {
            report.setStatus(Constant.ACTIVE);
            report.setVersion(1);
            report.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(report);
            User user = userService.one(LoginUtils.getUsername());
            resourceService.registryBusinessResource(String.valueOf(report.getId()), report.getName(), Constant.RESOURCE_CATEGORY_REPORT, user);
        }
        return upsert > 0;
    }

}
