package com.ideatech.ams.pbc.service;


/**
 * @Author yang
 * @Date 2019/11/20 15:24
 * @Version 1.0
 */
public interface PbcSyncConfigService {
    /**
     * 基本户先销后开是否使用人行数据（默认不使用）
     * @return
     */
    boolean isJibenSync();

    /**
     * 是否单独记录人行上报返回的HTML文件,默认不单独记录
     * @return
     */
    boolean isHtmlLog();

    /**
     * 取消核准征询暂停时间,单位:毫秒,默认：2000
     * @return
     */
    Long getStopDate();
}
