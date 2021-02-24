package com.ideatech.ams.image.dao;

import com.ideatech.ams.image.entity.ImageSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jzh
 * @date 2019-12-10.
 */
public interface ImageSessionDao extends JpaRepository<ImageSession,Long> {

    /**
     * 根据访客登录名获取最新的会话信息
     * @param clientName
     * @return
     */
    ImageSession findTopByClientNameOrderByCreatedDateDesc(String clientName);
}
