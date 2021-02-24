package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.ams.image.dto.ImageVideoDto;
import com.ideatech.common.dto.ResultDto;

import java.util.List;

public interface ImageApiService {
    /**
     * 提供给外围接口,配合上报接口使用；两个接口调用顺序会有影响
     * 1、先调用影像接口，后调用上报接口：此接口会返回一个临时编号，上报时必须将临时编号传入，以便做影像与流水之间的关联
     * 2、先调用上报接口，后调用影像接口：调用影像接口时必须传入流水id，做关联使用
     * @param info
     * @param flag 0 代表影像接口先调用
     *      *        1  代表上报接口先调用
     * @return
     */
    String saveImageFromOut(ImageAllInfo info, String flag);

    /**
     * 提供给外围系统查询接口：
     * 接口可查询法人是否录入视频,如果录入则返回相关信息
     * @param dto 条件
     * @return 集合数量代表法人的视频个数
     */
    List<ImageVideoDto> searchVideo(ImageVideoDto dto);

    /**
     * 修改流水表影像状态
     * @param billId 流水id
     * @param status 影像上报状态
     * @return
     */
    ResultDto updateImageStatus(Long billId, CompanySyncStatus status);
}
