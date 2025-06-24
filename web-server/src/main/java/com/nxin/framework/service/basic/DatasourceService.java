package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.DatasourceMapper;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DatasourceService extends ServiceImpl<DatasourceMapper, Datasource> {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    public Datasource one(Long id) {
        LambdaQueryWrapper<Datasource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Datasource::getId, id);
        queryWrapper.eq(Datasource::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public IPage<Datasource> search(Long projectId, List<Long> datasourceIdList, String name, int pageNo, int pageSize) {
        Page<Datasource> page = new Page<>(pageNo, pageSize);
        if (datasourceIdList.isEmpty()) {
            return page;
        }
        LambdaQueryWrapper<Datasource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Datasource::getId, datasourceIdList);
        queryWrapper.eq(Datasource::getProjectId, projectId);
        queryWrapper.eq(Datasource::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Datasource::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    public List<Datasource> all(Long projectId) {
        LambdaQueryWrapper<Datasource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Datasource::getStatus, Constant.ACTIVE);
        queryWrapper.eq(Datasource::getProjectId, projectId);
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
            User user = userService.one(LoginUtils.getUsername());
            resourceService.registryBusinessResource(String.valueOf(datasource.getId()), datasource.getName(), Constant.RESOURCE_CATEGORY_DATASOURCE, user);
        }
        return upsert > 0;
    }
}
