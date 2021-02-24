package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.dao.EzhMessageDao;
import com.ideatech.ams.apply.dao.spec.EzhMessageSpec;
import com.ideatech.ams.apply.dto.EzhMessageDto;
import com.ideatech.ams.apply.entity.EzhMessage;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.msg.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class EzhMessageServiceImpl implements EzhMessageService {

    @Autowired
    private EzhMessageDao ezhMessageDao;

    @Override
    public TableResultResponse<EzhMessageDto> query(EzhMessageDto dto, Pageable pageable) {
        Page<EzhMessage> page = ezhMessageDao.findAll(new EzhMessageSpec(dto), pageable);
        long count = ezhMessageDao.count(new EzhMessageSpec(dto));

        List<EzhMessage> list = page.getContent();
        List<EzhMessageDto> listDto = ConverterService.convertToList(list, EzhMessageDto.class);

        return new TableResultResponse<EzhMessageDto>((int)count, listDto);
    }

    @Override
    public EzhMessageDto getOne(Long id) {
        EzhMessage ezhMessage = ezhMessageDao.findOne(id);
        return ConverterService.convert(ezhMessage, EzhMessageDto.class);
    }

    @Override
    public void edit(EzhMessageDto dto) {
        EzhMessage ezhMessage = ezhMessageDao.findOne(dto.getId());
        BeanUtils.copyProperties(dto, ezhMessage);

        ezhMessageDao.save(ezhMessage);
    }

}
