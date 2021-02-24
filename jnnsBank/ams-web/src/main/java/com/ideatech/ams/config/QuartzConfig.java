package com.ideatech.ams.config;

import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.core.CompanyImportAccess;
import com.ideatech.ams.annual.dto.AnnualTaskDto;
import com.ideatech.ams.annual.enums.CollectType;
import com.ideatech.ams.annual.enums.TaskStatusEnum;
import com.ideatech.ams.annual.service.AnnualStatisticsService;
import com.ideatech.ams.annual.service.AnnualTaskService;
import com.ideatech.ams.annual.service.SaicCollectionService;
import com.ideatech.ams.annual.service.export.AnnualResultExportService;
import com.ideatech.ams.apply.dto.CompanyPreOpenAccountEntDto;
import com.ideatech.ams.apply.service.CompanyPreOpenAccountEntService;
import com.ideatech.ams.apply.service.SynchronizeOrgService;
import com.ideatech.ams.compare.enums.CompareState;
import com.ideatech.ams.compare.enums.TaskType;
import com.ideatech.ams.compare.service.CompareScheduledTaskService;
import com.ideatech.ams.kyc.service.idcard.IdCardComperService;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.enums.PwdModifyStatus;
import com.ideatech.ams.pbc.spi.AmsMainService;
import com.ideatech.ams.pbc.spi.EccsMainService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.org.dto.OrganizationSyncDto;
import com.ideatech.ams.system.org.service.OrganizationSyncService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.system.schedule.service.SchedulerLockService;
import com.ideatech.ams.system.sm4.service.EncryptPwdService;
import com.ideatech.common.constant.IdeaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@Slf4j
public class QuartzConfig implements SchedulingConfigurer {

    public static Boolean ANNUAL_ALL_FINISH_FLAG = Boolean.FALSE;

    @Autowired
    private CompanyPreOpenAccountEntService companyPreOpenAccountEntService;

    @Autowired
    private SynchronizeOrgService synchronizeOrgService;

    @Autowired
    private CompanyImportAccess access;

    @Autowired
    private PbcAccountService pbcAccountService;

    @Autowired
    private AmsWebProperties amsWebProperties;

    @Autowired
    private AmsMainService amsMainService;

    @Autowired
    private EccsMainService eccsMainService;

    @Autowired
    private IdCardComperService idCardComperService;

    @Autowired
    private AnnualTaskService annualTaskService;

    @Autowired
    private AnnualResultExportService annualResultExportService;

    @Autowired
    private SaicCollectionService saicCollectionService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private OrganizationSyncService organizationSyncService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private CompareScheduledTaskService compareScheduledTaskService;

    /**
     * 是否执行定时任务，集群环境中应该只有一台机器此项为true
     */
    @Value("${ams.schedule.host:true}")
    private boolean scheduleHost;

    @Value("${apply.task.scheduletiming.flag}")
    private boolean flag;

    @Value("${apply.task.scheduletiming.delaySec}")
    private long delaySec;

    @Value("${apply.task.syncScheduletiming.flag}")
    private boolean syncFlag;

    @Value("${apply.task.syncScheduletiming.delaySec}")
    private long syncDelaySec;

    @Value("${import.file.scheduletimingIsUse:true}")
    private boolean scheduleTimingIsUse;

    @Value("${import.file.scheduletiming}")
    private String scheduleTiming;

    @Value("${ams.scheduler.updateEccsPassword}")
    private String updateEccsPassword;

    @Value("${ams.scheduler.breakAppointTiming}")
    private String breakAppointTiming;

    @Value("${apply.images.scheduletimingIsUse}")
    private boolean imageScheduletimingIsUse;

    @Value("${apply.images.scheduletiming}")
    private String imageScheduletiming;

    @Value("${ams.annual.export-use}")
    private boolean annualExportFlag;

    @Value("${ams.annual.export-timing}")
    private String annualExportTiming;

    /**
     * 比对管理的定时采集是否开启
     */
    @Value("${compare.task.loop-use:true}")
    private boolean compareTaskLoopUser;

