package com.nxin.framework.jwt;

import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private ResourceService resourceService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        com.nxin.framework.entity.auth.User user = userService.one(s);
        if (user != null) {
            List<Resource> resources = resourceService.findByUserId(user.getId());
            List<String> authorities = resources.stream().map(Resource::getCode).collect(Collectors.toList());
            return new User(s, user.getPassword(), AuthorityUtils.createAuthorityList(authorities.toArray(new String[]{})));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + s);
        }
    }
}
