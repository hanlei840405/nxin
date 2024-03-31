package com.nxin.framework.controller.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.auth.ResourceConverter;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.vo.auth.ResourceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('PRIVILEGE')")
@RestController
@RequestMapping
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    private BeanConverter<ResourceVo, Resource> resourceConverter = new ResourceConverter();

    @GetMapping("/resources/{userId}")
    public ResponseEntity<List<ResourceVo>> resources(@PathVariable Long userId) {
        List<Resource> resources = resourceService.findByUserId(userId);
        List<ResourceVo> resourceVos = resourceConverter.convert(resources);
        return ResponseEntity.ok(resourceVos);
    }
}
