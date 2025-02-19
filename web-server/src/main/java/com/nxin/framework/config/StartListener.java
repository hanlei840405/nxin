package com.nxin.framework.config;

import com.nxin.framework.converter.kettle.ConvertFactory;
import com.nxin.framework.converter.kettle.job.JobConvertFactory;
import com.nxin.framework.converter.kettle.transform.TransformConvertFactory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.kettle.ShellService;
import com.nxin.framework.service.kettle.AttachmentStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private ShellService shellService;
    @Value("${dev.dir}")
    private String devDir;
    @Value("${attachment.dir}")
    private String attachmentDir;
    @Value("${download.dir}")
    private String downloadDir;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MetricsEndpoint metricsEndpoint;
    @Autowired
    private FileService fileService;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private AttachmentStorageService attachmentStorageService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        TransformConvertFactory.init(datasourceService, shellService, attachmentStorageService);
        JobConvertFactory.init(shellService, ftpService, attachmentStorageService);
        ConvertFactory.getVariable().put(Constant.VAR_ATTACHMENT_DIR, attachmentDir);
        ConvertFactory.getVariable().put(Constant.VAR_DOWNLOAD_DIR, downloadDir);
        File dev = new File(devDir);
        if (!dev.exists()) {
            dev.mkdirs();
        }
        File attachment = new File(attachmentDir);
        if (!attachment.exists()) {
            attachment.mkdirs();
        }
        File download = new File(downloadDir);
        if (!download.exists()) {
            download.mkdirs();
        }
        fileService.createEnv(Constant.ENV_PUBLISH);
        fileService.createEnv(Constant.ENV_DEV);

        Runnable runnable = () -> {
            try {
                String hostname = InetAddress.getLocalHost().getHostAddress();
                Map<String, Object> response = new HashMap<>(0);
                Map<String, Object> measurement = new HashMap<>(0);
                response.put("hostname", hostname);
                response.put("measure", measurement);
                while (true) {
                    response.put("time", new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    MetricsEndpoint.MetricResponse maxMemoryMetric = metricsEndpoint.metric("jvm.memory.max", null); //JVM最大内存
                    MetricsEndpoint.MetricResponse committedMemoryMetric = metricsEndpoint.metric("jvm.memory.committed", null); //JVM可用内存
                    MetricsEndpoint.MetricResponse usedMemoryMetric = metricsEndpoint.metric("jvm.memory.used", null); //JVM已用内存
                    MetricsEndpoint.MetricResponse threadMetric = metricsEndpoint.metric("jvm.threads.live", null); //JVM当前活跃线程数
                    MetricsEndpoint.MetricResponse systemCpuMetric = metricsEndpoint.metric("system.cpu.usage", null); //系统CPU使用率
                    MetricsEndpoint.MetricResponse processCpuMetric = metricsEndpoint.metric("process.cpu.usage", null); //当前进程CPU使用率
                    MetricsEndpoint.MetricResponse systemLoadMetric = metricsEndpoint.metric("system.load.average.1m", null); //系统CPU平均负载
                    MetricsEndpoint.MetricResponse processFileOpenMetric = metricsEndpoint.metric("process.files.open", null); //当前打开句柄数
                    measurement.put("maxMemoryMetric", String.format("%.2f", maxMemoryMetric.getMeasurements().get(0).getValue() / 1024 / 1024 / 1024));
                    measurement.put("committedMemoryMetric", String.format("%.2f", committedMemoryMetric.getMeasurements().get(0).getValue() / 1024 / 1024 / 1024));
                    measurement.put("usedMemoryMetric", String.format("%.2f", usedMemoryMetric.getMeasurements().get(0).getValue() / 1024 / 1024 / 1024));
                    measurement.put("threadMetric", String.format("%f", threadMetric.getMeasurements().get(0).getValue()));
                    measurement.put("systemCpuMetric", String.format("%.2f", systemCpuMetric.getMeasurements().get(0).getValue() * 100));
                    measurement.put("processCpuMetric", String.format("%.2f", processCpuMetric.getMeasurements().get(0).getValue() * 100));
                    if (systemLoadMetric != null) {
                        measurement.put("systemLoadMetric", String.format("%.2f", systemLoadMetric.getMeasurements().get(0).getValue()));
                    }
                    if (processFileOpenMetric != null) {
                        measurement.put("processFileOpenMetric", String.format("%f", processFileOpenMetric.getMeasurements().get(0).getValue()));
                    }
                    simpMessagingTemplate.convertAndSend("metrics", response);
                    TimeUnit.SECONDS.sleep(5L);
                }
            } catch (UnknownHostException | InterruptedException e) {
                log.error(e.toString());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
