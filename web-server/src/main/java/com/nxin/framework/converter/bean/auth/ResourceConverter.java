package com.nxin.framework.converter.bean.auth;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.vo.auth.ResourceVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceConverter extends BeanConverter<ResourceVo, Resource> {
    @Override
    public ResourceVo convert(Resource resource, String... ignores) {
        ResourceVo resourceVo = new ResourceVo();
        BeanUtils.copyProperties(resource, resourceVo, ignores);
        return resourceVo;
    }

    @Override
    public List<ResourceVo> convert(List<Resource> resources, String... ignores) {
        return resources.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}