package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.vo.auth.PrivilegeVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PrivilegeConverter extends BeanConverter<PrivilegeVo, Privilege> {

    @Override
    public PrivilegeVo convert(Privilege privilege, String... ignores) {
        PrivilegeVo privilegeVo = new PrivilegeVo();
        BeanUtils.copyProperties(privilege, privilegeVo, ignores);
        return privilegeVo;
    }

    @Override
    public List<PrivilegeVo> convert(List<Privilege> privileges, String... ignores) {
        return privileges.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}