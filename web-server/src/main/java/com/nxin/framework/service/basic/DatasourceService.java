package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.DatasourceMapper;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DatasourceService extends ServiceImpl<DatasourceMapper, Datasource> {

    public Datasource one(Long id) {
        return getBaseMapper().selectById(id);
    }

    public List<Datasource> all(Long projectId) {
        QueryWrapper<Datasource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Datasource.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.eq(Datasource.PROJECT_ID_COLUMN, projectId);
        return getBaseMapper().selectList(queryWrapper);
    }

    @Transactional
    public boolean save(Datasource datasource) {
        int upsert;
        if (datasource.getId() != null) {
            Datasource persisted = one(datasource.getId());
            BeanUtils.copyProperties(datasource, persisted, "version");
            datasource.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(persisted);
        } else {
            datasource.setVersion(1);
            datasource.setStatus(Constant.ACTIVE);
            datasource.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(datasource);
        }
        return upsert > 0;
    }

    public void delete(Long projectId, List<Long> idList) {
        QueryWrapper<Datasource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Datasource.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.eq(Datasource.PROJECT_ID_COLUMN, projectId);
        if (!idList.isEmpty()) {
            queryWrapper.in(Datasource.ID_COLUMN, idList);
        }
        getBaseMapper().delete(queryWrapper);
    }
}
