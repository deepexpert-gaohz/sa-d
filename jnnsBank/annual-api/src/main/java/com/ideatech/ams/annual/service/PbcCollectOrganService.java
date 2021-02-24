package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.PbcCollectOrganDto;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.service.BaseService;

import java.util.List;

public interface PbcCollectOrganService extends BaseService<PbcCollectOrganDto> {

    List<PbcCollectOrganDto> findByCollectBatch(String collectBatch);

    List<PbcCollectOrganDto> findByCollectBatchAndCollectState(String collectBatch, CollectState collectState);

    List<PbcCollectOrganDto> findByCollectBatchAndCollectStateNot(String collectBatch, CollectState collectState);

    String getMaxCollectBatch();

    void deleteAll();

    long countByCollectStateNot(CollectState... collectState);
}
