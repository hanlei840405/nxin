package com.nxin.framework.converter.bean.kettle;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.vo.kettle.ShellPublishVo;
import com.nxin.framework.vo.kettle.ShellVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ShellPublishConverter extends BeanConverter<ShellPublishVo, ShellPublish> {

    @Override
    public ShellPublishVo convert(ShellPublish shellPublish) {
        BeanConverter<ShellVo, Shell> shellConverter = new ShellConverter();
        ShellPublishVo shellPublishVo = new ShellPublishVo();
        BeanUtils.copyProperties(shellPublish, shellPublishVo, "shell");
        if (shellPublish.getShell() != null) {
            shellPublishVo.setShell(shellConverter.convert(shellPublish.getShell()));
        }
        return shellPublishVo;
    }

    @Override
    public List<ShellPublishVo> convert(List<ShellPublish> shellPublishes) {
        return shellPublishes.stream().map(this::convert).collect(Collectors.toList());
    }
}
