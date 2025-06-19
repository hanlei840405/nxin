package com.nxin.framework.controller.kettle;

import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.DynamicQueryDataService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.utils.LoginUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> preview(@RequestBody Payload payload) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Datasource datasource = datasourceService.one(payload.datasourceId);
        if (datasource != null && datasource.getProjectId() != null) {
            List<User> members = userService.findByResource(datasource.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                try {
                    Map<String, Object> result = dynamicQueryDataService.preview(datasource.getName(), datasource.getGeneric() ? Constant.GENERIC : datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource.getDriver(), payload.sql);
                    return ResponseEntity.ok(result);
                } catch (Exception e) {
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
                try {
                    List<Map<String, Object>> response = dynamicQueryDataService.structure(datasource.getName(), datasource.getGeneric() ? Constant.GENERIC : datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource.getDriver(), payload.category, payload.name);
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    return ResponseEntity.status(Constant.EXCEPTION_SQL_GRAMMAR).build();
                }
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
