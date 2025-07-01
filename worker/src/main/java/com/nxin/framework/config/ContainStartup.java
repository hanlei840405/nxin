package com.nxin.framework.config;

import com.nxin.framework.message.sender.SenderUtils;
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
public class ContainStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${production.dir}")
    private String productionDir;
    @Value("${attachment.dir}")
    private String attachmentDir;
    @Value("${log.dir}")
    private String logDir;
    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        SenderUtils.init();
        File production = new File(productionDir);
        if (!production.exists()) {
            production.mkdirs();
        }
        File attachment = new File(attachmentDir);
        if (!attachment.exists()) {
            attachment.mkdirs();
        }
        File log = new File(logDir);
        if (!log.exists()) {
            log.mkdirs();
        }
        try {
            scheduler.getListenerManager().addJobListener(new JobListener());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