    @Value("${ams.scheduler.check-ams-password:0 0 18 2 * ?}")
    private String checkAmsPassword;

    @Value("${apply.newRule.pull.startDateTime:2019-01-01 00:00:00}")
    private String applyPullStartDateTime;

    @Value("${apply.newRule.pull.endDateTime:2099-01-01 00:00:00}")
    private String applyPullEndDateTime;

    /**
     * 预约流程(true: 新模式 false:老模式)
     */
    @Value("${apply.newRule.flag:false}")
    private Boolean applyRuleFlag;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AnnualStatisticsService annualStatisticsService;

    @Autowired
    private EncryptPwdService encryptPwdService;

    private String ip = "";

    private String breakAppointEnabled = "false";

    @Autowired
    private SchedulerLockService schedulerLockService;

    /**
     * 字符串TRUE
     */
    public static final String TRUE = "TRUE";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler());

        if (scheduleHost) {
            if (flag) {
                taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ConfigDto isRunning = new ConfigDto();
                            isRunning.setConfigKey("FALSE");//不执行任务。

                            schedulerLockService.isRunningFun(1001L,isRunning);

                            if (TRUE.equals(isRunning.getConfigKey())){
                                log.info("定时同步预约开户信息开始");
                                companyPreOpenAccountEntService.getApplyRecordByLastDubiousApplyIds();
                                if(!applyRuleFlag) {
                                    log.info("定时同步预约开户信息-----根据最大id取值");
                                    Long maxId = companyPreOpenAccountEntService.getMaxId();
                                    companyPreOpenAccountEntService.getApplyRecordByPullId(maxId);
                                } else {
                                    log.info("定时同步预约开户信息-----根据日期区间取值");
                                    companyPreOpenAccountEntService.getApplyRecordByPullTime(applyPullStartDateTime, applyPullEndDateTime, "");
                                }

                                log.info("定时同步预约开户信息结束");
                                schedulerLockService.releaseLock(1001L);//释放锁
                            }

                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("定时同步预约开户信息失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("定时同步预约开户信息失败", e);
                            schedulerLockService.releaseLock(1001L);
                        }
                    }
                }, new PeriodicTrigger(delaySec, TimeUnit.SECONDS)));
            }

            if (syncFlag) {
                taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                    @Override
                    public void run() {

                        ConfigDto isRunning = new ConfigDto();
                        isRunning.setConfigKey("FALSE");//不执行任务。

                        try {

                            schedulerLockService.isRunningFun(1002L,isRunning);

                            if (TRUE.equals(isRunning.getConfigKey())){
                                log.info("定时同步机构信息开始");
                                List<OrganizationSyncDto> syncStatusOrderByCreatedDateAsc = organizationSyncService.findBySyncFinishStatusOrderByCreatedDateAsc(false);
                                for(OrganizationSyncDto organizationSyncDto : syncStatusOrderByCreatedDateAsc){
                                    synchronizeOrgService.syncOrg(organizationSyncDto);
                                }
                                log.info("定时同步机构信息结束");
                                schedulerLockService.releaseLock(1002L);
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("定时同步机构信息失败：已有主机启动该定时任务");
                        } catch (Exception e) {
                            log.error("定时同步机构信息失败", e);
                            schedulerLockService.releaseLock(1002L);
                        }
                    }
                }, new PeriodicTrigger(syncDelaySec, TimeUnit.SECONDS)));
            }


            // 定时任务,核心数据初始化
            if (scheduleTimingIsUse) {
                taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                    @Override
                    public void run() {

                        ConfigDto isRunning = new ConfigDto();
                        isRunning.setConfigKey("FALSE");//不执行任务。

                        try {

                            schedulerLockService.isRunningFun(1003L,isRunning);

                            if (TRUE.equals(isRunning.getConfigKey())){
                                log.info("核心数据初始化执行开始。。。");
                                access.mainAccess();
                                log.info("核心数据初始化执行结束。。。");
                                schedulerLockService.releaseLock(1003L);//释放锁
                            }

                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("核心数据初始化执行失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("核心数据初始化执行失败", e);
                            schedulerLockService.releaseLock(1003L);//释放锁
                        }
                    }
                }, new CronTrigger(scheduleTiming)));
            }


            // 定时任务,拉取预约影像
            if (imageScheduletimingIsUse) {
                taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                    @Override
                    public void run() {
                        ConfigDto isRunning = new ConfigDto();
                        isRunning.setConfigKey("FALSE");//不执行任务。

                        try {

                            schedulerLockService.isRunningFun(1004L,isRunning);

                            if (TRUE.equals(isRunning.getConfigKey())){
                                log.info("拉取预约影像执行开始。。。");
                                List<CompanyPreOpenAccountEntDto> top10ByHasocr = companyPreOpenAccountEntService.findTop10ByHasocr("1");
                                Integer num = 1;
                                Integer totalNum = 1;
                                for (CompanyPreOpenAccountEntDto companyPreOpenAccountEntDto : top10ByHasocr) {
                                    num = 1;
                                    try {
                                        totalNum = companyPreOpenAccountEntService.getApplyImagesByApplyIdAndPageNum(companyPreOpenAccountEntDto.getApplyid(), num, "ezhanghu");
                                    } catch (Exception e) {
                                        log.error("拉取预约影像失败:{}", e.getMessage());
                                        continue;
                                    }
                                    if (totalNum > 1) {//总数大于1的时候继续拉取
                                        for (num++; num <= totalNum; num++) {
                                            try {
                                                companyPreOpenAccountEntService.getApplyImagesByApplyIdAndPageNum(companyPreOpenAccountEntDto.getApplyid(), num, "ezhanghu");
                                            } catch (Exception e) {
                                                log.error("拉取预约影像失败:{}", e.getMessage());
                                                totalNum = -1;
                                                break;
                                            }
                                        }
                                    }
                                    if (totalNum != -1) {
                                        companyPreOpenAccountEntService.updateHasocr(companyPreOpenAccountEntDto.getId());
                                    }
                                }
                                log.info("拉取预约影像执行结束。。。");
                                schedulerLockService.releaseLock(1004L);//释放锁
                            }
                        } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                            log.error("拉取预约影像执行失败：已有主机启动该定时任务。");
                        } catch (Exception e) {
                            log.error("拉取预约影像执行失败", e);
                            schedulerLockService.releaseLock(1004L);//释放锁
                        }
                    }
                }, new CronTrigger(imageScheduletiming)));
            }

            taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                @Override
                public void run() {

                    ConfigDto isRunning = new ConfigDto();
                    isRunning.setConfigKey("FALSE");//不执行任务。

                    try {

                        schedulerLockService.isRunningFun(1005L,isRunning);

                        if (TRUE.equals(isRunning.getConfigKey())){
                            List<PbcAccountDto> accounts = pbcAccountService.listByType(EAccountType.ECCS);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                            String newPassword = amsWebProperties.getEccsPasswordPrefix() + sdf.format(new Date());
                            log.info("开始更新信用机构的密码，总数量是: "+accounts.size());
                            for (PbcAccountDto account : accounts) {
                                try{
                                    if (account.getEnabled() && account.getAccountStatus() == EAccountStatus.VALID) {
                                        PbcUserAccount pbcUserAccount = new PbcUserAccount();
                                        pbcUserAccount.setLoginIp(account.getIp());
                                        pbcUserAccount.setLoginPassWord(account.getPassword());
                                        pbcUserAccount.setLoginUserName(account.getAccount());

                                        LoginAuth loginAuth = eccsMainService.eccsLogin(pbcUserAccount);
                                        if (loginAuth.getLoginStatus() == LoginStatus.Success) {
                                            PwdModifyStatus pwdModifyStatus = null;
                                            try {
                                                eccsMainService.modifyEccsPwd(account.getIp(), account.getAccount(), account.getPassword(), newPassword);
                                                account.setPassword(encryptPwdService.encryptPwd(newPassword));
                                                account.setAccountStatus(EAccountStatus.VALID);
                                                log.info("账号{}修改信用代码证密码成功。修改后密码为:{}",account.getAccount(),account.getPassword());
                                            } catch (Exception e) {
                                                account.setAccountStatus(EAccountStatus.INVALID);
                                                log.info("账号{}修改信用代码证密码失败；",account.getAccount());
                                            }
                                        } else {
                                            account.setAccountStatus(EAccountStatus.INVALID);
                                            log.info("账号{}修改信用代码证密码失败,登录状态为{}",account.getAccount(),loginAuth.getLoginStatus().getFullName());
                                        }
                                        pbcAccountService.save(account);
                                        // TODO 发送通知到机构用户
                                    }
                                }catch (Exception e){
                                    log.error("更新信用机构异常,"+account,e);
                                }
                            }
                            log.info("更新信用机构的密码结束");
                            schedulerLockService.releaseLock(1005L);//释放锁
                        }

                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("更新信用机构的密码执行失败：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("更新信用机构的密码执行失败", e);
                        schedulerLockService.releaseLock(1005L);//释放锁
                    }
                }
            }, new CronTrigger(updateEccsPassword)));

            //TODO 什么定时任务
            taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                @Override
                public void run() {
                    List<ConfigDto> list = configService.findByKey("breakAppointCustomDivEnabled");
                    if (CollectionUtils.isNotEmpty(list)) {
                        ConfigDto dto = list.get(0);
                        breakAppointEnabled = dto.getConfigValue();
                    }
                    if ("true".equals(breakAppointEnabled)) {
                        accountBillsAllService.updateApplyStatus();
                    }
                }
            }, new CronTrigger(breakAppointTiming)));

        }


        taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
            @Override
            public void run() {

                ConfigDto isRunning = new ConfigDto();
                isRunning.setConfigKey("FALSE");//不执行任务。

                try {

                    schedulerLockService.isRunningFun(1006L,isRunning);

                    if (TRUE.equals(isRunning.getConfigKey())){
                        List<PbcAccountDto> pbcAccountDtos = pbcAccountService.listAllNewAccount();
                        for (PbcAccountDto pbcAccountDto : pbcAccountDtos) {
                            switch (pbcAccountDto.getAccountType()) {
                                case AMS: {
                                    try {
                                        PbcUserAccount pbcUserAccount = new PbcUserAccount();
                                        pbcUserAccount.setLoginIp(pbcAccountDto.getIp());
                                        pbcUserAccount.setLoginUserName(pbcAccountDto.getAccount());
                                        pbcUserAccount.setLoginPassWord(encryptPwdService.decryptEcbPwd(pbcAccountDto.getPassword()));
                                        LoginAuth loginAuth = amsMainService.amsLogin(pbcUserAccount);
                                        if (loginAuth.getLoginStatus() == LoginStatus.Success) {
                                            pbcAccountDto.setAccountStatus(EAccountStatus.VALID);
                                        } else {
                                            pbcAccountDto.setAccountStatus(EAccountStatus.INVALID);
                                        }
                                    } catch (Exception e) {
                                        log.error("每分钟定时任务人行校验密码解密失败", e);
                                    }
                                    break;
                                }
                                case ECCS: {
                                    try {
                                        PbcUserAccount pbcUserAccount = new PbcUserAccount();
                                        pbcUserAccount.setLoginIp(pbcAccountDto.getIp());
                                        pbcUserAccount.setLoginUserName(pbcAccountDto.getAccount());

                                        //校验密码时根据配置判断是否需要解密
                                        pbcUserAccount.setLoginPassWord(encryptPwdService.decryptEcbPwd(pbcAccountDto.getPassword()));

                                        LoginAuth loginAuth = eccsMainService.eccsLogin(pbcUserAccount);
                                        if (loginAuth.getLoginStatus() == LoginStatus.Success) {
                                            pbcAccountDto.setAccountStatus(EAccountStatus.VALID);
                                        } else {
                                            pbcAccountDto.setAccountStatus(EAccountStatus.INVALID);
                                        }
                                    } catch (Exception e) {
                                        log.error("每分钟定时任务机构信用代码校验密码解密失败", e);
                                    }
                                    break;
                                }
                                case PICP:
                                    try {
                                        //校验密码时根据配置判断是否需要解密
                                        String password = encryptPwdService.decryptEcbPwd(pbcAccountDto.getPassword());
                                        if (idCardComperService.login(pbcAccountDto.getIp(), pbcAccountDto.getAccount(), password)) {
                                            pbcAccountDto.setAccountStatus(EAccountStatus.VALID);
                                        } else {
                                            pbcAccountDto.setAccountStatus(EAccountStatus.INVALID);
                                        }
                                    } catch (Exception e) {
                                        log.error("每分钟定时任务联网核查校验密码解密失败", e);
                                    }
                                    break;
                                default:
                            }
                            pbcAccountService.save(pbcAccountDto);
                        }
                        schedulerLockService.releaseLock(1006L);//释放锁
                    }

                } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                    log.error("校验密码执行失败：已有主机启动该定时任务。");
                } catch (Exception e) {
                    log.error("密码执行失败", e);
                    schedulerLockService.releaseLock(1006L);//释放锁
                }

            }
        }, new PeriodicTrigger(1, TimeUnit.MINUTES)));

        taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
            @Override
            public void run() {

                    ConfigDto isRunning = new ConfigDto();
                    isRunning.setConfigKey("FALSE");//不执行任务。

                    try {

                        schedulerLockService.isRunningFun(1007L,isRunning);

                        if (TRUE.equals(isRunning.getConfigKey())){
                            log.info("判断工商是否可以开始在线采集");
                            Long taskId = annualTaskService.initAnnualTask();
                            int status = saicCollectionService.checkSaicCollectTaskStatus(taskId);
                            if (status == 0) {
                                log.info("核心导入未完成");
                                log.info("无法在线采集工商");
                            } else if (status == 1) {
                                log.info("人行数据未解析完成");
                                log.info("无法在线采集工商");
                            } else if (status == 2) {
                                log.info("工商采集已经开启");
                                log.info("无法在线采集工商");
                            } else if (status == 3) {
                                log.info("工商采集使用文件导入方式");
                                log.info("无法在线采集工商");
                            } else if (status == 4 ) {
                                log.info("工商可以手动采集");
                            } else if (status == 7) {
                                log.info("在线工商开始采集");
                                saicCollectionService.collect(CollectType.AFRESH, taskId);
                            } else if (status == 5) {
                                log.info("未配置工商采集方式");
                                log.info("无法在线采集工商");
                            } else if (status == 8) {
                                log.info("创建工商采集定时任务");
                                annualTaskService.createSaicTimedTask(taskId);
                            } else {
                                log.info("其他操作中，无法采集，请等待");
                                log.info("无法在线采集工商");
                            }
                            schedulerLockService.releaseLock(1007L);//释放锁
                        }

                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("判断工商是否可以在线采集执行失败：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("判断工商是否可以在线采集异常", e);
                        schedulerLockService.releaseLock(1007L);//释放锁
                    }
            }
//        }, new PeriodicTrigger(1, TimeUnit.DAYS)));
        }, new PeriodicTrigger(1, TimeUnit.DAYS)));


        // 定时任务,年检结果更新
        if (annualExportFlag) {
            taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                @Override
                public void run() {

                    ConfigDto isRunning = new ConfigDto();
                    isRunning.setConfigKey("FALSE");//不执行任务。

                    try {

                        schedulerLockService.isRunningFun(1008L,isRunning);

                        if (TRUE.equals(isRunning.getConfigKey())){
                            Long annualTaskId = annualTaskService.initAnnualTask();
                            AnnualTaskDto annualTaskDto = annualTaskService.findById(annualTaskId);
                            List<ConfigDto> configDtos = configService.findByKey(IdeaConstant.ANNUAL_LOOP_CONFIG);
                            ConfigDto configDto = null;
                            if (configDtos.size() > 0) {
                                configDto = configDtos.get(0);
                            } else {
                                configDto = new ConfigDto();
                                configDto.setConfigKey(IdeaConstant.ANNUAL_LOOP_CONFIG);
                                configDto.setConfigValue("0");
                                configService.save(configDto);
                            }
                            if ((annualTaskDto.getStatus() == TaskStatusEnum.FINISH || annualTaskDto.getStatus() == TaskStatusEnum.AGAIN_FINISH) && configDto != null && "0".equals(configDto.getConfigValue())) {//年检已经完成
                                TransactionDefinition definition = new DefaultTransactionDefinition(
                                        TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                                TransactionStatus transaction = null;
                                try {
                                    log.info("[年检结果定时更新]--开始更新年检结果");
                                    transaction = transactionManager.getTransaction(definition);
                                    log.info("[年检结果定时更新]--开始更新年检数量");
                                    //更新条数
                                    annualTaskService.updateLoopNumByTask(annualTaskId);
                                    annualStatisticsService.saveStatistics(annualTaskId);
                                    log.info("[年检结果定时更新]--更新年检数量结束！");
                                    AnnualTaskDto newAnnualTaskDto = annualTaskService.findById(annualTaskId);
                                    if (newAnnualTaskDto.getPassedNum().equals(newAnnualTaskDto.getProcessedNum())) {
                                        log.info("[年检结果定时更新]--年检的数据已经全部处理成功，停止定时任务");
                                        configDto.setConfigValue("1");
                                        configService.save(configDto);
                                    }
                                    transactionManager.commit(transaction);
                                    log.info("[年检结果定时更新]--开始生成年检导出内容");
                                    annualResultExportService.createAnnualResultExport(annualTaskId);
                                    log.info("[年检结果定时更新]--年检统计导出结束！");
                                    log.info("[年检结果定时更新]--年检已经完成，结束更新年检结果");
                                } catch (Exception e) {
                                    if (!transaction.isCompleted()) {
                                        transactionManager.rollback(transaction);
                                    }
                                    log.error("[年检结果定时更新]--年检结果定时更新提交失败", e);
                                }
                            } else {
                                log.info("[年检结果定时更新]--年检状态为{}，完成数量{},成功数量{},无需更新年检结果", annualTaskDto.getStatus(), annualTaskDto.getProcessedNum(), annualTaskDto.getPassedNum());
                            }
                            schedulerLockService.releaseLock(1008L);//释放锁
                        }

                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("[年检结果定时更新]--年检结果定时更新失败：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("[年检结果定时更新]--年检结果定时更新失败", e);
                        schedulerLockService.releaseLock(1008L);//释放锁
                    }
                }
            }, new CronTrigger(annualExportTiming)));
        }



        if(compareTaskLoopUser){
            taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                @Override
                public void run() {
                    ConfigDto isRunning = new ConfigDto();
                    isRunning.setConfigKey("FALSE");//不执行任务。

                    try {

                        schedulerLockService.isRunningFun(1009L,isRunning);

                        if (TRUE.equals(isRunning.getConfigKey())){
                            log.info("[比对管理]-定时任务判断开始");
                            compareScheduledTaskService.checkAndStartScheduledTask(TaskType.TIMING, CompareState.INIT,CompareState.SUCCESS,CompareState.FAIL);
                            log.info("[比对管理]-定时任务判断结束");
                            schedulerLockService.releaseLock(1009L);//释放锁
                        }
                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("[比对管理]-定时任务判断结束异常：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("[比对管理]-定时任务判断结束异常", e);
                        schedulerLockService.releaseLock(1009L);//释放锁
                    }
                }
            }, new PeriodicTrigger(1, TimeUnit.MINUTES)));


            taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                @Override
                public void run() {
                    ConfigDto isRunning = new ConfigDto();
                    isRunning.setConfigKey("FALSE");//不执行任务。

                    try {

                        schedulerLockService.isRunningFun(1010L,isRunning);

                        if (TRUE.equals(isRunning.getConfigKey())){
                            log.info("[比对管理]-定时采集任务启动开始");
                            compareScheduledTaskService.checkAndStartScheduledCollectionTask(TaskType.TIMING, CompareState.COLLECTING);
                            log.info("[比对管理]-定时采集任务启动结束");
                            schedulerLockService.releaseLock(1010L);//释放锁
                        }
                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("[比对管理]-定时采集任务启动异常：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("[比对管理]-定时采集任务启动异常", e);
                        schedulerLockService.releaseLock(1010L);//释放锁
                    }
                }
            }, new PeriodicTrigger(1, TimeUnit.MINUTES)));

            //定时比对
            taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
                @Override
                public void run() {
                    ConfigDto isRunning = new ConfigDto();
                    isRunning.setConfigKey("FALSE");//不执行任务。

                    try {

                        schedulerLockService.isRunningFun(1011L,isRunning);

                        if (TRUE.equals(isRunning.getConfigKey())){
                            log.info("[比对管理]-比对开始");
                            compareScheduledTaskService.doCompare(TaskType.TIMING, CompareState.COLLECTSUCCESS);
                            log.info("[比对管理]-比对结束");
                            schedulerLockService.releaseLock(1011L);//释放锁
                        }
                    } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                        log.error("[比对管理]-比对异常：已有主机启动该定时任务。");
                    } catch (Exception e) {
                        log.error("[比对管理]-比对异常", e);
                        schedulerLockService.releaseLock(1011L);//释放锁
                    }
                }
            }, new PeriodicTrigger(1, TimeUnit.MINUTES)));
        }


        //定时任务，定期检查人行用户名密码，触发过期后的自动修改
        taskRegistrar.addTriggerTask(new TriggerTask(new Runnable() {
            @Override
            public void run() {
                ConfigDto isRunning = new ConfigDto();
                isRunning.setConfigKey("FALSE");//不执行任务。

                try {

                    schedulerLockService.isRunningFun(1012L,isRunning);

                    if (TRUE.equals(isRunning.getConfigKey())){
                        List<PbcAccountDto> accounts = pbcAccountService.listByType(EAccountType.AMS);
                        log.info("定期校验人行用户名密码，总数量是: " + accounts.size());
                        for (PbcAccountDto account : accounts) {
                            try {
                                if (account.getEnabled() && account.getAccountStatus() == EAccountStatus.VALID) {
                                    PbcUserAccount pbcUserAccount = new PbcUserAccount();
                                    pbcUserAccount.setLoginIp(account.getIp());
                                    pbcUserAccount.setLoginPassWord(account.getPassword());
                                    pbcUserAccount.setLoginUserName(account.getAccount());
                                    LoginAuth loginAuth = amsMainService.amsLogin(pbcUserAccount);
                                    log.info("定期检查人行用户名密码账户登录状态：" + loginAuth.getLoginStatus().getFullName());
                                    account.setAccountStatus(loginAuth.getLoginStatus() == LoginStatus.Success ? EAccountStatus.VALID : EAccountStatus.INVALID);
                                    //是否走SM4加密配置
                                    account.setPassword(encryptPwdService.encryptPwd(account.getPassword()));
                                    pbcAccountService.save(account);
                                }
                            } catch (Exception e) {
                                log.error("定期校验人行用户名密码异常," + account, e);
                            }
                        }
                        log.info("定期校验人行用户名密码结束");
                        schedulerLockService.releaseLock(1012L);//释放锁
                    }
                } catch (ObjectOptimisticLockingFailureException e){//保存失败（乐观锁）
                    log.error("定期校验人行用户名密码异常：已有主机启动该定时任务。");
                } catch (Exception e) {
                    log.error("定期校验人行用户名密码异常", e);
                    schedulerLockService.releaseLock(1012L);//释放锁
                }
            }
        }, new CronTrigger(checkAmsPassword)));
    }

}
