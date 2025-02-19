
package com.nxin.framework.controller.kettle;

import com.nxin.framework.service.auth.UserService;
import com.nxin.framework.service.basic.ProjectService;
import com.nxin.framework.service.kettle.AttachmentStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
