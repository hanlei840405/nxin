package com.nxin.framework.controller.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.auth.PrivilegeConverter;
import com.nxin.framework.converter.bean.auth.ResourceConverter;
import com.nxin.framework.converter.bean.auth.UserConverter;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.auth.GrantDto;
import com.nxin.framework.dto.auth.UserPrivilegeDto;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.auth.PrivilegeVo;
import com.nxin.framework.vo.auth.ResourceVo;
import com.nxin.framework.vo.auth.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class PrivilegeController {
    @Autowired
    private UserService userService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private ResourceService resourceService;
    private final static BeanConverter<ResourceVo, Resource> resourceConverter = new ResourceConverter();
    private final static BeanConverter<PrivilegeVo, Privilege> privilegeConverter = new PrivilegeConverter();
    private final static BeanConverter<UserVo, User> userConverter = new UserConverter();

    @GetMapping("/resources/{userId}")
    public ResponseEntity<List<ResourceVo>> resources(@PathVariable Long userId) {
        List<Resource> resources = resourceService.findByUserId(userId);
        List<ResourceVo> resourceVos = resourceConverter.convert(resources);
        return ResponseEntity.ok(resourceVos);
    }

    @PostMapping("/resources")
    public ResponseEntity<List<ResourceVo>> resources(@RequestBody CrudDto crudDto) {
        if (StringUtils.hasLength(crudDto.getPayload())) {
            LambdaQueryWrapper<Resource> resourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
            resourceLambdaQueryWrapper.eq(Resource::getStatus, Constant.ACTIVE);
            resourceLambdaQueryWrapper.likeRight(Resource::getName, crudDto.getPayload());
            List<Resource> resources = resourceService.list(resourceLambdaQueryWrapper);
            return ResponseEntity.ok(resourceConverter.convert(resources));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/privileges/resource/{resourceId}")
    public ResponseEntity<List<PrivilegeVo>> privilegesByResource(@PathVariable Long resourceId) {
        LambdaQueryWrapper<Privilege> privilegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        privilegeLambdaQueryWrapper.eq(Privilege::getStatus, Constant.ACTIVE);
        privilegeLambdaQueryWrapper.eq(Privilege::getResourceId, resourceId);
        List<Privilege> privileges = privilegeService.list(privilegeLambdaQueryWrapper);
        return ResponseEntity.ok(privilegeConverter.convert(privileges));
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @PostMapping("/privileges")
    public ResponseEntity<PageVo<PrivilegeVo>> privileges(@RequestBody CrudDto crudDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        IPage<Privilege> page = privilegeService.search(crudDto.getPayload(), loginUser.getId(), crudDto.getPageNo(), crudDto.getPageSize());
        if (!page.getRecords().isEmpty()) {
            List<Long> resourceIdList = page.getRecords().stream().map(Privilege::getResourceId).distinct().collect(Collectors.toList());
            List<Resource> resourceList = resourceService.findAllByIdIn(resourceIdList);
            Map<Long, Resource> resourceMap = resourceList.stream().collect(Collectors.toMap(Resource::getId, v -> v));
            List<PrivilegeVo> privilegeVos = privilegeConverter.convert(page.getRecords());
            privilegeVos.forEach(item -> {
                item.setDescription(resourceMap.getOrDefault(item.getResourceId(), new Resource()).getCategory());
            });
            return ResponseEntity.ok(new PageVo<>(page.getTotal(), privilegeVos));
        }
        return ResponseEntity.ok(new PageVo<>(0, Collections.emptyList()));
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @GetMapping("/privileges/user/{userId}")
    public ResponseEntity<List<PrivilegeVo>> privilegesByUser(@PathVariable Long userId) {
        List<Privilege> grantedPrivileges = privilegeService.findByUserId(userId);
        return ResponseEntity.ok(privilegeConverter.convert(grantedPrivileges));
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @PostMapping("/grantByResource")
    public ResponseEntity grantByResource(@RequestBody List<GrantDto> grantDtos) {
        User loginUser = userService.one(LoginUtils.getUsername());
        for (GrantDto grantDto : grantDtos) {
            // 非root用户需要确认权限是否足够
            if (!resourceService.isRoot(loginUser.getId())) {
                List<Privilege> records = privilegeService.findByUserAndResource(loginUser.getId(), grantDto.getResourceCode(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
                if (records.isEmpty()) {
                    return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
                }
            }
            for (UserPrivilegeDto userPrivilegeDto : grantDto.getUserPrivileges()) {
                User user = userService.getById(userPrivilegeDto.getUserId());
                if (user == null) {
                    return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
                }
            }
        }
        // 为多个工程授权用户
        privilegeService.grantByResource(grantDtos);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @PostMapping("/grant")
    public ResponseEntity grant(@RequestBody List<GrantDto> grantDtos) {
        User loginUser = userService.one(LoginUtils.getUsername());
        for (GrantDto grantDto : grantDtos) {
            List<Privilege> privileges = privilegeService.findByPrivilegeIdListAndUserId(Collections.singletonList(grantDto.getPrivilegeId()), loginUser.getId());
            if (privileges.isEmpty()) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        privilegeService.grant(grantDtos);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @GetMapping("/grant/{resourceCategory}/{resourceLevel}/{resourceCode}")
    public ResponseEntity<List<UserVo>> grantUsers(@PathVariable String resourceCategory, @PathVariable String resourceLevel, @PathVariable String resourceCode) {
        List<User> members = userService.findByResource(resourceCode, resourceCategory, resourceLevel, null);
        List<UserVo> usersVo = new ArrayList<>();
        if (!members.isEmpty()) {
            User user = userService.one(LoginUtils.getUsername());
            if (members.contains(user)) {
                for (User member : members) {
                    List<Privilege> privileges = privilegeService.findByUserAndResource(member.getId(), resourceCode, resourceCategory, resourceLevel, null);
                    for (Privilege privilege : privileges) {
                        UserVo userVo = userConverter.convert(member);
                        usersVo.add(userVo);
                        userVo.setRw(privilege.getCategory());
                    }
                }
                return ResponseEntity.ok(usersVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usersVo);
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @GetMapping("/grant/{privilegeId}")
    public ResponseEntity<List<UserVo>> grantUsers(@PathVariable Long privilegeId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Privilege> privileges = privilegeService.findByPrivilegeIdListAndUserId(Collections.singletonList(privilegeId), loginUser.getId());
        if (privileges.isEmpty()) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        List<User> members = userService.findByPrivilege(privilegeId);
        List<UserVo> usersVo = userConverter.convert(members);
        return ResponseEntity.ok(usersVo);
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @DeleteMapping("/grant/{resourceCategory}/{resourceLevel}/{resourceCode}/{userId}/{privilegeCategory}")
    public ResponseEntity<List<UserVo>> deleteUser(@PathVariable String resourceCategory, @PathVariable String resourceLevel, @PathVariable String resourceCode, @PathVariable("userId") Long userId, @PathVariable("privilegeCategory") String privilegeCategory) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(resourceCode, resourceCategory, resourceLevel, Constant.PRIVILEGE_READ_WRITE); // 拥有WR该资源的所有成员
        if (members.contains(loginUser)) { // 拥有RW的用户且要删除的为操作人本人的，可执行删除操作
            if (members.size() > 1) {
                privilegeService.deletePrivilegesByResourceAndUser(resourceCode, resourceCategory, resourceLevel, Collections.singletonList(userId), privilegeCategory);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_FORBIDDEN_REMOVE_SELF).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
    @DeleteMapping("/grant/{privilegeId}/{userId}")
    public ResponseEntity<List<UserVo>> deleteUser(@PathVariable Long privilegeId, @PathVariable("userId") Long userId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Privilege> privileges = privilegeService.findByPrivilegeIdListAndUserId(Collections.singletonList(privilegeId), loginUser.getId());
        if (privileges.isEmpty()) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        Resource resource = resourceService.findByPrivilegeId(privilegeId);
        if (resource != null) {
            List<User> members = userService.findByResource(resource.getCode(), resource.getCategory(), resource.getLevel(), Constant.PRIVILEGE_READ_WRITE);
            if (members.size() == 1) {
                return ResponseEntity.status(Constant.EXCEPTION_FORBIDDEN_REMOVE_SELF).build();
            }
            privilegeService.deleteGrantedPrivileges(userId, Collections.singletonList(privilegeId));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
