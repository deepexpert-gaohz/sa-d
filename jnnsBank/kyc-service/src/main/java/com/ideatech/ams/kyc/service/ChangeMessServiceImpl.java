package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.ChangeMessDao;
import com.ideatech.ams.kyc.dto.ChangeMessDto;
import com.ideatech.ams.kyc.entity.ChangeMess;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChangeMessServiceImpl implements ChangeMessService {

    @Autowired
    private ChangeMessDao changeMessDao;

    @Override
    public void insertBatch(Long saicInfoId, List<ChangeMessDto> changeMessList) {
        ChangeMess changeMess = null;

        int size = changeMessList.size();
        if (size == 0) {
            return;
        }

        //设置主键
        for (ChangeMessDto changemessDto : changeMessList) {
            changeMess = new ChangeMess();
            BeanCopierUtils.copyProperties(changemessDto, changeMess);
//            changeMess.setId(Calendar.getInstance().getTimeInMillis());
            changeMess.setSaicinfoId(saicInfoId);
            changeMessDao.save(changeMess);
        }
    }

    @Override
    public List<ChangeMessDto> findBySaicInfoId(Long saicInfoId) {
        List<ChangeMessDto> dtoList = new ArrayList<ChangeMessDto>();
        List<ChangeMess> list = changeMessDao.findBySaicinfoId(saicInfoId);
        ChangeMessDto changeMessDto = null;
        //设置主键
        for (ChangeMess changeMess : list) {
            changeMessDto = new ChangeMessDto();
            BeanCopierUtils.copyProperties(changeMess, changeMessDto);
            dtoList.add(changeMessDto);
        }
        return dtoList;
    }
}
