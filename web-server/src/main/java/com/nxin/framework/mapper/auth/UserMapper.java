package com.nxin.framework.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nxin.framework.entity.auth.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jesse han
 * @since 2024-01-02
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> findByResource(@Param("resourceCode") String resourceCode, @Param("resourceCategory") String resourceCategory, @Param("resourceLevel") String resourceLevel, @Param("rw") String rw);

    List<User> findByPrivilege(@Param("privilegeId") Long privilegeId);
}
