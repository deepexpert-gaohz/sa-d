package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.mivs.dto.*;
import com.ideatech.ams.mivs.dto.acd.AnnouncementInformationConfirmDto;
import com.ideatech.ams.mivs.dto.bqd.BusinessAcceptTimeQueryDto;
import com.ideatech.ams.mivs.dto.bqrd.BusinessAcceptTimeQueryReplyDto;
import com.ideatech.ams.mivs.dto.bqrd.ServiceInformationDto;
import com.ideatech.ams.mivs.dto.mad.MobilePhoneNumberApplyDto;
import com.ideatech.ams.mivs.dto.mrd.MobilePhoneNumberReplyDto;
import com.ideatech.ams.mivs.dto.orfd.OpenRevokeFeedbackDto;
import com.ideatech.ams.mivs.dto.rad.RegisterInformationApplyDto;
import com.ideatech.ams.mivs.dto.rrd.*;
import com.ideatech.ams.mivs.dto.tad.TaxInformationApplyDto;
import com.ideatech.ams.mivs.dto.trd.TaxInformationReplyDto;
import com.ideatech.ams.mivs.dto.trd.TaxPaymentInformationDto;
import com.ideatech.ams.mivs.service.*;
import com.ideatech.common.converter.ConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jzh
 * @date 2019/7/17.
 */

