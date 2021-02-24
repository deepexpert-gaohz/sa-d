package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dao.PbcCollectAccountDao;
import com.ideatech.ams.annual.dao.PbcCollectOrganDao;
import com.ideatech.ams.annual.dto.PbcCollectAccountDto;
import com.ideatech.ams.annual.dto.PbcCollectOrganDto;
import com.ideatech.ams.annual.entity.PbcCollectAccount;
import com.ideatech.ams.annual.entity.PbcCollectOrgan;
import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Description TODO
 * @Author wanghongjie
 * @Date 2018/8/14
 **/
@Service
@Transactional
@Slf4j
public class PbcCollectOrganServiceImpl  extends BaseServiceImpl<PbcCollectOrganDao, PbcCollectOrgan, PbcCollectOrganDto> implements  PbcCollectOrganService{
    @Override
    public List<PbcCollectOrganDto> findByCollectBatch(String collectBatch) {
        return ConverterService.convertToList(getBaseDao().findByCollectBatch(collectBatch), PbcCollectOrganDto.class);
    }

    @Override
    public List<PbcCollectOrganDto> findByCollectBatchAndCollectState(String collectBatch, CollectState collectState) {
        return ConverterService.convertToList(getBaseDao().findByCollectBatchAndCollectState(collectBatch,collectState), PbcCollectOrganDto.class);
    }

    @Override
    public List<PbcCollectOrganDto> findByCollectBatchAndCollectStateNot(String collectBatch, CollectState collectState) {
        return ConverterService.convertToList(getBaseDao().findByCollectBatchAndCollectStateNot(collectBatch,collectState), PbcCollectOrganDto.class);
    }

    @Override
    public String getMaxCollectBatch() {
        return getBaseDao().getMaxCollectBatch();
    }

    @Override
    public void deleteAll() {
        getBaseDao().deleteAll();
    }

    @Override
    public long countByCollectStateNot(CollectState... collectState) {
        return getBaseDao().countByCollectStateNotIn(collectState);
    }
}
