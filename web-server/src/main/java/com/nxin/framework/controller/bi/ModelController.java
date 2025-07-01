package com.nxin.framework.controller.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.bi.MetadataConverter;
import com.nxin.framework.converter.bean.bi.ModelConverter;
import com.nxin.framework.dto.bi.ModelDto;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.entity.bi.Chart;
import com.nxin.framework.entity.bi.Metadata;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.bi.MetadataService;
import com.nxin.framework.service.bi.ModelService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.bi.MetadataVo;
import com.nxin.framework.vo.bi.ModelVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jesse han
 * @since 2024-07-24
 */
@Slf4j
@PreAuthorize("hasAuthority('ROOT') or hasAuthority('MODEL')")
@RestController
@RequestMapping
public class ModelController {
    @Autowired
    private ModelService modelService;
    @Autowired
    private UserService userService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private Configuration configuration;
    @Autowired
    private ResourceService resourceService;

    private static final BeanConverter<ModelVo, Model> modelConverter = new ModelConverter();
    private static final BeanConverter<MetadataVo, Metadata> metadataConverter = new MetadataConverter();

    @GetMapping("/model/{id}")
    public ResponseEntity<ModelVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model model = modelService.one(id);
        if (model != null && model.getProjectId() != null) {
            List<User> members = userService.findByResource(model.getId().toString(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                ModelVo modelVo = modelConverter.convert(model);
                LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Metadata::getModelId, id);
                queryWrapper.eq(Metadata::getStatus, Constant.ACTIVE);
                List<Metadata> metadataList = metadataService.list(queryWrapper);
                modelVo.setMetadataList(metadataConverter.convert(metadataList));
                return ResponseEntity.ok(modelVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/modelPage")
    public ResponseEntity<PageVo<ModelVo>> page(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> modelIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).distinct().collect(Collectors.toList());
        IPage<Model> modelIPage = modelService.search(modelDto.getProjectId(), modelIdList, modelDto.getPayload(), modelDto.getPageNo(), modelDto.getPageSize());
        if (!modelIPage.getRecords().isEmpty()) {
            LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Metadata::getModelId, modelIPage.getRecords().stream().map(Model::getId).collect(Collectors.toList()));
            queryWrapper.eq(Metadata::getStatus, Constant.ACTIVE);
            List<Metadata> metadataList = metadataService.list(queryWrapper);
            if (!metadataList.isEmpty()) {
                Map<Long, List<Metadata>> modelMetadataListMap = metadataList.stream().collect(Collectors.groupingBy(Metadata::getModelId));
                modelIPage.getRecords().forEach(record -> {
                    boolean unpublish = modelMetadataListMap.get(record.getId()).stream().anyMatch(item -> Objects.isNull(record.getPublishTime()) || item.getCreateTime().isAfter(record.getPublishTime()));
                    if (unpublish) {
                        record.setPublish(false);
                    }
                });
            }
        }
        return ResponseEntity.ok(new PageVo<>(modelIPage.getTotal(), modelConverter.convert(modelIPage.getRecords())));
    }

    @PostMapping("/modelList")
    public ResponseEntity<List<ModelVo>> list(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> modelIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).distinct().collect(Collectors.toList());
        if (modelIdList.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Model::getId, modelIdList);
        queryWrapper.eq(Model::getProjectId, modelDto.getProjectId());
        queryWrapper.eq(Model::getStatus, Constant.ACTIVE);
        queryWrapper.eq(Model::getPublish, Boolean.TRUE);
        return ResponseEntity.ok(modelConverter.convert(modelService.list(queryWrapper)));
    }

    @PostMapping("/model")
    public ResponseEntity save(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model model = new Model();
        if (modelDto.getId() != null) {
            List<User> members = userService.findByResource(modelDto.getId().toString(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        BeanUtils.copyProperties(modelDto, model);
        List<Metadata> metadataList = modelDto.getMetadataList().stream().map(dto -> {
            Metadata metadata = new Metadata();
            BeanUtils.copyProperties(dto, metadata);
            return metadata;
        }).collect(Collectors.toList());
        modelService.save(model, metadataList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/model/publish")
    public ResponseEntity publish(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(modelDto.getId().toString(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
        if (!members.contains(loginUser)) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        Model model = modelService.one(modelDto.getId());
        if (model != null) {
            model.setPublish(true);
            model.setPublishTime(LocalDateTime.now());
            modelService.updateById(model);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @DeleteMapping("/model/{id}")
    public ResponseEntity<ModelVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model persisted = modelService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(id.toString(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
            if (persisted.getPublish()) {
                return ResponseEntity.status(Constant.EXCEPTION_PUBLISHED).build();
            }
            modelService.delete(persisted);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/model/sql")
    public ResponseEntity<String> one(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model model = modelService.one(modelDto.getId());
        if (model != null && model.getProjectId() != null) {
            List<User> members = userService.findByResource(model.getId().toString(), Constant.RESOURCE_CATEGORY_MODEL, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Metadata::getModelId, modelDto.getId());
                queryWrapper.eq(Metadata::getStatus, Constant.ACTIVE);
                List<Metadata> metadataList = metadataService.list(queryWrapper);
                Datasource datasource = datasourceService.one(model.getDatasourceId());
                try {
                    Template t = configuration.getTemplate(datasource.getCategory().toLowerCase(Locale.ROOT) + ".ftl");
                    Map<String, Object> params = new HashMap<>();
                    params.put("code", model.getCode());
                    params.put("name", model.getName());
                    params.put("charset", datasource.getCharset());
                    params.put("columns", metadataList);
                    return ResponseEntity.ok(FreeMarkerTemplateUtils.processTemplateIntoString(t, params));
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                    return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
                }
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
