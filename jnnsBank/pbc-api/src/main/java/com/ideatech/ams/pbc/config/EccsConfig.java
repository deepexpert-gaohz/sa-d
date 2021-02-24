package com.ideatech.ams.pbc.config;

import com.ideatech.common.Config;

import java.util.Date;
import java.util.Properties;

public class EccsConfig {
	public static String XYDMZ_URL_BASE = "";
	public static String XYDMZ_URL_LOGIN = "/eccs/login.do?method=login";
	// 新增
	public static String XYDMZ_URL_PREENTERCODE = "/eccs/entering.do?op=preVerify&time=" + new Date().getTime(); // 1
	public static String XYDMZ_URL_CODECOMMIT = "/eccs/entering.do?op=commitEntering&time=" + new Date().getTime(); // 2
	public static String XYDMZ_URL_CODESUCCESS = "/eccs/entering.do?op=enteringSuccess"; // 3
	// 更新
	public static String XYDMZ_URL_MANAGE_PRESERVE = "/eccs/manage.do?op=CreditPreserve";
	// 变更(信用代码证已发放)
	public static String XYDMZ_URL_MODIFYDETAIL = "/eccs/manage.do?op=CreditCodeModifyDeteal&crcCode=";// 进入修改详细页面
	public static String XYDMZ_URL_MODIFYCOMMIT = "/eccs/modify.do?op=saveOrgCode&time=" + new Date().getTime();
	public static String XYDMZ_URL_MODIFYSUCCESS = "/eccs/modify.do?op=modifySuccess";
	// 变更（信用代码证未发放）
	public static String XYDMZ_URL_FaFangModifyDetail = "/eccs/manage.do?op=noCodeModifyDeteal&crcCode=";// 进入修改详细页面

	// 新增时 存在原先记录
	public static String XYDMZ_URL_MATCH = "/eccs/entering.do?op=matching";
	public static String XYDMZ_URL_MATCH_ADD = "/eccs/entering.do?op=enterData";
	public static String XYDMZ_URL_LOGOUT = "/eccs/logout.do";
	public static String XYDM_URL_SURE_FAFang = "/eccs/issueOrgCode.do?op=IssueOrgCode&time=" + new Date().getTime();

	// 密码修改
	public static String XYDMZ_URL_UpdatePassword = "/eccs/login.do?method=updatePassword";// 提示密码过期时修改面膜
	public static String XYDMZ_URL_UpdatePasswordResult = "/eccs/user.do?op=updatePasswordResult";// 手动修改面膜

	public static String ENCODING = "UTF-8";

	static {
		Properties config = new Properties();
		boolean loaded = Config.loadProperties(config, AmsConfig.class.getResourceAsStream("/" + "config_sync.properties"));
		if (!loaded) {
			throw new RuntimeException("载入账管配置文件[config_sync.properties]异常");
		}
	}
}
