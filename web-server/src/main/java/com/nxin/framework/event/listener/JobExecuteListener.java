package com.nxin.framework.event.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nxin.framework.entity.basic.Ftp;
import com.nxin.framework.entity.kettle.AttachmentStorage;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.event.JobExecuteEvent;
import com.nxin.framework.service.basic.FtpService;
import com.nxin.framework.service.kettle.AttachmentStorageService;
import com.nxin.framework.service.kettle.LogService;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.service.kettle.ShellService;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.job.DelegationAdapter;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransAdapter;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JobExecuteListener {
    @Autowired
    private LogService logService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RunningProcessService runningProcessService;
    @Autowired
    private ShellService shellService;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private AttachmentStorageService attachmentStorageService;
    @Value("${etl.log.send-delay}")
    private Integer sendDelay = 5;
    @Value("${dev.dir}")
    private String devDir;

    @Async
    @EventListener(JobExecuteEvent.class)
    public void action(JobExecuteEvent jobExecuteEvent) {
        Shell shell = shellService.one(jobExecuteEvent.getShellId());
        List<Ftp> ftps = ftpService.all(shell.getProjectId(), "SFTP");
        LambdaQueryWrapper<AttachmentStorage> attachmentStorageLambdaQueryWrapper = new LambdaQueryWrapper<>();
        attachmentStorageLambdaQueryWrapper.eq(AttachmentStorage::getShellId, jobExecuteEvent.getShellId());
        List<AttachmentStorage> attachmentStorages = attachmentStorageService.list(attachmentStorageLambdaQueryWrapper);

        RunningProcess runningProcess = (RunningProcess) jobExecuteEvent.getSource();
        LoggingRegistry loggingRegistry = LoggingRegistry.getInstance();
        try {
            for (Ftp ftp : ftps) {
                if (ftp.getUsePrivateKey()) {
                    String sshFilePath = devDir.concat(File.separator).concat(Constant.SSH_PATH).concat(File.separator).concat(shell.getProjectId().toString());
                    File sshFileFolder = new File(sshFilePath);
                    if (!sshFileFolder.exists()) {
                        sshFileFolder.mkdirs();
                    }
                    File ssh = new File(sshFilePath.concat(File.separator).concat(ftp.getId().toString()));
                    Files.write(ssh.toPath(), ftp.getPrivateKey().getBytes(Charset.defaultCharset()));
                }
            }
            for (AttachmentStorage attachmentStorage : attachmentStorages) {
                File folder = new File(attachmentStorage.getStorageDir());
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            }
            CarteSingleton.getInstance().getJobMap().addJob(jobExecuteEvent.getJob().getName(), jobExecuteEvent.getInstanceId(), jobExecuteEvent.getJob(), jobExecuteEvent.getJobConfiguration());
            // 空参调用
            jobExecuteEvent.getJob().setLogLevel(LogLevel.BASIC);
            KettleLoggingEventListener kettleLoggingEventListener = kettleLoggingEvent -> {
                LogMessageInterface logMessageInterface = (LogMessageInterface) kettleLoggingEvent.getMessage();
                List<String> childrenLogChannelIds = loggingRegistry.getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
                log.info("current: {}", logMessageInterface.getLogChannelId());
                if (childrenLogChannelIds.contains(logMessageInterface.getLogChannelId())) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("log", logMessageInterface.toString());
                    response.put("steps", Collections.EMPTY_LIST);
                    if (jobExecuteEvent.getJob().isFinished() || jobExecuteEvent.getJob().isStopped()) {
                        response.put("running", false);
                    } else {
                        response.put("running", true);
                    }
                    simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), response);
                }
            };
            KettleLogStore.getAppender().addLoggingEventListener(kettleLoggingEventListener);

            RowListener rowListener = new RowAdapter() {
                /**
                 * 输出每一行数据
                 */
                @Override
                public void rowReadEvent(RowMetaInterface rowMeta, Object[] row) throws KettleStepException {
//                    String[] names = rowMeta.getFieldNames();
//                    for (int i = 0; i < names.length; i++) {
//                        log.info("name: {}, value: {}", names[i], row[i]);
//                    }
                    super.rowReadEvent(rowMeta, row);
                }
            };
            // 添加每个步骤的数据监听器，可以实现步骤数据的审计功能
            TransAdapter transAdapter = new TransAdapter() {
                @Override
                public void transStarted(Trans trans) throws KettleException {
                    trans.getSteps().forEach(stepMetaDataCombi -> {
                        stepMetaDataCombi.step.addRowListener(rowListener);
                    });
                }
            };
            DelegationAdapter delegationAdapter = new DelegationAdapter() {
                @Override
                public void transformationDelegationStarted(Trans delegatedTrans, TransExecutionConfiguration transExecutionConfiguration) {
                    delegatedTrans.addTransListener(transAdapter);
                }
            };
            jobExecuteEvent.getJob().addDelegationListener(delegationAdapter);
            jobExecuteEvent.getJob().start();

            jobExecuteEvent.getJob().waitUntilFinished();
            KettleLogStore.getAppender().removeLoggingEventListener(kettleLoggingEventListener);
            CarteSingleton.getInstance().getJobMap().removeJob(new CarteObjectEntry(jobExecuteEvent.getJob().getName(), jobExecuteEvent.getInstanceId()));
            runningProcessService.delete(runningProcess);
            List<String> logChannelIds = LoggingRegistry.getInstance().getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
            Map<String, Object> response = logService.fetchLogs(logChannelIds);
            response.put("log", KettleLogStore.getAppender().getBuffer(jobExecuteEvent.getJob().getLogChannelId(), true).toString());
            if (jobExecuteEvent.getJob().isFinished() || jobExecuteEvent.getJob().isStopped()) {
                response.put("running", false);
            } else {
                response.put("running", true);
            }
            simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), response);
            KettleLogStore.discardLines(jobExecuteEvent.getJob().getLogChannelId(), true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            List<String> logChannelIds = loggingRegistry.getLogChannelChildren(jobExecuteEvent.getJob().getLogChannelId());
//            Map<String, Object> error = logService.fetchLogs(logChannelIds);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e + "\r\n" + e.getStackTrace()[0].toString());
            simpMessagingTemplate.convertAndSend(jobExecuteEvent.getInstanceId(), error);
        }
    }
}
