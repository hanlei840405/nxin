package com.nxin.framework.mapper.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nxin.framework.entity.basic.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jesse han
 * @since 2024-01-04
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> selectByMemberIdAndName(@Param("userId") Long userId, @Param("name") String name);

    void insertMember(@Param("projectId") Long projectId, @Param("users") List<Long> users);

    void deleteMember(@Param("projectId") Long projectId, @Param("users") List<Long> users);
}
