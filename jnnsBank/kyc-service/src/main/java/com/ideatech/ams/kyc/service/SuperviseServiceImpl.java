package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.SuperviseDao;
import com.ideatech.ams.kyc.dto.SuperviseDto;
import com.ideatech.ams.kyc.entity.Supervise;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SuperviseServiceImpl implements SuperviseService {

    @Autowired
    private SuperviseDao superviseDao;


    @Override
    public void insertBatch(Long saicInfoId, List<SuperviseDto> superviseList) {
        Supervise supervise = null;

        int size = superviseList.size();
        if(size == 0){
            return;
        }

        //设置主键
        for (SuperviseDto superviseDto: superviseList) {
            supervise = new Supervise();
            BeanCopierUtils.copyProperties(superviseDto, supervise);
//            supervise.setId(Calendar.getInstance().getTimeInMillis());
            supervise.setSaicinfoId(saicInfoId);
            superviseDao.save(supervise);
        }
    }
}
