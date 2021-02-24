package com.ideatech.ams.compare.service;

import com.ideatech.common.dto.TreeTable;

public interface CompareStatisticsService {

    void statistics(Long taskId);

    TreeTable query(Long pid, Long organId, Long taskId);
}