@Slf4j
public class DefaultSendMsgServiceImpl implements SendMsgService {
    @Override
    public ReplyMsgDto sendMobilePhoneNumberApplyMsg(MobilePhoneNumberApplyDto mobilePhoneNumberDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendTaxInformationApplyMsg(TaxInformationApplyDto taxInformationApplyDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendBusinessAcceptTimeQueryMsg(BusinessAcceptTimeQueryDto businessAcceptTimeQueryDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendRegisterInformationApplyMsg(RegisterInformationApplyDto registerInformationApplyDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendOpenRevokeFeedbackMsg(OpenRevokeFeedbackDto openRevokeFeedbackDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendMobilePhoneNumberFeedbackMsg(CommonFeedbackDto commonFeedbackDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendTaxInformationFeedbackMsg(CommonFeedbackDto commonFeedbackDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendRegisterInformationbackMsg(CommonFeedbackDto commonFeedbackDto) {
        return null;
    }

    @Override
    public ReplyMsgDto sendAnnouncementInformationConfirmMsg(AnnouncementInformationConfirmDto confirmDto) {
        return null;
    }


//    @Autowired
//    private TaxInformationLogService taxInformationLogService;
//
//    @Autowired
//    private TaxPaymentInformationService taxPaymentInformationService;
//
//    @Autowired
//    private BusinessAcceptTimeLogService businessAcceptTimeLogService;
//
//    @Autowired
//    private ServiceInformationService serviceInformationService;
//
//    @Autowired
//    private RegisterInformationLogService registerInformationLogService;
//
//    @Autowired
//    private AbnormalBusinessInformationService abnormalBusinessInformationService;
//
//    @Autowired
//    private BasicInformationOfEnterpriseService basicInformationOfEnterpriseService;
//
//    @Autowired
//    private BasicInformationOfSelfEmployedPeopleService basicInformationOfSelfEmployedPeopleService;
//
//    @Autowired
//    private ChangeInformationService changeInformationService;
//
//    @Autowired
//    private CompanyShareholdersAndFundingInformationService companyShareholdersAndFundingInformationService;
//
//    @Autowired
//    private DirectorSupervisorSeniorManagerInformationService directorSupervisorSeniorManagerInformationService;
//
//    @Autowired
//    private IllegalAndDiscreditInformationService illegalAndDiscreditInformationService;
//
//    @Autowired
//    private LicenseNullifyService licenseNullifyService;
//
//    @Autowired
//    private CommonFeedbackLogService commonFeedbackLogService;
//
//    @Autowired
//    private OpenRevokeFeedbackLogService openRevokeFeedbackLogService;
//
//    @Autowired
//    private AnnouncementInformationConfirmLogService informationConfirmLogService;
//
////    @Override
////    public ReplyMsgDto sendMobilePhoneNumberApplyMsg(MobilePhoneNumberApplyDto mobilePhoneNumberApplyDto) {
////        log.info(mobilePhoneNumberApplyDto.getId().toString());
////
////        //模拟报文处理时间
////        try {
////            Thread.sleep(3000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////
////        //组装报文
////
////        //校验报文
////
////        //发送报文
////
////        //解析返回结果(
////        // MobilePhoneNumberReplyDto 成功
////        // 或DiscardNoticeDto 丢弃
////        // 或OperationalErrorDto 失败
////        // )
////
////        //模拟3秒内无法获取应答报文，直接返回DefaultMsgDto，并异步获取应答报文。msgCode默认返回000001
////        if (new Random().nextBoolean()){
////            return new DefaultMsgDto("000001");
////        }
////
////        //模拟3秒内获取应答报文
////        MobilePhoneNumberReplyDto mobilePhoneNumberReplyDto = new MobilePhoneNumberReplyDto();
////        if (new Random().nextBoolean()){//查询成功
////
////            //模拟结果
////            mobilePhoneNumberReplyDto.setMobNb("8618312345678");
////            mobilePhoneNumberReplyDto.setRslt("MCHD");
////            mobilePhoneNumberReplyDto.setMobCrr("1000");
////            mobilePhoneNumberReplyDto.setLocMobNb("010");
////            mobilePhoneNumberReplyDto.setLocNmMobNb("北京");
////            mobilePhoneNumberReplyDto.setCdTp("INDV");
////            mobilePhoneNumberReplyDto.setSts("ENBL");
////
////            //保存应答报文解析结果
////            MobilePhoneNumberLogDto mobilePhoneNumberLogDto = mobilePhoneNumberLogService.findById(mobilePhoneNumberApplyDto.getId());
////            ConverterService.convert(mobilePhoneNumberReplyDto,mobilePhoneNumberLogDto);
////            mobilePhoneNumberLogDto.setFlag(true);//获取应答报文标记。
////            mobilePhoneNumberLogService.save(mobilePhoneNumberLogDto);
////
////            return mobilePhoneNumberReplyDto;
////        }else if (new Random().nextBoolean()){//查询失败
////
////            //模拟结果
////            OperationalErrorDto operationalErrorDto = new OperationalErrorDto();
////            operationalErrorDto.setProcCd("O3045");
////            operationalErrorDto.setProcSts("PR09");
////            operationalErrorDto.setRjctInf("手机号码核查业务瞬时峰值超限");
////
////            //保存应答报文解析结果
////            MobilePhoneNumberLogDto mobilePhoneNumberLogDto = mobilePhoneNumberLogService.findById(mobilePhoneNumberApplyDto.getId());
////            ConverterService.convert(operationalErrorDto,mobilePhoneNumberLogDto);
////            mobilePhoneNumberLogDto.setFlag(true);//获取应答报文标记。
////            mobilePhoneNumberLogService.save(mobilePhoneNumberLogDto);
////
////            return operationalErrorDto;
////        }else {//MIVS无法解析请求报文返回报文丢弃通知报文
////            return new DiscardNoticeDto();
////        }
////
////    }
//
//    @Override
//    public ReplyMsgDto sendMobilePhoneNumberApplyMsg(MobilePhoneNumberApplyDto mobilePhoneNumberDto) {
//        return null;
//    }
//
//    @Override
//    public ReplyMsgDto sendTaxInformationApplyMsg(TaxInformationApplyDto taxInformationApplyDto) {
//
//        //模拟报文处理时间
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //组装报文
//
//        //校验报文
//
//        //发送报文
//
//        //解析返回结果(
//        // TaxInformationReplyDto 成功
//        // 或PublicProcessConfirmDto 通用处理确认 成功
//        // 或DiscardNoticeDto 丢弃
//        // 或OperationalErrorDto 失败
//        // )
//
//        //模拟3秒内无法获取应答报文，直接返回DefaultMsgDto，并异步获取应答报文。msgCode默认返回000001
//        if (new Random().nextBoolean()){
//            return new DefaultMsgDto("000001");
//        }
//
//        //模拟3秒内获取应答报文
//        TaxInformationReplyDto taxInformationReplyDto = new TaxInformationReplyDto();
//        if (new Random().nextBoolean()){
//
//            //模拟数据
//            taxInformationReplyDto.setRslt("MCHD");
//            taxInformationReplyDto.setDataResrcDt("2019-02-14");
//            List<TaxPaymentInformationDto> taxPaymentInformationDtoList = new ArrayList<>();
//            for (int i = 0;i<5;i++){
//                TaxPaymentInformationDto taxPaymentInformationDto = new TaxPaymentInformationDto();
//                taxPaymentInformationDto.setTxAuthCd("4651345313"+i);
//                taxPaymentInformationDto.setTxAuthNm("税务机关名称"+i);
//                taxPaymentInformationDto.setTxpyrSts("OPEN");
//                taxPaymentInformationDtoList.add(taxPaymentInformationDto);
//                if (new Random().nextBoolean()){
//                    break;
//                }
//                taxPaymentInformationDto.setTaxInformationLogId(taxInformationApplyDto.getId());
//                taxPaymentInformationService.save(taxPaymentInformationDto);
//            }
//            taxInformationReplyDto.setTxpmtInf(taxPaymentInformationDtoList);
//
//            //保存应答报文解析结果
//            TaxInformationLogDto taxInformationLogDto = taxInformationLogService.findById(taxInformationApplyDto.getId());
//            ConverterService.convert(taxInformationReplyDto,taxInformationLogDto);
//            taxInformationLogDto.setFlag(true);//获取应答报文标记。
//            taxInformationLogService.save(taxInformationLogDto);
//
//        }else if (new Random().nextBoolean()){//查询失败
//
//            //模拟数据
//            OperationalErrorDto operationalErrorDto = new OperationalErrorDto();
//            operationalErrorDto.setProcCd("O3047");
//            operationalErrorDto.setProcSts("PR09");
//            operationalErrorDto.setRjctInf("核查业务受理状态未开启");
//
//            //保存应答报文解析结果
//            TaxInformationLogDto taxInformationLogDto = taxInformationLogService.findById(taxInformationApplyDto.getId());
//            ConverterService.convert(operationalErrorDto,taxInformationLogDto);
//            taxInformationLogDto.setFlag(true);//获取应答报文标记。
//            taxInformationLogService.save(taxInformationLogDto);
//
//            return operationalErrorDto;
//        }else{
//            return new DiscardNoticeDto();
//        }
//
//        return taxInformationReplyDto;
//    }
//
//    @Override
//    public ReplyMsgDto sendBusinessAcceptTimeQueryMsg(BusinessAcceptTimeQueryDto businessAcceptTimeQueryDto) {
//
//        //模拟报文处理时间
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //组装报文
//
//        //校验报文
//
//        //发送报文
//
//        //解析返回结果(
//        // BusinessAcceptTimeQueryReplyDto 成功
//        // 或DiscardNoticeDto 丢弃
//        // 或OperationalErrorDto 失败
//        // )
//        BusinessAcceptTimeQueryReplyDto businessAcceptTimeQueryReplyDto = new BusinessAcceptTimeQueryReplyDto();
//
//        if (new Random().nextBoolean()){
//
//            //模拟数据
//            businessAcceptTimeQueryReplyDto.setOrgnlQueDt("2019-02-14");
//            businessAcceptTimeQueryReplyDto.setProcSts("PR07");
//            ServiceInformationDto svcInf = new ServiceInformationDto();
//            svcInf.setSysInd("SAMR");
//            svcInf.setSvcInd("ENBL");
//            svcInf.setSysClTm("2019-02-14T14:08:07");
//            svcInf.setSysOpTm("2019-02-14T14:08:07");
//            svcInf.setBusinessAcceptTimeLogId(businessAcceptTimeQueryDto.getId());
//            businessAcceptTimeQueryReplyDto.setSvcInf(svcInf);
//
//            //保存应答报文解析结果
//            serviceInformationService.save(svcInf);
//            BusinessAcceptTimeLogDto businessAcceptTimeLogDto = businessAcceptTimeLogService.findById(businessAcceptTimeQueryDto.getId());
//            ConverterService.convert(businessAcceptTimeQueryReplyDto,businessAcceptTimeLogDto);
//            businessAcceptTimeLogDto.setFlag(true);//获取应答报文标记。
//            businessAcceptTimeLogService.save(businessAcceptTimeLogDto);
//
//        }else if (new Random().nextBoolean()){//查询失败
//            //模拟数据
//            OperationalErrorDto operationalErrorDto = new OperationalErrorDto();
//            operationalErrorDto.setProcCd("12345678");
//            operationalErrorDto.setProcSts("PR09");
//            operationalErrorDto.setRjctInf("当申请报文处理状态为PR09已拒绝时填写");
//
//            //保存应答报文解析结果
//            BusinessAcceptTimeLogDto businessAcceptTimeLogDto = businessAcceptTimeLogService.findById(businessAcceptTimeQueryDto.getId());
//            ConverterService.convert(operationalErrorDto,businessAcceptTimeLogDto);
//            businessAcceptTimeLogDto.setFlag(true);//获取应答报文标记。
//            businessAcceptTimeLogService.save(businessAcceptTimeLogDto);
//
//            return operationalErrorDto;
//        }else {
//            return new DiscardNoticeDto();
//        }
//
//        return businessAcceptTimeQueryReplyDto;
//    }
//
//    @Override
//    public ReplyMsgDto sendRegisterInformationApplyMsg(RegisterInformationApplyDto registerInformationApplyDto) {
//
//        //组装报文
//
//        //校验报文
//
//        //发送报文
//
//        //解析返回结果(
//        // RegisterInformationReplyDto 成功
//        // 或DiscardNoticeDto 丢弃
//        // 或OperationalErrorDto 失败
//        // )
//        RegisterInformationReplyDto registerInformationReplyDto = new RegisterInformationReplyDto();
//        if (new Random().nextBoolean()){
//
//            //模拟数据
//            OperationalErrorDto operationalErrorDto = new OperationalErrorDto();
//            operationalErrorDto.setProcCd("O3047");
//            operationalErrorDto.setProcSts("PR09");
//            operationalErrorDto.setRjctInf("核查业务受理状态未开启");
//
//            //保存应答报文解析结果
//            RegisterInformationLogDto registerInformationLogDto = registerInformationLogService.findById(registerInformationApplyDto.getId());
//            ConverterService.convert(operationalErrorDto,registerInformationLogDto);
//            registerInformationLogDto.setFlag(true);//获取应答报文标记。
//            registerInformationLogService.save(registerInformationLogDto);
//
//            return operationalErrorDto;
//        }else if(new Random().nextBoolean()){
//            registerInformationReplyDto.setRslt("MCHD");
//            registerInformationReplyDto.setDataResrcDt("2019-02-14");
//            if (new Random().nextBoolean()){
//                BasicInformationOfEnterpriseDto basicInformationOfEnterpriseDto = new BasicInformationOfEnterpriseDto();
//                basicInformationOfEnterpriseDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//                registerInformationReplyDto.setBasicInformationOfEnterpriseDto(basicInformationOfEnterpriseDto);
//                basicInformationOfEnterpriseService.save(basicInformationOfEnterpriseDto);
//            }else {
//                BasicInformationOfSelfEmployedPeopleDto basicInformationOfSelfEmployedPeopleDto = new BasicInformationOfSelfEmployedPeopleDto();
//                basicInformationOfSelfEmployedPeopleDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//                registerInformationReplyDto.setBasicInformationOfSelfEmployedPeopleDto(basicInformationOfSelfEmployedPeopleDto);
//                basicInformationOfSelfEmployedPeopleService.save(basicInformationOfSelfEmployedPeopleDto);
//            }
//
//            List<CompanyShareholdersAndFundingInformationDto> companyShareholdersAndFundingInformationDtoList = new ArrayList<>();
//            CompanyShareholdersAndFundingInformationDto companyShareholdersAndFundingInformationDto = new CompanyShareholdersAndFundingInformationDto();
//            companyShareholdersAndFundingInformationDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//            companyShareholdersAndFundingInformationDtoList.add(companyShareholdersAndFundingInformationDto);
//            companyShareholdersAndFundingInformationService.save(companyShareholdersAndFundingInformationDto);
//            registerInformationReplyDto.setCompanyShareholdersAndFundingInformationDtoList(companyShareholdersAndFundingInformationDtoList);
//
//            List<DirectorSupervisorSeniorManagerInformationDto> directorSupervisorSeniorManagerInformationDtoList = new ArrayList<>();
//            DirectorSupervisorSeniorManagerInformationDto directorSupervisorSeniorManagerInformationDto = new DirectorSupervisorSeniorManagerInformationDto();
//            directorSupervisorSeniorManagerInformationDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//            directorSupervisorSeniorManagerInformationDtoList.add(directorSupervisorSeniorManagerInformationDto);
//            directorSupervisorSeniorManagerInformationService.save(directorSupervisorSeniorManagerInformationDto);
//            registerInformationReplyDto.setDirectorSupervisorSeniorManagerInformationDtoList(directorSupervisorSeniorManagerInformationDtoList);
//
//            List<ChangeInformationDto> changeInformationDtoList = new ArrayList<>();
//            ChangeInformationDto changeInformationDto = new ChangeInformationDto();
//            changeInformationDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//            changeInformationDtoList.add(changeInformationDto);
//            changeInformationService.save(changeInformationDto);
//            registerInformationReplyDto.setChangeInformationDtoList(changeInformationDtoList);
//
//            List<AbnormalBusinessInformationDto> abnormalBusinessInformationDtoList = new ArrayList<>();
//            AbnormalBusinessInformationDto abnormalBusinessInformationDto = new AbnormalBusinessInformationDto();
//            abnormalBusinessInformationDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//            abnormalBusinessInformationDtoList.add(abnormalBusinessInformationDto);
//            abnormalBusinessInformationService.save(abnormalBusinessInformationDto);
//            registerInformationReplyDto.setAbnormalBusinessInformationDtoList(abnormalBusinessInformationDtoList);
//
//            List<IllegalAndDiscreditInformationDto> illegalAndDiscreditInformationDtoList = new ArrayList<>();
//            IllegalAndDiscreditInformationDto illegalAndDiscreditInformationDto = new IllegalAndDiscreditInformationDto();
//            illegalAndDiscreditInformationDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//            illegalAndDiscreditInformationDtoList.add(illegalAndDiscreditInformationDto);
//            illegalAndDiscreditInformationService.save(illegalAndDiscreditInformationDto);
//            registerInformationReplyDto.setIllegalAndDiscreditInformationDtoList(illegalAndDiscreditInformationDtoList);
//
//            List<LicenseNullifyDto> licenseNullifyDtoList = new ArrayList<>();
//            LicenseNullifyDto licenseNullifyDto = new LicenseNullifyDto();
//            licenseNullifyDto.setRegisterInformationLogId(registerInformationApplyDto.getId());
//            licenseNullifyDtoList.add(licenseNullifyDto);
//            licenseNullifyService.save(licenseNullifyDto);
//            registerInformationReplyDto.setLicenseNullifyDtoList(licenseNullifyDtoList);
//
//            //保存应答报文解析结果
//            RegisterInformationLogDto registerInformationLogDto = registerInformationLogService.findById(registerInformationApplyDto.getId());
//            ConverterService.convert(registerInformationReplyDto,registerInformationLogDto);
//            registerInformationLogDto.setFlag(true);//获取应答报文标记。
//            registerInformationLogService.save(registerInformationLogDto);
//
//
//        }else if(new Random().nextBoolean()){
//            registerInformationReplyDto.setRslt("WLPI");
//            registerInformationReplyDto.setDataResrcDt("2019-02-14");
//
//            //保存应答报文解析结果
//            RegisterInformationLogDto registerInformationLogDto = registerInformationLogService.findById(registerInformationApplyDto.getId());
//            ConverterService.convert(registerInformationReplyDto,registerInformationLogDto);
//            registerInformationLogDto.setFlag(true);//获取应答报文标记。
//            registerInformationLogService.save(registerInformationLogDto);
//        }else {
//            return new DiscardNoticeDto();//丢弃报文
//        }
//
//        return registerInformationReplyDto;
//    }
//
//    @Override
//    public ReplyMsgDto sendOpenRevokeFeedbackMsg(OpenRevokeFeedbackDto openRevokeFeedbackDto) {
//        //组装报文
//
//        //校验报文
//
//        //发送报文
//
//        //解析返回结果(
//        // 或PublicProcessConfirmDto 通用处理确认
//        // 或DiscardNoticeDto 丢弃
//        // )
//        if(new Random().nextBoolean()){
//            PublicProcessConfirmDto publicProcessConfirmDto = new PublicProcessConfirmDto();
//            publicProcessConfirmDto.setPrcSts("PR09");
//
//            OpenRevokeFeedbackLogDto openRevokeFeedbackLogDto = openRevokeFeedbackLogService.findById(openRevokeFeedbackDto.getId());
//            openRevokeFeedbackLogDto.setProcSts("PR09");
//            openRevokeFeedbackLogDto.setFlag(true);
//            openRevokeFeedbackLogService.save(openRevokeFeedbackLogDto);
//            return publicProcessConfirmDto;
//        }else {
//            return new DiscardNoticeDto();
//        }
//
//    }
//
//    @Override
//    public ReplyMsgDto sendMobilePhoneNumberFeedbackMsg(CommonFeedbackDto commonFeedbackDto) {
//        CommonFeedbackLogDto commonFeedbackLogDto = commonFeedbackLogService.findById(commonFeedbackDto.getId());
//        commonFeedbackLogDto.setFlag(true);
//        commonFeedbackLogDto.setProcSts("PR07");
//        commonFeedbackLogService.save(commonFeedbackLogDto);
//        return new DefaultMsgDto("000001");
//    }
//
//    @Override
//    public ReplyMsgDto sendTaxInformationFeedbackMsg(CommonFeedbackDto commonFeedbackDto) {
//        return new DefaultMsgDto("000001");
//    }
//
//    @Override
//    public ReplyMsgDto sendRegisterInformationbackMsg(CommonFeedbackDto commonFeedbackDto) {
//        return new DefaultMsgDto("000001");
//    }
//
//    @Override
//    public ReplyMsgDto sendAnnouncementInformationConfirmMsg(AnnouncementInformationConfirmDto confirmDto) {
//        if(new Random().nextBoolean()){
//            //正常发送。
//            AnnouncementInformationConfirmLogDto confirmLogDto = informationConfirmLogService.findById(confirmDto.getId());
//            //设置发送成功。
//            confirmLogDto.setFlag(true);
//            informationConfirmLogService.save(confirmLogDto);
//
//            //返回成功编码
//            return new DefaultMsgDto("000002");
//        }else {
//            //发送失败。
//            return new DefaultMsgDto("000003");
//        }
//    }
}
