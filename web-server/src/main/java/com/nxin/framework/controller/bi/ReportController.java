package com.nxin.framework.controller.bi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.bi.ReportChartParamsConverter;
import com.nxin.framework.converter.bean.bi.ReportConverter;
import com.nxin.framework.dto.bi.ReportChartParamsDto;
import com.nxin.framework.dto.bi.ReportDto;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.basic.Datasource;
import com.nxin.framework.entity.bi.*;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.DynamicQueryDataService;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.DatasourceService;
import com.nxin.framework.service.bi.*;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.bi.ReportChartParamsVo;
import com.nxin.framework.vo.bi.ReportVo;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
@PreAuthorize("hasAuthority('ROOT') or hasAuthority('REPORT')")
@RestController
@RequestMapping
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ChartService chartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ReportChartParamsService reportChartParamsService;
    @Autowired
    private ChartParamsService chartParamsService;
    @Autowired
    private DynamicQueryDataService dynamicQueryDataService;
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private ModelService modelService;

    private static final BeanConverter<ReportVo, Report> reportConverter = new ReportConverter();
    private static final BeanConverter<ReportChartParamsVo, ReportChartParams> reportChartParamsConverter = new ReportChartParamsConverter();
    private static final String CATEGORY_SQL = "sql";
    private static final String FIELD_CATEGORY_NUMBER = "number";
    private static final String FIELD_CATEGORY_ARRAY = "array";
    private static final String FIELD_CATEGORY_OBJECT = "object";

    @GetMapping("/report/{id}")
    public ResponseEntity<ReportVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Report report = reportService.one(id);
        if (report != null && report.getProjectId() != null) {
            List<User> members = userService.findByResource(report.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                ReportVo reportVo = reportConverter.convert(report);
                LambdaQueryWrapper<ReportChartParams> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ReportChartParams::getReportId, id);
                List<ReportChartParams> reportChartParamsList = reportChartParamsService.list(queryWrapper);
                List<Long> chartParamsIdList = reportChartParamsList.stream().map(ReportChartParams::getChartParamsId).collect(Collectors.toList());
                List<ReportChartParamsVo> reportChartParamsVos = reportChartParamsConverter.convert(reportChartParamsList);
                if (!chartParamsIdList.isEmpty()) {
                    LambdaQueryWrapper<ChartParams> chartParamsQueryWrapper = new LambdaQueryWrapper<>();
                    chartParamsQueryWrapper.in(ChartParams::getId, chartParamsIdList);
                    chartParamsQueryWrapper.eq(ChartParams::getStatus, Constant.ACTIVE);
                    List<ChartParams> chartParamsList = chartParamsService.list(chartParamsQueryWrapper);
                    Map<Long, ChartParams> chartParamsMap = chartParamsList.stream().collect(Collectors.toMap(ChartParams::getId, v -> v));
                    reportChartParamsVos.forEach(item -> {
                        item.setField(chartParamsMap.get(item.getChartParamsId()).getField());
                        item.setFieldCategory(chartParamsMap.get(item.getChartParamsId()).getCategory());
                        item.setDescription(chartParamsMap.get(item.getChartParamsId()).getDescription());
                    });
                }
                reportVo.setReportChartParamsList(reportChartParamsVos);
                return ResponseEntity.ok(reportVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/reportPage")
    public ResponseEntity<PageVo<ReportVo>> page(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_REPORT, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> reportIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).distinct().collect(Collectors.toList());
        IPage<Report> reportIPage = reportService.search(reportDto.getProjectId(), reportIdList, reportDto.getPayload(), reportDto.getPageNo(), reportDto.getPageSize());
        if (!reportIPage.getRecords().isEmpty()) {
            LambdaQueryWrapper<ReportChartParams> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(ReportChartParams::getReportId, reportIPage.getRecords().stream().map(Report::getId).collect(Collectors.toList()));
            queryWrapper.eq(ReportChartParams::getStatus, Constant.ACTIVE);
            List<ReportChartParams> reportChartParamsList = reportChartParamsService.list(queryWrapper);
            if (!reportChartParamsList.isEmpty()) {
                Map<Long, List<ReportChartParams>> reportChartParamsListMap = reportChartParamsList.stream().collect(Collectors.groupingBy(ReportChartParams::getReportId));
                reportIPage.getRecords().forEach(record -> {
                    boolean unpublish = reportChartParamsListMap.get(record.getId()).stream().anyMatch(item -> Objects.isNull(record.getPublishTime()) || item.getCreateTime().isAfter(record.getPublishTime()));
                    if (unpublish) {
                        record.setPublish(false);
                    }
                });
            }
        }
        return ResponseEntity.ok(new PageVo<>(reportIPage.getTotal(), reportConverter.convert(reportIPage.getRecords())));
    }

    @PostMapping("/reportList")
    public ResponseEntity<List<ReportVo>> list(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(reportDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Report::getProjectId, reportDto.getProjectId());
            queryWrapper.eq(Report::getStatus, Constant.ACTIVE);
            queryWrapper.eq(Report::getPublish, Boolean.TRUE);
            return ResponseEntity.ok(reportConverter.convert(reportService.list(queryWrapper)));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/report")
    public ResponseEntity save(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Report report = new Report();
        if (reportDto.getId() != null) {
            List<User> members = userService.findByResource(reportDto.getId().toString(), Constant.RESOURCE_CATEGORY_REPORT, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        BeanUtils.copyProperties(reportDto, report);

        List<ReportChartParams> reportChartParamsList = reportDto.getReportChartParamsList().stream().map(dto -> {
            ReportChartParams reportChartParams = new ReportChartParams();
            BeanUtils.copyProperties(dto, reportChartParams);
            return reportChartParams;
        }).collect(Collectors.toList());
        reportService.save(report, reportChartParamsList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/publish")
    public ResponseEntity publish(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(reportDto.getId().toString(), Constant.RESOURCE_CATEGORY_REPORT, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
        if (!members.contains(loginUser)) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        Report report = reportService.one(reportDto.getId());
        if (report != null) {
            report.setPublish(true);
            report.setPublishTime(LocalDateTime.now());
            reportService.updateById(report);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @DeleteMapping("/report/{id}")
    public ResponseEntity<ReportVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Report persisted = reportService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_REPORT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                reportService.delete(persisted);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/report/view")
    public ResponseEntity<String> view(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(reportDto.getId().toString(), Constant.RESOURCE_CATEGORY_REPORT, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ);
        if (!members.contains(loginUser)) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        if (reportDto.getChartId() != null) {
            Chart chart = chartService.one(reportDto.getChartId());
            if (chart == null || !StringUtils.hasLength(chart.getOptions()) || !StringUtils.hasLength(reportDto.getMapping())) {
                return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
            }
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            try {
                StringTemplateLoader stringLoader = new StringTemplateLoader();
                stringLoader.putTemplate("chartTemplate", chart.getOptions());
                cfg.setTemplateLoader(stringLoader);
                Template template = cfg.getTemplate("chartTemplate");
                Map<String, Object> params = new HashMap<>();
                // todo 解析mapping内容为可执行参数
                JSONObject source = JSON.parseObject(reportDto.getMapping());
                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    params.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
                }
                return ResponseEntity.ok(FreeMarkerTemplateUtils.processTemplateIntoString(template, params));
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/report/paint")
    public ResponseEntity<String> paint(@RequestBody ReportDto reportDto) {
        if (reportDto.getId() != null) {
            User loginUser = userService.one(LoginUtils.getUsername());
            Report report = reportService.one(reportDto.getId());
            List<User> members = userService.findByResource(report.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        if (reportDto.getModelId() == null) {
            return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
        }
        Model model = modelService.one(reportDto.getModelId());
        Datasource datasource = datasourceService.one(model.getDatasourceId());
        Map<String, Object> source = new HashMap<>();
        for (ReportChartParamsDto item : reportDto.getReportChartParamsList()) {
            if (CATEGORY_SQL.equals(item.getCategory())) {
                try {
                    Map<String, Object> result = dynamicQueryDataService.query(datasource.getName(), datasource.getGeneric() ? Constant.GENERIC : datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource.getDriver(), item.getScript());
                    source.put(item.getField(), result.get("records"));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
                }
            } else if (FIELD_CATEGORY_NUMBER.equals(item.getFieldCategory())) {
                source.put(item.getField(), NumberUtils.parseNumber(item.getScript(), Integer.class));
            } else if (FIELD_CATEGORY_ARRAY.equals(item.getFieldCategory())) {
                source.put(item.getField(), JSON.parseArray(item.getScript()));
            } else if (FIELD_CATEGORY_OBJECT.equals(item.getFieldCategory())) {
                source.put(item.getField(), JSON.parseObject(item.getScript()));
            } else {
                source.put(item.getField(), item.getScript());
            }
        }
        Chart chart = chartService.one(reportDto.getChartId());
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("chartTemplate", chart.getOptions());
            cfg.setTemplateLoader(stringLoader);
            Template template = cfg.getTemplate("chartTemplate");
            return ResponseEntity.ok(FreeMarkerTemplateUtils.processTemplateIntoString(template, source));
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
        }
    }

    @GetMapping("/report/paint/{id}")
    public ResponseEntity<String> paint(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Report report = reportService.one(id);
        List<User> members = userService.findByResource(report.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (!members.contains(loginUser)) {
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        Model model = modelService.one(report.getModelId());
        Datasource datasource = datasourceService.one(model.getDatasourceId());
        LambdaQueryWrapper<ReportChartParams> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ReportChartParams::getReportId, id);
        List<ReportChartParams> reportChartParamsList = reportChartParamsService.list(queryWrapper);

        LambdaQueryWrapper<ChartParams> chartParamsQueryWrapper = new LambdaQueryWrapper<>();
        chartParamsQueryWrapper.in(ChartParams::getId, reportChartParamsList.stream().map(ReportChartParams::getChartParamsId).collect(Collectors.toList()));
        List<ChartParams> chartParamsList = chartParamsService.list(chartParamsQueryWrapper);
        Map<Long, ChartParams> chartParamsMap = chartParamsList.stream().collect(Collectors.toMap(ChartParams::getId, v -> v));
        Map<String, Object> source = new HashMap<>();
        for (ReportChartParams item : reportChartParamsList) {
            if (CATEGORY_SQL.equals(item.getCategory())) {
                try {
                    Map<String, Object> result = dynamicQueryDataService.query(datasource.getName(), datasource.getGeneric() ? Constant.GENERIC : datasource.getCategory(), datasource.getHost(), datasource.getSchemaName(), String.valueOf(datasource.getPort()), datasource.getUsername(), datasource.getPassword(), datasource.getUrl(), datasource.getDriver(), item.getScript());
                    source.put(chartParamsMap.get(item.getChartParamsId()).getField(), result.get("records"));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
                }
            } else if (FIELD_CATEGORY_NUMBER.equals(chartParamsMap.get(item.getChartParamsId()).getCategory())) {
                source.put(chartParamsMap.get(item.getChartParamsId()).getField(), NumberUtils.parseNumber(item.getScript(), Integer.class));
            } else if (FIELD_CATEGORY_ARRAY.equals(chartParamsMap.get(item.getChartParamsId()).getCategory())) {
                source.put(chartParamsMap.get(item.getChartParamsId()).getField(), JSON.parseArray(item.getScript()));
            } else if (FIELD_CATEGORY_OBJECT.equals(chartParamsMap.get(item.getChartParamsId()).getCategory())) {
                source.put(chartParamsMap.get(item.getChartParamsId()).getField(), JSON.parseObject(item.getScript()));
            } else {
                source.put(chartParamsMap.get(item.getChartParamsId()).getField(), item.getScript());
            }
        }
        Chart chart = chartService.one(report.getChartId());
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("chartTemplate", chart.getOptions());
            cfg.setTemplateLoader(stringLoader);
            Template template = cfg.getTemplate("chartTemplate");
            return ResponseEntity.ok(FreeMarkerTemplateUtils.processTemplateIntoString(template, source));
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
        }
    }
}
