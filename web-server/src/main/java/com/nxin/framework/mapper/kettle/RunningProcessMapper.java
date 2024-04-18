package com.nxin.framework.mapper.kettle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.entity.kettle.RunningProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jesse han
 * @since 2024-02-09
 */
public interface RunningProcessMapper extends BaseMapper<RunningProcess> {

    IPage<RunningProcess> selectStreamingRunningProcess(IPage<RunningProcess> page, @Param("category") String category, @Param("streaming") String streaming, @Param("projectIds") List<Long> projectIds);
}
