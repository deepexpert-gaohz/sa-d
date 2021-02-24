package com.ideatech.ams.controller.notice;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dto.AccountsAllSearchInfo;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.service.AccountPublicLogService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.customer.dao.CustomerPublicDao;
import com.ideatech.ams.customer.dto.CustomerPublicInfo;
import com.ideatech.ams.customer.entity.CustomerPublic;
import com.ideatech.ams.customer.service.CustomerPublicService;
import com.ideatech.ams.kyc.service.holiday.HolidayService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.notice.service.NoticeService;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController {
    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private CustomerPublicService customerPublicService;

    @Autowired
    private AccountPublicLogService accountPublicLogService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CustomerPublicDao customerPublicDao;

    @Autowired
    private AccountsAllDao accountsAllDao;



    /**
     * 通知提醒数量显示
     * @return
     */
    @GetMapping("/count")
    public ResultDto noticeCount() throws Exception {
        HashMap<String, Long> counts = new HashMap<>();
        String organFullId = SecurityUtils.getCurrentOrgFullId();

        //显示权限控制
        Map<String, Boolean> map = noticeService.setNoticeConfigPermission();

        //超期提醒显示权限
        Map<String, Boolean> overNoticeMap = noticeService.overNoticeConfig();

        // 预约未及时处理
        Long resvrProcessDay = configService.findOneByKey("resvrProcessDay");
        if(resvrProcessDay == null) {
            resvrProcessDay = 3L;
        }

        if (resvrProcessDay != null && map.get("resvrConfigEnabled")) {
            try {
                Date dateBefore = holidayService.addWorkday(DateUtils.getNowDateShort(), (int) -resvrProcessDay);
                Long count = companyPreOpenAccountEntService.countUnprocessedCountBefore(dateBefore, organFullId);
                counts.put("resvrUnprocess", count);
            } catch (Exception e) {
                log.error("统计预约未及时接洽数量时出错", e);
            }
        }

        //临时户到期
        Long tempAcctNoticeDay = configService.findOneByKey("tempAcctNoticeDay");
        Long tempAcctOverNoticeDay = configService.findOneByKey("tempAcctOverNoticeDay");
        if(tempAcctNoticeDay == null) {
            tempAcctNoticeDay = 3L;
        }
        if(tempAcctOverNoticeDay == null) {
            tempAcctOverNoticeDay = 3L;
        }

        if (tempAcctNoticeDay != null && map.get("tempConfigEnabled")) {
            Date nowDate = DateUtil.beginOfDate(new Date());
            Date afterDate = DateUtil.addDays(nowDate, (int) (tempAcctNoticeDay + 0));
            Date beforeDate = DateUtil.subDays(nowDate, (int) (tempAcctOverNoticeDay + 0));

            Long count = accountsAllService.countTempAcctBefore(DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, overNoticeMap.get("tempAcctOverConfigEnabled"));
            counts.put("tempAcct", count);
        }

        //证明文件到期提醒
        Long fileDueNoticeDay = configService.findOneByKey("fileDueNoticeDay");
        Long fileOverNoticeDay = configService.findOneByKey("fileOverNoticeDay");
        if(fileDueNoticeDay == null) {
            fileDueNoticeDay = 3L;
        }
        if(fileOverNoticeDay == null) {
            fileOverNoticeDay = 3L;
        }

        if (fileDueNoticeDay != null && map.get("fileDueConfigEnabled")) {
            Date nowDate = DateUtil.beginOfDate(new Date());
            Date afterDate = DateUtil.addDays(nowDate, (int) (fileDueNoticeDay + 0));
            Date beforeDate = DateUtil.subDays(nowDate, (int) (fileOverNoticeDay + 0));

            Long count = customerPublicService.countFileDueBefore(DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, overNoticeMap.get("fileOverConfigEnabled"));
            counts.put("fileDueNoticeDay", count);
        }

        //法人证件到期提醒
        Long legalDueNoticeDay = configService.findOneByKey("legalDueNoticeDay");
        Long legalOverNoticeDay = configService.findOneByKey("legalOverNoticeDay");
        if(legalDueNoticeDay == null) {
            legalDueNoticeDay = 3L;
        }
        if(legalOverNoticeDay == null) {
            legalOverNoticeDay = 3L;
        }

        if (legalDueNoticeDay != null && map.get("legalDueConfigEnabled")) {
            Date nowDate = DateUtil.beginOfDate(new Date());
            Date afterDate = DateUtil.addDays(nowDate, (int) (legalDueNoticeDay + 0));
            Date beforeDate = DateUtil.subDays(nowDate, (int) (legalOverNoticeDay + 0));
            Long count = customerPublicService.countLegalIdcardDueBefore(DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, overNoticeMap.get("legalOverConfigEnabled"));
            counts.put("legalDueNoticeDay", count);
        }

        //经办人证件到期提醒
        Long operatorDueNoticeDay = configService.findOneByKey("operatorDueNoticeDay");
        Long operatorOverNoticeDay = configService.findOneByKey("operatorOverNoticeDay");
        if(operatorDueNoticeDay == null) {
            operatorDueNoticeDay = 3L;
        }
        if(operatorOverNoticeDay == null) {
            operatorOverNoticeDay = 3L;
        }

        if (operatorDueNoticeDay != null && map.get("operatorDueConfigEnabled")) {
            Date nowDate = DateUtil.beginOfDate(new Date());
            Date afterDate = DateUtil.addDays(nowDate, (int) (operatorDueNoticeDay + 0));
            Date beforeDate = DateUtil.subDays(nowDate, (int) (operatorOverNoticeDay + 0));

            Long count = accountPublicLogService.countOperatorIdcardDueBefore(DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, overNoticeMap.get("operatorOverConfigEnabled"));
            counts.put("operatorDueNoticeDay", count);
        }

        return ResultDtoFactory.toAckData(counts);
    }

    /**
     * 通知管理显示权限配置
     * @return
     */
    @GetMapping("/permission")
    public ResultDto noticePermission() {
        HashMap<String, Boolean> map = new HashMap<>();

        //默认全部开启
        List<ConfigDto> resvrConfig = configService.findByKey("resvrConfigEnabled");
        if (CollectionUtils.isNotEmpty(resvrConfig)) {
            map.put("resvrConfigEnabled", Boolean.valueOf(resvrConfig.get(0).getConfigValue()));
        } else {
            map.put("resvrConfigEnabled", true);
        }

        List<ConfigDto> tempConfig = configService.findByKey("tempConfigEnabled");
        if (CollectionUtils.isNotEmpty(tempConfig)) {
            map.put("tempConfigEnabled", Boolean.valueOf(tempConfig.get(0).getConfigValue()));
        } else {
            map.put("tempConfigEnabled", true);
        }

        List<ConfigDto> fileDueConfig = configService.findByKey("fileDueConfigEnabled");
        if (CollectionUtils.isNotEmpty(fileDueConfig)) {
            map.put("fileDueConfigEnabled", Boolean.valueOf(fileDueConfig.get(0).getConfigValue()));
        } else {
            map.put("fileDueConfigEnabled", true);
        }

        List<ConfigDto> legalDueConfig = configService.findByKey("legalDueConfigEnabled");
        if (CollectionUtils.isNotEmpty(legalDueConfig)) {
            map.put("legalDueConfigEnabled", Boolean.valueOf(legalDueConfig.get(0).getConfigValue()));
        } else {
            map.put("legalDueConfigEnabled", true);
        }

        List<ConfigDto> operatorDueConfig = configService.findByKey("operatorDueConfigEnabled");
        if (CollectionUtils.isNotEmpty(operatorDueConfig)) {
            map.put("operatorDueConfigEnabled", Boolean.valueOf(operatorDueConfig.get(0).getConfigValue()));
        } else {
            map.put("operatorDueConfigEnabled", true);
        }

        return ResultDtoFactory.toAckData(map);
    }

    @GetMapping("/list")
    public ResultDto noticeList(CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto , CustomerPublicInfo customerPublicInfo, AccountsAllSearchInfo accountsAllInfo,
                                String operatorIdcardDue, Boolean isOperatorIdcardDue, @RequestParam(required = true) String noticeType, PagingDto pagingDto) throws Exception {
        String organFullId = SecurityUtils.getCurrentOrgFullId();

        switch (noticeType) {
            //临时户
            case "tempAcct": {
                if(isOperatorIdcardDue!=null){
                        accountsAllInfo.setIsEffectiveDateOver(isOperatorIdcardDue);
                }
                if(operatorIdcardDue!=null){
                    accountsAllInfo.setEffectiveDate(operatorIdcardDue);
                }
                Long tempAcctNoticeDay = configService.findOneByKey("tempAcctNoticeDay");
                Long tempAcctOverNoticeDay = configService.findOneByKey("tempAcctOverNoticeDay");
                if(tempAcctNoticeDay == null) {
                    tempAcctNoticeDay = 3L;
                }
                if(tempAcctOverNoticeDay == null) {
                    tempAcctOverNoticeDay = 3L;
                }

                if (tempAcctNoticeDay != null) {
                    Date nowDate = DateUtil.beginOfDate(new Date());
                    Date afterDate = DateUtil.addDays(nowDate, (int) (tempAcctNoticeDay + 0));
                    Date beforeDate = DateUtil.subDays(nowDate, (int) (tempAcctOverNoticeDay + 0));
                    accountsAllInfo.setOrganFullId(organFullId);

                    return ResultDtoFactory.toAckData(
                            accountsAllService.listTempAcctBefore(accountsAllInfo, DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), pagingDto));
                }
                break;
            }
            //法人证件
            case "legalDueNotice": {
                Long legalDueNoticeDay = configService.findOneByKey("legalDueNoticeDay");
                Long legalOverNoticeDay = configService.findOneByKey("legalOverNoticeDay");
                if(legalDueNoticeDay == null) {
                    legalDueNoticeDay = 3L;
                }
                if(legalOverNoticeDay == null) {
                    legalOverNoticeDay = 3L;
                }

                if (legalDueNoticeDay != null) {
                    Date nowDate = DateUtil.beginOfDate(new Date());
                    Date afterDate = DateUtil.addDays(nowDate, (int) (legalDueNoticeDay + 0));
                    Date beforeDate = DateUtil.subDays(nowDate, (int) (legalOverNoticeDay + 0));

                    return ResultDtoFactory.toAckData(
                            customerPublicService.listCustDueBefore("legalDueNotice", customerPublicInfo, DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, pagingDto));
                }
                break;
            }
            //证明文件
            case "fileDueNotice": {
                Long fileDueNoticeDay = configService.findOneByKey("fileDueNoticeDay");
                Long fileOverNoticeDay = configService.findOneByKey("fileOverNoticeDay");
                if(fileDueNoticeDay == null) {
                    fileDueNoticeDay = 3L;
                }
                if(fileOverNoticeDay == null) {
                    fileOverNoticeDay = 3L;
                }

                if (fileDueNoticeDay != null) {
                    Date nowDate = DateUtil.beginOfDate(new Date());
                    Date afterDate = DateUtil.addDays(nowDate, (int) (fileDueNoticeDay + 0));
                    Date beforeDate = DateUtil.subDays(nowDate, (int) (fileOverNoticeDay + 0));

                    return ResultDtoFactory.toAckData(
                            customerPublicService.listCustDueBefore("fileDueNotice", customerPublicInfo, DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, pagingDto));
                }
                break;
            }

            case "operatorDueNotice": {
                //经办人证件到期提醒
                Long operatorDueNoticeDay = configService.findOneByKey("operatorDueNoticeDay");
                Long operatorOverNoticeDay = configService.findOneByKey("operatorOverNoticeDay");
                if(operatorDueNoticeDay == null) {
                    operatorDueNoticeDay = 3L;
                }
                if(operatorOverNoticeDay == null) {
                    operatorOverNoticeDay = 3L;
                }

                if (operatorDueNoticeDay != null) {
                    Date nowDate = DateUtil.beginOfDate(new Date());
                    Date afterDate = DateUtil.addDays(nowDate, (int) (operatorDueNoticeDay + 0));
                    Date beforeDate = DateUtil.subDays(nowDate, (int) (operatorOverNoticeDay + 0));

                    return ResultDtoFactory.toAckData(
                            accountPublicLogService.listOperatorIdcardDueBefore(isOperatorIdcardDue, operatorIdcardDue, DateUtils.DateToStr(afterDate, "yyyy-MM-dd"),
                                    DateUtils.DateToStr(beforeDate, "yyyy-MM-dd"), organFullId, pagingDto));
                }
                break;
            }

            case "resvrUnprocess": {
                // 预约未及时处理
                Long resvrProcessDay = configService.findOneByKey("resvrProcessDay");
                if(resvrProcessDay == null) {
                    resvrProcessDay = 3L;
                }

                if (resvrProcessDay != null) {
                    try {
                        Date dateBefore = holidayService.addWorkday(DateUtils.getNowDateShort(), (int) -resvrProcessDay);
                        companyPreOpenAccountEntDto.setStatus(ApplyEnum.UnComplete.getValue());//未处理
                        companyPreOpenAccountEntDto.setCreatedDate(dateBefore);//过期
                        return ResultDtoFactory.toAckData(
                                companyPreOpenAccountEntService.listUnprocessedCountBefore(companyPreOpenAccountEntDto,dateBefore,
                                        organFullId,
                                        pagingDto));
                    } catch (Exception e) {
                        log.error("统计预约未及时接洽数量时出错", e);
                    }
                }

                break;
            }
            default:
                break;
        }
        return ResultDtoFactory.toNack("数据不存在");
    }

    /**
     * 超期提醒是否显示配置
     * @return
     */
    @GetMapping("/overNoticeConfig")
    public ResultDto overNoticeConfig() {
        Map<String, Boolean> map = noticeService.overNoticeConfig();

        return ResultDtoFactory.toAckData(map);
    }


    /**
     * 2021-1-6
     * 修改法人证件日期，证明文件到期日期
     */
    @GetMapping("/{id}")
    public ResultDto<CustomerPublic> findById(@PathVariable("id") Long id) {
        CustomerPublic customerPublic = customerPublicDao.findOne(id);
        return ResultDtoFactory.toAckData(customerPublic);
    }


    /**
     * 2021-1-6
     * 修改法人证件日期,证明文件到期日期
     */
    @PostMapping("/{id}")
    public ResultDto update(@PathVariable("id") Long id, CustomerPublic customerPublic) {
        customerPublicDao.updateLegalIdcardDue(id,customerPublic.getLegalIdcardDue(),customerPublic.getFileDue());
        return ResultDtoFactory.toAck();
    }


    /**
     * 2021-1-6
     * 修改临时户到期日期
     */
    @GetMapping("/temAcctId/{id}")
    public ResultDto<AccountsAll> findByIdForTemporary(@PathVariable("id") Long id) {
        AccountsAll accountsall = accountsAllDao.findOne(id);
        return ResultDtoFactory.toAckData(accountsall);
    }


    /**
     * 2021-1-6
     * 修改临时户到期日期
     */
    @PostMapping("/temAcctId/{id}")
    public ResultDto updateForTemporary(@PathVariable("id") Long id, AccountsAll accountsAll) {
        accountsAllDao.updateTempAcct(id,accountsAll.getEffectiveDate());
        return ResultDtoFactory.toAck();
    }

}
