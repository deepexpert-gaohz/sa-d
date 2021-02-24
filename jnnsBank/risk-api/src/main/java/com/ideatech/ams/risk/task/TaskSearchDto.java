package com.ideatech.ams.risk.task;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;



@Data
public class TaskSearchDto extends PagingDto<TaskDto> {
    private String name;
    private String startEndTime;
}
