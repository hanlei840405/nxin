package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.DatasourceMapper;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DatasourceService extends ServiceImpl<DatasourceMapper, Datasource> {
    @Autowired
    private DatasourceMapper datasourceMapper;

    public Datasource one(Long id) {
        return datasourceMapper.selectById(id);
    }

    public List<Datasource> all(Long projectId) {
        QueryWrapper<Datasource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Datasource.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.eq(Datasource.PROJECT_ID_COLUMN, projectId);
        return datasourceMapper.selectList(queryWrapper);
    }

    @Transactional
    public boolean save(Datasource datasource) {
        int upsert;
        if (datasource.getId() != null) {
            Datasource persisted = one(datasource.getId());
            BeanUtils.copyProperties(datasource, persisted, "version");
            datasource.setModifier(LoginUtils.getUsername());
            upsert = datasourceMapper.updateById(persisted);
        } else {
            datasource.setStatus(Constant.ACTIVE);
            datasource.setCreator(LoginUtils.getUsername());
            upsert = datasourceMapper.insert(datasource);
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
        datasourceMapper.delete(queryWrapper);
    }
}
