package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.dto.auth.GrantDto;
import com.nxin.framework.dto.auth.UserPrivilegeDto;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.auth.PrivilegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PrivilegeService extends ServiceImpl<PrivilegeMapper, Privilege> {

    @Autowired
    private ResourceService resourceService;

    public List<Privilege> findByRwAndResource(String resourceCode, String resourceCategory, String resourceLevel, String rw) {
        return getBaseMapper().findByRwAndResource(resourceCode, resourceCategory, resourceLevel, rw);
    }

    public IPage<Privilege> search(String name, Long userId, int pageNo, int pageSize) {
        Page<Privilege> page = new Page<>(pageNo, pageSize);
        if (resourceService.isRoot(userId)) {
            QueryWrapper<Privilege> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Privilege.STATUS_COLUMN, Constant.ACTIVE);
            if (StringUtils.hasLength(name)) {
                queryWrapper.likeRight(Privilege.NAME_COLUMN, name);
            }
            return getBaseMapper().selectPage(page, queryWrapper);
        }

        return getBaseMapper().selectByUserAndName(page, userId, name);
    }

    public List<Privilege> findByUserId(Long userId) {
        return getBaseMapper().selectByUserId(userId);
    }

    public Privilege findByPrivilegeIdAndUserId(Long privilegeId, Long userId) {
        return getBaseMapper().selectByPrivilegeIdAndUserId(privilegeId, userId);
    }

    public List<Privilege> findByUserAndResource(Long userId, String resourceCode, String resourceCategory, String resourceLevel, String rw) {
        return getBaseMapper().selectByUserAndResource(userId, resourceCode, resourceCategory, resourceLevel, rw);
    }

    @Transactional
    public void grantByResource(List<GrantDto> grantDtos) {
        for (GrantDto grantDto : grantDtos) {
            for (UserPrivilegeDto userPrivilegeDto : grantDto.getUserPrivileges()) {
                List<Privilege> privileges = findByRwAndResource(grantDto.getResourceCode(), grantDto.getResourceCategory(), grantDto.getResourceLevel(), userPrivilegeDto.getRw());
                grant(privileges.stream().map(Privilege::getId).collect(Collectors.toList()), userPrivilegeDto.getUserId(), false);
            }
        }
    }

    @Transactional
    public void grant(List<GrantDto> grantDtos) {
        Map<Long, List<Long>> records = grantDtos.stream().collect(Collectors.groupingBy(GrantDto::getUserId, Collectors.mapping(GrantDto::getPrivilegeId, Collectors.toList())));
        records.forEach((k, v) -> {
            grant(v, k, false);
        });
    }

    @Transactional
    public void grant(List<Long> privilegeIds, Long userId, boolean delete) {
        // 查找用户之前的授权数据
        List<Long> grantedList = findByUserId(userId).stream().map(Privilege::getId).collect(Collectors.toList());
        if (delete) {
            // 删除本次取消的授权
            grantedList.removeIf(privilegeIds::contains);
            deleteGrantedPrivileges(userId, grantedList);
        }
        // 新增授权
        List<Long> copyPrivilegeIds = new ArrayList<>(privilegeIds);
        copyPrivilegeIds.removeIf(grantedList::contains);
        if (!copyPrivilegeIds.isEmpty()) {
            getBaseMapper().grantPrivileges(userId, copyPrivilegeIds);
        }
    }

    public void deleteGrantedPrivileges(Long userId, List<Long> privilegeIds) {
        getBaseMapper().deleteGrantedPrivileges(userId, privilegeIds);
    }

    public void deletePrivilegesByUserId(Long userId) {
        getBaseMapper().deletePrivilegesByUserId(userId);
    }

    public void deletePrivilegesByResourceAndUser(String resourceCode, String resourceCategory, String resourceLevel, List<Long> users) {
        getBaseMapper().deletePrivilegesByResourceAndUsers(resourceCode, resourceCategory, resourceLevel, users);
    }
}
