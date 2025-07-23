package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.dto.auth.GrantDto;
import com.nxin.framework.entity.auth.Apply;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.ExistedException;
import com.nxin.framework.exception.RecordsNotMatchException;
import com.nxin.framework.mapper.auth.ApplyMapper;
import com.nxin.framework.utils.LoginUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限申请 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-06-23
 */
@Service
public class ApplyService extends ServiceImpl<ApplyMapper, Apply> {
    @Autowired
    private UserService userService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public Apply one(Long id) {
        LambdaQueryWrapper<Apply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Apply::getId, id);
        queryWrapper.eq(Apply::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public IPage<Apply> search(Boolean searchAudit, String creator, int pageNo, int pageSize) {
        Page<Apply> page = new Page<>(pageNo, pageSize);
        User loginUser = userService.one(LoginUtils.getUsername());
        if (BooleanUtils.isTrue(searchAudit)) {
            return getBaseMapper().selectUnAudit(page, creator, resourceService.isRoot(loginUser.getId()) ? null : loginUser.getId());
        }
        LambdaQueryWrapper<Apply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Apply::getStatus, Constant.ACTIVE);
        if (!resourceService.isRoot(loginUser.getId())) {
            queryWrapper.eq(Apply::getCreator, LoginUtils.getUsername());
        }
        queryWrapper.orderByDesc(Apply::getId);
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Apply apply) {
        Resource resource = resourceService.findByPrivilegeId(apply.getPrivilegeId());
        if (resource == null) {
            throw new RecordsNotMatchException();
        }
        List<Privilege> privilegesBelong2Resource = privilegeService.findByRwAndResource(resource.getCode(), resource.getCategory(), resource.getLevel(), null);
        if (privilegesBelong2Resource.isEmpty()) {
            throw new RecordsNotMatchException();
        }
        int upsert;
        if (apply.getId() != null) {
            Apply persisted = one(apply.getId());
            if (persisted == null) {
                throw new RecordsNotMatchException();
            }
            BeanUtils.copyProperties(apply, persisted, "auditStatus", "version");
            upsert = getBaseMapper().updateById(persisted);
        } else {
            LambdaQueryWrapper<Apply> applyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            applyLambdaQueryWrapper.eq(Apply::getStatus, Constant.ACTIVE);
            applyLambdaQueryWrapper.eq(Apply::getCreator, LoginUtils.getUsername());
            applyLambdaQueryWrapper.in(Apply::getPrivilegeId, privilegesBelong2Resource.stream().map(Privilege::getId).collect(Collectors.toList()));
            applyLambdaQueryWrapper.ne(Apply::getAuditStatus, Constant.AUDIT_STATUS_REJECT);
            if (getBaseMapper().exists(applyLambdaQueryWrapper)) {
                throw new ExistedException(privilegesBelong2Resource.get(0).getName());
            }
            apply.setStatus(Constant.ACTIVE);
            apply.setVersion(1);
            upsert = getBaseMapper().insert(apply);
        }

        List<User> auditorList = userService.findByPrivilegeAndRw(apply.getPrivilegeId(), Constant.PRIVILEGE_READ_WRITE);
        for (User auditor : auditorList) {
            try {
                Map<String, Object> headers = new HashMap<>();
                headers.put("type", "application_apply");
                simpMessagingTemplate.convertAndSendToUser(auditor.getEmail(), Constant.WEB_SOCKET_DESTINATION_MESSAGE, apply, headers);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
        return upsert > 0;
    }

    @Transactional
    public void audit(List<Apply> applyList, List<User> creators) {
        Map<String, User> userMap = creators.stream().collect(Collectors.toMap(User::getEmail, v -> v));
        List<GrantDto> grantDtoList = new ArrayList<>();
        for (Apply apply : applyList) {
            if (Constant.AUDIT_STATUS_PASS.equals(apply.getAuditStatus())) {
                GrantDto grantDto = new GrantDto();
                grantDto.setPrivilegeId(apply.getPrivilegeId());
                grantDto.setUserId(userMap.getOrDefault(apply.getCreator(), new User()).getId());
                grantDto.setExpireDate(apply.getExpireDate());
                grantDtoList.add(grantDto);
            }
        }
        if (!grantDtoList.isEmpty()) {
            privilegeService.grant(grantDtoList);
        }
        this.updateBatchById(applyList, applyList.size());
        for (Apply apply : applyList) {
            try {
                Map<String, Object> headers = new HashMap<>();
                headers.put("type", "application_audit");
                simpMessagingTemplate.convertAndSendToUser(apply.getCreator(), Constant.WEB_SOCKET_DESTINATION_MESSAGE, apply, headers);
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void delete(Apply persisted) {
        persisted.setStatus(Constant.INACTIVE);
        getBaseMapper().updateById(persisted);
    }
}
