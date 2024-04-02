package com.nxin.framework.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author jesse han
 * @since 2024-02-09
 */
@TableName("task_history")
@ApiModel(value = "task_history对象", description = "")
public class TaskHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long runningProcessId;

    private String logChannelId;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Long shellPublishId;

    private String status;

    public static String ID_COLUMN = "id";

    public static String LOG_CHANNEL_ID_COLUMN = "log_channel_id";

    public static String SHELL_PUBLISH_ID_COLUMN = "shell_publish_id";

    public static String BEGIN_TIME_COLUMN = "begin_time";

    public static String END_TIME_COLUMN = "end_time";

    public static String STATUS_COLUMN = "status";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRunningProcessId() {
        return runningProcessId;
    }

    public void setRunningProcessId(Long runningProcessId) {
        this.runningProcessId = runningProcessId;
    }

    public String getLogChannelId() {
        return logChannelId;
    }

    public void setLogChannelId(String logChannelId) {
        this.logChannelId = logChannelId;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getShellPublishId() {
        return shellPublishId;
    }

    public void setShellPublishId(Long shellPublishId) {
        this.shellPublishId = shellPublishId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ShellRunningLog{" +
                "id = " + id +
                ", runningProcessId = " + runningProcessId +
                ", logChannelId = " + logChannelId +
                ", beginTime = " + beginTime +
                ", endTime = " + endTime +
                ", shellPublishId = " + shellPublishId +
                ", status = " + status +
                "}";
    }
}
