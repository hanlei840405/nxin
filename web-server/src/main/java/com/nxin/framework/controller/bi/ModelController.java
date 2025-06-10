package com.nxin.framework.controller.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.bi.MetadataConverter;
import com.nxin.framework.converter.bean.bi.ModelConverter;
import com.nxin.framework.dto.bi.ModelDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.entity.bi.Metadata;
import com.nxin.framework.entity.bi.Model;
import com.nxin.framework.enums.Constant;
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
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private Configuration configuration;
    private BeanConverter<ModelVo, Model> modelConverter = new ModelConverter();
    private BeanConverter<MetadataVo, Metadata> metadataConverter = new MetadataConverter();

    @GetMapping("/model/{id}")
    public ResponseEntity<ModelVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model model = modelService.one(id);
        if (model != null && model.getProjectId() != null) {
            List<User> members = userService.findByResource(model.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                ModelVo modelVo = modelConverter.convert(model);
                LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Metadata::getModelId, id);
                List<Metadata> metadataList = metadataService.list(queryWrapper);
                modelVo.setMetadataList(metadataConverter.convert(metadataList));
                return ResponseEntity.ok(modelVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/modelPage")
    public ResponseEntity<PageVo<ModelVo>> page(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(modelDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            IPage<Model> modelIPage = modelService.search(modelDto.getProjectId(), modelDto.getPayload(), modelDto.getPageNo(), modelDto.getPageSize());
            return ResponseEntity.ok(new PageVo<>(modelIPage.getTotal(), modelConverter.convert(modelIPage.getRecords())));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/modelList")
    public ResponseEntity<List<ModelVo>> list(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(modelDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Model::getProjectId, modelDto.getProjectId());
            queryWrapper.eq(Model::getStatus, Constant.ACTIVE);
            return ResponseEntity.ok(modelConverter.convert(modelService.list(queryWrapper)));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/model")
    public ResponseEntity save(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(modelDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            Model model = new Model();
            BeanUtils.copyProperties(modelDto, model);
            List<Metadata> metadataList = modelDto.getMetadataList().stream().map(dto -> {
                Metadata metadata = new Metadata();
                BeanUtils.copyProperties(dto, metadata, "id");
                return metadata;
            }).collect(Collectors.toList());
            modelService.save(model, metadataList);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @DeleteMapping("/model/{id}")
    public ResponseEntity<ModelVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model persisted = modelService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                modelService.delete(Collections.singletonList(persisted.getId()));
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/model/sql")
    public ResponseEntity<String> one(@RequestBody ModelDto modelDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Model model = modelService.one(modelDto.getId());
        if (model != null && model.getProjectId() != null) {
            List<User> members = userService.findByResource(model.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                LambdaQueryWrapper<Metadata> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Metadata::getModelId, modelDto.getId());
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
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }
}
