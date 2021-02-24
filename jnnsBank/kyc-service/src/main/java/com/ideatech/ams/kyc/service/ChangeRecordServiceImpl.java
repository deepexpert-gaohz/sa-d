package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.ChangeRecordDao;
import com.ideatech.ams.kyc.dto.ChangeRecordDto;
import com.ideatech.ams.kyc.entity.ChangeRecord;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liangding
 * @create 2018-08-22 下午3:47
 **/
@Service
@Slf4j
public class ChangeRecordServiceImpl implements ChangeRecordService {

    @Autowired
    private ChangeRecordDao changeRecordDao;

    @Override
    public void insertBatch(final Long saicInfoId, List<ChangeRecordDto> changeRecordDtos) {
        List<ChangeRecord> changeRecords = ConverterService.convertToList(changeRecordDtos, ChangeRecord.class);
        CollectionUtils.forAllDo(changeRecords, new Closure() {
            @Override
            public void execute(Object input) {
                ((ChangeRecord) input).setSaicinfoId(saicInfoId);
            }
        });
        changeRecordDao.save(changeRecords);
    }

    @Override
    public List<ChangeRecordDto> findBySaicInfoId(Long saicInfoId) {
        List<ChangeRecord> bySaicinfoId = changeRecordDao.findBySaicinfoId(saicInfoId);
        return ConverterService.convertToList(bySaicinfoId, ChangeRecordDto.class);
    }

}
