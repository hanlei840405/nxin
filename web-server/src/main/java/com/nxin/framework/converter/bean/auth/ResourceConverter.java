package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.vo.auth.ResourceVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceConverter extends BeanConverter<ResourceVo, Resource> {
    @Override
    public ResourceVo convert(Resource resource) {
        ResourceVo resourceVo = new ResourceVo();
        BeanUtils.copyProperties(resource, resourceVo);
        return resourceVo;
    }

    @Override
    public List<ResourceVo> convert(List<Resource> resources) {
        return resources.stream().map(this::convert).collect(Collectors.toList());
    }
}