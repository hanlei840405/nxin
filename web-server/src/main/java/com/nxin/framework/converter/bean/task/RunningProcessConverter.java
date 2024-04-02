package com.nxin.framework.converter.bean.task;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.vo.task.RunningProcessVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RunningProcessConverter extends BeanConverter<RunningProcessVo, RunningProcess> {

    @Override
    public RunningProcessVo convert(RunningProcess runningProcess) {
        RunningProcessVo runningProcessVo = new RunningProcessVo();
        BeanUtils.copyProperties(runningProcess, runningProcessVo);
        return runningProcessVo;
    }

    @Override
    public List<RunningProcessVo> convert(List<RunningProcess> runningProcesses) {
        return runningProcesses.stream().map(this::convert).collect(Collectors.toList());
    }
}
