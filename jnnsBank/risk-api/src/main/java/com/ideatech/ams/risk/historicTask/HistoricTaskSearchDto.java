package com.ideatech.ams.risk.historicTask;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class HistoricTaskSearchDto extends PagingDto<HistoricTaskDto> {
    String name;
    String startEndTime;
}
