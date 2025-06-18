package com.nxin.framework.controller.bi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.bi.ChartConverter;
import com.nxin.framework.converter.bean.bi.ChartParamsConverter;
import com.nxin.framework.dto.bi.ChartDto;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Chart;
import com.nxin.framework.entity.bi.ChartParams;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.bi.ChartParamsService;
import com.nxin.framework.service.bi.ChartService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.bi.ChartParamsVo;
import com.nxin.framework.vo.bi.ChartVo;
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
import java.util.List;
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
@PreAuthorize("hasAuthority('ROOT') or hasAuthority('CHART')")
@RestController
@RequestMapping
public class ChartController {
    @Autowired
    private ChartService chartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ChartParamsService chartParamsService;

    private final BeanConverter<ChartVo, Chart> chartConverter = new ChartConverter();
    private final BeanConverter<ChartParamsVo, ChartParams> chartParamsConverter = new ChartParamsConverter();

    @GetMapping("/chart/{id}")
    public ResponseEntity<ChartVo> one(@PathVariable Long id) {
        Chart chart = chartService.one(id);
        if (chart != null) {
            ChartVo chartVo = chartConverter.convert(chart);
            LambdaQueryWrapper<ChartParams> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ChartParams::getChartId, id);
            List<ChartParams> chartParams = chartParamsService.list(queryWrapper);
            chartVo.setChartParamsList(chartParamsConverter.convert(chartParams));
            return ResponseEntity.ok(chartVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PostMapping("/chartPage")
    public ResponseEntity<PageVo<ChartVo>> page(@RequestBody ChartDto chartDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_CHART, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> chartIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).collect(Collectors.toList());
        IPage<Chart> chartIPage = chartService.search(LoginUtils.getUsername(), chartIdList, chartDto.getPayload(), chartDto.getPageNo(), chartDto.getPageSize());
        return ResponseEntity.ok(new PageVo<>(chartIPage.getTotal(), chartConverter.convert(chartIPage.getRecords())));
    }

    @PostMapping("/chartList")
    public ResponseEntity<List<ChartVo>> list() {
        LambdaQueryWrapper<Chart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Chart::getStatus, Constant.ACTIVE);
        return ResponseEntity.ok(chartConverter.convert(chartService.list(queryWrapper)));
    }

    @PostMapping("/chart")
    public ResponseEntity save(@RequestBody ChartDto chartDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Chart chart = new Chart();
        if (chartDto.getId() != null) {
            List<User> members = userService.findByResource(chartDto.getId().toString(), Constant.RESOURCE_CATEGORY_CHART, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        BeanUtils.copyProperties(chartDto, chart);

        List<ChartParams> chartParamsList = chartDto.getChartParamsList().stream().map(dto -> {
            ChartParams chartParams = new ChartParams();
            BeanUtils.copyProperties(dto, chartParams, "id");
            return chartParams;
        }).collect(Collectors.toList());
        chartService.save(chart, chartParamsList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/chart/{id}")
    public ResponseEntity<ChartVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Chart persisted = chartService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getId().toString(), Constant.RESOURCE_CATEGORY_CHART, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (members.contains(loginUser)) {
                chartService.removeById(persisted);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @PostMapping("/chart/preview")
    public ResponseEntity<String> preview(@RequestBody ChartDto chartDto) {
        if (!StringUtils.hasLength(chartDto.getOptions()) || !StringUtils.hasLength(chartDto.getData())) {
            if (chartDto.getId() != null) {
                Chart chart = chartService.one(chartDto.getId());
                if (chart != null) {
                    chartDto.setData(chart.getData());
                    chartDto.setOptions(chart.getOptions());
                } else {
                    return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
                }
            } else {
                return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
            }
        }
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("chartTemplate", chartDto.getOptions());
            cfg.setTemplateLoader(stringLoader);
            Template template = cfg.getTemplate("chartTemplate");
//            Map<String, Object> params = new HashMap<>();
            JSONObject source = JSON.parseObject(chartDto.getData());
//            for (Map.Entry<String, Object> entry : source.entrySet()) {
//                params.put(entry.getKey(), JSON.toJSONString(entry.getValue()));
//            }
            return ResponseEntity.ok(FreeMarkerTemplateUtils.processTemplateIntoString(template, source));
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(Constant.EXCEPTION_XML_PARSE).build();
        }
    }
}
