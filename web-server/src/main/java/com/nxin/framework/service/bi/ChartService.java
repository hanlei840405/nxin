package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
public class ChartService extends ServiceImpl<ChartMapper, Chart> {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChartParamsService chartParamsService;

    public Chart one(Long id) {
        LambdaQueryWrapper<Chart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Chart::getId, id);
        queryWrapper.eq(Chart::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public IPage<Chart> search(List<Long> chartIdList, String name, int pageNo, int pageSize) {
        Page<Chart> page = new Page<>(pageNo, pageSize);
        if (chartIdList.isEmpty()) {
            return page;
        }
        LambdaQueryWrapper<Chart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Chart::getId, chartIdList);
        queryWrapper.eq(Chart::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Chart::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Chart chart, List<ChartParams> chartParamsList) {
        int upsert;
        if (chart.getId() != null) {
            Chart persisted = one(chart.getId());
            BeanUtils.copyProperties(chart, persisted, "publish", "publishTime", "version");
            upsert = getBaseMapper().updateById(persisted);
        } else {
            chart.setStatus(Constant.ACTIVE);
            chart.setVersion(1);
            upsert = getBaseMapper().insert(chart);
            User user = userService.one(LoginUtils.getUsername());
            resourceService.registryBusinessResource(String.valueOf(chart.getId()), chart.getName(), Constant.RESOURCE_CATEGORY_CHART, user);
        }
        LambdaQueryWrapper<ChartParams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChartParams::getChartId, chart.getId());
        queryWrapper.eq(ChartParams::getStatus, Constant.ACTIVE);
        chartParamsList.forEach(chartParams -> {
            if (chartParams.getId() == null) {
                chartParams.setChartId(chart.getId());
                chartParams.setVersion(1);
                chartParams.setStatus(Constant.ACTIVE);
            }
        });
        List<ChartParams> afterPublishCreateList = chartParamsService.list(queryWrapper).stream().filter(item -> Objects.isNull(chart.getPublishTime()) || item.getCreateTime().isAfter(chart.getPublishTime())).collect(Collectors.toList());
        List<ChartParams> deletedList = afterPublishCreateList.stream().filter(item -> chartParamsList.stream().noneMatch(i -> Objects.equals(i.getId(), item.getId()))).peek(item -> item.setStatus(Constant.INACTIVE)).collect(Collectors.toList());
        chartParamsList.addAll(deletedList);
        chartParamsService.saveOrUpdateBatch(chartParamsList);
        return upsert > 0;
    }

    @Transactional
    public void delete(Chart persisted) {
        persisted.setStatus(Constant.INACTIVE);
        getBaseMapper().updateById(persisted);
        LambdaUpdateWrapper<ChartParams> chartParamsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        chartParamsLambdaUpdateWrapper.eq(ChartParams::getChartId, persisted.getId());
        chartParamsLambdaUpdateWrapper.set(ChartParams::getStatus, Constant.INACTIVE);
        chartParamsService.update(chartParamsLambdaUpdateWrapper);
    }

}
