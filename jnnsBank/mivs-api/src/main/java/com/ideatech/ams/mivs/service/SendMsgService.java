package com.ideatech.ams.mivs.service;

import com.ideatech.ams.mivs.dto.*;
import com.ideatech.ams.mivs.dto.acd.AnnouncementInformationConfirmDto;
import com.ideatech.ams.mivs.dto.ad.AnnouncementInformationDto;
import com.ideatech.ams.mivs.dto.bqd.BusinessAcceptTimeQueryDto;
import com.ideatech.ams.mivs.dto.mad.MobilePhoneNumberApplyDto;
import com.ideatech.ams.mivs.dto.orfd.OpenRevokeFeedbackDto;
import com.ideatech.ams.mivs.dto.rad.RegisterInformationApplyDto;
import com.ideatech.ams.mivs.dto.tad.TaxInformationApplyDto;

/**
 *
 * 发送报文（功能包括组装报文、校验报文、发送报文、解析返回的报文）
 * @author jzh
 * @date 2019/7/17.
 */
public interface SendMsgService {

    /**
     * 发送手机号码联网核查报文
     * @param mobilePhoneNumberDto
     * @return
     */
    ReplyMsgDto sendMobilePhoneNumberApplyMsg(MobilePhoneNumberApplyDto mobilePhoneNumberDto);

    /**
     * 发送纳税信息联网核查报文
     * @param taxInformationApplyDto
     * @return
     */
    ReplyMsgDto sendTaxInformationApplyMsg(TaxInformationApplyDto taxInformationApplyDto);

    /**
     * 发送企业信息联网核查业务受理时间查询
     * @param businessAcceptTimeQueryDto
     * @return
     */
    ReplyMsgDto sendBusinessAcceptTimeQueryMsg(BusinessAcceptTimeQueryDto businessAcceptTimeQueryDto);

    /**
     * 发送登记信息联网核查申请报文
     * @param registerInformationApplyDto
     * @return
     */
    ReplyMsgDto sendRegisterInformationApplyMsg(RegisterInformationApplyDto registerInformationApplyDto);

    /**
     * 发送企业开销户状态反馈报文
     * @param openRevokeFeedbackDto
     * @return
     */
    ReplyMsgDto sendOpenRevokeFeedbackMsg(OpenRevokeFeedbackDto openRevokeFeedbackDto);

    /**
     * 发送手机号码核查结果疑义反馈报文
     * @param commonFeedbackDto
     * @return
     */
    ReplyMsgDto sendMobilePhoneNumberFeedbackMsg(CommonFeedbackDto commonFeedbackDto);

    /**
     * 发送纳税信息核查结果疑义反馈报文
     * @param commonFeedbackDto
     * @return
     */
    ReplyMsgDto sendTaxInformationFeedbackMsg(CommonFeedbackDto commonFeedbackDto);

    /**
     * 发送登记信息核查结果疑义反馈报文
     * @param commonFeedbackDto
     * @return
     */
    ReplyMsgDto sendRegisterInformationbackMsg(CommonFeedbackDto commonFeedbackDto);

    /**
     * 发送公告信息确认报文
     * @param confirmDto
     * @return
     */
    ReplyMsgDto sendAnnouncementInformationConfirmMsg(AnnouncementInformationConfirmDto confirmDto);

}
