package com.nxin.framework.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.entity.auth.Apply;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 权限申请 Mapper 接口
 * </p>
 *
 * @author jesse han
 * @since 2025-06-23
 */
public interface ApplyMapper extends BaseMapper<Apply> {

    IPage<Apply> selectUnAudit(IPage<Apply> page, @Param("creator") String creator, @Param("auditor") Long auditor);
}
