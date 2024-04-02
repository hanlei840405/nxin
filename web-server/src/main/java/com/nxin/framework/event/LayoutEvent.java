package com.nxin.framework.event;

import com.nxin.framework.entity.analysis.Layout;
import com.nxin.framework.entity.auth.User;
import org.springframework.context.ApplicationEvent;

public class LayoutEvent extends ApplicationEvent {
    private User user;

    public LayoutEvent(Layout layout) {
        super(layout);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
