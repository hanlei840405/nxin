package com.nxin.framework.controller.bi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.bi.ReportConverter;
import com.nxin.framework.dto.bi.ReportDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Chart;
import com.nxin.framework.entity.bi.Report;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.bi.ChartService;
import com.nxin.framework.service.bi.ReportService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final BeanConverter<ReportVo, Report> reportConverter = new ReportConverter();

    @GetMapping("/report/{id}")
    public ResponseEntity<ReportVo> one(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Report report = reportService.one(id);
        if (report != null && report.getProjectId() != null) {
            List<User> members = userService.findByResource(report.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                ReportVo reportVo = reportConverter.convert(report);
                return ResponseEntity.ok(reportVo);
            }
            return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/reportPage")
    public ResponseEntity<PageVo<ReportVo>> page(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(reportDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            IPage<Report> reportIPage = reportService.search(reportDto.getProjectId(), reportDto.getPayload(), reportDto.getPageNo(), reportDto.getPageSize());
            return ResponseEntity.ok(new PageVo<>(reportIPage.getTotal(), reportConverter.convert(reportIPage.getRecords())));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/reportList")
    public ResponseEntity<List<ReportVo>> list(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(reportDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Report::getProjectId, reportDto.getProjectId());
            queryWrapper.eq(Report::getStatus, Constant.ACTIVE);
            return ResponseEntity.ok(reportConverter.convert(reportService.list(queryWrapper)));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/report")
    public ResponseEntity save(@RequestBody ReportDto reportDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(reportDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            Report report = new Report();
            BeanUtils.copyProperties(reportDto, report);
            reportService.save(report);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @DeleteMapping("/report/{id}")
    public ResponseEntity<ReportVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Report persisted = reportService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (members.contains(loginUser)) {
                reportService.removeById(persisted);
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
}
