package com.nxin.framework.controller.basic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.base.DatasourceConverter;
import com.nxin.framework.dto.basic.DatasourceDto;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.utils.DatabaseMetaUtils;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.basic.DatasourceVo;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseTestResults;
import org.pentaho.di.core.encryption.Encr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PreAuthorize("hasAuthority('ROOT') or hasAuthority('DATASOURCE')")
@RestController
@RequestMapping
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    private static final BeanConverter<DatasourceVo, Datasource> datasourceConverter = new DatasourceConverter();

    @GetMapping("/datasource/{id}")
    public ResponseEntity<DatasourceVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource datasource = datasourceService.one(id);
        if (datasource != null) {
            List<User> members = userService.findByResource(id.toString(), Constant.RESOURCE_CATEGORY_DATASOURCE, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                return ResponseEntity.ok(datasourceConverter.convert(datasource));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/datasourcePage")
    public ResponseEntity<PageVo<DatasourceVo>> page(@RequestBody DatasourceDto datasourceDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_DATASOURCE, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> datasourceIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).distinct().collect(Collectors.toList());
        IPage<Datasource> datasourceIPage = datasourceService.search(datasourceDto.getProjectId(), datasourceIdList, datasourceDto.getPayload(), datasourceDto.getPageNo(), datasourceDto.getPageSize());
        return ResponseEntity.ok(new PageVo<>(datasourceIPage.getTotal(), datasourceConverter.convert(datasourceIPage.getRecords())));
    }

    @PostMapping("/datasourceList")
    public ResponseEntity<List<DatasourceVo>> list(@RequestBody DatasourceDto datasourceDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(datasourceDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            return ResponseEntity.ok(datasourceConverter.convert(datasourceService.all(datasourceDto.getProjectId()), "username", "password", "useCursor", "usePool", "parameter", "poolInitial", "poolInitialSize", "poolMaxActive", "poolMaxIdle", "poolMaxSize", "poolMaxWait", "poolMinIdle", "charset"));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/datasource")
    public ResponseEntity save(@RequestBody DatasourceDto datasourceDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource datasource = new Datasource();
        if (datasourceDto.getId() != null) {
            List<User> members = userService.findByResource(datasourceDto.getId().toString(), Constant.RESOURCE_CATEGORY_DATASOURCE, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        BeanUtils.copyProperties(datasourceDto, datasource);
        datasourceService.save(datasource);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/datasource/test")
    public ResponseEntity<Boolean> test(@RequestBody DatasourceDto datasourceDto) {
        try {
            DatabaseMeta databaseMeta = DatabaseMetaUtils.init(datasourceDto.getName(), datasourceDto.getGeneric() ? Constant.GENERIC : datasourceDto.getCategory(), datasourceDto.getHost(), datasourceDto.getSchemaName(), String.valueOf(datasourceDto.getPort()), datasourceDto.getUsername(), Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(datasourceDto.getPassword()), datasourceDto.getUrl(), datasourceDto.getDriver());
            DatabaseTestResults databaseTestResults = databaseMeta.testConnectionSuccess();
            if (databaseTestResults.isSuccess()) {
                return ResponseEntity.ok(databaseTestResults.isSuccess());
            }
            return ResponseEntity.status(Constant.EXCEPTION_DATASOURCE_CONNECT).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(Constant.EXCEPTION_DATASOURCE_CONNECT).build();
        }
    }

    @DeleteMapping("/datasource/{id}")
    public ResponseEntity<DatasourceVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource persisted = datasourceService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(id.toString(), Constant.RESOURCE_CATEGORY_DATASOURCE, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
            persisted.setStatus(Constant.INACTIVE);
            datasourceService.updateById(persisted);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
