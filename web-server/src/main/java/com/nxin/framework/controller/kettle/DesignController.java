package com.nxin.framework.controller.kettle;

import com.google.common.io.Files;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.event.JobExecuteEvent;
import com.nxin.framework.event.TransformExecuteEvent;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.kettle.KettleGeneratorService;
import com.nxin.framework.service.kettle.LogService;
import com.nxin.framework.service.kettle.RunningProcessService;
import com.nxin.framework.service.kettle.ShellService;
import com.nxin.framework.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingObjectType;
import org.pentaho.di.core.logging.SimpleLoggingObject;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobConfiguration;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransConfiguration;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;


@PreAuthorize("hasAuthority('ROOT') or hasAuthority('DESIGNER')")
@Slf4j
@RestController
@RequestMapping("/design")
public class DesignController {
    @Autowired
    private LogService logService;
    @Autowired
    private KettleGeneratorService kettleGeneratorService;
    @Autowired
    private ShellService shellService;
    @Autowired
    private UserService userService;
    @Autowired
    private RunningProcessService runningProcessService;
    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String CHANNEL_TYPE_TRANS = "TRANS";

    private static final String CHANNEL_TYPE_STEP = "STEP";

    private static final String CHANNEL_TYPE_JOB = "JOB";

    @PostMapping("/execute/{id}")
    public ResponseEntity execute(@PathVariable("id") String id, @RequestBody Shell shell) throws IOException, KettleException {
        Shell existed = shellService.one(shell.getId());
        if (existed != null) {
            existed.setContent(shell.getContent());
            List<User> members = userService.findByResource(existed.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                RunningProcess runningProcess = new RunningProcess();
                runningProcess.setProd("0");
                runningProcess.setOwner(Constant.OWNER_DESIGNER);
                runningProcess.setProjectId(existed.getProjectId());
                runningProcess.setShellId(existed.getId());
                runningProcess.setInstanceId(id);
                existed.setProjectId(existed.getProjectId());
                SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject("SPOON", LoggingObjectType.SPOON, null);
                spoonLoggingObject.setContainerObjectId(id);
                if (Constant.TRANSFORM.equals(existed.getCategory())) {
                    TransExecutionConfiguration transExecutionConfiguration = new TransExecutionConfiguration();
                    transExecutionConfiguration.setLogLevel(LogLevel.BASIC);
                    Map<String, Object> transMap = kettleGeneratorService.getTransMeta(existed, false);
                    TransMeta transMeta = (TransMeta) transMap.get("transMeta");
                    TransConfiguration transConfiguration = new TransConfiguration(transMeta, transExecutionConfiguration);
                    spoonLoggingObject.setLogLevel(transExecutionConfiguration.getLogLevel());
                    Trans trans = new Trans(transMeta, spoonLoggingObject);
                    trans.injectVariables(transConfiguration.getTransExecutionConfiguration().getVariables());
                    trans.setGatheringMetrics(true);
                    transMeta.getXML();
                    TransformExecuteEvent transformExecuteEvent = new TransformExecuteEvent(runningProcess, id, trans, transConfiguration);
                    applicationContext.publishEvent(transformExecuteEvent);
                    runningProcess.setInstanceName(trans.getName());
                    runningProcess.setCategory(Constant.TRANSFORM);
                    runningProcess.setVersion(1);
                    runningProcessService.save(runningProcess);
                    return ResponseEntity.ok().build();
                } else if (Constant.JOB.equals(existed.getCategory())) {
                    JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
                    jobExecutionConfiguration.setLogLevel(LogLevel.BASIC);
                    Map<String, Object> jobMap = kettleGeneratorService.getJobMeta(existed, false);
                    JobMeta jobMeta = (JobMeta) jobMap.get("jobMeta");
                    JobConfiguration jobConfiguration = new JobConfiguration(jobMeta, jobExecutionConfiguration);
                    spoonLoggingObject.setContainerObjectId(id);
                    spoonLoggingObject.setLogLevel(jobExecutionConfiguration.getLogLevel());
                    jobMeta.getXML();
                    Job job = new Job(null, jobMeta, spoonLoggingObject);
                    job.injectVariables(jobConfiguration.getJobExecutionConfiguration().getVariables());
                    job.setGatheringMetrics(true);
                    JobExecuteEvent jobExecuteEvent = new JobExecuteEvent(runningProcess, id, job, jobConfiguration);
                    applicationContext.publishEvent(jobExecuteEvent);
                    runningProcess.setInstanceName(job.getName());
                    runningProcess.setCategory(Constant.JOB);
                    runningProcess.setVersion(1);
                    runningProcessService.save(runningProcess);
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/stop")
    public ResponseEntity stop(@RequestBody CrudDto crudDto) {
        Shell existed = shellService.one(crudDto.getId());
        if (existed != null) {
            List<User> members = userService.findByResource(existed.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.stream().anyMatch(member -> member.getEmail().equals(LoginUtils.getUsername()))) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("category", existed.getCategory());
                    jsonObject.put("name", existed.getName());
                    jsonObject.put("instanceId", crudDto.getPayload());
                    stringRedisTemplate.convertAndSend(Constant.TOPIC_DESIGNER_SHUTDOWN, jsonObject.toJSONString());
                    return ResponseEntity.ok().build();
                } catch (NullPointerException e) {
                    return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
