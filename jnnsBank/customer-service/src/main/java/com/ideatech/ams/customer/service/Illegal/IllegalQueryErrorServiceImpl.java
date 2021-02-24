package com.ideatech.ams.customer.service.Illegal;

import com.ideatech.ams.customer.dao.illegal.IllegalQueryErrorDao;
import com.ideatech.ams.customer.dto.illegal.IllegalQueryErrorDto;
import com.ideatech.ams.customer.entity.illegal.IllegalQueryError;
import com.ideatech.ams.customer.service.illegal.IllegalQueryErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
public class IllegalQueryErrorServiceImpl implements IllegalQueryErrorService {

    @Autowired
    private IllegalQueryErrorDao illegalQueryErrorDao;

    @Override
    public void save(IllegalQueryErrorDto illegalQueryErrorDto) {
        IllegalQueryError illegalQueryError = null;
        if(illegalQueryErrorDto.getId() == null){
            illegalQueryError = new IllegalQueryError();
            BeanUtils.copyProperties(illegalQueryErrorDto,illegalQueryError);
            illegalQueryErrorDao.save(illegalQueryError);
        }else{
            illegalQueryError = illegalQueryErrorDao.findOne(illegalQueryErrorDto.getId());
            BeanUtils.copyProperties(illegalQueryErrorDto,illegalQueryError);
            illegalQueryErrorDao.save(illegalQueryError);
        }
    }
}
