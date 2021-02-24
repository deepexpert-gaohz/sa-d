package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.IllegalDao;
import com.ideatech.ams.kyc.dto.IllegalDto;
import com.ideatech.ams.kyc.entity.Illegal;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class IllegalServiceImpl implements IllegalService {

    @Autowired
    private IllegalDao illegalDao;


    @Override
    public void insertBatch(Long saicInfoId, List<IllegalDto> illegalDtoList) {
        Illegal illegal = null;

        int size = illegalDtoList.size();
        if (size == 0) {
            return;
        }

        //设置主键
        for (IllegalDto illegalDto : illegalDtoList) {
            illegal = new Illegal();
            BeanCopierUtils.copyProperties(illegalDto, illegal);
//            illegal.setId(Calendar.getInstance().getTimeInMillis());
            illegal.setSaicinfoId(saicInfoId);
            illegalDao.save(illegal);
        }

    }

    @Override
    public List<IllegalDto> findBySaicInfoId(Long saicInfoId) {
        List<IllegalDto> dtoList = new ArrayList<IllegalDto>();
        List<Illegal> list = illegalDao.findBySaicinfoId(saicInfoId);
        IllegalDto illegalDto = null;
        //设置主键
        for (Illegal illegal : list) {
            illegalDto = new IllegalDto();
            BeanCopierUtils.copyProperties(illegal, illegalDto);
            dtoList.add(illegalDto);
        }
        return dtoList;
    }
}
