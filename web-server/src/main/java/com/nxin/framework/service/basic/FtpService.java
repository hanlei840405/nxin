package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.FtpMapper;
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
 * FTP服务器信息 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-01-25
 */
@Service
public class FtpService extends ServiceImpl<FtpMapper, Ftp> {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    public Ftp one(Long id) {
        LambdaQueryWrapper<Ftp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Ftp::getId, id);
        queryWrapper.eq(Ftp::getStatus, Constant.ACTIVE);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public IPage<Ftp> search(Long projectId, List<Long> ftpIdList, String name, int pageNo, int pageSize) {
        Page<Ftp> page = new Page<>(pageNo, pageSize);
        if (ftpIdList.isEmpty()) {
            return page;
        }
        LambdaQueryWrapper<Ftp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Ftp::getId, ftpIdList);
        queryWrapper.eq(Ftp::getProjectId, projectId);
        queryWrapper.eq(Ftp::getStatus, Constant.ACTIVE);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(Ftp::getName, name);
        }
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    public List<Ftp> all(Long projectId, String category) {
        LambdaQueryWrapper<Ftp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Ftp::getStatus, Constant.ACTIVE);
        queryWrapper.eq(Ftp::getProjectId, projectId);
        if (StringUtils.hasLength(category)) {
            queryWrapper.eq(Ftp::getCategory, category);
        }
        return getBaseMapper().selectList(queryWrapper);
    }

    @Transactional
    public boolean save(Ftp ftp) {
        int upsert;
        if (ftp.getId() != null) {
            Ftp persisted = one(ftp.getId());
            BeanUtils.copyProperties(ftp, persisted, "version");
            ftp.setModifier(LoginUtils.getUsername());
            upsert = getBaseMapper().updateById(persisted);
        } else {
            ftp.setStatus(Constant.ACTIVE);
            ftp.setVersion(1);
            ftp.setCreator(LoginUtils.getUsername());
            upsert = getBaseMapper().insert(ftp);
            User user = userService.one(LoginUtils.getUsername());
            resourceService.registryBusinessResource(String.valueOf(ftp.getId()), ftp.getName(), Constant.RESOURCE_CATEGORY_FTP, user);
        }
        return upsert > 0;
    }
}
