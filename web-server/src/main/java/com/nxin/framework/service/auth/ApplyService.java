package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.dto.auth.GrantDto;
import com.nxin.framework.entity.auth.Apply;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.auth.ApplyMapper;
import com.nxin.framework.utils.LoginUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

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

    private static final String AUDIT_STATUS_APPLY = "0";

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
            return getBaseMapper().selectUnAudit(page, creator, loginUser.getId());
        }
        LambdaQueryWrapper<Apply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Apply::getCreator, creator);
        queryWrapper.orderByDesc(Apply::getId);
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Apply apply) {
        int upsert;
        if (apply.getId() != null) {
            Apply persisted = one(apply.getId());
            BeanUtils.copyProperties(apply, persisted, "auditStatus", "version");
            upsert = getBaseMapper().updateById(persisted);
        } else {
            apply.setStatus(Constant.ACTIVE);
            apply.setAuditStatus(AUDIT_STATUS_APPLY);
            apply.setVersion(1);
            upsert = getBaseMapper().insert(apply);
        }
        return upsert > 0;
    }

    @Transactional
    public void audit(Apply persisted) {
        User user = userService.one(persisted.getCreator());
        GrantDto grantDto = new GrantDto();
        grantDto.setPrivilegeId(persisted.getPrivilegeId());
        grantDto.setUserId(user.getId());
        privilegeService.grant(Collections.singletonList(grantDto));
        getBaseMapper().updateById(persisted);
    }

    @Transactional
    public void delete(Apply persisted) {
        persisted.setStatus(Constant.INACTIVE);
        getBaseMapper().updateById(persisted);
    }
}
