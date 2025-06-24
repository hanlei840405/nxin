package com.nxin.framework.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nxin.framework.entity.auth.Resource;
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
public interface ResourceMapper extends BaseMapper<Resource> {

    Resource selectRootByUserId(@Param("userId") Long userId, @Param("status") String status);

    List<Resource> selectByUserId(@Param("userId") Long userId, @Param("status") String status);

    List<Resource> selectByCategoryAndLevel(@Param("category") String category, @Param("level") String level);

    List<Resource> selectByUserIdAndCategoryAndLevel(@Param("userId") Long userId, @Param("category") String category, @Param("level") String level);

    List<Resource> findByPrivilegeId(@Param("privilegeId") Long privilegeId);
}
