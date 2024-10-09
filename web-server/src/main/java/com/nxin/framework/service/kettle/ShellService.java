package com.nxin.framework.service.kettle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.kettle.Shell;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.mapper.kettle.ShellMapper;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.io.FileService;
import com.nxin.framework.utils.LoginUtils;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class ShellService extends ServiceImpl<ShellMapper, Shell> {

    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ShellMapper shellMapper;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private KettleGeneratorService kettleGeneratorService;
    @Autowired
    private FileService fileService;

    public Shell one(Long id) {
        Shell shell = shellMapper.selectById(id);
        if (!LoginUtils.DEFAULT_VALUE.equals(LoginUtils.getUsername())) {
            User loginUser = userService.one(LoginUtils.getUsername());
            if (resourceService.isRoot(loginUser.getId())) {
                return shell;
            }
            List<Privilege> records = privilegeService.findByUserAndResource(loginUser.getId(), shell.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (records.isEmpty()) {
                return null;
            }
        }
        return shell;
    }

    public List<Shell> all(Long projectId, Long parentId) {
        QueryWrapper<Shell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Shell.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.eq(Shell.PROJECT_ID_COLUMN, projectId);
        if (parentId != null) {
            queryWrapper.eq(Shell.PARENT_ID_COLUMN, parentId);
        } else {
            queryWrapper.isNull(Shell.PARENT_ID_COLUMN);
        }
        return shellMapper.selectList(queryWrapper);
    }

    @Transactional
    public boolean save(Shell shell) {
        if (StringUtils.hasLength(shell.getContent())) {
            shell.setStreaming(Constant.BATCH);
            if (Constant.TRANSFORM.equals(shell.getCategory())) {
                try {
                    Map<String, Object> transResult = kettleGeneratorService.getTransMeta(shell, false);
                    TransMeta transMeta = (TransMeta) transResult.get("transMeta");
                    StepMeta[] stepMetas = transMeta.getStepsArray();
                    for (StepMeta stepMeta : stepMetas) {
                        if (Constant.STREAMING_STEP.contains(stepMeta.getTypeId())) {
                            shell.setStreaming(Constant.STREAMING);
                            break;
                        }
                    }
                    String md5 = DigestUtils.md5DigestAsHex(shell.getContent().getBytes(StandardCharsets.UTF_8));
                    if (!md5.equals(shell.getMd5Graph())) {
                        md5 = fileService.createFile(Constant.ENV_DEV, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + Constant.DOT + Constant.TRANS_SUFFIX, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + transMeta.getXML());
                        shell.setMd5Xml(md5);
                        md5 = fileService.createFile(Constant.ENV_DEV, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + Constant.DOT + Constant.GRAPH_SUFFIX, shell.getContent());
                        shell.setMd5Graph(md5);
                    }
//                    String xml = transMeta.getXML();
//                    String path = devDir + File.separator + shell.getProjectId() + File.separator + shell.getParentId();
//                    File folder = new File(path);
//                    if (!folder.exists()) {
//                        folder.mkdirs();
//                    }
//
//                    File transFile = new File(path + File.separator + shell.getName() + ".ktr");
//                    Files.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xml).getBytes(StandardCharsets.UTF_8), transFile);
//                    shell.setXml(transFile.getCanonicalPath());
                    shell.setReference((String) transResult.get("referenceIds"));
                    shell.setExecutable(true);
                } catch (Exception e) {
                    shell.setExecutable(false);
                }
            } else if (Constant.JOB.equals(shell.getCategory())) {
                try {
                    Map<String, Object> jobResult = kettleGeneratorService.getJobMeta(shell, false);
                    JobMeta jobMeta = (JobMeta) jobResult.get("jobMeta");
                    String reference = (String) jobResult.get("referenceIds");
                    if (StringUtils.hasLength(reference)) {
                        String[] references = reference.split(",");
                        for (String referenceId : references) {
                            Shell referenceShell = one(Long.parseLong(referenceId));
                            if (Constant.STREAMING.equals(referenceShell.getStreaming())) {
                                shell.setStreaming(Constant.STREAMING);
                            }
                        }
                    }
                    String md5 = DigestUtils.md5DigestAsHex(shell.getContent().getBytes(StandardCharsets.UTF_8));
                    if (!md5.equals(shell.getMd5Graph())) {
                        md5 = fileService.createFile(Constant.ENV_DEV, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + Constant.DOT + Constant.JOB_SUFFIX, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + jobMeta.getXML());
                        shell.setMd5Xml(md5);
                        md5 = fileService.createFile(Constant.ENV_DEV, shell.getProjectId() + File.separator + shell.getParentId() + File.separator + shell.getId() + Constant.DOT + Constant.GRAPH_SUFFIX, shell.getContent());
                        shell.setMd5Graph(md5);
                    }
//                    String xml = jobMeta.getXML();
//                    String path = devDir + File.separator + shell.getProjectId() + File.separator + shell.getParentId();
//                    File folder = new File(path);
//                    if (!folder.exists()) {
//                        folder.mkdirs();
//                    }
//                    File jobFile = new File(path + File.separator + shell.getName() + ".kjb");
//                    Files.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xml).getBytes(StandardCharsets.UTF_8), jobFile);
//                    shell.setXml(jobFile.getCanonicalPath());
                    shell.setReference((String) jobResult.get("referenceIds"));
                    shell.setExecutable(true);
                } catch (Exception e) {
                    shell.setExecutable(false);
                }
            }
        }
        int upsert;
        if (shell.getId() != null) {
            shell.setModifier(LoginUtils.getUsername());
            upsert = shellMapper.updateById(shell);
        } else {
            shell.setStatus(Constant.ACTIVE);
            shell.setCreator(LoginUtils.getUsername());
            shell.setVersion(1);
            upsert = shellMapper.insert(shell);
        }
        return upsert > 0;
    }

    @Transactional
    public void move(long parentId, List<Long> idList) {
        UpdateWrapper<Shell> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(Shell.STATUS_COLUMN, Constant.ACTIVE);
        updateWrapper.in(Shell.ID_COLUMN, idList);
        updateWrapper.set(Shell.PARENT_ID_COLUMN, parentId > 0 ? parentId : null);
        shellMapper.update(updateWrapper);
    }

    public void delete(Long projectId, List<Long> idList) {
        QueryWrapper<Shell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Shell.STATUS_COLUMN, Constant.ACTIVE);
        queryWrapper.eq(Shell.PROJECT_ID_COLUMN, projectId);
        if (!idList.isEmpty()) {
            queryWrapper.in(Shell.ID_COLUMN, idList);
        }
        shellMapper.delete(queryWrapper);
    }
}
