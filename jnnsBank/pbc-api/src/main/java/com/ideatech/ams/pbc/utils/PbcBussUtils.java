package com.ideatech.ams.pbc.utils;

import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 业务处理类 zl Jan 21, 2015 3:07:20 PM
 * 
 * @version 1.0.0
 */
public class PbcBussUtils {

	public static Logger logger = LoggerFactory.getLogger(PbcBussUtils.class);

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
	 * 开户是否根据基本户开户许可证进行开户
	 * 
	 * @param acctType
	 * @return
	 */
	public static boolean openAcctByAccountKey(SyncAcctType acctType) {
		if (acctType == null) {
			return false;
		}
		if (acctType == SyncAcctType.jiben) {
			return false;
		} else if (acctType == SyncAcctType.linshi) {
			return false;
		} else if (acctType == SyncAcctType.teshu) {
			return false;
		} else {
			return true;
		}

	}
	/**
	 * @throws SyncException
	 *             验证开户许可证是否正确
	 *
	 * @param accountKey
	 * @return 返回空则校验通过，不为空则返回值为错误接口
	 * @exception
	 */
	public static String  valiDateAccountKey(String accountKey)  {
		if (StringUtils.isEmpty(accountKey)) {
			return "开户许可证不能为空";
		} else if (!accountKey.substring(0, 1).equalsIgnoreCase("J")) {
			return  "开户许可证不正确,应以J开头";
		} else if (accountKey.getBytes().length != 14) {
			return "开户许可证长度不正确，应14位";
		} else if (!RegexUtils.isNumeric(accountKey.substring(1, 14))) {
			return "开户许可证(2-14位)必须是数字";
		}
		return "";
	}

	/**
	 * @throws SyncException
	 *             校验注册地地区代码
	 *
	 * @param regAreaCode
	 *            注册地地区代码
	 * @return 返回空则校验通过，不为空则返回值为错误接口
	 */
	public static String valiDateRegAreaCode(String regAreaCode) {
		if (StringUtils.isEmpty(regAreaCode)) {
			return "注册地地区代码不能为空";
		} else if (regAreaCode.getBytes().length != 6) {
			return "注册地地区代码长度不正确，应6位";
		} else if (!RegexUtils.isNumberOrLetter(regAreaCode)) {
			return "注册地地区代码应为6位数字";
		}
		return "";
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

	public static void printObjectColumn(Object object) {
		if (HttpConfig.HTTP_PRINT_HTML) {
			Map<String, Object> map = PbcBeanRefUtil.getFieldValueMap(object);
			logger.info("==========" + object.getClass().getSimpleName() + "对象字段值开始===========");
			for (String key : map.keySet()) {
				if (map.get(key) != null && StringUtils.isNotEmpty(map.get(key).toString())) {
					logger.info(key + ":" + map.get(key));
				}
			}
			logger.info("==========" + object.getClass().getSimpleName() + "对象字段值结束===========");
		}
	}

	public static void printObject(Object object) {
		Map<String, Object> map = PbcBeanRefUtil.getFieldValueMap(object);
		logger.info("==========" + object.getClass().getSimpleName() + "对象字段值开始===========");
		for (String key : map.keySet()) {
			if (map.get(key) != null && StringUtils.isNotEmpty(map.get(key).toString())) {
				logger.info(key + ":" + map.get(key));
			}
		}
		logger.info("==========" + object.getClass().getSimpleName() + "对象字段值结束===========");
	}

}
