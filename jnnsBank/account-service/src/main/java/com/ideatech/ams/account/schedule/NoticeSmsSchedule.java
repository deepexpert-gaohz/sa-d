package com.ideatech.ams.account.schedule;

import com.ideatech.ams.account.dto.AccountPublicInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.AllAccountPublicDTO;
import com.ideatech.ams.account.service.AccountPublicLogService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.AllAccountPublicService;
import com.ideatech.ams.customer.dto.CustomerAllResponse;
import com.ideatech.ams.customer.dto.CustomerPublicMidInfo;
import com.ideatech.ams.customer.service.CustomerPublicMidService;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.notice.service.NoticeService;
import com.ideatech.ams.system.notice.service.NoticeSmsService;
import com.ideatech.common.msg.ObjectRestResponse;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 到期超期提醒定时发送
 * @author jzh
 * @date 2019/5/6.
 */

@Component
@Slf4j
public class NoticeSmsSchedule implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 发送时间（频率）
     */
    @Value("${ams.notice.timing:0 0 12 * * ?}")
    private String times;

    /**
     * 是否启用定时任务
     */
    @Value("${ams.notice.use:false}")
    private boolean isUse;

    /**
     * 模版
     */
    @Value("${ams.notice.noticeMessage:短信模版}")
    private String noticeMessage;

    @Autowired
    private NoticeSmsService noticeSmsService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private CustomerPublicMidService customerPublicMidService;

    @Autowired
    private AllAccountPublicService allAccountPublicService;

    @Autowired
    private AccountPublicLogService accountPublicLogService;

    @Autowired
    private NoticeService noticeService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //1、是否启用定时任务 （配置文件配置）
        if (isUse){
            log.info("启用定时任务");
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {

                    //2、是否发送提醒短信（系统配置模块配置）
                    ConfigDto configDto = configService.findByKey("msgNoticeConfigEnabled").get(0);
                    Date nowDate = DateUtil.beginOfDate(new Date());
                    //显示权限控制
                    Map<String, Boolean> map = noticeService.setNoticeConfigPermission();
                    Map<String, Boolean> overNoticeMap = noticeService.overNoticeConfig();

                    if ("true".equals(configDto.getConfigValue())){
                        log.info("发送提醒短信");

                        //3.1、临时户提醒
                        if(map.get("tempConfigEnabled")) {
                            Long tempAcctNoticeDay = configService.findOneByKey("tempAcctMsgNoticeDay");
                            Long tempAcctOverNoticeDay = configService.findOneByKey("tempAcctMsgOverNoticeDay");
                            if (tempAcctNoticeDay == null) {
                                tempAcctNoticeDay = 3L;
                            }
                            if (tempAcctOverNoticeDay == null) {
                                tempAcctOverNoticeDay = 3L;
                            }
                            Date afterDate = DateUtil.addDays(nowDate, (int) (tempAcctNoticeDay + 0));
                            Date beforeDate = DateUtil.subDays(nowDate, (int) (tempAcctOverNoticeDay + 0));
                            List<AccountsAllInfo> accountsAllInfoList = accountsAllService.getTempAcctDueAndOver(
                                    DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), overNoticeMap.get("tempAcctOverConfigEnabled"));
                            for (AccountsAllInfo accountsAllInfo : accountsAllInfoList) {
                                List<CustomerPublicMidInfo> customerPublicMidInfoList = customerPublicMidService.findByCustomerId(accountsAllInfo.getCustomerLogId());
                                if (customerPublicMidInfoList.size() != 0) {
                                    if (null != customerPublicMidInfoList.get(0).getLegalName() && null != customerPublicMidInfoList.get(0).getLegalTelephone()) {
                                        try {
                                            noticeSmsService.sendMessage(customerPublicMidInfoList.get(0).getLegalTelephone(), noticeMessage);
                                            log.info("短信发送成功！" + customerPublicMidInfoList.get(0));
                                        } catch (Exception e) {
                                            log.warn("短信发送失败！" + customerPublicMidInfoList.get(0));
                                        }
                                    } else {
                                        log.warn("法定代表人或负责人信息不完整（姓名或联系方式）:" + customerPublicMidInfoList.get(0));
                                    }
                                }
                            }
                        }


                        //3.2、法人证件提醒
                        if(map.get("legalDueConfigEnabled")) {
                            Long legalDueNoticeDay = configService.findOneByKey("legalMsgDueNoticeDay");
                            Long legalOverNoticeDay = configService.findOneByKey("legalMsgOverNoticeDay");
                            if(legalDueNoticeDay == null) {
                                legalDueNoticeDay = 3L;
                            }
                            if(legalOverNoticeDay == null) {
                                legalOverNoticeDay = 3L;
                            }
                            Date afterDate2 = DateUtil.addDays(nowDate, (int) (legalDueNoticeDay + 0));
                            Date beforeDate2 = DateUtil.subDays(nowDate, (int) (legalOverNoticeDay + 0));
                            List<CustomerAllResponse> customerAllResponseList= customerPublicService.getLegalDueAndOver(
                                    DateUtils.DateToStr(afterDate2, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate2, "yyyy-MM-dd"), overNoticeMap.get("legalOverConfigEnabled"));
                            for (CustomerAllResponse customerAllResponse : customerAllResponseList){
                                if (null != customerAllResponse.getLegalName() && null != customerAllResponse.getLegalTelephone()){
                                    try {
                                        noticeSmsService.sendMessage(customerAllResponse.getLegalTelephone(),noticeMessage);
                                        log.info("短信发送成功！"+customerAllResponse);
                                    }catch (Exception e){
                                        log.warn("短信发送失败！"+customerAllResponse);
                                    }
                                }else {
                                    log.warn("法定代表人或负责人信息不完整（姓名或联系方式）:"+customerAllResponse);
                                }
                            }
                        }

                        //3.3、证明文件提醒
                        if(map.get("fileDueConfigEnabled")) {
                            Long fileDueNoticeDay = configService.findOneByKey("fileMsgDueNoticeDay");
                            Long fileOverNoticeDay = configService.findOneByKey("fileMsgOverNoticeDay");
                            if(fileDueNoticeDay == null) {
                                fileDueNoticeDay = 3L;
                            }
                            if(fileOverNoticeDay == null) {
                                fileOverNoticeDay = 3L;
                            }
                            Date afterDate3 = DateUtil.addDays(nowDate, (int) (fileDueNoticeDay + 0));
                            Date beforeDate3 = DateUtil.subDays(nowDate, (int) (fileOverNoticeDay + 0));
                            List<CustomerAllResponse> customerAllResponseList2 = customerPublicService.getFileDueAndOver(
                                    DateUtils.DateToStr(afterDate3, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate3, "yyyy-MM-dd"), overNoticeMap.get("fileOverConfigEnabled"));
                            for (CustomerAllResponse customerAllResponse : customerAllResponseList2){
                                if (null != customerAllResponse.getLegalName() && null != customerAllResponse.getLegalTelephone()){
                                    try {
                                        noticeSmsService.sendMessage(customerAllResponse.getLegalTelephone(),noticeMessage);
                                        log.info("短信发送成功！"+customerAllResponse);
                                    }catch (Exception e){
                                        log.warn("短信发送失败！"+customerAllResponse);
                                    }
                                }else {
                                    log.warn("法定代表人或负责人信息不完整（姓名或联系方式）:"+customerAllResponse);
                                }
                            }

                        }

                        //3.4、经办人证件提醒
                        if(map.get("operatorDueConfigEnabled")) {
                            Long operatorDueNoticeDay = configService.findOneByKey("operatorMsgDueNoticeDay");
                            Long operatorOverNoticeDay = configService.findOneByKey("operatorMsgOverNoticeDay");
                            if(operatorDueNoticeDay == null) {
                                operatorDueNoticeDay = 3L;
                            }
                            if(operatorOverNoticeDay == null) {
                                operatorOverNoticeDay = 3L;
                            }
                            Date afterDate4 = DateUtil.addDays(nowDate, (int) (operatorDueNoticeDay + 0));
                            Date beforeDate4 = DateUtil.subDays(nowDate, (int) (operatorOverNoticeDay + 0));
                            List<AccountPublicInfo> accountPublicLogInfoList = accountPublicLogService.getOperatorDueAndOver(
                                    DateUtils.DateToStr(afterDate4, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate4, "yyyy-MM-dd"), overNoticeMap.get("operatorOverConfigEnabled"));
                            for (AccountPublicInfo accountPublicInfo : accountPublicLogInfoList){
                                ObjectRestResponse<AllAccountPublicDTO> objectRestResponse = allAccountPublicService.getDetailsByAccountId(accountPublicInfo.getAccountId());
                                AllAccountPublicDTO accountPublicDTO = objectRestResponse.getResult();
                                if (null!=accountPublicDTO && null!=accountPublicDTO.getLegalName() && null!=accountPublicDTO.getLegalTelephone()){
                                    try {
                                        noticeSmsService.sendMessage(accountPublicDTO.getLegalTelephone(),noticeMessage);
                                        log.info("短信发送成功！"+accountPublicDTO);
                                    }catch (Exception e){
                                        log.warn("短信发送失败！"+accountPublicDTO);
                                    }
                                }else {
                                    log.warn("法定代表人或负责人信息不完整（姓名或联系方式）:"+accountPublicDTO);
                                }

                            }
                        }

                    }
                }
            }, new CronTrigger(times));
        }
    }


}
