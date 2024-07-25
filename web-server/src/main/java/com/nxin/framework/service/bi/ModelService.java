package com.nxin.framework.service.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.bi.Metadata;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.bi.ModelMapper;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Model one(Long id) {
        return getBaseMapper().selectById(id);
    }

    public List<Model> all(Long projectId) {
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Model::getStatus, Constant.ACTIVE);
        queryWrapper.eq(Model::getProjectId, projectId);
        return getBaseMapper().selectList(queryWrapper);
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
            model.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(model);
        }
        LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Metadata::getModelId, model.getId());
        metadataService.remove(queryWrapper);
        metadataList.forEach(metadata -> metadata.setModelId(model.getId()));
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
