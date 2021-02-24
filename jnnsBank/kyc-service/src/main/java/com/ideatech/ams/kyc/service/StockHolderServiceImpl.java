package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.StockHolderDao;
import com.ideatech.ams.kyc.dto.StockHolderDto;
import com.ideatech.ams.kyc.entity.StockHolder;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StockHolderServiceImpl implements StockHolderService {

    @Autowired
    private StockHolderDao stockHolderDao;

    /**
     * 批量插入股东信息
     *
     * @param stockHolderDtoList
     */
    @Override
    public void insertBatch(Long saicInfoId, List<StockHolderDto> stockHolderDtoList) {
        StockHolder stockHolder = null;

        int size = stockHolderDtoList.size();
        if (size == 0) {
            return;
        }

        //设置主键
        for (StockHolderDto stockHolderDto : stockHolderDtoList) {
            stockHolder = new StockHolder();
            BeanCopierUtils.copyProperties(stockHolderDto, stockHolder);
//            stockHolder.setId(Calendar.getInstance().getTimeInMillis());
            stockHolder.setSaicinfoId(saicInfoId);
            stockHolderDao.save(stockHolder);
        }

    }

    @Override
    public List<StockHolderDto> findBySaicInfoId(Long saicInfoId) {
        List<StockHolderDto> dtoList = new ArrayList<StockHolderDto>();
        List<StockHolder> list = stockHolderDao.findBySaicinfoId(saicInfoId);
        StockHolderDto stockHolderDto = null;
        //设置主键
        for (StockHolder stockHolder : list) {
            stockHolderDto = new StockHolderDto();
            BeanCopierUtils.copyProperties(stockHolder, stockHolderDto);
            dtoList.add(stockHolderDto);
        }
        return dtoList;
    }

}
