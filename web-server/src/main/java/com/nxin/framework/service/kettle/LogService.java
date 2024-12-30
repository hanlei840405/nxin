package com.nxin.framework.service.kettle;

import com.nxin.framework.vo.kettle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LogService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String SQL_STEP = "SELECT `ID_BATCH`,\n" +
            "    `CHANNEL_ID`,\n" +
            "    `LOG_DATE`,\n" +
            "    `TRANSNAME`,\n" +
            "    `STEPNAME`,\n" +
            "    `STEP_COPY`,\n" +
            "    `LINES_READ`,\n" +
            "    `LINES_WRITTEN`,\n" +
            "    `LINES_UPDATED`,\n" +
            "    `LINES_INPUT`,\n" +
            "    `LINES_OUTPUT`,\n" +
            "    `LINES_REJECTED`,\n" +
            "    `ERRORS`\n" +
            "FROM `log_etl_transform_step` WHERE CHANNEL_ID in (:channelIds);\n";

    public List<StepLogVo> stepLog(List<String> channelIds) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        return namedParameterJdbcTemplate.query(SQL_STEP, Collections.singletonMap("channelIds", channelIds), (rs, i) -> {
            String name = rs.getString("STEPNAME");
            String logDate = rs.getString("LOG_DATE");
            Integer stepCopy = rs.getInt("STEP_COPY");
            Long read = rs.getLong("LINES_READ");
            Long written = rs.getLong("LINES_WRITTEN");
            Long updated = rs.getLong("LINES_UPDATED");
            Long input = rs.getLong("LINES_INPUT");
            Long output = rs.getLong("LINES_OUTPUT");
            Long rejected = rs.getLong("LINES_REJECTED");
            Long errors = rs.getLong("ERRORS");
            StepLogVo stepLog = StepLogVo.builder().build();
            stepLog.setName(name);
            if (logDate != null) {
                stepLog.setLogDate(logDate);
            }
            stepLog.setStepCopy(stepCopy);
            stepLog.setRead(read);
            stepLog.setWritten(written);
            stepLog.setUpdated(updated);
            stepLog.setInput(input);
            stepLog.setOutput(output);
            stepLog.setRejected(rejected);
            stepLog.setErrors(errors);
            return stepLog;
        });
    }

    public void clearJobLogField() {
        jdbcTemplate.update("delete from log_etl_transform_channel where log_date<curdate()");
        jdbcTemplate.update("delete from log_etl_job where logdate<curdate()");
        jdbcTemplate.update("delete from log_etl_job_entry where log_date<curdate()");
        jdbcTemplate.update("delete from log_etl_transform where logdate<curdate()");
        jdbcTemplate.update("delete from log_etl_transform_step where log_date<curdate()");
    }

    public Map<String, Object> fetchLogs(List<String> logChannelIds) {
        List<StepLogVo> stepLogs = new ArrayList<>(0);
        stepLogs.addAll(this.stepLog(logChannelIds));
        Map<String, Object> result = new HashMap<>(0);
        result.put("steps", stepLogs);
        return result;
    }
}