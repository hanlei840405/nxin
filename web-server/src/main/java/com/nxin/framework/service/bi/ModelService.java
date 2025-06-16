package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Metadata;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.bi.ModelMapper;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2024-07-24
 */
@Service
public class ModelService extends ServiceImpl<ModelMapper, Model> {

    @Autowired
    private MetadataService metadataService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    public Model one(Long id) {
        return getBaseMapper().selectById(id);
    }

    public IPage<Model> search(Long projectId, String name, int pageNo, int pageSize) {
        Page<Model> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Model::getProjectId, projectId);
        queryWrapper.eq(Model::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Model::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Transactional
    public boolean save(Model model, List<Metadata> metadataList) {
        int upsert;
        if (model.getId() != null) {
            Model persisted = one(model.getId());
            BeanUtils.copyProperties(model, persisted, "version");
            model.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(persisted);
        } else {
            model.setStatus(Constant.ACTIVE);
            model.setVersion(1);
            model.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(model);
            User user = userService.one(LoginUtils.getUsername());
            resourceService.registryBusinessResource(String.valueOf(model.getId()), model.getName(), Constant.RESOURCE_CATEGORY_MODEL, user);
        }
        LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Metadata::getModelId, model.getId());
        metadataService.remove(queryWrapper);
        metadataList.forEach(metadata -> {
            metadata.setModelId(model.getId());
            metadata.setVersion(1);
        });
        metadataService.saveBatch(metadataList);
        return upsert > 0;
    }

    @Transactional
    public void delete(List<Long> idList) {
        LambdaQueryWrapper<Model> modelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        modelLambdaQueryWrapper.in(Model::getId, idList);
        getBaseMapper().delete(modelLambdaQueryWrapper);
        LambdaQueryWrapper<Metadata> metadataLambdaQueryWrapper = new LambdaQueryWrapper<>();
        metadataLambdaQueryWrapper.in(Metadata::getModelId, idList);
        metadataService.remove(metadataLambdaQueryWrapper);
    }
}
