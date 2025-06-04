package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.auth.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Resource.STATUS_COLUMN, Constant.ACTIVE);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<Resource> findAllByIdIn(List<Long> idList) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Resource.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.in(Resource.ID_COLUMN, idList);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<Resource> findByUserId(Long userId) {
        return getBaseMapper().selectByUserId(userId, Constant.ACTIVE);
    }

    public List<Resource> findByUserIdCategoryAndLevel(Long userId, String category, String level) {
        return getBaseMapper().selectByUserIdAndCategoryAndLevel(userId, category, level);
    }

    public List<Resource> findByPrivilegeId(Long privilegeId) {
        return getBaseMapper().findByPrivilegeId(privilegeId);
    }

    @Transactional
    public void delete(String code, String category, String level) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Resource.CODE_COLUMN, code);
        queryWrapper.eq(Resource.CATEGORY_COLUMN, category);
        queryWrapper.eq(Resource.LEVEL_COLUMN, level);
        queryWrapper.eq(Resource.STATUS_COLUMN, Constant.ACTIVE);
        Resource resource = getBaseMapper().selectOne(queryWrapper);
        getBaseMapper().deleteById(resource);
        QueryWrapper<Privilege> privilegeQueryWrapper = new QueryWrapper<>();
        privilegeQueryWrapper.eq(Privilege.RESOURCE_ID_COLUMN, resource.getId());
        privilegeQueryWrapper.eq(Resource.STATUS_COLUMN, Constant.ACTIVE);
        privilegeService.remove(privilegeQueryWrapper);
    }
}
