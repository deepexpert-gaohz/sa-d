package com.ideatech.ams.pbc.config;

import com.ideatech.common.Config;
import com.ideatech.common.MyLog;
import com.ideatech.common.utils.NumberUtils;

import java.util.Properties;

public class AmsConfig {

	final public static String CONFIG_PATH = "config_sync.properties";

	final public static Properties config = new Properties();

	public static int ZG_LOGIN_COUNT = 2;

	public static String ZG_URL_LOGIN = null;

	public static String ZG_URL_BANK_QUERY_INPUT = null;

	public static String ZG_URL_BANK_QUERY_POST = null;

	public static String ZG_URL_BANK_QUERY_DETAIL = null;

	public static String ZG_URL_ACCOUNT_OPEN = null;// 开户页面

	public static String ZG_URL_ACCOUNT_OPEN_LIST = null;// 核准类

	// 一般户
	public static String ZG_URL_ACCOUNT_TOGENERAL = null;// 一般化进入页面

	public static String ZG_URL_ACCOUNT_TOOPENGENERALPAGE = null;

	public static String ZG_URL_ACCOUNT_GENERALVALIDATE = null; // 一般户开户审核

	public static String ZG_URL_ACCOUNT_SHOWBASEINFO = null; // 一般户开户验证后显示的基本信息

	public static String ZG_URL_ACCOUNT_OPENGENERALACCTINFO = null; // 一般户开户

	public static String ZG_URL_ACCOUNT_TOCHANGEGENERAL = null;// 一般户变更进入页面

	public static String ZG_URL_ACCOUNT_TOCHANGEGENERALDETAIL = null;// 一般户变更进入具体页面

	public static String ZG_URL_ACCOUNT_CHANGE_GENERALVALIDATE = null; // 一般户变更审核

	public static String ZG_URL_ACCOUNT_CHANGE_SHOWBASEINFO = null; // 一般户变更验证后显示的基本信息

	public static String ZG_URL_ACCOUNT_CHANGE_OPENGENERALACCTINFO = null; // 一般户变更

	// 预算
	public static String ZG_URL_ACCOUNT_TOFYS = null;

	public static String ZG_URL_ACCOUNT_TOOPENFYSPAGE = null;

	public static String ZG_URL_ACCOUNT_FYSACCOUNTVALIDATE = null; // 非预算开户审核

	public static String ZG_URL_ACCOUNT_FYSACCOUNTSHOWBASEINFO = null; // 非预算开户验证后显示的基本信息

	public static String ZG_URL_ACCOUNT_OPENFYSACCOUNT = null; // 非预算开户

	public static String ZG_URL_ACCOUNT_TOCHANGEFYS = null;// 非预算变更进入页面

	public static String ZG_URL_ACCOUNT_TOCHANGEFYSDETAIL = null;// 非预算变更进入具体页面

	public static String ZG_URL_ACCOUNT_CHANGEFYSACCOUNTVALIDATE = null; // 非预算变更审核

	public static String ZG_URL_ACCOUNT_CHANGEFYSACCOUNTSHOWBASEINFO = null; // 非预算验证验证后显示的基本信息

	public static String ZG_URL_ACCOUNT_CHANGEFYSACCOUNT = null; // 非预算确定变更

	public static String ZG_URL_ACCTOUNT_MODIFY = null;// 变更页面

	// 撤销
	public static String ZG_URL_ACCOUNT_CLOSEBASIC = null; // 撤销账管详细页面

	public static String ZG_URL_ACCOUNT_CLOSECOMMIT = null; // 提交账户撤销页面

	// 久悬
	public static String ZG_URL_ACCOUNT_SUPSENDTOBASIC = null; // 久悬账户详细页面

	public static String ZG_URL_ACCOUNT_SUSPENDCOMMIT = null; // 提交久悬账户页面

	public static String ZG_URL_GRYHJSZH = null;// 个人银行结算账户

	public static String ZG_URL_ADD_ENTANDTRADE = null; // 行业归属录入 URL

	// 查询检测
	public static String ZG_URL_FORSHOWRESULT = null;// 账户查询

	public static String ZG_URL_FORSHOW00101DETAILRESULT = null;// 账户详情

	public static String ZG_URL_FORSHOWRESULTREPORT = null;// 高级查询

	public static String ZG_URL_FORSHOWRESULTDETAILSREPORT = null;// 高级查看报备类信息

	// 年检
	public static String ZG_URL_ANNUAL_INPUT = null;// 跳转到年检页面
	public static String ZG_URL_ANNUAL_SEARCH = null;// 根据账户查询账户信息
	public static String ZG_URL_ANNUAL_SUBMIT = null;// 账户年检提交

	// 个人账户信息查询
	// 跳转到个人查询页面
	public static String ZG_URL_toPersonSearchPage = null;
	// 录入查询条件查询账户信息
	public static String ZG_URL_personSearchCondition = null;
	// 查询账户的基本信息
	public static String ZG_URL_personDetialResult = null;
	// 高级查询
	public static String ZG_URL_personAdvanceSearch = null;


