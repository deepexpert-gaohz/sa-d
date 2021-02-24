package com.ideatech.ams.annual.service;

/**
 * @Description 人行服务接口
 * @Author wanghongjie
 * @Date 2018/8/2
 **/
public interface PbcAccountCollectionService {

    /**
     * 根据机构号下载对应机构人行excel
     *
     * <pre>
     * 开始时间为2000-01-01，结束时间为下载当天
     * </pre>
     *
     * @param organId
     *            要下载的机构Id(登录人行使用该机构的人行用户名、密码)
     *
     * @return 下载excel文件路径,返回 null或""则下载失败
     *
     */
    String downRHAccount(long organId) throws Exception;

    /**
     * 根据机构号下载对应机构人行excel
     *
     * <pre>
     * 开始时间startDate,结束时间为下载当天
     * </pre>
     *
     * @param organId
     *            要下载的机构Id(登录人行使用该机构的人行用户名、密码)
     * @param startdate
     *            下载开始日期
     * @return 下载excel文件路径,返回 null或""则下载失败
     * @throws Exception
     */
    String downRHAccount(long organId, String startdate) throws Exception;

    /**
     * 根据机构号下载对应机构人行excel
     *
     * @param organId
     *            要下载的机构Id(登录人行使用该机构的人行用户名、密码)
     * @param startdate
     *            下载开始时间
     * @param endDate
     *            下载截止时间
     * @return 下载excel文件路径,返回 null或""则下载失败
     * @throws Exception
     */
    String downRHAccount(long organId, String startdate, String endDate) throws Exception;

}
