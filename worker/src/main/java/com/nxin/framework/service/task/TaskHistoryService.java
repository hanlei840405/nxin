package com.nxin.framework.service.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.task.TaskHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskHistoryService extends ServiceImpl<TaskHistoryMapper, TaskHistory> {
    @Autowired
    private TaskHistoryMapper taskHistoryMapper;

    public IPage<TaskHistory> allByShellPublish(Long shellPublishId, LocalDate begin, LocalDate end, int pageNo, int pageSize) {
        IPage<TaskHistory> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TaskHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TaskHistory.SHELL_PUBLISH_ID_COLUMN, shellPublishId);
        if (begin != null) {
            queryWrapper.ge(TaskHistory.BEGIN_TIME_COLUMN, begin);
        }
        if (end != null) {
            queryWrapper.le(TaskHistory.END_TIME_COLUMN, end);
        }
        queryWrapper.le(TaskHistory.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.orderByDesc(TaskHistory.BEGIN_TIME_COLUMN);
        return taskHistoryMapper.selectPage(page, queryWrapper);
    }

    @Transactional
    public void save(List<TaskHistory> taskHistories) {
        this.saveBatch(taskHistories);
    }
}
