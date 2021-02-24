package com.ideatech.ams.pbc.spi.sync;

import com.ideatech.ams.pbc.config.AmsConfig;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import com.ideatech.ams.pbc.dto.AmsRevokeSyncCondition;
import com.ideatech.ams.pbc.dto.AmsSuspendSyncCondition;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.ams.AmsRevokeService;
import com.ideatech.ams.pbc.service.ams.AmsSearchService;
import com.ideatech.ams.pbc.service.ams.AmsSuspendService;
import com.ideatech.ams.pbc.service.ams.AmsSyncOperateService;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.spi.SyncParameter;
import com.ideatech.ams.pbc.spi.Synchronizer;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSynchronizer implements Synchronizer {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    Map<String, SyncParameter> syncAccountparameter;

    @Autowired
    protected AmsSearchService amsSearchService;

    @Autowired
    AmsRevokeService amsRevokeService;

    @Autowired
    AmsSuspendService amsSuspendService;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected AmsSyncOperateService amsSyncOperateService;

    @Autowired
    PbcMockService pbcMockService;

/*	// 报备人行挡板开始时，允许报备的业务类型
	@Value("${ams.company.sync.allow.operateType:}")
	private String flapAllowOperateType;
	// 报备人行挡板开始时，允许报备的账户性质
	@Value("${ams.company.sync.allow.acctType:}")
	private String flapAllowAcctType;*/

    @Override
    public void synchronizer(LoginAuth auth, SyncSystem syncSystem, AllAcct allAcct) throws SyncException, Exception {
        printStartlog(syncSystem, allAcct);
		if (syncSystem == SyncSystem.ams && allAcct.getOperateType() != null && allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
			setOpenAccountSityType(allAcct, auth);
		}
        // 报备
        if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
            //模拟获取查询密码
			if(allAcct.getCancelHeZhun() && allAcct.getAcctType()==SyncAcctType.jiben){
				logger.info("人行挡板开启，测试数据写入.......");
				allAcct.setSelectPwd("88888888");
				allAcct.setAccountKey("J8888888888889");
				allAcct.setOpenKey("8888888");
				allAcct.setAccountLicenseNo("888888888");
			}else if (allAcct.getCancelHeZhun() && allAcct.getAcctType()==SyncAcctType.feilinshi){
				logger.info("人行挡板开启，feilinshi测试数据写入......");
				allAcct.setOpenKey("3333333333");
				allAcct.setAccountLicenseNo("999999999999");
            }
			printEndlog(syncSystem, allAcct);
            return;
        }
		logger.info("【本外币更新后上报版本！】");
        doSynchron(syncSystem, auth, allAcct);
        printEndlog(syncSystem, allAcct);
    }

    protected abstract void doSynchron(SyncSystem syncSystem, LoginAuth auth, AllAcct allAcct) throws SyncException, Exception;

    /**
     * 人行账管系统核准类账户开户、变更接口
     *
     * @param auth
     * @param allAcct
     * @throws Exception
     */
    protected void doAmsCheckTypeAcctSynchron(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
        if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
            return;
        }
        HttpResult result = null;
        String html = "";
        SyncParameter syncAccountParameter = getSyncAccountParameter(allAcct, SyncSystem.ams);
        // url
        StringBuffer url = new StringBuffer("");// 指定url
        String[] bankInfo = amsSearchService.getBankCodeByLoginName(auth);
        if (ArrayUtils.isNotEmpty(bankInfo)) {
            allAcct.setBankCode(bankInfo[1]);
            allAcct.setBankName(bankInfo[0]);
        }
        url.append(auth.getDomain());
        if (allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
            url.append(AmsConfig.ZG_URL_ACCOUNT_OPEN);
        } else if (allAcct.getOperateType() == SyncOperateType.ACCT_CHANGE) {
            url.append(AmsConfig.ZG_URL_ACCTOUNT_MODIFY);
        }
        String urlParms = syncAccountParameter.getParams(allAcct, SyncSystem.ams);// 参数
		logger.info("变更上报拼接上报：{}",urlParms);
        // 行业归属
        if (!PbcBussUtils.openAcctByAccountKey(allAcct.getAcctType()) && StringUtils.isNotEmpty(allAcct.getIndustryCode()) && allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
            domainCode(auth, allAcct.getIndustryCode());// 行业归属
        }
        result = HttpUtils.post(url.toString(), urlParms, HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        if (result.getStatuCode() == 200) {
            result.makeHtml(HttpConfig.HTTP_ENCODING);
            html = result.getHtml();
			logger.info("核准类返回HTML：" + html);
            judgeSyncIsSuccess(html, allAcct);
        } else if (html.indexOf("当前系统正在运行后台批处理，请稍等再进行业务") > -1) {
            throw new SyncException("人行账管系统服务已关闭,请工作时间使用");
        } else {
            logger.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{allAcct.getAcctNo(), html});
            // 人行接口异常
            throw new SyncException("账号" + allAcct.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,页面请求类型为" + result.getStatuCode() + ",請联系系统管理员");
        }
        try {
            if (!amsSearchService.existAcctInCheckListByAcctNo(auth, allAcct.getAcctNo())) {
                logger.info("账号" + allAcct.getAcctNo() + "请求同步" + SyncSystem.ams.getFullName() + "时，但列表中查询异常！请手动登录人行账管系统确认是否开户成功 ");
                throw new SyncException("账号" + allAcct.getAcctNo() + "由于网络原因，账户报备失败@");
            }
        } catch (Exception e) {
            logger.error("账号" + allAcct.getAcctNo() + "请求同步" + SyncSystem.ams.getFullName() + "时，但列表中查询异常！ ", e);
        }

    }

    /**
     * 报备类账户销户
     *
     * @param auth
     * @param allAcct
     * @throws SyncException
     * @throws Exception
     */
    protected void doRevokeReportTypeAcct(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
        if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
            return;
        }
        AmsRevokeSyncCondition condition = new AmsRevokeSyncCondition();
        BeanUtils.copyProperties(allAcct, condition);
        amsRevokeService.revokeAccountFirstStep(auth, condition);
        amsRevokeService.revokeAccountLastStep(auth, condition);
        // 校验人行是否存在
        // 2017年6月27日20:07:47 去除，无需校验
        /*
         * if (!checkSyncStatus(auth, allAcct.getAcctNo(),
         * allAcct.getOperateType())) { throw new SyncException("账户" +
         * allAcct.getAcctNo() + "报备人行成功,但人行账管系统无法查到该账户报备后信息，请联系系统管理员."); }
         */
    }

    /**
     * 报备类账户销户
     *
     * @param auth
     * @param allAcct
     * @throws SyncException
     * @throws Exception
     */
    protected void doSuspendReportTypeAcct(LoginAuth auth, AllAcct allAcct) throws SyncException, Exception {
        if (!isProEnvironment(allAcct.getAcctNo(), allAcct.getOperateType(), allAcct.getAcctType())) {
            return;
        }
        AmsSuspendSyncCondition condition = new AmsSuspendSyncCondition();
        BeanUtils.copyProperties(allAcct, condition);
        amsSuspendService.suspendAccountFirstStep(auth, condition);
        amsSuspendService.suspendAccountLastStep(auth);
        // 校验人行是否存在
        if (!checkSyncStatus(auth, allAcct.getAcctNo(), allAcct.getOperateType())) {
            throw new SyncException("账户" + allAcct.getAcctNo() + "报备人行成功,但人行账管系统无法查到该账户报备后信息，请联系系统管理员.");
        }
    }

    /**
     * 根据结果判断核准类账户报备是否成功
     *
     * @param html
     * @param allAcct
     * @throws SyncException
     */
    private void judgeSyncIsSuccess(String html, AllAcct allAcct) throws SyncException {
        if (allAcct.getOperateType() == SyncOperateType.ACCT_OPEN && html.indexOf("开户申请资料信息录入成功") > -1) {
            logger.info("账号" + allAcct.getAcctNo() + "在" + SyncSystem.ams.getFullName() + allAcct.getOperateType().getFullName() + "成功");
        } else if (allAcct.getOperateType() == SyncOperateType.ACCT_CHANGE && html.indexOf("开户申请书变更录入成功") > -1) {
            logger.info("账号" + allAcct.getAcctNo() + "上报成功");
        } else if (allAcct.getOperateType() == SyncOperateType.ACCT_CHANGE && html.indexOf("账户不存在") > -1) {
            throw new SyncException("账号" + allAcct.getAcctNo() + "变更时人行账管不存在");
        } else if (html.equals("")) {
            throw new SyncException("账号" + allAcct.getAcctNo() + "账管同步数据不正确，请重新输入");
        } else if (html.indexOf("提示信息") > -1) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(html);
            html = m.replaceAll("");
            p = Pattern.compile("<div\\s*?class=\"error\".*?>(.*?)<br>.*?</div>");
            m = p.matcher(html);
            if (m.find()) {
                throw new SyncException(m.group(1));
            } else {
                logger.info("{}报送异常,出现错误信息，请求页面如下：\n{}", new Object[]{allAcct.getAcctNo(), html});
                throw new SyncException("账号" + allAcct.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,請联系系统管理员");
            }
        } else {
            String validaStr = amsSyncOperateService.validateOp(html, "录入成功");
            if (StringUtils.isNotEmpty(validaStr)) {
                throw new SyncException(validaStr);
            }
            logger.info("{}报送失败,出现未知错误，请求页面如下：\n{}", new Object[]{allAcct.getAcctNo(), html});
            throw new SyncException("账号" + allAcct.getAcctNo() + "报送" + SyncSystem.ams.getFullName() + "失败,請联系系统管理员");
        }
    }

    protected SyncParameter getSyncAccountParameter(AllAcct allAcct, SyncSystem syncSystem) throws SyncException {
        String syncParamName = allAcct.getAccountSyncParmsName(syncSystem.toString());
        SyncParameter syncAccountParameter = syncAccountparameter.get(syncParamName);
        if (syncAccountParameter == null) {
            throw new SyncException("获取上报系统参数对象为空,请联系系统管理员");
        }
        return syncAccountParameter;
    }

    /**
     * 行业归属 保存 应该是存在cookies中
     *
     * @param auth
     * @param domainCodes
     * @return
     * @throws Exception
     */
    private void domainCode(LoginAuth auth, String domainCodes) throws SyncException, Exception {
        StringBuffer urlPars = new StringBuffer();
        for (String str : domainCodes.split(",")) {
            urlPars.append("&selectTrades=1");
            urlPars.append("&stradecodes=").append(URLEncoder.encode(str, "gbk"));
            urlPars.append("&sdomaincodes=");
            urlPars.append(str.substring(str.length() - 1, str.length()));
        }
        HttpResult result = HttpUtils.post(auth.getDomain() + AmsConfig.ZG_URL_ADD_ENTANDTRADE, urlPars.toString(), HttpConfig.HTTP_ENCODING, auth.getCookie(), null);
        result.makeHtml(HttpConfig.HTTP_ENCODING);
        result.getHtml();
    }

    private void printStartlog(SyncSystem syncSystem, AllAcct allAcct) {
        StringBuffer logComm = new StringBuffer();
        logComm.append("开始进行报备");
        logComm.append(allAcct.getAcctType().getFullName());
        logComm.append(allAcct.getAcctNo());
        logComm.append(allAcct.getOperateType().getFullName());
        logComm.append("到").append(syncSystem.getFullName());
        logger.info(logComm.toString());
        PbcBussUtils.printSyncColunm(syncSystem, allAcct);
    }

    private void printEndlog(SyncSystem syncSystem, AllAcct allAcct) {
        StringBuffer logComm = new StringBuffer();
        logComm.append(allAcct.getAcctType().getFullName());
        logComm.append(allAcct.getAcctNo());
        logComm.append(allAcct.getOperateType().getFullName());
        logComm.append("报备到").append(syncSystem.getFullName()).append("成功");
        logger.info(logComm.toString());
    }

    /**
     * 校验报备类开户、销户和账户的久悬是否成功报备到人行
     *
     * @param auth
     * @param acctNo
     * @param operateType
     * @return
     * @throws Exception
     * @throws SyncException
     */
    protected boolean checkSyncStatus(LoginAuth auth, String acctNo, SyncOperateType operateType) {
        boolean result = false;
        try {
            AmsAccountInfo amsAccountInfo = amsSearchService.getAmsAccountInfoByAcctNo(auth, acctNo);
            if (amsAccountInfo != null && StringUtils.isNotBlank(amsAccountInfo.getDepositorName())) {
                if (operateType == SyncOperateType.ACCT_OPEN) {
                    if (amsAccountInfo.getAccountStatus() == AccountStatus.normal) {
                        result = true;
                    }
                } else if (operateType == SyncOperateType.ACCT_REVOKE) {
                    if (amsAccountInfo.getAccountStatus() == AccountStatus.revoke) {
                        result = true;
                    }
                } else if (operateType == SyncOperateType.ACCT_SUSPEND) {
                    if (amsAccountInfo.getAccountStatus() == AccountStatus.suspend || amsAccountInfo.getAccountStatus() == AccountStatus.revoke) {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("报备后校验异常", e);
            result = true;
        }
        return result;
    }

    /**
     * 是否生产环境,制作成挡板
     *
     * <pre>
     * application.properties=pro、test时 挡板关闭，可上报人行
     * application.properties=dev时 挡板开启，除了配置的业务类型、账户性质之外其他的操作都是需要报备人行
     * </pre>
     *
     * @param acctNo      账号
     * @param operateType 要处理的业务类型
     * @param acctType    要处理的 账户性质
     * @return true挡板关闭、false挡板开启
     */
    protected boolean isProEnvironment(String acctNo, SyncOperateType operateType, SyncAcctType acctType) {
        //pbcMock表示开启挡板，此处返回fasle
		logger.info("报送挡板状态：{}",pbcMockService.isSyncMockOpen());
        if (!pbcMockService.isSyncMockOpen()) {
            return true;
        } else {
            logger.info("===================非生产环境，帐号" + acctNo + "系统默认报备成功=====================");
            return false;
        }
	}
	protected void setOpenAccountSityType(AllAcct allAcct, LoginAuth auth) throws Exception {
		if (!pbcMockService.isLoginMockOpen()) {
			if (allAcct.getOperateType() != null && allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
				String[] bankInfo = amsSearchService.getBankCodeByLoginName(auth);
				if (ArrayUtils.isNotEmpty(bankInfo) && StringUtils.isNotBlank(bankInfo[0])) {
					allAcct.setBankCode(bankInfo[1]);
					allAcct.setBankName(bankInfo[0]);
    }
				String bankAreaCode = amsSearchService.getBankAreCode(auth, allAcct.getBankCode())[1];
				if (StringUtils.isNotBlank(bankAreaCode) && StringUtils.isNotBlank(allAcct.getRegAreaCode())) {
					if (bankAreaCode.equals(allAcct.getRegAreaCode())) {
						allAcct.setOpenAccountSiteType("1");
					} else {
						allAcct.setOpenAccountSiteType("2");
					}
				}
			}
		}
		else {
			allAcct.setOpenAccountSiteType("1");
		}
	}
}
