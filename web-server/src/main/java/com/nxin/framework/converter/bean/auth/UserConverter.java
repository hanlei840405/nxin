package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.vo.auth.UserVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter extends BeanConverter<UserVo, User> {

    @Override
    public UserVo convert(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo, "password");
        return userVo;
    }

    @Override
    public List<UserVo> convert(List<User> users) {
        return users.stream().map(this::convert).collect(Collectors.toList());
    }
}
