package com.nxin.framework.service.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.nxin.framework.entity.kettle.RunningProcess;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.entity.kettle.ShellPublish;
import com.nxin.framework.entity.task.TaskHistory;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.exception.*;
import com.nxin.framework.mapper.kettle.ShellPublishMapper;
import com.nxin.framework.request.StreamingReq;
import com.nxin.framework.request.TaskReq;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.service.task.TaskHistoryService;
import com.nxin.framework.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.www.CarteObjectEntry;
import org.pentaho.di.www.CarteSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShellPublishService extends ServiceImpl<ShellPublishMapper, ShellPublish> {
    @Value("${dev.dir}")
    private String devDir;
    @Value("${etl.log.send-delay}")
    private Integer sendDelay = 5;
    @Value("${worker.schedule-create-job-uri}")
    private String createJobUri;
    @Value("${worker.schedule-stop-uri}")
    private String stopUri;
    @Value("${worker.schedule-create-streaming-uri}")
    private String createStreamingUri;
    @Autowired
    private ShellPublishMapper shellPublishMapper;
    @Autowired
    private KettleGeneratorService kettleGeneratorService;
    @Autowired
    private RunningProcessService runningProcessService;
    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;
    @Autowired
    private TaskHistoryService taskHistoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ShellService shellService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RestTemplate restTemplate;

    public ShellPublish one(Long id) {
        return shellPublishMapper.selectById(id);
    }

//    public ShellPublish online(Long shellId) {
//        QueryWrapper<ShellPublish> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(ShellPublish.STATUS_COLUMN, Constant.ACTIVE);
//        queryWrapper.eq(ShellPublish.SHELL_ID_COLUMN, shellId);
//        queryWrapper.orderByDesc(ShellPublish.CREATE_TIME_COLUMN);
//        List<ShellPublish> records = shellPublishMapper.selectList(queryWrapper);
//        if (records.isEmpty()) {
//            return null;
//        }
//        return records.get(0);
//    }

    public IPage<ShellPublish> online(String name, String streaming, Set<Long> ids, int pageNo, int pageSize) {
        Page<ShellPublish> page = new Page<>(pageNo, pageSize);
        QueryWrapper<ShellPublish> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(ShellPublish.ID_COLUMN, ids);
        if (StringUtils.hasLength(name)) {
            queryWrapper.likeRight(ShellPublish.NAME_COLUMN, name);
        }
        queryWrapper.eq(ShellPublish.STREAMING_COLUMN, streaming);
        return shellPublishMapper.selectPage(page, queryWrapper);
    }

    public IPage<ShellPublish> findHistories(Long shellId, int pageNo, int pageSize) {
        Page<ShellPublish> page = new Page<>(pageNo, pageSize);
        QueryWrapper<ShellPublish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ShellPublish.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.eq(ShellPublish.SHELL_ID_COLUMN, shellId);
        queryWrapper.orderByDesc(ShellPublish.CREATE_TIME_COLUMN);
        return shellPublishMapper.selectPage(page, queryWrapper);
    }

//    public List<ShellPublish> references(ShellPublish shellPublish, Long tenantId) {
//        if (StringUtils.hasLength(shellPublish.getReference())) {
//            long[] ids = Arrays.asList(shellPublish.getReference().split(",")).stream().mapToLong(Long::parseLong).toArray();
//            return shellPublishMapper.findAllByIdInAndTenantId(ids, tenantId);
//        }
//        return Collections.EMPTY_LIST;
//    }

    /**
     * 创建历史版本
     *
     * @param shell
     */
    @Transactional
    public void save(Shell shell, String description) throws RecordsNotMatchException, IOException, FileNotExistedException {
        if (shell.getExecutable()) {
            String streamingOrBatch = Constant.BATCH;
            Map<String, Object> result;
            String suffix;
            AbstractMeta abstractMeta;
            String content = fileService.content(Constant.ENV_DEV, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + Constant.DOT + Constant.GRAPH_SUFFIX);
            shell.setContent(content);
            if (Constant.JOB.equals(shell.getCategory())) {
                result = kettleGeneratorService.getJobMeta(shell, true);
                suffix = Constant.JOB_SUFFIX;
                abstractMeta = (JobMeta) result.get("jobMeta");
            } else {
                result = kettleGeneratorService.getTransMeta(shell, true);
                TransMeta transMeta = (TransMeta) result.get("transMeta");
                StepMeta[] stepMetas = transMeta.getStepsArray();
                for (StepMeta stepMeta : stepMetas) {
                    if (Constant.STREAMING_STEP.contains(stepMeta.getTypeId())) {
                        streamingOrBatch = Constant.STREAMING;
                    }
                }
                suffix = Constant.TRANS_SUFFIX;
                abstractMeta = (TransMeta) result.get("transMeta");
            }
            ShellPublish shellPublish = new ShellPublish();
            shellPublish.setName(shell.getName());
            shellPublish.setStreaming(streamingOrBatch);
            shellPublish.setProjectId(shell.getProjectId());
            shellPublish.setShellId(shell.getId());
            shellPublish.setDescription(description);
            shellPublish.setStreaming(shell.getStreaming());
            shellPublish.setStatus(Constant.ACTIVE);
            shellPublish.setVersion(1);
            shellPublish.setCreator(LoginUtils.getUsername());
            // 将脚本文件目录指定到publish目录下
            String reference = (String) result.get("referenceIds");
            List<ShellPublish> referencedList = new ArrayList<>(0);
            if (StringUtils.hasLength(reference)) {
                String[] references = reference.split(",");
                for (String referenceId : references) {
                    ShellPublish latestReferenceShellPublish = shellPublishMapper.selectLatestByShellId(Long.parseLong(referenceId));
                    if (latestReferenceShellPublish != null) {
                        referencedList.add(latestReferenceShellPublish);
                    }
                }
                // 如果关联脚本有未发布的，将抛出异常
                if (referencedList.size() != references.length) {
                    throw new RecordsNotMatchException();
                }
                shellPublish.setReference(referencedList.stream().map(item -> String.valueOf(item.getId())).collect(Collectors.joining(",")));
            }
            shellPublishMapper.insert(shellPublish);
            try {
                String md5Shell = fileService.createFile(Constant.ENV_PUBLISH, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + shellPublish.getId() + Constant.DOT + suffix, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + abstractMeta.getXML());
                shellPublish.setMd5Xml(md5Shell);
                String md5graph = fileService.createFile(Constant.ENV_PUBLISH, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + shellPublish.getId() + Constant.DOT + Constant.GRAPH_SUFFIX, shell.getContent());
                shellPublish.setMd5Graph(md5graph);
                shellPublishMapper.updateById(shellPublish);
            } catch (KettleException e) {
                log.error(e.getMessage(), e);
                throw new UnExecutableException();
            }
        }
    }

    /**
     * 上线新版并下线旧版
     *
     * @param shellPublish
     */
    @Transactional
    public void deploySchedule(ShellPublish shellPublish, String cron, int misfire) {
        ShellPublish deployedShellPublish = shellPublishMapper.selectLatestByProdAndShellId(shellPublish.getShellId()); // 获取目前正在运行的发布
        if (deployedShellPublish != null) {
            List<ShellPublish> deployedShellPublishes = new ArrayList<>(0);
            // 将关联脚本也一并下线
            String reference = deployedShellPublish.getReference();
            if (StringUtils.hasLength(reference)) {
                List<Long> referenceIds = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
                deployedShellPublishes.addAll(this.listByIds(referenceIds));
            }
            if (StringUtils.hasLength(deployedShellPublish.getTaskId())) {
                TaskReq taskReq = new TaskReq();
                taskReq.setGroup(shellPublish.getProjectId().toString());
                taskReq.setId(deployedShellPublish.getTaskId());
                HttpEntity<TaskReq> requestEntity = new HttpEntity<>(taskReq);
                ResponseEntity<Boolean> response = restTemplate.postForEntity(stopUri, requestEntity, Boolean.class);
                if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                    throw new StopJobException();
                }
            }
            deployedShellPublishes.add(deployedShellPublish);
            deployedShellPublishes.forEach(deployed -> deployed.setProd(Constant.INACTIVE));
            this.updateBatchById(deployedShellPublishes); // 将正在执行的脚本更新为下线状态
        }
//        if (StringUtils.hasLength(shellPublish.getTaskId())) {
//            taskId = shellPublish.getTaskId();
//            // 关闭当前任务
//            if (scheduler.checkExists(TriggerKey.triggerKey(shellPublish.getTaskId()))) {
//                TriggerKey triggerKey = TriggerKey.triggerKey(shellPublish.getTaskId());
//                scheduler.pauseTrigger(triggerKey);
//                scheduler.unscheduleJob(triggerKey);
//                if (scheduler.checkExists(JobKey.jobKey(shellPublish.getTaskId()))) {
//                    scheduler.deleteJob(JobKey.jobKey(shellPublish.getTaskId()));
//                }
//            }
//        } else {
//            taskId = UUID.randomUUID().toString();
//        }
        List<Map<String, String>> referencePathList = new ArrayList<>();
        // 将新关联的脚本启用上线
        String reference = shellPublish.getReference();
        if (StringUtils.hasLength(reference)) {
            String taskId = UUID.randomUUID().toString();
            List<Long> ids = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
            ids.add(shellPublish.getId());
            List<ShellPublish> toExecuteList = this.listByIds(ids);
            toExecuteList.forEach(item -> {
                Shell shell = shellService.one(item.getShellId());
                String suffix;
                if (Constant.JOB.equals(shell.getCategory())) {
                    suffix = Constant.JOB_SUFFIX;
                } else {
                    suffix = Constant.TRANS_SUFFIX;
                }
                String ossPath = shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + item.getId() + Constant.DOT + suffix;
                String nativePath = shell.getProjectId() + File.separator + shell.getParentId() + File.separator;
                String name = shell.getName() + Constant.DOT + suffix;
                referencePathList.add(ImmutableMap.of(name, ossPath + "," + nativePath));
                item.setProd(Constant.ACTIVE);
                item.setTaskId(taskId);
            });
            this.updateBatchById(toExecuteList);
            Shell shell = shellService.one(shellPublish.getShellId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", shellPublish.getId().toString());
            jsonObject.put("referencePathList", referencePathList);
            jsonObject.put("rootPath", UUID.randomUUID() + File.separator);
            jsonObject.put("shellId", shellPublish.getShellId().toString());
            jsonObject.put("jobName", shell.getName());
            jsonObject.put("projectId", shellPublish.getProjectId().toString());
            TaskReq taskReq = new TaskReq();
            taskReq.setCron(cron);
            taskReq.setDescription(shellPublish.getName());
            taskReq.setId(taskId);
            taskReq.setGroup(shellPublish.getProjectId().toString());
            taskReq.setMisfire(misfire);
            taskReq.setData(jsonObject.toJSONString());
            HttpEntity<TaskReq> requestEntity = new HttpEntity<>(taskReq);
            ResponseEntity<Date> response = restTemplate.postForEntity(createJobUri, requestEntity, Date.class);
            if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new CreateJobException();
            }
        }
    }

    /**
     * 发布cdc任务
     *
     * @param shellPublish
     */
    public void deployStreaming(ShellPublish shellPublish) {
        ShellPublish deployedShellPublish = shellPublishMapper.selectLatestByProdAndShellId(shellPublish.getShellId()); // 获取目前正在运行的发布
        if (deployedShellPublish != null) {
            RunningProcess runningProcess = runningProcessService.shellPublishId(deployedShellPublish.getId());
            if (runningProcess != null) {
                TaskHistory taskHistory = taskHistoryService.runningProcessId(runningProcess.getId());
                if (taskHistory != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("taskHistoryId", String.valueOf(taskHistory.getId()));
                    jsonObject.put("name", runningProcess.getInstanceName());
                    jsonObject.put("instanceId", runningProcess.getInstanceId());
                    stringRedisTemplate.convertAndSend(Constant.TOPIC_TASK_SHUTDOWN, jsonObject.toJSONString());
                }
            }
            // 将关联脚本也一并下线
            List<ShellPublish> deployedShellPublishes = new ArrayList<>(0);
            String reference = deployedShellPublish.getReference();
            if (StringUtils.hasLength(reference)) {
                List<Long> referenceIds = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
                deployedShellPublishes.addAll(this.listByIds(referenceIds));
            }
            deployedShellPublishes.add(deployedShellPublish);
            deployedShellPublishes.forEach(deployed -> deployed.setProd(Constant.INACTIVE));
            this.updateBatchById(deployedShellPublishes); // 将正在执行的脚本更新为下线状态
        }
        List<Map<String, String>> referencePathList = new ArrayList<>();
        // 将新关联的脚本启用上线
        String reference = shellPublish.getReference();
        if (StringUtils.hasLength(reference)) {
            String taskId = UUID.randomUUID().toString();
            List<Long> ids = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
            ids.add(shellPublish.getId());
            List<ShellPublish> toExecuteList = this.listByIds(ids);
            toExecuteList.forEach(item -> {
                Shell shell = shellService.one(item.getShellId());
                String suffix;
                if (Constant.JOB.equals(shell.getCategory())) {
                    suffix = Constant.JOB_SUFFIX;
                } else {
                    suffix = Constant.TRANS_SUFFIX;
                }
                String ossPath = shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + item.getId() + Constant.DOT + suffix;
                String nativePath = shell.getProjectId() + File.separator + shell.getParentId() + File.separator;
                String name = shell.getName() + Constant.DOT + suffix;
                referencePathList.add(ImmutableMap.of(name, ossPath + "," + nativePath));
                item.setProd(Constant.ACTIVE);
                item.setTaskId(taskId);
            });
            this.updateBatchById(toExecuteList);
            Shell shell = shellService.one(shellPublish.getShellId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", shellPublish.getId());
            jsonObject.put("referencePathList", referencePathList);
            jsonObject.put("rootPath", UUID.randomUUID() + File.separator);
            jsonObject.put("shellId", shellPublish.getShellId());
            jsonObject.put("jobName", shell.getName());
            jsonObject.put("projectId", shellPublish.getProjectId());

            StreamingReq streamingReq = new StreamingReq();
            streamingReq.setPayload(jsonObject.toJSONString());
            HttpEntity<StreamingReq> requestEntity = new HttpEntity<>(streamingReq);
            ResponseEntity<Boolean> response = restTemplate.postForEntity(createStreamingUri, requestEntity, Boolean.class);
            if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new CreateJobException();
            }
        }
//        ShellPublish deployedShellPublish = shellPublishMapper.selectLatestByProdAndShellId(shellPublish.getShellId()); // 获取目前正在运行的发布
//        // 找到已发行的版本，停止运行
//        if (deployedShellPublish != null) {
//            List<ShellPublish> deployedShellPublishes = new ArrayList<>(0);
//            deployedShellPublish.setProd(Constant.INACTIVE);
//            // 将关联脚本也一并下线
//            String reference = deployedShellPublish.getReference();
//            if (StringUtils.hasLength(reference)) {
//                List<Long> referenceIds = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
//                deployedShellPublishes.addAll(this.listByIds(referenceIds));
//            }
//            // 如果之前任务为schedule任务，则将之前发布的任务停止
//            if (StringUtils.hasLength(deployedShellPublish.getTaskId())) {
//                ResponseDto responseDto = scheduleService.stop(shellPublish.getProjectId().toString(), deployedShellPublish.getTaskId());
//                if (!responseDto.isSuccess()) {
//                    throw new RuntimeException(responseDto.getMessage());
//                }
//            }
//            deployedShellPublishes.add(deployedShellPublish);
//            deployedShellPublishes.forEach(deployed -> deployed.setProd(Constant.INACTIVE));
//            this.updateBatchById(deployedShellPublishes); // 将正在执行的脚本更新为下线状态
//        }
//        String taskId;
//        if (StringUtils.hasLength(shellPublish.getTaskId())) {
//            taskId = shellPublish.getTaskId();
//            // 关闭当前任务
//            if (scheduler.checkExists(TriggerKey.triggerKey(shellPublish.getTaskId()))) {
//                TriggerKey triggerKey = TriggerKey.triggerKey(shellPublish.getTaskId());
//                scheduler.pauseTrigger(triggerKey);
//                scheduler.unscheduleJob(triggerKey);
//                if (scheduler.checkExists(JobKey.jobKey(shellPublish.getTaskId()))) {
//                    scheduler.deleteJob(JobKey.jobKey(shellPublish.getTaskId()));
//                }
//            }
//        } else {
//            taskId = UUID.randomUUID().toString();
//            shellPublish.setTaskId(taskId);
//        }
//
        // 创建生产环境目录
//        File folder = new File(productionDir + shellPublish.getShell().getProject().getTenant().getId() + File.separator + shellPublish.getShell().getProject().getId() + File.separator + shellPublish.getShell().getId());
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        List<String> files = new ArrayList<>(0);
//        // 将新关联的脚本启用上线
//        reference = shellPublish.getReference();
//        if (StringUtils.hasLength(reference)) {
//            long[] ids = Arrays.stream(reference.split(",")).mapToLong(Long::parseLong).toArray();
//            List<ShellPublish> existedList = shellPublishMapper.findAllByIdInAndTenantId(ids, shellPublish.getTenant().getId()).stream().peek(spr -> {
//                spr.setProd(Constant.ACTIVE);
//                try {
//                    // 复制到生产环境
//                    String fileName = spr.getShell().getName().concat(spr.getShell().getCategory().equals(Constant.TRANSFORM) ? ".ktr" : ".kjb");
//                    String direct = productionDir + shellPublish.getShell().getProject().getTenant().getId() + File.separator + shellPublish.getShell().getProject().getId() + File.separator + shellPublish.getShell().getId() + File.separator;
//                    File target = new File(direct + fileName);
//                    Files.copy(new File(spr.getXml()), target);
//                    files.add(target.getCanonicalPath());
//                    spr.setProdPath(direct + fileName);
//                } catch (IOException e) {
//                    log.error(e.toString());
//                }
//            }).collect(Collectors.toList());
//            referencedList.addAll(existedList);
//        }
//        String fileName = shellPublish.getShell().getName().concat(shellPublish.getShell().getCategory().equals(Constant.TRANSFORM) ? ".ktr" : ".kjb");
//        String direct = productionDir + shellPublish.getShell().getProject().getTenant().getId() + File.separator + shellPublish.getShell().getProject().getId() + File.separator + shellPublish.getShell().getId() + File.separator;
//        File target = new File(direct + fileName);
//        Files.copy(new File(shellPublish.getXml()), target);
//        files.add(target.getCanonicalPath());
//        modifyFileName(files, direct);
//        shellPublish.setProdPath(direct + fileName);
//        referencedList.add(shellPublish);
//        shellPublishMapper.saveAll(referencedList);
//
//        List<Map<String, String>> referencePathList = new ArrayList<>();
//        // 将新关联的脚本启用上线
//        String reference = shellPublish.getReference();
//        if (StringUtils.hasLength(reference)) {
//            String taskId = UUID.randomUUID().toString();
//            List<Long> ids = Arrays.stream(reference.split(",")).map(Long::parseLong).collect(Collectors.toList());
//            ids.add(shellPublish.getId());
//            List<ShellPublish> toExecuteList = this.listByIds(ids);
//            toExecuteList.forEach(item -> {
//                Shell shell = shellService.one(item.getShellId());
//                String suffix;
//                if (Constant.JOB.equals(shell.getCategory())) {
//                    suffix = Constant.JOB_SUFFIX;
//                } else {
//                    suffix = Constant.TRANS_SUFFIX;
//                }
//                String ossPath = shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + File.separator + item.getId() + File.separator;
//                String nativePath = shell.getProjectId() + File.separator + shell.getParentId() + File.separator;
//                String name = shell.getId() + Constant.DOT + suffix;
//                referencePathList.add(ImmutableMap.of(name, ossPath + "," + nativePath));
//                item.setProd(Constant.ACTIVE);
//                item.setTaskId(taskId);
//            });
//            this.updateBatchById(toExecuteList);
//
//            String rootPath = productionDir + UUID.randomUUID() + File.separator;
//
//            Shell shell = shellService.one(shellPublish.getShellId());
//            String name = shell.getId() + Constant.DOT + Constant.JOB_SUFFIX;
//            String entryJobPath = null;
//            for (Map<String, String> referencePathMap : referencePathList) {
//                String path = fileService.downloadFile(Constant.ENV_PUBLISH, rootPath, referencePathMap);
//                if (referencePathMap.containsKey(name)) {
//                    entryJobPath = path;
//                }
//            }
//            if (StringUtils.hasLength(entryJobPath)) {
//                shellPublish.setTaskId(taskId);
//                DBCache.getInstance().clear(null);
//                SimpleLoggingObject spoonLoggingObject = new SimpleLoggingObject("SPOON", LoggingObjectType.SPOON, null);
//                spoonLoggingObject.setContainerObjectId(taskId);
//                JobExecutionConfiguration jobExecutionConfiguration = new JobExecutionConfiguration();
//                jobExecutionConfiguration.setLogLevel(LogLevel.BASIC);
//                JobMeta jobMeta = new JobMeta(entryJobPath, null);
//                JobConfiguration jobConfiguration = new JobConfiguration(jobMeta, jobExecutionConfiguration);
//                spoonLoggingObject.setLogLevel(jobExecutionConfiguration.getLogLevel());
//                Job job = new Job(null, jobMeta, spoonLoggingObject);
//                job.injectVariables(jobConfiguration.getJobExecutionConfiguration().getVariables());
//                job.setGatheringMetrics(true);
//                job.setLogLevel(LogLevel.BASIC);
//                job.start();
//                taskExecutor.execute(() -> {
//                    CarteSingleton.getInstance().getJobMap().addJob(job.getName(), taskId, job, jobConfiguration);
//                    while (!job.isStopped() && !job.isFinished()) {
//                        log.info("【{}】正在运行", job.getName());
//                        List<String> logChannelIds = LoggingRegistry.getInstance().getLogChannelChildren(job.getLogChannelId());
//                        taskHistoryService.save(logChannelIds.stream().map(logChannelId -> {
//                            TaskHistory taskHistory = new TaskHistory();
//                            taskHistory.setLogChannelId(logChannelId);
//                            taskHistory.setShellPublishId(shellPublish.getId());
//                            taskHistory.setStatus(Constant.ACTIVE);
//                            taskHistory.setBeginTime(LocalDateTime.now());
//                            return taskHistory;
//                        }).collect(Collectors.toList()));
//                        try {
//                            TimeUnit.SECONDS.sleep(sendDelay);
//                        } catch (InterruptedException e) {
//                            log.error(e.toString());
//                        }
//                    }
//                    log.info("【{}】中断运行", job.getName());
//                    List<String> logChannelIds = LoggingRegistry.getInstance().getLogChannelChildren(job.getLogChannelId());
//                    taskHistoryService.save(logChannelIds.stream().map(logChannelId -> {
//                        TaskHistory taskHistory = new TaskHistory();
//                        taskHistory.setLogChannelId(logChannelId);
//                        taskHistory.setShellPublishId(shellPublish.getId());
//                        taskHistory.setStatus(Constant.ACTIVE);
//                        taskHistory.setBeginTime(LocalDateTime.now());
//                        return taskHistory;
//                    }).collect(Collectors.toList()));
//                });
//                // 将流处理任务纳入进程管理，可随时中断
//                RunningProcess runningProcess = new RunningProcess();
//                runningProcess.setProd(Constant.ACTIVE);
//                runningProcess.setOwner(Constant.OWNER_TASK);
//                runningProcess.setProjectId(shellPublish.getProjectId());
//                runningProcess.setShellId(shellPublish.getShellId());
//                runningProcess.setShellPublishId(shellPublish.getId());
//                runningProcess.setInstanceId(taskId);
//                runningProcess.setInstanceName(job.getName());
//                runningProcess.setCategory(Constant.JOB);
//                runningProcessService.save(runningProcess);
//            }
//        }
    }

    @Transactional
    public void stop(ShellPublish shellPublish) {
        if (Constant.BATCH.equals(shellPublish.getStreaming())) {
            TaskReq taskReq = new TaskReq();
            taskReq.setGroup(shellPublish.getProjectId().toString());
            taskReq.setId(shellPublish.getTaskId());
            HttpEntity<TaskReq> requestEntity = new HttpEntity<>(taskReq);
            ResponseEntity<Boolean> response = restTemplate.postForEntity(stopUri, requestEntity, Boolean.class);
            if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new StopJobException();
            }
        } else {
            RunningProcess runningProcess = runningProcessService.instanceId(shellPublish.getTaskId());
            if (runningProcess != null) {
                CarteObjectEntry carteObjectEntry = new CarteObjectEntry(runningProcess.getInstanceName(), runningProcess.getInstanceId());
                Job job = CarteSingleton.getInstance().getJobMap().getJob(carteObjectEntry);
                job.stopAll();
                CarteSingleton.getInstance().getJobMap().removeJob(carteObjectEntry);
                runningProcessService.delete(runningProcess);
            }
        }
        String[] reference = shellPublish.getReference().split(",");
        List<Long> ids = Arrays.stream(reference).map(Long::parseLong).collect(Collectors.toList());
        List<ShellPublish> shellPublishes = this.listByIds(ids);
        shellPublishes.add(shellPublish);
        shellPublishes.forEach(item -> item.setProd(Constant.INACTIVE));
        this.updateBatchById(shellPublishes);
    }

