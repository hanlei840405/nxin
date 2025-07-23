package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Layout;
import com.nxin.framework.entity.bi.LayoutReport;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.bi.LayoutMapper;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 展示图标 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-06-06
 */
@Service
public class LayoutService extends ServiceImpl<LayoutMapper, Layout> {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private LayoutReportService layoutReportService;

    public Layout one(Long id) {
        LambdaQueryWrapper<Layout> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Layout::getId, id);
        queryWrapper.eq(Layout::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public IPage<Layout> search(List<Long> layoutIdList, String name, int pageNo, int pageSize) {
        Page<Layout> page = new Page<>(pageNo, pageSize);
        if (layoutIdList.isEmpty()) {
            return page;
        }
        LambdaQueryWrapper<Layout> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Layout::getId, layoutIdList);
        queryWrapper.eq(Layout::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Layout::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Layout layout, List<LayoutReport> layoutReportList) {
        int upsert;
        if (layout.getId() != null) {
            Layout persisted = one(layout.getId());
            BeanUtils.copyProperties(layout, persisted, "authenticate", "version");
            upsert = getBaseMapper().updateById(persisted);
        } else {
            layout.setStatus(Constant.ACTIVE);
            layout.setVersion(1);
            upsert = getBaseMapper().insert(layout);
            User user = userService.one(LoginUtils.getUsername());
            if (layout.getAuthenticate()) {
                resourceService.registryBusinessResource(String.valueOf(layout.getId()), layout.getName(), Constant.RESOURCE_CATEGORY_LAYOUT, user);
            }
        }
        LambdaQueryWrapper<LayoutReport> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LayoutReport::getLayoutId, layout.getId());
        queryWrapper.eq(LayoutReport::getStatus, Constant.ACTIVE);
        layoutReportList.forEach(layoutReport -> {
            if (layoutReport.getId() == null) {
                layoutReport.setLayoutId(layout.getId());
                layoutReport.setVersion(1);
                layoutReport.setStatus(Constant.ACTIVE);
            }
        });
        List<LayoutReport> persistedLayoutReportList = layoutReportService.list(queryWrapper);
        Map<Long, LayoutReport> persistedLayoutReportMap = persistedLayoutReportList.stream().collect(Collectors.toMap(LayoutReport::getId, v -> v));
        layoutReportList.forEach(item -> {
            if (persistedLayoutReportMap.containsKey(item.getId())) {
                item.setVersion(persistedLayoutReportMap.get(item.getId()).getVersion());
                item.setCreator(persistedLayoutReportMap.get(item.getId()).getCreator());
                item.setCreateTime(persistedLayoutReportMap.get(item.getId()).getCreateTime());
            }
        });
        List<LayoutReport> deletedList = persistedLayoutReportList.stream().filter(item -> layoutReportList.stream().noneMatch(i -> Objects.equals(i.getId(), item.getId()))).peek(item -> item.setStatus(Constant.INACTIVE)).collect(Collectors.toList());
        layoutReportList.addAll(deletedList);
        layoutReportService.saveOrUpdateBatch(layoutReportList);
        return upsert > 0;
    }

    @Transactional
    public void delete(Layout persisted) {
        persisted.setStatus(Constant.INACTIVE);
        getBaseMapper().updateById(persisted);
    }

}
