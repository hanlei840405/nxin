package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.vo.auth.PrivilegeVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PrivilegeConverter extends BeanConverter<PrivilegeVo, Privilege> {

    @Override
    public PrivilegeVo convert(Privilege privilege) {
        PrivilegeVo privilegeVo = new PrivilegeVo();
        BeanUtils.copyProperties(privilege, privilegeVo);
        return privilegeVo;
    }

    @Override
    public List<PrivilegeVo> convert(List<Privilege> privileges) {
        return privileges.stream().map(this::convert).collect(Collectors.toList());
    }
}