package com.nxin.framework.controller.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.auth.ApplyConverter;
import com.nxin.framework.dto.auth.ApplyDto;
import com.nxin.framework.entity.auth.Apply;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.ExistedException;
import com.nxin.framework.exception.RecordsNotMatchException;
import com.nxin.framework.service.auth.ApplyService;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.auth.ApplyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限申请 前端控制器
 * </p>
 *
 * @author jesse han
 * @since 2025-06-23
 */
@RestController
public class ApplyController {

    @Autowired
    private ApplyService applyService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private UserService userService;

    private static final BeanConverter<ApplyVo, Apply> applyConverter = new ApplyConverter();

    @GetMapping("/apply/{id}")
    public ResponseEntity<ApplyVo> one(@PathVariable Long id) {
        Apply apply = applyService.one(id);
        if (apply != null) {
            ApplyVo chartVo = applyConverter.convert(apply);
            return ResponseEntity.ok(chartVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/applyPage")
    public ResponseEntity<PageVo<ApplyVo>> page(@RequestBody ApplyDto applyDto) {
        IPage<Apply> applyIPage = applyService.search(applyDto.getSearchAudit(), applyDto.getCreator(), applyDto.getPageNo(), applyDto.getPageSize());
        List<ApplyVo> applyVos = applyConverter.convert(applyIPage.getRecords());
        if (!applyIPage.getRecords().isEmpty()) {
            LambdaQueryWrapper<Privilege> privilegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            privilegeLambdaQueryWrapper.in(Privilege::getId, applyIPage.getRecords().stream().map(Apply::getPrivilegeId).collect(Collectors.toList()));
            privilegeLambdaQueryWrapper.eq(Privilege::getStatus, Constant.ACTIVE);
            List<Privilege> privilegeList = privilegeService.list(privilegeLambdaQueryWrapper);
            Map<Long, Privilege> privilegeMap = privilegeList.stream().collect(Collectors.toMap(Privilege::getId, v -> v));
            applyVos.forEach(item -> {
                item.setPrivilegeName(privilegeMap.get(item.getPrivilegeId()).getName());
                item.setPrivilegeCategory(privilegeMap.get(item.getPrivilegeId()).getCategory());
            });
        }
        return ResponseEntity.ok(new PageVo<>(applyIPage.getTotal(), applyVos));
    }

    @PostMapping("/apply")
    public ResponseEntity save(@RequestBody ApplyDto applyDto) {
        Apply apply = new Apply();
        BeanUtils.copyProperties(applyDto, apply);
        apply.setAuditStatus(Constant.AUDIT_STATUS_APPLY);
        try {
            applyService.save(apply);
            return ResponseEntity.ok().build();
        } catch (RecordsNotMatchException e) {
            return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
        } catch (ExistedException e) {
            return ResponseEntity.status(Constant.EXCEPTION_DUPLICATED).body(e.getMessage());
        }
    }

    @PostMapping("/audit")
    public ResponseEntity<ApplyVo> audit(@RequestBody ApplyDto applyDto) {
        if (!CollectionUtils.isEmpty(applyDto.getApplyIdList())) {
            User loginUser = userService.one(LoginUtils.getUsername());
            LambdaQueryWrapper<Apply> applyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            applyLambdaQueryWrapper.eq(Apply::getStatus, Constant.ACTIVE);
            applyLambdaQueryWrapper.eq(Apply::getAuditStatus, Constant.AUDIT_STATUS_APPLY);
            applyLambdaQueryWrapper.in(Apply::getId, applyDto.getApplyIdList());
            List<Apply> applies = applyService.list(applyLambdaQueryWrapper);
            if (!applies.isEmpty()) {
                List<User> creatorList = userService.findByEmail(applies.stream().map(Apply::getCreator).collect(Collectors.toList()));
                LambdaQueryWrapper<Privilege> privilegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                privilegeLambdaQueryWrapper.eq(Privilege::getStatus, Constant.ACTIVE);
                privilegeLambdaQueryWrapper.in(Privilege::getId, applies.stream().map(Apply::getPrivilegeId).collect(Collectors.toList()));
                List<Privilege> privilegeList = privilegeService.list(privilegeLambdaQueryWrapper);
                if (!privilegeList.isEmpty()) {
                    List<Privilege> privileges = privilegeService.findByPrivilegeIdListAndUserId(privilegeList.stream().map(Privilege::getId).collect(Collectors.toList()), loginUser.getId());
                    if (!privileges.isEmpty()) {
                        Map<Long, Privilege> privilegeMap = privileges.stream().collect(Collectors.toMap(Privilege::getId, v -> v));
                        applies.removeIf(item -> !privilegeMap.containsKey(item.getPrivilegeId()));
                    }
                }
                if (!applies.isEmpty()) {
                    for (Apply apply : applies) {
                        apply.setAuditStatus(applyDto.getAuditStatus());
                    }
                    applyService.audit(applies, creatorList);
                    return ResponseEntity.ok().build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @DeleteMapping("/apply/{id}")
    public ResponseEntity<ApplyVo> delete(@PathVariable Long id) {
        Apply persisted = applyService.one(id);
        if (persisted != null && Constant.AUDIT_STATUS_APPLY.equals(persisted.getAuditStatus())) {
            if (persisted.getCreator().equals(LoginUtils.getUsername())) {
                applyService.delete(persisted);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
