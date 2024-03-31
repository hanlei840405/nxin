package com.nxin.framework.service.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.kettle.RunningProcessMapper;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunningProcessService extends ServiceImpl<RunningProcessMapper, RunningProcess> {
    @Autowired
    private RunningProcessMapper runningProcessMapper;
//
//    public RunningProcess one(Long id) {
//        return runningProcessMapper.selectById(id);
//    }

    public RunningProcess instanceId(String instanceId) {
        QueryWrapper<RunningProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RunningProcess.INSTANCE_ID_COLUMN, instanceId);
        queryWrapper.eq(RunningProcess.STATUS_COLUMN, Constant.ACTIVE);
        return runningProcessMapper.selectOne(queryWrapper);
    }

    public IPage<RunningProcess> page(List<Long> projectIds, int pageNo, int pageSize) {
        Page<RunningProcess> page = new Page<>(pageNo, pageSize);
        QueryWrapper<RunningProcess> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(RunningProcess.PROJECT_ID_COLUMN, projectIds);
        return runningProcessMapper.selectPage(page, queryWrapper);
    }
//
//    public RunningProcess save(RunningProcess runningProcess, Tenant tenant) {
//        runningProcess.setTenant(tenant);
//        return runningProcessRepository.save(runningProcess);
//    }

    public void delete(RunningProcess runningProcess) {
        runningProcessMapper.deleteById(runningProcess);
    }

    public void delete(List<Long> idList) {
        runningProcessMapper.deleteBatchIds(idList);
    }
}
