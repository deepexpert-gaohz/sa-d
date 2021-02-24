package com.ideatech.ams.image.service;

import com.ideatech.ams.image.dto.ImageSessionDTO;

/**
 * @author jzh
 * @date 2019-12-10.
 */
public interface ImageSessionService {

    /**
     * 根据id获取双录会话信息
     * @param id
     * @return
     */
    ImageSessionDTO findOneById(Long id);

    /**
     * 保存创建 双录会话信息
     * @param imageSessionDTO
     * @return
     */
    ImageSessionDTO create(ImageSessionDTO imageSessionDTO);

    /**
     * 根据访客登录名（手机号）获取双录会话信息，最近的一条信息
     * @param clientName
     * @return
     */
    ImageSessionDTO findOneByClientName(String clientName);
}
