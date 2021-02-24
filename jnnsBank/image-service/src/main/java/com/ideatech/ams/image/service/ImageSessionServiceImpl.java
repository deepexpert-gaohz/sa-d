package com.ideatech.ams.image.service;

import com.ideatech.ams.image.dao.ImageSessionDao;
import com.ideatech.ams.image.dto.ImageSessionDTO;
import com.ideatech.ams.image.entity.ImageSession;
import com.ideatech.common.converter.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jzh
 * @date 2019-12-10.
 */

@Service
public class ImageSessionServiceImpl implements ImageSessionService {

    @Autowired
    private ImageSessionDao imageSessionDao;

    @Override
    public ImageSessionDTO findOneById(Long id) {
        return ConverterService.convert(imageSessionDao.findOne(id),ImageSessionDTO.class);
    }

    @Override
    public ImageSessionDTO create(ImageSessionDTO imageSessionDTO) {
        ImageSession imageSession = imageSessionDao.save(ConverterService.convert(imageSessionDTO, ImageSession.class));
        return ConverterService.convert(imageSession,ImageSessionDTO.class);
    }

    @Override
    public ImageSessionDTO findOneByClientName(String clientName) {
        return ConverterService.convert(imageSessionDao.findTopByClientNameOrderByCreatedDateDesc(clientName),ImageSessionDTO.class);
    }
}
