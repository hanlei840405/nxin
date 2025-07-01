package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.auth.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ResourceService extends ServiceImpl<ResourceMapper, Resource> {

    @Autowired
    private PrivilegeService privilegeService;

    public boolean isRoot(Long userId) {
        Resource resource = getBaseMapper().selectRootByUserId(userId, Constant.ACTIVE);
        return resource != null;
    }

    public List<Resource> all() {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<Resource> findAllByIdIn(List<Long> idList) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getStatus, Constant.ACTIVE);
        queryWrapper.in(Resource::getId, idList);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<Resource> findByUserId(Long userId) {
        return getBaseMapper().selectByUserId(userId, Constant.ACTIVE);
    }

    public List<Resource> findByUserIdCategoryAndLevel(Long userId, String category, String level) {
        if (isRoot(userId)) {
            return getBaseMapper().selectByCategoryAndLevel(category, level);
        }
        return getBaseMapper().selectByUserIdAndCategoryAndLevel(userId, category, level);
    }

    public Resource findByPrivilegeId(Long privilegeId) {
        return getBaseMapper().findByPrivilegeId(privilegeId);
    }

    @Transactional
    public void delete(String code, String category, String level) {
        LambdaQueryWrapper<Resource> resourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        resourceLambdaQueryWrapper.eq(Resource::getCode, code);
        resourceLambdaQueryWrapper.eq(Resource::getCategory, category);
        resourceLambdaQueryWrapper.eq(Resource::getLevel, level);
        resourceLambdaQueryWrapper.eq(Resource::getStatus, Constant.ACTIVE);
        Resource resource = getBaseMapper().selectOne(resourceLambdaQueryWrapper);
        getBaseMapper().updateById(resource);
        // 将权限数据更新为无效
        LambdaUpdateWrapper<Privilege> privilegeLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        privilegeLambdaUpdateWrapper.eq(Privilege::getResourceId, resource.getId());
        privilegeLambdaUpdateWrapper.set(Privilege::getStatus, Constant.INACTIVE);
        privilegeService.update(privilegeLambdaUpdateWrapper);
        // 删除权限与用户的关联关系
        privilegeService.deletePrivilegesByResourceAndUser(code, category, Constant.RESOURCE_LEVEL_BUSINESS, Collections.EMPTY_LIST, null);
    }

    @Transactional
    public void registryBusinessResource(String code, String name, String category, User user) {
        // 创建资源码
        Resource resource = new Resource();
        resource.setCode(code);
        resource.setName(name);
        resource.setCategory(category);
        resource.setStatus(Constant.ACTIVE);
        resource.setLevel(Constant.RESOURCE_LEVEL_BUSINESS);
        resource.setVersion(1);
        this.save(resource);
        // 创建默认权限
        Privilege privilegeR = new Privilege();
        privilegeR.setName(resource.getName());
        privilegeR.setResourceId(resource.getId());
        privilegeR.setCategory(Constant.PRIVILEGE_READ);
        privilegeR.setStatus(Constant.ACTIVE);
        privilegeR.setVersion(1);
        Privilege privilegeRw = new Privilege();
        privilegeRw.setName(resource.getName());
        privilegeRw.setResourceId(resource.getId());
        privilegeRw.setCategory(Constant.PRIVILEGE_READ_WRITE);
        privilegeRw.setStatus(Constant.ACTIVE);
        privilegeRw.setVersion(1);
        privilegeService.saveBatch(Arrays.asList(privilegeR, privilegeRw));
        // 为创建者分配R+W级别权限
        if (user != null) {
            privilegeService.grant(Collections.singletonList(privilegeRw.getId()), user.getId(), false);
        }
    }
}
