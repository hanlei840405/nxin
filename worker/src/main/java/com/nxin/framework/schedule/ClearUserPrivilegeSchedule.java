package com.nxin.framework.schedule;

import com.nxin.framework.enums.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ClearUserPrivilegeSchedule {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final long DEFAULT_LOCK_TIMEOUT = 10000;
    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Scheduled(cron = "0 0 0 * * ?")
    protected void clear() {
        Boolean locked = stringRedisTemplate.opsForValue().setIfAbsent(this.getClass().getName(), Thread.currentThread().getName(), DEFAULT_LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
        try {
            if (locked) {
                List<Map<String, Object>> records = jdbcTemplate.queryForList("select user_id, privilege_id from auth_user_privilege where expire_date < now()");
                if (!records.isEmpty()) {
                    jdbcTemplate.batchUpdate("insert into auth_log(`user_id`,`privilege_id`,`action`,`action_time`,`operator`) values (?,?,?,?,?)", new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setLong(1, (Long) records.get(i).get("user_id"));
                            preparedStatement.setLong(2, (Long) records.get(i).get("privilege_id"));
                            preparedStatement.setString(3, Constant.ACTION_EXPIRE);
                            preparedStatement.setDate(4, Date.valueOf(LocalDate.now()));
                            preparedStatement.setString(5, "schedule");
                        }

                        @Override
                        public int getBatchSize() {
                            return records.size();
                        }
                    });
                    jdbcTemplate.execute("delete from auth_user_privilege where expire_date < now()");
                }
            }
        } finally {
            stringRedisTemplate.execute(new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class), Collections.singletonList(this.getClass().getName()), Thread.currentThread().getName());
        }

    }
}
