package com.nxin.framework.config;

import com.nxin.framework.message.sender.SenderUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ContainStartup implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        SenderUtils.init();
    }
}
