package com.nxin.framework.interfaces;

import com.nxin.framework.dto.CronTriggerDto;
import com.nxin.framework.dto.ResponseDto;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    ResponseDto create(String group, String id, String description, String cron, Integer misfire, String data);

    ResponseDto<List<CronTriggerDto>> findAllCronTrigger(List<String> groupList);

    ResponseDto pause(String group, String id);

    ResponseDto resume(String group, String id);

    ResponseDto stop(String group, String id);

    ResponseDto modify(String group, String id, String cron, Integer misfire);
}
