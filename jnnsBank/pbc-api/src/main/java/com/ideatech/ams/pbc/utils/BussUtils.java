package com.ideatech.ams.pbc.utils;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 业务处理类 zl Jan 21, 2015 3:07:20 PM
 * 
 * @version 1.0.0
 */
public class BussUtils {


	private static final Logger logger = LoggerFactory.getLogger(BussUtils.class);
	/**
	 * 根据账户类型判断人行中可以销户
	 * 
	 * @param acctType
	 * @return
	 * @exception
	 */
	public static boolean canCancel(SyncAcctType acctType) {
		if (acctType == null) {
			return false;
		}
		if (acctType == SyncAcctType.yiban) {
			return true;
		} else if (acctType == SyncAcctType.feiyusuan) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为核准类账户
	 * 
	 * @param acctType
	 * @return
	 */
	public static boolean isHeZhunAccount(SyncAcctType acctType) {
		if (acctType == null) {
			return false;
		}
		if (canCancel(acctType)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 根据账户类型判断同步机构信用代码证系统
	 * 
	 * @param acctType
	 * @return
	 * @exception
	 */
	public static boolean canSyncEccs(SyncAcctType acctType) {
		if (acctType == null) {
			return false;
		}
		if (acctType == SyncAcctType.jiben) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取机构信用代码证系统注册资金（万元转为元）
	 * 
	 * @param registeredCapital
	 * @return
	 * @exception
	 */
	public static String getEccsRegisteredCapital(String registeredCapital) {
		String regCapital = registeredCapital;
		if (StringUtils.isEmpty(regCapital)) {
			regCapital = "";
		} else {
			double regCapitalDouble = Double.valueOf(regCapital) / 10000;
			regCapital = NumberUtils.numberToStr(regCapitalDouble);
		}
		return regCapital;
	}

	/**
	 * 根据传递的组织机构代码 转换为标准格式(12345678-9)
	 * 
	 * @param orgCode
	 * @return
	 */
	public static String getStandardOrgCode(String orgCode) {
		String result = orgCode;
		if (StringUtils.isNotBlank(orgCode) && orgCode.indexOf("-") < 0) {
			result = new String(orgCode.substring(0, orgCode.length() - 1)) + "-" + new String(orgCode.substring(orgCode.length() - 1));
		}
		return result;
	}

	/**
	 * 获取账管系统注册资金（万元转为元）
	 * 
	 * @param registeredCapital
	 * @return
	 * @exception
	 */
	public static String getAmsRegisteredCapital(String registeredCapital) {
		String regCapital = registeredCapital;
		if (StringUtils.isEmpty(regCapital)) {
			regCapital = "";
		} else {
			double regCapitalDouble = Double.valueOf(regCapital) * 10000;
			regCapital = NumberUtils.numberToStr(regCapitalDouble);
		}
		return regCapital;
	}

	/**
	 * 打印账管同步时的参数值
	 * 
	 * @param object
	 * @exception
	 */
	public static void printSyncColunm(SyncSystem syncSystem, AllAcct object) {

		Map<String, Object> map = PbcBeanRefUtil.getFieldValueMap(object);
		logger.info("==========" + syncSystem.getFullName() + "同步的参数值（不为空）开始===========");
		for (String key : map.keySet()) {
			if (map.get(key) != null && StringUtils.isNotEmpty(map.get(key).toString())) {
				logger.info(key + ":" + map.get(key));
			}
		}
		logger.info("==========" + syncSystem.getFullName() + "同步的参数值（不为空）结束===========");
	}
}