	//取消核准基本户 基本户
	public static String ZG_URL_JIBEN_APPRLOCALBASICPILOT = null;
	//已开立其他银行的账户情况查询
	public static String ZG_URL_ACCOUNT_OPEN_SEARCH=null;


	//取消核准基本户 变更验证
	public static String ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change = null;
	//取消核准基本户 变更成功
	public static String ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change_succsess = null;
	//取消核准非临时 开户
	public static String ZG_URL_feilinshi_apprnontempinsttempPilot = null;
	//取消核准非临时 变更
	public static String ZG_URL_feilinshi_changeOrgTempAccountPilot = null;
	//取消核准销户
	public static String ZG_URL_closeBasicPilot = null;
	//取消核准非临时 展期
	public static String ZG_URL_feilinshi_changeAccountTempLimitPilot = null;
	//基本存款账户密码重置
	public static String ZG_URL_depositorPwdReset = null;
	//补打基本户存款人信息
	public static String ZG_URL_resetJiBenPrint = null;
	static {
		init();
	}

	public static void init() {
		boolean loaded = Config.loadProperties(config, AmsConfig.class.getResourceAsStream("/" + CONFIG_PATH));
		if (!loaded) {
			throw new RuntimeException("载入账管配置文件[" + CONFIG_PATH + "]异常");
		}
		ZG_LOGIN_COUNT = NumberUtils.str2Int(config.getProperty("config.zg.login.count"), ZG_LOGIN_COUNT);
		ZG_URL_LOGIN = config.getProperty("config.zg.url.login");
		ZG_URL_BANK_QUERY_INPUT = config.getProperty("config.zg.url.bank.query.input");
		ZG_URL_BANK_QUERY_POST = config.getProperty("config.zg.url.bank.query.post");
		ZG_URL_BANK_QUERY_DETAIL = config.getProperty("config.zg.url.bank.query.detail");
		ZG_URL_ANNUAL_INPUT = config.getProperty("config.zg.url.annual.input");
		ZG_URL_ANNUAL_SEARCH = config.getProperty("config.zg.url.annual_check");
		ZG_URL_ANNUAL_SUBMIT = config.getProperty("config.zg.url.annual_submit");
		ZG_URL_ACCOUNT_OPEN = config.getProperty("config.zg.url.account_open");// 核准类开户
		ZG_URL_ACCOUNT_OPEN_LIST = config.getProperty("config.zg.url.open_list");// 核准类列表
		ZG_URL_ACCTOUNT_MODIFY = config.getProperty("config.zg.url.account_edit"); // 到时试试
		// 可以用config.zg.url.account_modify
		ZG_URL_GRYHJSZH = config.getProperty("config.zg.url.gryhjszh");
		ZG_URL_ADD_ENTANDTRADE = config.getProperty("config.zg.url.add_entandtrade");
		// 一般户开户
		ZG_URL_ACCOUNT_TOGENERAL = config.getProperty("config.zg.url.toGeneral");
		ZG_URL_ACCOUNT_TOOPENGENERALPAGE = config.getProperty("config.zg.url.toOpenPage");
		ZG_URL_ACCOUNT_GENERALVALIDATE = config.getProperty("config.zg.url.account_generAlValidate");
		ZG_URL_ACCOUNT_SHOWBASEINFO = config.getProperty("config.zg.url.account_showBaseInfo");
		ZG_URL_ACCOUNT_OPENGENERALACCTINFO = config.getProperty("config.zg.url.account_openGeneralAcctInfo");
		// 一般户变更
		ZG_URL_ACCOUNT_TOCHANGEGENERAL = config.getProperty("config.zg.url.toChangeGeneral");
		ZG_URL_ACCOUNT_TOCHANGEGENERALDETAIL = config.getProperty("config.zg.url.toChangeGeneralDetail");
		ZG_URL_ACCOUNT_CHANGE_GENERALVALIDATE = config.getProperty("config.zg.url.account_change_generAlValidate");
		ZG_URL_ACCOUNT_CHANGE_SHOWBASEINFO = config.getProperty("config.zg.url.account_change_showBaseInfo");
		ZG_URL_ACCOUNT_CHANGE_OPENGENERALACCTINFO = config.getProperty("config.zg.url.account_change_openGeneralAcctInfo");
		// 非预算专用单位开户
		ZG_URL_ACCOUNT_TOFYS = config.getProperty("config.zg.url.account_toFys");
		ZG_URL_ACCOUNT_TOOPENFYSPAGE = config.getProperty("config.zg.url.toOpenFysPage");
		ZG_URL_ACCOUNT_FYSACCOUNTVALIDATE = config.getProperty("config.zg.url.account_fysAccountValidate");
		ZG_URL_ACCOUNT_FYSACCOUNTSHOWBASEINFO = config.getProperty("config.zg.url.account_fysAccountshowBaseInfo");
		ZG_URL_ACCOUNT_OPENFYSACCOUNT = config.getProperty("config.zg.url.account_openFysAccount");
		// 非预算专用单位变更
		ZG_URL_ACCOUNT_TOCHANGEFYS = config.getProperty("config.zg.url.toChangeFys");
		ZG_URL_ACCOUNT_TOCHANGEFYSDETAIL = config.getProperty("config.zg.url.toChangeFysDetail");
		ZG_URL_ACCOUNT_CHANGEFYSACCOUNTVALIDATE = config.getProperty("config.zg.url.account_changefysAccountValidate");
		ZG_URL_ACCOUNT_CHANGEFYSACCOUNTSHOWBASEINFO = config.getProperty("config.zg.url.account_changefysAccountshowBaseInfo");
		ZG_URL_ACCOUNT_CHANGEFYSACCOUNT = config.getProperty("config.zg.url.account_changeFysAccount");
		// 撤销报备类
		ZG_URL_ACCOUNT_CLOSEBASIC = config.getProperty("config.zg.url.account_closeBasicData");
		ZG_URL_ACCOUNT_CLOSECOMMIT = config.getProperty("config.zg.url.account_closeCommit");
		// 久悬
		ZG_URL_ACCOUNT_SUPSENDTOBASIC = config.getProperty("config.zg.url.account_suspendBasicData");
		ZG_URL_ACCOUNT_SUSPENDCOMMIT = config.getProperty("config.zg.url.account_suspendCommit");
		// 查询检测
		ZG_URL_FORSHOWRESULT = config.getProperty("config.zg.url.bank.query.post");
		ZG_URL_FORSHOW00101DETAILRESULT = config.getProperty("config.zg.url.bank.query.detail");
		ZG_URL_FORSHOWRESULTREPORT = config.getProperty("config.zg.url.bank.query.gjSerarch");
		ZG_URL_FORSHOWRESULTDETAILSREPORT = config.getProperty("config.zg.url.bank.query.gjDetetailSearch");
		// 个人账户查询条件
		ZG_URL_toPersonSearchPage = config.getProperty("config.zg.url.toPersonSearchPage");
		ZG_URL_personSearchCondition = config.getProperty("config.zg.url.personSearchCondition");
		ZG_URL_personDetialResult = config.getProperty("config.zg.url.personDetialResult");
		ZG_URL_personAdvanceSearch = config.getProperty("config.zg.url.personAdvanceSearch");
		//取消核准url
		ZG_URL_JIBEN_APPRLOCALBASICPILOT = config.getProperty("config.zg.url.jiben_apprLocalBasicPilot");
		ZG_URL_ACCOUNT_OPEN_SEARCH = config.getProperty("config.zg.url.account_open_search");
		//取消核准基本户 变更验证
		ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change = config.getProperty("config.ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change");
		//取消核准基本户 变更成功
		ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change_succsess = config.getProperty("config.ZG_URL_JIBEN_APPRLOCALBASICPILOT_Change_success");
		//取消核准非临时 开户
		ZG_URL_feilinshi_apprnontempinsttempPilot = config.getProperty("config.zg.url.feilinshi_apprnontempinsttempPilot");
		//取消核准非临时 变更
		ZG_URL_feilinshi_changeOrgTempAccountPilot = config.getProperty("config.zg.url.feilinshi_changeOrgTempAccountPilot");
		//取消核准销户
		ZG_URL_closeBasicPilot = config.getProperty("config.zg.url.closeBasicPilot");
		//取消核准 非临时户 展期
		ZG_URL_feilinshi_changeAccountTempLimitPilot = config.getProperty("config.zg.url.feilinshi_changeAccountTempLimitPilot");
		//基本存款账户密码重置
		ZG_URL_depositorPwdReset = config.getProperty("config.zg.url.depositorPwdReset");
		//补打基本户存款人信息-config.zg.url.amsResetJibenPrint
		ZG_URL_resetJiBenPrint = config.getProperty("config.zg.url.amsResetJibenPrint");
		// 年检
		MyLog.debug("账管工作目录: " + Config.getSoftRootPath());
		MyLog.debug("账管登录URL: " + ZG_URL_LOGIN);
		MyLog.debug("查询银行账户INPUT: " + ZG_URL_BANK_QUERY_INPUT);
		MyLog.debug("查询银行账户POST: " + ZG_URL_BANK_QUERY_POST);
		MyLog.debug("查询银行账户明细URL: " + ZG_URL_BANK_QUERY_DETAIL);
		MyLog.debug("跳转年检URL: " + ZG_URL_ANNUAL_INPUT);
		MyLog.debug("年检账户查询URL: " + ZG_URL_ANNUAL_SEARCH);
		MyLog.debug("年检账户提交URL: " + ZG_URL_ANNUAL_SUBMIT);
		MyLog.debug("开户URL: " + ZG_URL_ACCOUNT_OPEN);
		MyLog.debug("开户核准类管理列表：" + ZG_URL_ACCOUNT_OPEN_LIST);
		MyLog.debug("变更URL: " + ZG_URL_ACCTOUNT_MODIFY);
		MyLog.debug("个人银行结算账户URL: " + ZG_URL_ACCOUNT_OPEN);
		MyLog.debug("行业归属URL: " + ZG_URL_ADD_ENTANDTRADE);
	}

	public static String getProperty(String key) {
		return config.getProperty(key);
	}
}
