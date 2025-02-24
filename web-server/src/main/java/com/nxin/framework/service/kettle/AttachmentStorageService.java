package com.nxin.framework.service.kettle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.kettle.AttachmentStorage;
import com.nxin.framework.mapper.kettle.AttachmentStorageMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 脚本运行时生成的文件存放位置 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-02-17
 */
@Service
public class AttachmentStorageService extends ServiceImpl<AttachmentStorageMapper, AttachmentStorage> {

    public List<AttachmentStorage> all(Long projectId, Long shellParentId, String category) {
        LambdaQueryWrapper<AttachmentStorage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AttachmentStorage::getProjectId, projectId).eq(AttachmentStorage::getShellParentId, shellParentId);
        if (StringUtils.hasLength(category)) {
            queryWrapper.eq(AttachmentStorage::getCategory, category);
        }
        return getBaseMapper().selectList(queryWrapper);
    }
}
