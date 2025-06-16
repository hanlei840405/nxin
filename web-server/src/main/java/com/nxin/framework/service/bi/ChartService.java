package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Chart;
import com.nxin.framework.entity.bi.ChartParams;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.bi.ChartMapper;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 展示图标 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-06-06
 */
@Service
public class ChartService extends ServiceImpl<ChartMapper, Chart> {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChartParamsService chartParamsService;

    public Chart one(Long id) {
        return getBaseMapper().selectById(id);
    }

    public IPage<Chart> search(String username, List<Long> chartIdList, String name, int pageNo, int pageSize) {
        Page<Chart> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Chart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Chart::getId, chartIdList);
        queryWrapper.eq(Chart::getCreator, username);
        queryWrapper.eq(Chart::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Chart::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Chart chart, List<ChartParams> chartParams) {
        int upsert;
        if (chart.getId() != null) {
            Chart persisted = one(chart.getId());
            BeanUtils.copyProperties(chart, persisted, "version");
            chart.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(persisted);
        } else {
            chart.setStatus(Constant.ACTIVE);
            chart.setVersion(1);
            chart.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(chart);
            User user = userService.one(LoginUtils.getUsername());
            resourceService.registryBusinessResource(String.valueOf(chart.getId()), chart.getName(), Constant.RESOURCE_CATEGORY_CHART, user);
        }
        LambdaQueryWrapper<ChartParams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChartParams::getChartId, chart.getId());
        chartParamsService.remove(queryWrapper);
        chartParams.forEach(metadata -> {
            metadata.setChartId(chart.getId());
            metadata.setVersion(1);
        });
        chartParamsService.saveBatch(chartParams);
        return upsert > 0;
    }

}
