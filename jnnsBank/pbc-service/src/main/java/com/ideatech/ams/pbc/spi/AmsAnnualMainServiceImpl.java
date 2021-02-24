package com.ideatech.ams.pbc.spi;

import com.ideatech.ams.pbc.dto.AmsAnnualInfo;
import com.ideatech.ams.pbc.dto.AmsDownAnnualTask;
import com.ideatech.ams.pbc.dto.PbcUserAccount;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.pbc.enums.LoginStatus;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.ams.AmsAnnualService;
import com.ideatech.ams.pbc.service.ams.AmsDownExcelService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class AmsAnnualMainServiceImpl implements AmsAnnualMainService {

	@Autowired
	AmsMainService amsMainService;

	@Autowired
	AmsAnnualService amsAnnualService;

	@Autowired
	AmsDownExcelService amsDownExcelService;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public AmsAnnualResultStatus sumbitAnnual(PbcUserAccount pbcUserAccount, String acctNo) throws SyncException, Exception {
		// 登录
		LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
		return sumbitAnnual(auth, acctNo);
	}

	/**
	 * 根据账户登录状态判断是否满足年检提交
	 * 
	 * @param loginStatus
	 * @return
	 */
	private AmsAnnualResultStatus getAnnualResultStatusByLoginAuth(LoginStatus loginStatus) {
		if (loginStatus == LoginStatus.NetNotConn) {
			return AmsAnnualResultStatus.Exception;
		} else if (loginStatus == LoginStatus.NetTimeOut) {
			return AmsAnnualResultStatus.Exception;
		} else if (loginStatus == LoginStatus.NetUnknow) {
			return AmsAnnualResultStatus.Exception;
		} else if (loginStatus == LoginStatus.PassWordEmpty) {
			return AmsAnnualResultStatus.PassWordEmpty;
		} else if (loginStatus == LoginStatus.PasswordError) {
			return AmsAnnualResultStatus.PasswordError;
		} else if (loginStatus == LoginStatus.PasswordExpire) {
			return AmsAnnualResultStatus.PasswordExpire;
		} else if (loginStatus == LoginStatus.UserNameError) {
			return AmsAnnualResultStatus.UserNameError;
		} else if (loginStatus == LoginStatus.UserNameLock) {
			return AmsAnnualResultStatus.UserNameLock;
		} else {
			return null;
		}
	}

	@Override
	public void downAnnuanAccountExcel(PbcUserAccount pbcUserAccount, String folderPath, String bankId, String annualYear) throws SyncException, Exception {
		LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
		downAnnuanAccountExcel(auth, folderPath, bankId, annualYear);
	}

	@Override
	public void downAnnuanAccountExcel(PbcUserAccount pbcUserAccount, AmsDownAnnualTask task) throws SyncException, Exception {
		LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
		downAnnuanAccountExcel(auth, task);
	}

	@Override
	public void downAnnuanAccountExcel(PbcUserAccount pbcUserAccount, String folderPath, String bankId) throws SyncException, Exception {
		LoginAuth auth = amsMainService.amsLogin(pbcUserAccount);
		downAnnuanAccountExcel(auth, folderPath, bankId);
	}

	@Override
	public AmsAnnualResultStatus sumbitAnnual(LoginAuth auth, String acctNo) throws SyncException, Exception {
		if (auth == null) {
			throw new SyncException("年检时，登录对象为空");
		}
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
		if (StringUtils.isBlank(acctNo)) {
			throw new SyncException("年检账号不能为空");
		}
		logger.info("账号" + acctNo + "开始到人行账管系统中进行年检");
		AmsAnnualInfo amsAnnualInfo = new AmsAnnualInfo();
		amsAnnualInfo.setAcctNo(acctNo);
		// 根据登录状态判断是否可以进行下一步
		AmsAnnualResultStatus amsAnnualResultStatus = getAnnualResultStatusByLoginAuth(auth.getLoginStatus());
		if (amsAnnualResultStatus != null) {
			return amsAnnualResultStatus;
		}
		amsAnnualResultStatus = amsAnnualService.gotoAnnuanlPage(auth);
		if (amsAnnualResultStatus == AmsAnnualResultStatus.Success) {
			amsAnnualResultStatus = amsAnnualService.validAcctNoAnnual(auth, acctNo, amsAnnualInfo);
			if (amsAnnualResultStatus == AmsAnnualResultStatus.Success) {
				amsAnnualResultStatus = amsAnnualService.sumitAnnualAccount(auth, acctNo);
			}
		}
		return amsAnnualResultStatus;
	}

	@Override
	public void downAnnuanAccountExcel(LoginAuth auth, String folderPath, String bankId, String annualYear) throws SyncException, Exception {
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
		AmsDownAnnualTask task = new AmsDownAnnualTask();
		task.setAnnualYear(String.valueOf(Integer.valueOf(annualYear) - 1));// 年检年份,查询的时候一般是当年-1
		task.setBankId(bankId);
		task.setCheckYearType("0");
		task.setFolderPath(folderPath);
		amsDownExcelService.downAnnualAccount(auth, task);
	}

	@Override
	public void downAnnuanAccountExcel(LoginAuth auth, AmsDownAnnualTask task) throws SyncException, Exception {
		if (auth == null) {
			throw new SyncException("登录对象不能为空");
		}
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
		task.setCheckYearType("0");
		amsDownExcelService.downAnnualAccount(auth, task);
	}

	@Override
	public void downAnnuanAccountExcel(LoginAuth auth, String folderPath, String bankId) throws SyncException, Exception {
		if (auth.getLoginStatus() != LoginStatus.Success) {
			throw new SyncException(auth.getLoginStatus().getFullName());
		}
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int year = c.get(Calendar.YEAR);
		downAnnuanAccountExcel(auth, folderPath, bankId, String.valueOf(year));

	}

}
