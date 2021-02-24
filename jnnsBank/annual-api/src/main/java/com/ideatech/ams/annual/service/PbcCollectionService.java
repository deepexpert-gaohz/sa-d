package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.FetchPbcInfoDto;
import com.ideatech.ams.annual.enums.CollectType;

import java.util.List;

public interface PbcCollectionService {
    List<FetchPbcInfoDto> getAll(Long taskId);
    void collect(Long taskId);
    void collect(CollectType collectType,Long taskId);
    void collectReset(Long annualTaskId);
    void clearFuture();
    void endFuture();
}
