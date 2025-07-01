package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.auth.UserMapper;
import com.nxin.framework.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${init.password}")
    private String password;

    public User one(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        queryWrapper.eq(User::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public User one(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStatus, Constant.ACTIVE);
        queryWrapper.eq(User::getEmail, email);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public List<User> findByEmail(List<String> emailList) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStatus, Constant.ACTIVE);
        queryWrapper.in(User::getEmail, emailList);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<User> findByResource(String resourceCode, String resourceCategory, String resourceLevel, String rw) {
        List<User> users = getBaseMapper().findByResource(resourceCode, resourceCategory, resourceLevel, rw);
        User user = one(LoginUtils.getUsername());
        if (resourceService.isRoot(user.getId()) && users.stream().noneMatch(item -> item.getId().equals(user.getId()))) {
            users.add(user);
        }
        return users;
    }

    public List<User> findByPrivilege(Long privilegeId) {
        return getBaseMapper().findByPrivilege(privilegeId);
    }

    public IPage<User> search(String name, int pageNo, int pageSize) {
        Page<User> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(User::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    public User lock(User user) {
        user.setStatus(Constant.LOCKED);
        save(user);
        return user;
    }

    @Transactional
    public User close(User user) {
        privilegeService.deletePrivilegesByUserId(user.getId());
        user.setStatus(Constant.INACTIVE);
        save(user);
        return user;
    }

    public boolean save(User user) {
        if (user.getId() != null) {
            user.setModifier(LoginUtils.getUsername());
            return getBaseMapper().updateById(user) > 0;
        }
        user.setCreator(LoginUtils.getUsername());
        user.setModifier(LoginUtils.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setStatus(Constant.ACTIVE);
        user.setVersion(1);
        return getBaseMapper().insert(user) > 0;
    }
}
