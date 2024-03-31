package com.nxin.framework.mapper.kettle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nxin.framework.entity.kettle.ShellPublish;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author jesse han
 * @since 2024-02-10
 */
public interface ShellPublishMapper extends BaseMapper<ShellPublish> {

    ShellPublish selectLatestByShellId(@Param("shellId") Long shellId);

    ShellPublish selectLatestByProdAndShellId(@Param("shellId") Long shellId);
}
