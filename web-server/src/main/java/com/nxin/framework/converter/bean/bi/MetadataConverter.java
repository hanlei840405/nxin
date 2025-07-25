package com.nxin.framework.converter.bean.bi;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.bi.Metadata;
import com.nxin.framework.vo.bi.MetadataVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MetadataConverter extends BeanConverter<MetadataVo, Metadata> {

    @Override
    public MetadataVo convert(Metadata metadata, String... ignores) {
        MetadataVo metadataVo = new MetadataVo();
        BeanUtils.copyProperties(metadata, metadataVo, ignores);
        return metadataVo;
    }

    @Override
    public List<MetadataVo> convert(List<Metadata> modelList, String... ignores) {
        return modelList.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
