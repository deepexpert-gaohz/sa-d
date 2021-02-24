package com.ideatech.ams.kyc.service;

import com.ideatech.ams.kyc.dao.DirectorDao;
import com.ideatech.ams.kyc.dto.DirectorDto;
import com.ideatech.ams.kyc.entity.Director;
import com.ideatech.common.util.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private DirectorDao directorDao;

    @Override
    public void insertBatch(Long saicInfoId, List<DirectorDto> directorList) {
        Director director = null;

        int size = directorList.size();
        if(size == 0){
            return;
        }

        //设置主键
        for (DirectorDto directorDto: directorList) {
            director = new Director();
            BeanCopierUtils.copyProperties(directorDto, director);
//            director.setId(Calendar.getInstance().getTimeInMillis());
            director.setSaicinfoId(saicInfoId);
            directorDao.save(director);
        }
    }
}
