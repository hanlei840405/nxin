package com.nxin.framework.converter.bean.kettle;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.vo.kettle.ShellVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ShellConverter extends BeanConverter<ShellVo, Shell> {

    @Override
    public ShellVo convert(Shell shell) {
        ShellVo shellVo = new ShellVo();
        BeanUtils.copyProperties(shell, shellVo);
        return shellVo;
    }

    @Override
    public List<ShellVo> convert(List<Shell> shells) {
        return shells.stream().map(this::convert).collect(Collectors.toList());
    }
}
