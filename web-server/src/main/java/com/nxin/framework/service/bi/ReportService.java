package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Report;
import com.nxin.framework.entity.bi.ReportChartParams;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Autowired
    private ReportChartParamsService reportChartParamsService;
    @Autowired
    private ModelService modelService;

    public Report one(Long id) {
        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Report::getId, id);
        queryWrapper.eq(Report::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public IPage<Report> search(Long projectId, List<Long> reportIdList, String name, int pageNo, int pageSize) {
        Page<Report> page = new Page<>(pageNo, pageSize);
        if (reportIdList.isEmpty()) {
            return page;
        }
        LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Report::getId, reportIdList);
        queryWrapper.eq(Report::getProjectId, projectId);
        queryWrapper.eq(Report::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Report::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Report report, List<ReportChartParams> reportChartParamsList) {
        int upsert;
        if (report.getId() != null) {
            Report persisted = one(report.getId());
            BeanUtils.copyProperties(report, persisted, "publish", "publishTime", "version");
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
        LambdaQueryWrapper<ReportChartParams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReportChartParams::getReportId, report.getId());
        queryWrapper.eq(ReportChartParams::getStatus, Constant.ACTIVE);
        reportChartParamsList.forEach(reportChartParams -> {
            if (reportChartParams.getId() == null) {
                reportChartParams.setReportId(report.getId());
                reportChartParams.setVersion(1);
                reportChartParams.setStatus(Constant.ACTIVE);
            }
        });
        List<ReportChartParams> afterPublishCreateList = reportChartParamsService.list(queryWrapper).stream().filter(item -> Objects.isNull(report.getPublishTime()) || item.getCreateTime().isAfter(report.getPublishTime())).collect(Collectors.toList());
        List<ReportChartParams> deletedList = afterPublishCreateList.stream().filter(item -> reportChartParamsList.stream().noneMatch(i -> Objects.equals(i.getId(), item.getId()))).peek(item -> item.setStatus(Constant.INACTIVE)).collect(Collectors.toList());
        reportChartParamsList.addAll(deletedList);
        reportChartParamsService.saveOrUpdateBatch(reportChartParamsList);
        return upsert > 0;
    }

    @Transactional
    public void delete(Report persisted) {
        persisted.setStatus(Constant.INACTIVE);
        getBaseMapper().updateById(persisted);
        LambdaUpdateWrapper<ReportChartParams> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ReportChartParams::getReportId, persisted.getId());
        updateWrapper.set(ReportChartParams::getStatus, Constant.INACTIVE);
        reportChartParamsService.update(updateWrapper);
    }

}
