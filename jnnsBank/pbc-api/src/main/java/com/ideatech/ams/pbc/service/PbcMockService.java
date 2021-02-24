package com.ideatech.ams.pbc.service;

import lombok.Data;

/**
 * @author van
 * @date 23:59 2018/7/18
 */
public interface PbcMockService {

    /**
     * 登录挡板，未配置的话默认关闭
     * @return
     */
    Boolean isLoginMockOpen();

    /**
     * 登录挡板，未配置的话默认开启
     * @return
     */
    Boolean isSyncMockOpen();

}
