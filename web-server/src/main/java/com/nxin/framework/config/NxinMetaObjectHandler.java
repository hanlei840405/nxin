package com.nxin.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.nxin.framework.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
public class NxinMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("creator", LoginUtils.getUsername(), metaObject);
        this.setFieldValByName("modifier", LoginUtils.getUsername(), metaObject);
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("modifyTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifier", LoginUtils.getUsername(), metaObject);
        this.setFieldValByName("modifyTime", LocalDateTime.now(), metaObject);
    }
}
