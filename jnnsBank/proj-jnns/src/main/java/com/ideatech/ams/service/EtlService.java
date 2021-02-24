package com.ideatech.ams.service;

/**
 * Etl文件处理
 */
public interface EtlService {

    /**
     *初始化核心数据，并生成产品数据初始化文件
     */
    void doInitCore();

    /**
     * 每天更新核心存量数据数据
     */
    void douUpdateCoreData();


    /**
     * 每天同步核心数据
     */
    void  doCoreData();
}
