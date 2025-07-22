package com.nxin.framework.controller.bi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.bi.LayoutConverter;
import com.nxin.framework.converter.bean.bi.LayoutReportConverter;
import com.nxin.framework.dto.bi.LayoutDto;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.bi.Layout;
import com.nxin.framework.entity.bi.LayoutReport;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.ResourceService;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.bi.LayoutReportService;
import com.nxin.framework.service.bi.LayoutService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.PageVo;
import com.nxin.framework.vo.bi.LayoutReportVo;
import com.nxin.framework.vo.bi.LayoutVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping
public class LayoutController {
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private LayoutReportService layoutReportService;

    private static final BeanConverter<LayoutVo, Layout> layoutConverter = new LayoutConverter();
    private static final BeanConverter<LayoutReportVo, LayoutReport> layoutReportConverter = new LayoutReportConverter();

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('LAYOUT')")
    @GetMapping("/layout/{id}")
    public ResponseEntity<LayoutVo> one(@PathVariable Long id) {
        Layout layout = layoutService.one(id);
        if (layout != null) {
            User loginUser = userService.one(LoginUtils.getUsername());
            List<User> members = userService.findByResource(id.toString(), Constant.RESOURCE_CATEGORY_LAYOUT, Constant.RESOURCE_LEVEL_BUSINESS, null);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
            LayoutVo layoutVo = layoutConverter.convert(layout);
            LambdaQueryWrapper<LayoutReport> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LayoutReport::getLayoutId, id);
            queryWrapper.eq(LayoutReport::getStatus, Constant.ACTIVE);
            List<LayoutReport> layoutReportList = layoutReportService.list(queryWrapper);
            layoutVo.setLayoutReportList(layoutReportConverter.convert(layoutReportList));
            return ResponseEntity.ok(layoutVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('LAYOUT')")
    @PostMapping("/layoutPage")
    public ResponseEntity<PageVo<LayoutVo>> page(@RequestBody LayoutDto layoutDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<Resource> resources = resourceService.findByUserIdCategoryAndLevel(loginUser.getId(), Constant.RESOURCE_CATEGORY_LAYOUT, Constant.RESOURCE_LEVEL_BUSINESS);
        List<Long> layoutIdList = resources.stream().map(resource -> Long.valueOf(resource.getCode())).distinct().collect(Collectors.toList());
        IPage<Layout> chartIPage = layoutService.search(layoutIdList, layoutDto.getPayload(), layoutDto.getPageNo(), layoutDto.getPageSize());
        return ResponseEntity.ok(new PageVo<>(chartIPage.getTotal(), layoutConverter.convert(chartIPage.getRecords())));
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('LAYOUT')")
    @PostMapping("/layout")
    public ResponseEntity save(@RequestBody LayoutDto layoutDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Layout layout = new Layout();
        if (layoutDto.getId() != null) {
            List<User> members = userService.findByResource(layoutDto.getId().toString(), Constant.RESOURCE_CATEGORY_LAYOUT, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (!members.contains(loginUser)) {
                return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
            }
        }
        BeanUtils.copyProperties(layoutDto, layout);
        List<LayoutReport> layoutReportList = layoutDto.getLayoutReportList().stream().map(dto -> {
            LayoutReport layoutReport = new LayoutReport();
            BeanUtils.copyProperties(dto, layoutReport);
            return layoutReport;
        }).collect(Collectors.toList());
        layoutService.save(layout, layoutReportList);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROOT') or hasAuthority('LAYOUT')")
    @DeleteMapping("/layout/{id}")
    public ResponseEntity<LayoutVo> delete(@PathVariable Long id) {
        User loginUser = userService.one(LoginUtils.getUsername());
        Layout persisted = layoutService.one(id);
        if (persisted != null) {
            List<User> members = userService.findByResource(persisted.getId().toString(), Constant.RESOURCE_CATEGORY_LAYOUT, Constant.RESOURCE_LEVEL_BUSINESS, Constant.PRIVILEGE_READ_WRITE);
            if (members.contains(loginUser)) {
                layoutService.delete(persisted);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }

    @GetMapping("/layout/view/{id}")
    public ResponseEntity<LayoutVo> view(@PathVariable Long id) {
        Layout layout = layoutService.one(id);
        if (layout != null) {
            if (layout.getAuthenticate()) {
                User loginUser = userService.one(LoginUtils.getUsername());
                List<User> members = userService.findByResource(id.toString(), Constant.RESOURCE_CATEGORY_LAYOUT, Constant.RESOURCE_LEVEL_BUSINESS, null);
                if (!members.contains(loginUser)) {
                    return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
                }
            }
            LayoutVo layoutVo = layoutConverter.convert(layout);
            LambdaQueryWrapper<LayoutReport> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LayoutReport::getLayoutId, id);
            queryWrapper.eq(LayoutReport::getStatus, Constant.ACTIVE);
            List<LayoutReport> layoutReportList = layoutReportService.list(queryWrapper);
            layoutVo.setLayoutReportList(layoutReportConverter.convert(layoutReportList));
            return ResponseEntity.ok(layoutVo);
        }
        return ResponseEntity.status(Constant.EXCEPTION_NOT_FOUNT).build();
    }
}
