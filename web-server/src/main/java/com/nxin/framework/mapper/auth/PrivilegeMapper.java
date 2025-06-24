package com.nxin.framework.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.entity.auth.Privilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jesse han
 * @since 2024-01-02
 */
public interface PrivilegeMapper extends BaseMapper<Privilege> {

    List<Privilege> selectByUserId(@Param("userId") Long userId);

    Privilege selectByPrivilegeIdAndUserId(@Param("privilegeId") Long privilegeId, @Param("userId") Long userId);

    List<Privilege> selectByUserAndResource(@Param("userId") Long userId, @Param("resourceCode") String resourceCode, @Param("resourceCategory") String resourceCategory, @Param("resourceLevel") String resourceLevel, @Param("rw") String rw);

    List<Privilege> findByRwAndResource(@Param("resourceCode") String resourceCode, @Param("resourceCategory") String resourceCategory, @Param("resourceLevel") String resourceLevel, @Param("rw") String rw);

    IPage<Privilege> selectBusinessPrivilegeByUserAndName(IPage<Privilege> page, @Param("userId") Long userId, @Param("name") String name);

    void grantPrivileges(@Param("userId") Long userId, @Param("privileges") List<Long> privileges);

    int deletePrivilegesByUserId(@Param("userId") Long userId);

    int deletePrivilegesByResourceAndUsers(@Param("resourceCode") String resourceCode, @Param("resourceCategory") String resourceCategory, @Param("resourceLevel") String resourceLevel, @Param("users") List<Long> users, @Param("privilegeCategory") String privilegeCategory);

    int deleteGrantedPrivileges(@Param("userId") Long userId, @Param("privileges") List<Long> privileges);
}
