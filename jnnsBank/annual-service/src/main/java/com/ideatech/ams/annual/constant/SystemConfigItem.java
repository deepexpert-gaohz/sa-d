/**
 * 
 */
package com.ideatech.ams.annual.constant;

/**
 * 系统配置项
 *
 * @author zoulang
 * @date 2015年7月10日 下午5:57:53
 * @since 1.0.0
 */
public interface SystemConfigItem {

	/**
	 * 创建用户时的默认密码
	 */
	String DEFAULT_PASSWORD = "defaultPassword";

	/**
	 * 人行ams默认Ip
	 * 
	 * @since 1.0.0
	 */
	String AMS_DEFAULT_IP = "amsDefaultIp";
	/**
	 * 联网核查人行IP
	 * hy
	 * 20180104
	 */
	String AMS_IDCARD_IP="amsIDCardIp";
	/**
	 * 开销户流程 check: 审核、上报同时 sync: 审核上报分开，先审核后上报
	 */
	String PROCESSOR_TYPE = "processType";
	/**
	 * 年检年份 输入当年的年份
	 */
	String ANNUAL_YEAR = "annualYear";
	/**
	 * 年检是否开始
	 */
	String ANNUAL_BEGIN = "annualBegin";

	/**
	 * 比对不一致颜色设置
	 */
	String ANNUAL_COLOR = "annualColor";

	/**
	 * 核心文件最后下载的日期
	 */
	String LASTCOREFILEDATE = "lastcoreFileDate";
	/**
	 * 个人报备文件最后下载日期
	 */
	String LASTPERSONFILEDATE = "lastPersonFileDate";
	/**
	 * 同业存放文件最后下载日期
	 */
	String LASTTRADEFILEDATE = "lastTradeFileDate";
	/**
	 * 证件到期最后下载日期按照月份来。
	 */
	String LASTCERTIFICATEFILEDATE = "lastCertificateFileDate";
	/**
	 * 预上线模式
	 */
	String isReadyProduction = "isReadyProduction";
	/**
	 * 预上线的机构的机构列表,用逗号分割
	 */
	String readyProductionOrgans = "readyProductionOrgans";
	/**
	 * 配置回调核心时间yyyyMMdd格式
	 */
	String CALLBACKDATE = "callBackDate";

	/**
	 * 系统时间
	 */
	String SystemName = "systemName";
	/**
	 * 三要素url
	 */
	String url3EleVal = "url3EleVal";

	/**
	 * 是否使用代理
	 */
	String PROXY = "proxy";

	/**
	 * 是否包含影像
	 */
	String IMAGE = "image";

	/**
	 * 是否工作时间暂停采集
	 */
	String PBC_COLLECT_PAUSE = "pbcCollectPause";

	/**
	 * 工作时间暂停采集IP
	 */
	String PBC_COLLECT_PAUSE_ORGANS = "pbcCollectPauseOrgans";

	/**
	 * 年检中是否忽略年报
	 */
	String ANNUAL_REPORT_IGNORE = "annualReportIgnore";

	/**
	 * 人行不打标记ip
	 */
	String ANNUAL_NOT_SUBMIT_ORGANS = "annualNotSubmitOrgans";

	/**
	 * 人行不打标记ip
	 */
	String ANNUAL_FINISH_DATE = "annualFinishDate";
	
	/**
	 * T+1是否自动上报
	 */
	String T_PLUS_ONE_AUTO_SYNC = "tplusoneAutoSync";
	
	/**
	 * 九江银行核心推送时间配置
	 */
	String DELAY_DATE = "delayDate";
	
	/**
	 * 核准类推送时间配置天数
	 */
	String HZ_PLUS_DAYS = "hzPlusDays";
	
	/**
	 * 工商信息有效天数
	 */
	String SAIC_VALID_DAYS = "saicValidDays";

}
