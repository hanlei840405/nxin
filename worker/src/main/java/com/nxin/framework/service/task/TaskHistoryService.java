package com.nxin.framework.service.task;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.mapper.task.TaskHistoryMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskHistoryService extends ServiceImpl<TaskHistoryMapper, TaskHistory> {
}
