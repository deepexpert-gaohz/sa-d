package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.ams.image.service.ImageAllService;
import com.ideatech.ams.image.service.ImageVideoService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ImageApiServiceImpl implements ImageApiService{
    @Autowired
    private ImageAllService imageAllService;
    @Autowired
    private ImageVideoService imageVideoService;
    @Autowired
    private AccountBillsAllService accountBillsAllService;
    @Override
    public String saveImageFromOut(ImageAllInfo info, String flag) {
        return imageAllService.saveImageFromOut(info,flag);
    }

    @Override
    public List<ImageVideoDto> searchVideo(ImageVideoDto dto) {
        return imageVideoService.searchByOut(dto);
    }

    @Override
    public ResultDto updateImageStatus(Long billId, CompanySyncStatus status) {
        try {
            accountBillsAllService.updateImageSyncStatus(billId,status);
            return ResultDtoFactory.toAck();
        }catch (Exception e){
            log.error("修改异常：{}",e.getMessage());
            return ResultDtoFactory.toApiError(ResultCode.IMAGE_RELEV_FAIL.code() , "；流水表修改状态失败，失败原因："+e.getMessage());
        }
    }
}
