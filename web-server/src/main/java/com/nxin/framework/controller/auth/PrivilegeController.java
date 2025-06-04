package com.nxin.framework.controller.auth;

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
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
@RestController
@RequestMapping
public class PrivilegeController {
    @Autowired
    private UserService userService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private ResourceService resourceService;
    private BeanConverter<ResourceVo, Resource> resourceConverter = new ResourceConverter();
    private BeanConverter<PrivilegeVo, Privilege> privilegeConverter = new PrivilegeConverter();
    private BeanConverter<UserVo, User> userConverter = new UserConverter();

    @PostMapping("/privileges")
    public ResponseEntity<PageVo<PrivilegeVo>> privileges(@RequestBody CrudDto crudDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        IPage<Privilege> page = privilegeService.search(crudDto.getPayload(), loginUser.getId(), crudDto.getPageNo(), crudDto.getPageSize());
        return ResponseEntity.ok(new PageVo<>(page.getTotal(), privilegeConverter.convert(page.getRecords())));
    }

    @GetMapping("/privileges")
    public ResponseEntity<Map<String, List<ResourceVo>>> privileges() {
        return ResponseEntity.ok(resourceConverter.convert(resourceService.all()).stream().collect(Collectors.groupingBy(ResourceVo::getCategory, LinkedHashMap::new, Collectors.toList())));
    }

    @GetMapping("/privileges/{userId}")
    public ResponseEntity<List<PrivilegeVo>> privileges(@PathVariable Long userId) {
        List<Privilege> grantedPrivileges = privilegeService.findByUserId(userId);
        return ResponseEntity.ok(privilegeConverter.convert(grantedPrivileges));
    }

    @PostMapping("/grantByResource")
    public ResponseEntity grantByResource(@RequestBody List<GrantDto> grantDtos) {
        User loginUser = userService.one(LoginUtils.getUsername());
        for (GrantDto grantDto : grantDtos) {
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

    @PostMapping("/grant")
    public ResponseEntity grant(@RequestBody List<GrantDto> grantDtos) {
        User loginUser = userService.one(LoginUtils.getUsername());
        for (GrantDto grantDto : grantDtos) {
            Privilege privilege = privilegeService.findByPrivilegeIdAndUserId(grantDto.getPrivilegeId(), loginUser.getId());
            if (privilege == null) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        privilegeService.grant(grantDtos);
        return ResponseEntity.ok().build();
    }

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

    @GetMapping("/grant/{privilegeId}")
    public ResponseEntity<List<UserVo>> grantUsers(@PathVariable Long privilegeId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Privilege privilege = privilegeService.findByPrivilegeIdAndUserId(privilegeId, loginUser.getId());
        if (privilege == null) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        List<User> members = userService.findByPrivilege(privilegeId);
        List<UserVo> usersVo = userConverter.convert(members);
        return ResponseEntity.ok(usersVo);
    }

    @DeleteMapping("/grant/{resourceCategory}/{resourceLevel}/{resourceCode}/{userId}")
    public ResponseEntity<List<UserVo>> deleteUser(@PathVariable String resourceCategory, @PathVariable String resourceLevel, @PathVariable String resourceCode, @PathVariable("userId") Long userId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(resourceCode, resourceCategory, resourceLevel, Constant.PRIVILEGE_READ_WRITE); // 拥有WR该资源的所有成员
        if (members.contains(loginUser)) { // 拥有RW的用户且要删除的为操作人本人的，可执行删除操作
            if (members.size() > 1) {
                privilegeService.deletePrivilegesByResourceAndUser(resourceCode, resourceCategory, resourceLevel, Collections.singletonList(userId));
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(Constant.EXCEPTION_FORBIDDEN_REMOVE_SELF).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @DeleteMapping("/grant/{privilegeId}/{userId}")
    public ResponseEntity<List<UserVo>> deleteUser(@PathVariable Long privilegeId, @PathVariable("userId") Long userId) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Privilege privilege = privilegeService.findByPrivilegeIdAndUserId(privilegeId, loginUser.getId());
        if (privilege == null) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        List<Resource> resources = resourceService.findByPrivilegeId(privilegeId);
        for (Resource resource : resources) {
            List<User> members = userService.findByResource(resource.getCode(), resource.getCategory(), resource.getLevel(), Constant.PRIVILEGE_READ_WRITE);
            if (members.size() == 1) {
                return ResponseEntity.status(Constant.EXCEPTION_FORBIDDEN_REMOVE_SELF).build();
            }
        }
        privilegeService.deleteGrantedPrivileges(userId, Collections.singletonList(privilegeId));
        return ResponseEntity.ok().build();
    }
}
