package com.nxin.framework.controller.kettle;

import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.enums.DatasourceType;
import com.nxin.framework.service.DynamicQueryDataService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.utils.LoginUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class PreviewDataController {
    @Autowired
    private UserService userService;
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private DynamicQueryDataService dynamicQueryDataService;
    public static final int MAX_BINARY_STRING_PREVIEW_SIZE = 1000000;

    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> preview(@RequestBody Payload payload) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource datasource = datasourceService.one(payload.datasourceId);
        if (datasource != null && datasource.getProjectId() != null) {
            List<User> members = userService.findByResource(datasource.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                try {
                    Map<String, Object> result = dynamicQueryDataService.preview(datasource.getName(), datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource.getDriver(), payload.sql);
                    return ResponseEntity.ok(result);
                } catch (KettleDatabaseException | KettleValueException e) {
                    return ResponseEntity.status(Constant.EXCEPTION_SQL_GRAMMAR).build();
                }
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/structure")
    public ResponseEntity<List<Map<String, Object>>> structure(@RequestBody Payload payload) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource datasource = datasourceService.one(payload.datasourceId);
        if (datasource != null && datasource.getProjectId() != null) {
            List<User> members = userService.findByResource(datasource.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                List<Map<String, Object>> response = dynamicQueryDataService.structure(datasource.getName(), datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource.getDriver(), payload.category, payload.name);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @Data
    public static class Payload implements Serializable {
        private Long datasourceId;
        private String name;
        private String category;
        private String sql;
        private List<String> filenames;
        private String sheetName;
    }
}
