package com.nxin.framework.service.basic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.basic.FtpMapper;
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
    private FtpMapper ftpMapper;

    public Ftp one(Long id) {
        return ftpMapper.selectById(id);
    }

    public List<Ftp> all(Long projectId, String category) {
        LambdaQueryWrapper<Ftp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Ftp::getStatus, Constant.ACTIVE);
        queryWrapper.eq(Ftp::getProjectId, projectId);
        if (StringUtils.hasLength(category)) {
            queryWrapper.eq(Ftp::getCategory, category);
        }
        return ftpMapper.selectList(queryWrapper);
    }

    @Transactional
    public boolean save(Ftp datasource) {
        int upsert;
        if (datasource.getId() != null) {
            Ftp persisted = one(datasource.getId());
            BeanUtils.copyProperties(datasource, persisted, "version");
            datasource.setModifier(LoginUtils.getUsername());
            upsert = ftpMapper.updateById(persisted);
        } else {
            datasource.setStatus(Constant.ACTIVE);
            datasource.setCreator(LoginUtils.getUsername());
            upsert = ftpMapper.insert(datasource);
        }
        return upsert > 0;
    }

    public void delete(Long projectId, List<Long> idList) {
        LambdaQueryWrapper<Ftp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Ftp::getStatus, Constant.ACTIVE);
        queryWrapper.eq(Ftp::getProjectId, projectId);
        if (!idList.isEmpty()) {
            queryWrapper.in(Ftp::getId, idList);
        }
        ftpMapper.delete(queryWrapper);
    }
}
