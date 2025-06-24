
package com.nxin.framework.controller.kettle;

import com.nxin.framework.converter.bean.BeanConverter;
import com.nxin.framework.converter.bean.kettle.AttachmentStorageConverter;
import com.nxin.framework.dto.kettle.AttachmentStorageDto;
import com.nxin.framework.entity.auth.User;
import com.nxin.framework.entity.kettle.AttachmentStorage;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.kettle.AttachmentStorageService;
import com.nxin.framework.utils.LoginUtils;
import com.nxin.framework.vo.kettle.AttachmentStorageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@PreAuthorize("hasAuthority('ROOT') or hasAuthority('ATTACHMENT_STORAGE')")
@RestController
@RequestMapping
public class AttachmentStorageController {
    @Autowired
    private AttachmentStorageService attachmentStorageService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    private static final BeanConverter<AttachmentStorageVo, AttachmentStorage> attachmentStorageConverter = new AttachmentStorageConverter();


    @PostMapping("/attachmentStorageList")
    public ResponseEntity<List<AttachmentStorageVo>> list(@RequestBody AttachmentStorageDto attachmentStorageDto) {
        User loginUser = userService.one(LoginUtils.getUsername());
        List<User> members = userService.findByResource(attachmentStorageDto.getProjectId().toString(), Constant.RESOURCE_CATEGORY_PROJECT, Constant.RESOURCE_LEVEL_BUSINESS, null);
        if (members.contains(loginUser)) {
            return ResponseEntity.ok(attachmentStorageConverter.convert(attachmentStorageService.all(attachmentStorageDto.getProjectId(), attachmentStorageDto.getShellParentId(), attachmentStorageDto.getCategory())));
        }
        return ResponseEntity.status(Constant.EXCEPTION_UNAUTHORIZED).build();
    }
}
