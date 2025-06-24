package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.vo.auth.UserVo;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter extends BeanConverter<UserVo, User> {

    @Override
    public UserVo convert(User user, String... ignores) {
        UserVo userVo = new UserVo();
        Arrays.append(ignores, "password");
        BeanUtils.copyProperties(user, userVo, ignores);
        return userVo;
    }

    @Override
    public List<UserVo> convert(List<User> users, String... ignores) {
        return users.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
