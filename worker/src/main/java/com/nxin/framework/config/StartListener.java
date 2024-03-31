package com.nxin.framework.config;

import com.nxin.framework.service.task.JobListener;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${dev.dir}")
    private String devDir;
    @Value("${production.dir}")
    private String productionDir;
    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        File dev = new File(devDir);
        if (!dev.exists()) {
            dev.mkdirs();
        }
        File production = new File(productionDir);
        if (!production.exists()) {
            production.mkdirs();
        }
        try {
            scheduler.getListenerManager().addJobListener(new JobListener());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