//    private void modifyFileName(List<String> files, String target) {
//        files.forEach(file -> {
//            try {
//                SAXReader reader = new SAXReader();
//                Document document = reader.read(new File(file));
//                Element rootElement = document.getRootElement();
//                if (file.toLowerCase(Locale.ROOT).endsWith(".kjb")) {
//                    List<Element> elements = rootElement.element("entries").elements();
//                    for (Element element : elements) {
//                        Element filenameElement = element.element("filename");
//                        if (filenameElement != null) {
//                            Element nameElement = element.element("name");
//                            if (nameElement != null && StringUtils.hasLength(nameElement.getText())) {
//                                String value = filenameElement.getTextTrim();
//                                if (StringUtils.hasLength(value)) {
//                                    String[] path = value.split(File.separator);
//                                    filenameElement.setText(target + path[path.length - 1]);
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    List<Element> elements = rootElement.element("step").elements();
//                    for (Element element : elements) {
//                        if ("transformationPath".equals(element.getName())) {
//                            if (StringUtils.hasLength(element.getText())) {
//                                String value = element.getTextTrim();
//                                if (StringUtils.hasLength(value)) {
//                                    String[] path = value.split(File.separator);
//                                    element.setText(target + path[path.length - 1]);
//                                }
//                            }
//                        }
//                    }
//                }
//                //格式化为缩进格式
//                OutputFormat format = OutputFormat.createPrettyPrint();
//                //设置编码格式
//                format.setEncoding("UTF-8");
//                XMLWriter writer = new XMLWriter(new FileWriter(file), format);
//                //写入数据
//                writer.write(document);
//                writer.close();
//            } catch (Exception e) {
//                log.error(e.toString());
//            }
//        });
//    }
}
