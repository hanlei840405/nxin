package com.nxin.framework.service.task;

import com.nxin.framework.enums.DateFormatType;
import com.nxin.framework.service.DynamicQueryDataService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class CreateTableTaskComp extends QuartzJobBean {
    @Autowired
    private DynamicQueryDataService dynamicQueryDataService;

    @Transactional
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String sql = jobDataMap.getString("sql");
        String frequency = jobDataMap.getString("frequency");
        String tableName = jobDataMap.getString("tableName");
        String name = jobDataMap.getString("name");
        String category = jobDataMap.getString("category");
        String host = jobDataMap.getString("host");
        String schemaName = jobDataMap.getString("schemaName");
        String port = jobDataMap.getString("port");
        String username = jobDataMap.getString("username");
        String password = jobDataMap.getString("password");
        String format = DateFormatType.getValue(frequency);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        if (format != null) {
            tableName += new SimpleDateFormat(format).format(calendar.getTime());
        } else {
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            tableName += "_" + week;
        }
        dynamicQueryDataService.execute(name, category, host, schemaName, port, username, password, String.format(sql, tableName));
    }
}
