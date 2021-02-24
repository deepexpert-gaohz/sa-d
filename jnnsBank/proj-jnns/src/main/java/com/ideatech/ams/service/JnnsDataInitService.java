package com.ideatech.ams.service;

/**
 * 数据初始化服务类
 *
 * @auther zoulang
 * @create 2019-06-17 9:26 AM
 **/
public interface JnnsDataInitService {

    /**
     * 初始化机构信息
     */
    void uploadOrganization();

    /**
     * 初始化用户信息
     */
    void uploadUser();
}
