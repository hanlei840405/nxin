package com.nxin.framework.controller.basic;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.base.DatasourceConverter;
import com.nxin.framework.dto.CrudDto;
import com.nxin.framework.dto.basic.DatasourceDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.enums.DatasourceType;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.utils.DatabaseMetaUtils;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.basic.DatasourceVo;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.database.DatabaseTestResults;
import org.pentaho.di.core.database.GenericDatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

@PreAuthorize("hasAuthority('ROOT') or hasAuthority('DATASOURCE')")
@RestController
@RequestMapping
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    private BeanConverter<DatasourceVo, Datasource> datasourceConverter = new DatasourceConverter();

    @GetMapping("/datasource/{id}")
    public ResponseEntity<DatasourceVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource datasource = datasourceService.one(id);
        if (datasource != null && datasource.getProjectId() != null) {
            List<User> members = userService.findByResource(datasource.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                return ResponseEntity.ok(datasourceConverter.convert(datasource));
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/datasourceList")
    public ResponseEntity<List<DatasourceVo>> list(@RequestBody CrudDto crudDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(crudDto.getId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            return ResponseEntity.ok(datasourceConverter.convert(datasourceService.all(crudDto.getId())));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/datasource")
    public ResponseEntity save(@RequestBody DatasourceDto datasourceDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(datasourceDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            Datasource datasource = new Datasource();
            BeanUtils.copyProperties(datasourceDto, datasource);
            datasourceService.save(datasource);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/datasource/test")
    public ResponseEntity<Boolean> test(@RequestBody DatasourceDto datasourceDto) {
        DatabaseMeta databaseMeta = DatabaseMetaUtils.init(datasourceDto.getName(), datasourceDto.getCategory(), datasourceDto.getHost(), datasourceDto.getSchemaName(), String.valueOf(datasourceDto.getPort()), datasourceDto.getUsername(), Constant.PASSWORD_ENCRYPTED_PREFIX + Encr.encryptPassword(datasourceDto.getPassword()), datasourceDto.getUrl(), datasourceDto.getDriver());
        DatabaseTestResults databaseTestResults = databaseMeta.testConnectionSuccess();
        return ResponseEntity.ok(databaseTestResults.isSuccess());
    }

    @DeleteMapping("/datasource/{id}")
    public ResponseEntity<DatasourceVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource persisted = datasourceService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                datasourceService.delete(persisted.getProjectId(), Collections.singletonList(persisted.getId()));
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }
}
