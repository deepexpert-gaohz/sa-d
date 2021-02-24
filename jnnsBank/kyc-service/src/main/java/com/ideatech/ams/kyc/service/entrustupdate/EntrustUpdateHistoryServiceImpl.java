package com.ideatech.ams.kyc.service.entrustupdate;

import com.ideatech.ams.kyc.dao.entrustupdate.EntrustUpdateHistoryDao;
import com.ideatech.ams.kyc.dto.entrustupdate.EntrustUpdateHistoryDto;
import com.ideatech.ams.kyc.entity.entrustupdate.EntrustUpdateHistory;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class EntrustUpdateHistoryServiceImpl extends BaseServiceImpl<EntrustUpdateHistoryDao, EntrustUpdateHistory, EntrustUpdateHistoryDto>
        implements EntrustUpdateHistoryService {

    @Autowired
    private EntrustUpdateHistoryDao entrustUpdateHistoryDao;

    @Override
    public List<EntrustUpdateHistoryDto> listByCompanyNameAndUpdateStatus(String companyName, Boolean updateStatus) {
        List<EntrustUpdateHistory> list = entrustUpdateHistoryDao.findByCompanyNameAndUpdateStatus(companyName, updateStatus);
        return ConverterService.convertToList(list, EntrustUpdateHistoryDto.class);
    }


}
