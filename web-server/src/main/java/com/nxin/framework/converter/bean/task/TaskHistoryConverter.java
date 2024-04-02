package com.nxin.framework.converter.bean.task;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.vo.task.TaskHistoryVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TaskHistoryConverter extends BeanConverter<TaskHistoryVo, TaskHistory> {

    @Override
    public TaskHistoryVo convert(TaskHistory taskHistory) {
        TaskHistoryVo taskHistoryVo = new TaskHistoryVo();
        BeanUtils.copyProperties(taskHistory, taskHistoryVo);
        return taskHistoryVo;
    }

    @Override
    public List<TaskHistoryVo> convert(List<TaskHistory> taskHistories) {
        return taskHistories.stream().map(this::convert).collect(Collectors.toList());
    }
}
