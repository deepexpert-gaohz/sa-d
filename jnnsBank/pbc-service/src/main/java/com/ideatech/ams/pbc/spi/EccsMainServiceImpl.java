package com.ideatech.ams.pbc.spi;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.dto.EccsAccountInfo;
import com.ideatech.ams.pbc.dto.EccsSearchCondition;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.enums.PwdModifyStatus;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.PbcMockService;
import com.ideatech.ams.pbc.service.eccs.EccsLoginService;
import com.ideatech.ams.pbc.service.eccs.EccsSearchService;
import com.ideatech.ams.pbc.utils.PbcBeanRefUtil;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class EccsMainServiceImpl implements EccsMainService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	EccsLoginService eccsLoginService;

	@Autowired
	Map<String, Synchronizer> accountSynchronizerMap;
	@Autowired
	Map<String, SyncValidater> syncAccountValidaterMap;
	@Autowired
	EccsSearchService eccsSearchService;

	@Autowired
	PbcMockService pbcMockService;

	@Override
	public LoginAuth eccsLogin(PbcUserAccount eccsUserAccount) {
		LoginAuth auth = new LoginAuth(eccsUserAccount.getLoginIp(), eccsUserAccount.getLoginUserName(), eccsUserAccount.getLoginPassWord());

		//2018年7月19日 如果挡板打开，此处返回登录成功
		if (pbcMockService.isLoginMockOpen()) {
			logger.info("人行挡板开启，默认返回登录成功");
			auth.setLoginStatus(LoginStatus.Success);
			auth.setCookie(new BasicCookieStore());
			return auth;
		}
		// 校验规则
		LoginStatus validaStatus = eccsLoginService.validateEccsLogin(eccsUserAccount);
		if (validaStatus != LoginStatus.Success) {
			auth.setLoginStatus(validaStatus);
			return auth;
		}
		// 登录
		return eccsLoginService.eccsLogin(eccsUserAccount.getLoginIp(), eccsUserAccount.getLoginUserName(), eccsUserAccount.getLoginPassWord());
	}

	@Override
	public void modifyEccsPwd(String eccsIp, String eccsUserName, String eccsOldPwd, String eccsNewPwd) throws SyncException, Exception {
		PbcUserAccount eccsUserAccount = new PbcUserAccount();
		eccsUserAccount.setLoginIp(eccsIp);
		eccsUserAccount.setLoginPassWord(eccsOldPwd);
		eccsUserAccount.setLoginUserName(eccsUserName);
		LoginAuth auth = eccsLogin(eccsUserAccount);
		if (StringUtils.isBlank(eccsNewPwd)) {
			throw new SyncException("新密码不能为空");
		}
		if (StringUtils.equals(eccsNewPwd, eccsOldPwd)) {
			throw new SyncException("新密码与原密码不能为空");
		}
		if (auth.getLoginStatus() != LoginStatus.Success && auth.getLoginStatus() != LoginStatus.FirstLogin) {
			throw new SyncException("修改密码时登录异常:" + auth.getLoginStatus());
		}
		PwdModifyStatus pwdStatus = eccsLoginService.modifyEccsPassWord(auth, eccsOldPwd, eccsNewPwd);
		if (pwdStatus != PwdModifyStatus.Success) {
			throw new SyncException(pwdStatus.getFullName());
		}
	}

	@Override
	public void checkEccsUserName(PbcUserAccount eccsUserAccount) throws SyncException, Exception {
		LoginAuth auth = eccsLogin(eccsUserAccount);
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
	}

	@Override
	public void eccsAccountOpenSync(PbcUserAccount pbcUserAccount, AllAcct allAcct) throws Exception, SyncException {
		// 登录
		LoginAuth auth = eccsLogin(pbcUserAccount);
		eccsAccountOpenSync(auth, allAcct);
	}

	@Override
	public void eccsAccountChangeSync(PbcUserAccount pbcUserAccount, AllAcct allAcct, EccsSearchCondition condition) throws Exception, SyncException {
		// 登录
		LoginAuth auth = eccsLogin(pbcUserAccount);
		eccsAccountChangeSync(auth, allAcct, condition);
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByCondition(PbcUserAccount eccsUserAccount, EccsSearchCondition condition) throws SyncException, Exception {
		// 登录
		LoginAuth auth = eccsLogin(eccsUserAccount);
		EccsAccountInfo info = getEccsAccountInfoByCondition(auth, condition);
		PbcBussUtils.printObjectColumn(info);
		return info;
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByOrgCode(PbcUserAccount eccsUserAccount, String orgCode) throws Exception, SyncException {
		// 登录
		LoginAuth auth = eccsLogin(eccsUserAccount);
		EccsAccountInfo info = getEccsAccountInfoByOrgCode(auth, orgCode);
		PbcBussUtils.printObjectColumn(info);
		return info;

	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByRegTypeAndRegNo(PbcUserAccount eccsUserAccount, String regType, String regNo) throws Exception, SyncException {
		// 登录
		LoginAuth auth = eccsLogin(eccsUserAccount);
		EccsAccountInfo info = getEccsAccountInfoByRegTypeAndRegNo(auth, regType, regNo);
		PbcBussUtils.printObjectColumn(info);
		return info;
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByOrgEccsNo(PbcUserAccount eccsUserAccount, String orgEccsNo) throws Exception, SyncException {
		// 登录
		LoginAuth auth = eccsLogin(eccsUserAccount);
		EccsAccountInfo info = getEccsAccountInfoByOrgEccsNo(auth, orgEccsNo);
		PbcBussUtils.printObjectColumn(info);
		return info;
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByAccountKey(PbcUserAccount eccsUserAccount, String accountKey) throws Exception, SyncException {
		// 登录
		LoginAuth auth = eccsLogin(eccsUserAccount);
		EccsAccountInfo info = getEccsAccountInfoByAccountKey(auth, accountKey);
		PbcBussUtils.printObjectColumn(info);
		return info;
	}

	/**
	 * 变更、查询机构信用代码证系统时校验查询条件
	 * 
	 * @param condition
	 * @throws SyncException
	 */
	private void validEccsSearchCondtion(EccsSearchCondition condition) throws SyncException {
		int num = 0;
		if (condition == null) {
			throw new SyncException("机构代码证查询对象不能为空");
		}
		if (StringUtils.isNotBlank(condition.getAccountKey())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getOrgCode())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getOrgEccsNo())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getRegNo()) && StringUtils.isNotBlank(condition.getRegType())) {
			num++;
		} else {
			String[] regTypeArray = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "99" };
			if (ArrayUtils.contains(regTypeArray, condition.getRegType())) {
				num++;
			}
		}
		if (StringUtils.isNotBlank(condition.getStateTaxRegNo())) {
			num++;
		}
		if (StringUtils.isNotBlank(condition.getTaxRegNo())) {
			num++;
		}
		if (num < 1) {
			throw new SyncException("机构信用代码证系统查询条件不能为空");
		}
	}

	@Override
	public void eccsAccountOpenSync(LoginAuth auth, AllAcct allAcct) throws Exception, SyncException {
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
		PbcBeanRefUtil.setFieldEmpty(allAcct);
		// 校验
		String validaterName = allAcct.getAccountSyncValidater(SyncSystem.eccs.toString());
		// String validaterName = "eccsJibenOpenSyncValidater";
		SyncValidater validater = syncAccountValidaterMap.get(validaterName);
		if (validater == null) {
			validater = syncAccountValidaterMap.get("defaultSyncValidater");
			if (validater == null) {
				throw new SyncException("未找到报备机构信用代码系统校验器,请联系系统管理员");
			}
		}
		validater.validater(allAcct, SyncSystem.eccs);

		// 报备
		String syncName = allAcct.getAccountSynchronizerName(SyncSystem.eccs.toString());
		// String syncName = "eccsJibenOpenSynchronizer";
		Synchronizer accountSynchronizer = accountSynchronizerMap.get(syncName);
		if (accountSynchronizer == null) {
			accountSynchronizer = accountSynchronizerMap.get("defaultSynchronizer");
			if (accountSynchronizer == null) {
				throw new SyncException("未找到报备机构信用代码系统报备器,请联系系统管理员");
			}
		}
		accountSynchronizer.synchronizer(auth, SyncSystem.eccs, allAcct);

	}

	@Override
	public void eccsAccountChangeSync(LoginAuth auth, AllAcct allAcct, EccsSearchCondition condition) throws Exception, SyncException {
		// 校验查询条件
		validEccsSearchCondtion(condition);
		allAcct.setEccsSerarchCondition(condition);
		// 报备
		eccsAccountOpenSync(auth, allAcct);

	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByCondition(LoginAuth auth, EccsSearchCondition condition) throws Exception, SyncException {
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
		// 校验查询条件
		validEccsSearchCondtion(condition);
		// 查询
		StringBuffer searchUrlParms = eccsSearchService.getEccsSearchUrlParams(condition);
		return eccsSearchService.getEccsAccountInfo(auth, searchUrlParms);
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByOrgCode(LoginAuth auth, String orgCode) throws Exception, SyncException {
		if (StringUtils.isBlank(orgCode)) {
			throw new SyncException("组织机构代码不能为空");
		}
		EccsSearchCondition condition = new EccsSearchCondition();
		condition.setOrgCode(orgCode);
		return getEccsAccountInfoByCondition(auth, condition);
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByRegTypeAndRegNo(LoginAuth auth, String regType, String regNo) throws Exception, SyncException {
		String[] regTypeArray = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "99" };
		if (StringUtils.isBlank(regType) || StringUtils.isBlank(regNo)) {
			throw new SyncException("工商注册类型、编号不能为空");
		}
		if (!ArrayUtils.contains(regTypeArray, regType)) {
			throw new SyncException("工商注册类型值不正确");
		}
		EccsSearchCondition condition = new EccsSearchCondition();
		condition.setRegNo(regNo);
		condition.setRegType(regType);
		return getEccsAccountInfoByCondition(auth, condition);
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByOrgEccsNo(LoginAuth auth, String orgEccsNo) throws Exception, SyncException {
		if (StringUtils.isBlank(orgEccsNo)) {
			throw new SyncException("机构信用代码证编号不能为空");
		}
		EccsSearchCondition condition = new EccsSearchCondition();
		condition.setOrgEccsNo(orgEccsNo);
		return getEccsAccountInfoByCondition(auth, condition);
	}

	@Override
	public EccsAccountInfo getEccsAccountInfoByAccountKey(LoginAuth auth, String accountKey) throws Exception, SyncException {
		if (StringUtils.isBlank(accountKey)) {
			throw new SyncException("基本户开户许可证号不能为空");
		}
		EccsSearchCondition condition = new EccsSearchCondition();
		condition.setAccountKey(accountKey);
		return getEccsAccountInfoByCondition(auth, condition);
	}
}
