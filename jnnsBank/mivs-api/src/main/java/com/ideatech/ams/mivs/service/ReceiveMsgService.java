package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.*;

/**
 * 接收报文（功能包括 接收报文、解析接收的报文、保存报文信息）
 * @author jzh
 * @date 2019/7/24.
 */

public interface ReceiveMsgService {

    /**
     * receiveInstitutionAbnormalNotice()更为抽象。
     *
     * receiveInstitutionAbnormalNotice(String msg, Class<T> targetClass)
     * 是receiveInstitutionAbnormalNotice()的一种更具体的接口
     */
    //<T> T receiveInstitutionAbnormalNotice(String msg, Class<T> targetClass);

    /**
     * 接收手机联网核查应答报文
     * @param mobilePhoneNumberLogDto
     */
    void receiveMobilePhoneNumberReplyMsg(MobilePhoneNumberLogDto mobilePhoneNumberLogDto);

    /**
     * 接收纳税信息联网核查
     * @param taxInformationLogDto
     */
    void receiveTaxInformationReplyMsg(TaxInformationLogDto taxInformationLogDto);

    /**
     * 接收业务受理时间查询应答报文
     * @param businessAcceptTimeLogDto
     */
    void receiveBusinessAcceptTimeQueryReplyMsg(BusinessAcceptTimeLogDto businessAcceptTimeLogDto);

    /**
     * 接收登记信息联网核查应答报文
     * @param registerInformationLogDto
     */
    void receiveRegisterInformationReplyMsg(RegisterInformationLogDto registerInformationLogDto);

    /**
     * 接收通用处理确认报文（公共疑义反馈返回信息）
     * @param commonFeedbackLogDto
     */
    void receiveCommonFeedbackMsg(CommonFeedbackLogDto commonFeedbackLogDto);

    /**
     * 接收通用处理确认报文（开销户状态疑义反馈返回信息）
     * @param openRevokeFeedbackLogDto
     */
    void receiveOpenRevokeFeedbackMsg(OpenRevokeFeedbackLogDto openRevokeFeedbackLogDto);


    /**
     * 接收机构异常核查通知报文
     */
    void receiveInstitutionAbnormalNotice();

    /**
     * 接收企业异常核查通知报文
     */
    void receiveEnterpriseAbnormalNotice();

    /**
     * 接收企业信息联网核查业务受理时间通知报文
     */
    void receiveBusinessAcceptTimeNotice();

    /**
     * 接收公告信息
     */
    void receiveAnnouncementInformation();

    /**
     * 接收公告信息确认报文的返回结果
     */
    void receiveAnnouncementInformationConfirm(AnnouncementInformationConfirmLogDto confirmLogDto);
}
