package com.nxin.framework.service.auth;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.Log;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.auth.LogMapper;
import com.nxin.framework.utils.LoginUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限操作日志 服务实现类
 * </p>
 *
 * @author jesse han
 * @since 2025-07-23
 */
@Service
public class AuthLogService extends ServiceImpl<LogMapper, Log> {

    public void save(Long userId, List<Privilege> privilegeList) {
        List<Log> logs = new ArrayList<>();
        for (Privilege privilege : privilegeList) {
            Log log = new Log();
            log.setUserId(userId);
            log.setPrivilegeId(privilege.getId());
            log.setAction(Constant.ACTION_DEL);
            log.setActionTime(LocalDateTime.now());
            log.setOperator(LoginUtils.getUsername());
            logs.add(log);
        }
        this.saveBatch(logs);
    }
}
