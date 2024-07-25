package com.nxin.framework.event.listener;

import com.nxin.framework.entity.bi.Layout;
import com.nxin.framework.entity.auth.Privilege;
import com.nxin.framework.entity.auth.Resource;
import com.nxin.framework.enums.Constant;
import com.nxin.framework.event.LayoutEvent;
import com.nxin.framework.service.auth.PrivilegeService;
import com.nxin.framework.service.auth.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
public class LayoutListener {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PrivilegeService privilegeService;

    @Async
    @Transactional
    @EventListener(LayoutEvent.class)
    public void action(LayoutEvent layoutEvent) {
        Layout persisted = (Layout) layoutEvent.getSource();
        Resource resource = new Resource();
        resource.setStatus(Constant.ACTIVE);
        resource.setCode(persisted.getResourceCode());
        resource.setName(persisted.getName());
        resource.setCreator(persisted.getCreator());
        resource.setModifier(persisted.getCreator());
        resourceService.save(resource);
        Privilege privilege = new Privilege();
        privilege.setName(persisted.getName());
        privilege.setResourceId(resource.getId());
        privilege.setCategory(Constant.PRIVILEGE_READ_WRITE);
        privilege.setCreator(persisted.getCreator());
        privilege.setModifier(persisted.getCreator());
        privilegeService.save(privilege);
        privilegeService.grant(Collections.singletonList(privilege.getId()), layoutEvent.getUser().getId(), false);
    }
}
