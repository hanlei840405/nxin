package com.nxin.framework.converter.bean.kettle;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.entity.kettle.AttachmentStorage;
import com.nxin.framework.vo.kettle.AttachmentStorageVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class AttachmentStorageConverter extends BeanConverter<AttachmentStorageVo, AttachmentStorage> {

    @Override
    public AttachmentStorageVo convert(AttachmentStorage attachmentStorage, String... ignores) {
        AttachmentStorageVo attachmentStorageVo = new AttachmentStorageVo();
        BeanUtils.copyProperties(attachmentStorage, attachmentStorageVo, ignores);
        return attachmentStorageVo;
    }

    @Override
    public List<AttachmentStorageVo> convert(List<AttachmentStorage> attachmentStorages, String... ignores) {
        return attachmentStorages.stream().map(item -> this.convert(item, ignores)).collect(Collectors.toList());
    }
}
