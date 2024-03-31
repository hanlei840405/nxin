package com.nxin.framework.controller.auth;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.auth.UserConverter;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.auth.UserDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.jwt.JwtUserDetailsService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.auth.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('SETTING')")
@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private BeanConverter<UserVo, User> userConverter = new UserConverter();

    @GetMapping("/user/{id}")
    public ResponseEntity<UserVo> one(@PathVariable("id") Long id) {
        User targetUser = userService.one(id);
        if (targetUser != null) {
            return ResponseEntity.ok(userConverter.convert(targetUser));
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/users")
    public ResponseEntity<PageVo<UserVo>> users(@RequestBody CrudDto crudDto) {
        IPage<User> userPage = userService.search(crudDto.getPayload(), crudDto.getPageNo(), crudDto.getPageSize());
        return ResponseEntity.ok(new PageVo<>(userPage.getTotal(), userConverter.convert(userPage.getRecords())));
    }

    @PostMapping("/user")
    public ResponseEntity save(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user, "version", "password");
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity lock(@PathVariable("id") Long id) {
        User persist = userService.one(id);
        if (persist != null) {
            userService.lock(persist);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        User persist = userService.one(id);
        if (persist != null) {
            userService.close(persist);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }
}
