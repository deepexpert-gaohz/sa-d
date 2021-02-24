package com.ideatech.ams.ws.api.config;

import com.ideatech.ams.compare.service.CompareDataConvertService;
import com.ideatech.ams.compare.service.CustomerSmsService;
import com.ideatech.ams.image.service.FaceRecognitionService;
import com.ideatech.ams.image.service.ImageSmsService;
import com.ideatech.ams.mivs.service.QueryMsgService;
import com.ideatech.ams.mivs.service.ReceiveMsgService;
import com.ideatech.ams.mivs.service.SendMsgService;
import com.ideatech.ams.system.notice.service.NoticeSmsService;
import com.ideatech.ams.ws.api.service.CoreJibenValidateApiService;
import com.ideatech.ams.apply.service.SmsService;
import com.ideatech.ams.customer.service.CustomerNoGenerateService;
import com.ideatech.ams.system.user.service.UserSendService;
import com.ideatech.ams.system.user.service.UserSendServiceImp;
import com.ideatech.ams.ws.api.service.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author van
 * @date 16:24 2018/5/30
 */
@Configuration
public class WsApiConfig {

    @Bean
    @ConditionalOnMissingBean(name = "amsHzPushService")
    public AmsHzPushService amsHzPushService() {
        return new DefaultAmsHzPushServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsService")
    public SmsService smsService() {
        return new DefaultSmsServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean(name = "customerNoGenerateService")
    public CustomerNoGenerateService customerNoGenerateService() {
        return new DefaultCustomerNoGenerateService();
    }

    @Bean
    @ConditionalOnMissingBean(name = "blacklistValidationService")
    public BlacklistValidationService blacklistValidationService() {
        return new DefaultBlacklistValidationServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "ssoValidationService")
    public SsoValidationService ssoValidationService() {
        return new DefaultSsoValiadtionService();
    }


    @Bean
    @ConditionalOnMissingBean(name = "accountImageApiService")
    public AccountImageApiService accountImageApiService() {
        return new AccountImageApiServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageClearingTypeApiService")
    public ImageClearingTypeApiService imageClearingTypeApiService() {
        return new ImageClearingTypeApiServiceImpl();
    }


    @Bean
    @ConditionalOnMissingBean(name = "annualResultApiService")
    public AnnualResultApiService annualResultApiService() {
        return new AnnualResultApiServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "applyApiService")
    public ApplyApiService applyApiService() {
        return new ApplyApiServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "annualResultNoticeApiService")
    public AnnualResultNoticeApiService annualResultNoticeApiService() {
        return new AnnualResultNoticeApiServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "userSendService")
    public UserSendService userSendService() {
        return new UserSendServiceImp();
    }

    @Bean
    @ConditionalOnMissingBean(name = "coreJibenValidateApiService")
    public CoreJibenValidateApiService coreJibenValidateApiService() {
        return new CoreJibenValidateApiServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "noticeSmsService")
    public NoticeSmsService noticeSmsService(){
        return new DefaultNoticeSmsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "customerSmsService")
    public CustomerSmsService customerSmsService(){
        return new DefaultCustomerSmsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "sendMsgService")
    public SendMsgService sendMsgService(){
        return new DefaultSendMsgServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "receiveMsgService")
    public ReceiveMsgService receiveMsgService(){
        return new DefaultReceiveMsgServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "queryMsgService")
    public QueryMsgService queryMsgService(){
        return new DefaultQueryMsgServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "compareDataConvertService")
    public CompareDataConvertService compareDataConvertService(){
        return new DefaultCompareDataConvertServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageAllSyncService")
    public ImageAllSyncService imageAllSyncService(){
        return new ImageAllSyncServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageVideoSyncService")
    public ImageVideoSyncService imageVideoSyncService(){
        return new ImageVideoSyncServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageSmsService")
    public ImageSmsService imageSmsService(){
        return new DefaultImageSmsServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "faceRecognitionService")
    public FaceRecognitionService faceRecognitionService(){
        return new DefaultFaceRecognitionServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageRecordApiService")
    public ImageRecordApiService imageRecordApiService(){
        return new DefaultImageRecordApiServiceImpl();
    }
}
