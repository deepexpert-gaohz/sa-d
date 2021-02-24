package com.ideatech.ams.pbc.service.ams;

import com.ideatech.ams.pbc.common.HeadsCache;
import com.ideatech.ams.pbc.config.HttpConfig;
import com.ideatech.ams.pbc.dto.AmsDownAnnualTask;
import com.ideatech.ams.pbc.dto.AmsDownTask;
import com.ideatech.ams.pbc.dto.auth.LoginAuth;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.service.protocol.HttpResult;
import com.ideatech.ams.pbc.service.protocol.HttpUtils;
import com.ideatech.ams.pbc.utils.IOUtils;
import com.ideatech.ams.pbc.utils.Utils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AmsDownExcelServiceImpl implements AmsDownExcelService {


	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AmsSyncOperateService amsSyncOperateService;

	@Override
	public void downRHAccount(LoginAuth auth, AmsDownTask task) throws SyncException, Exception {
		// TODO 校验
		validaDownAccount(task);
		File folderPath = new File(task.getFolderPath());
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		/* 跳转到查询条件录入页面 并获取下载excel中所需参数值 */
		String url = auth.getDomain() + "/ams/query002.do?method=forShowQueryConditions";
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		HttpResult result = HttpUtils.get(url, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.getEncoding());
		String html = result.getHtml();
		if (html.indexOf("当前系统正在运行后台批处理，请稍等再进行业务") > -1) {
			throw new SyncException("人行账管系统服务已关闭,请工作时间使用");
		} else if (html.indexOf("用户信息失效") > 0 || html.indexOf("用户登录信息失效") > 0) {
			throw new SyncException("用户登录信息失效");
		}
		Document doc = Jsoup.parse(html);
		Elements els = doc.select("form[name=query002Form]");
		Element form = els == null ? null : els.get(0);
		Validate.notNull(form, "下载form表单找不到");
		/* 获取下载参数的默认值 */
		els = form.select("input[type=hidden]");
		Map<String, String> map = new HashMap<String, String>();
		for (Element e : els) {
			map.put(StringUtils.trim(e.attr("name")), StringUtils.trim(e.attr("value")));
		}
		els = form.select("select[name=formvo.sbanktypecode] option");
		Validate.noNullElements(els.toArray());
		Element option = els.get(0);
		String sbanktypecode = option.attr("value");
		map.put("begindate", task.getBeginDay());
		map.put("enddate", task.getEndDay());
		map.put("sbanktypecode", sbanktypecode);
		map.put("groupbankcodecol", task.getBankId());
		map.put("scurtype", "0");
		map.put("cruArray", "1");
		/* 查询要下载列表参数 */
		StringBuffer urlPars = new StringBuffer("");
		// sbanktype以银行机构代码 查询条件0行别，1银行机构代码，2银行名称，3操作员代码
		urlPars.append("&sbanktype=1");
		// userareacode 登录用户地区代码
		urlPars.append("&userareacode=${userareacode}");
		// userarealevel 登录用户地区层次
		urlPars.append("&userarealevel=${userarealevel}");
		// userbankcode 登录用户银行代码
		urlPars.append("&userbankcode=${userbankcode}");
		// usertype 登录用户 级别
		urlPars.append("&usertype=${usertype}");
		// tmpbankcode 默认值2
		urlPars.append("&tmpbankcode=${tmpbankcode}");
		// sinputbankcode、sinputbankname、formvo.operatorcode暂时无用
		urlPars.append("&sinputbankcode=&sinputbankname=&formvo.operatorcode=");
		// formvo.sbanktypecode 行别 城市商业银行
		urlPars.append("&formvo.sbanktypecode=${sbanktypecode}");
		// 要下载的银行行号
		urlPars.append("&groupbankcodecol=${groupbankcodecol}");
		// formvo.operationmanagekind 业务处理种类 1所有，2开立，3变更，4撤销
		urlPars.append("&formvo.operationmanagekind=1");
		// 账户性质 0所有，1基本户，2一般户，3非预算单位专户，4预算单位专户，5特殊专户，6非临时机构临时户，7临时机构临时户
		urlPars.append("&formvo.strArray=0&formvo.strArray=1");
		urlPars.append("&formvo.strArray=2&formvo.strArray=4");
		urlPars.append("&formvo.strArray=5&formvo.strArray=3");
		urlPars.append("&formvo.strArray=7&formvo.strArray=6");
		// 下载开始时间
		urlPars.append("&formvo.begindate=${begindate}");
		// //下载结束时间
		urlPars.append("&formvo.enddate=${enddate}");
		urlPars.append("&formvo.scurtype=${scurtype}");
		urlPars.append("&cruArray=${cruArray}");
		urlPars.append("&ispage=true");
		String tempURlPars = urlPars.toString();
		tempURlPars = Utils.format(tempURlPars, map);
		// 调用查询方法
		url = auth.getDomain() + "/ams/query002.do?method=forShow002DetailResult01";
		List<Header> heads = HeadsCache.getRhzgHeader("query_down");
		result = HttpUtils.post(url, tempURlPars, HttpConfig.HTTP_SOCKET_TIMEOUT * 2, HttpConfig.getEncoding(), auth.getCookie(), heads);
		result.makeHtml(HttpConfig.getEncoding());
		html = result.getHtml();
		logger.debug("------------人行下载Excel的结果[人行机构号"+task.getBankId()+"]--------");
		logger.debug(html);
		logger.info(html);
		logger.debug("------------人行下载Excel的结果[人行机构号"+task.getBankId()+"]--------");
		if (html.indexOf("提示信息") > -1) {
			String errorMessage = amsSyncOperateService.validateOp(html, "下载成功");
			logger.error("机构" + task.getBankId() + "下载excel时异常[" + errorMessage + "],返回的结果:" + html);
			throw new SyncException("机构" + task.getBankId() + "下载excel失败[" + errorMessage + "]");
		} else if (html.indexOf("共0行") > -1 && !map.get("userbankcode").equals(task.getBankId())) {
			throw new SyncException("该银行机构不是当前银行机构辖属的银行机构");
		}
		// 调用下载方法
		url = auth.getDomain() + "/ams/query002.do?method=forShow002DetailResult01";
		url = url + "&format=excel&" + tempURlPars;
		result = HttpUtils.get(url, auth.getCookie(), heads);
		heads = result.getHeaders();
		boolean isExl = false;
		for (Header head : heads) {
			String val = head.getValue();
			if (val.indexOf("application/vnd.ms-excel") > -1) {
				isExl = true;
			} else if (val.indexOf("text/html") > -1) {
				isExl = false;
				break;
			}
		}
		if (isExl) {
			byte[] bytes = result.getBuffer();
			IOUtils.buildFile(getFilePath(task.getFolderPath(), task.getBankId()), bytes);
		} else {
			html = result.makeHtml(HttpConfig.getEncoding());
			logger.info("下载{}excel失败:\n{}", new Object[] { task.getBankId(), html });
			if (html.indexOf("error") > -1) {
				doc = Jsoup.parse(html);
				String errorInfo = doc.select("div[class=error]").html();
				throw new SyncException("下载excel时，下载方法失败" + errorInfo);
			}
		}

	}

	private void validaDownAccount(AmsDownTask task) throws SyncException {
		if (task == null) {
			throw new SyncException("下载任务对象为空");
		}
		if (StringUtils.isBlank(task.getBankId())) {
			throw new SyncException("银行机构代码不能为空");
		}
		if (StringUtils.isBlank(task.getFolderPath())) {
			throw new SyncException("下载的文件夹路径不能为空");
		}
		if (StringUtils.isBlank(task.getBeginDay())) {
			throw new SyncException("下载开始时间不能为空");
		}
		if (StringUtils.isBlank(task.getEndDay())) {
			throw new SyncException("下载截止日期不能为空");
		}

	}

	@Override
	public void downAnnualAccount(LoginAuth auth, AmsDownAnnualTask task) throws SyncException, Exception {
		validaDownAnnualAccount(task);
		File folderPath = new File(task.getFolderPath());
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		/* 跳转到查询条件录入页面 并获取下载excel中所需参数值 */
		String url = auth.getDomain() + "/ams/query005.do?method=forShowQueryConditions";
		List<Header> headers = HeadsCache.getRhzgCommon(auth.getDomain());
		HttpResult result = HttpUtils.get(url, auth.getCookie(), headers);
		result.makeHtml(HttpConfig.getEncoding());
		String html = result.getHtml();
		if (html.indexOf("功能已被暂停") > -1) {
			throw new SyncException("下载年检excel功能已被关闭", html);
		}
		Document doc = Jsoup.parse(html);
		Elements els = doc.select("form[name=query005Form]");
		Element form = els == null ? null : els.get(0);
		Validate.notNull(form, "下载form表单找不到");
		/* 获取下载参数的默认值 */
		els = form.select("input[type=hidden]");
		Map<String, String> map = new HashMap<String, String>();
		for (Element e : els) {
			map.put(StringUtils.trim(e.attr("name")), StringUtils.trim(e.attr("value")));
		}
		els = form.select("select[name=sbanktypecode] option");
		Validate.noNullElements(els.toArray());
		Element option = els.get(0);
		String sbanktypecode = option.attr("value");
		map.put("scheckyear", task.getAnnualYear());// 年检年份
		map.put("scheckyeartype", task.getCheckYearType());// 年检状况
		map.put("sbanktypecode", sbanktypecode);
		map.put("groupbankcodecol", task.getBankId());
		/* 查询要下载列表参数 */
		StringBuffer urlPars = new StringBuffer("");
		// sbanktype以银行机构代码 查询条件0行别，1银行机构代码，2银行名称，3操作员代码
		urlPars.append("&sbanktype=1");
		// userareacode 登录用户地区代码
		urlPars.append("&userareacode=${userareacode}");
		// userarealevel 登录用户地区层次
		urlPars.append("&userarealevel=${userarealevel}");
		// userbankcode 登录用户银行代码
		urlPars.append("&userbankcode=${userbankcode}");
		// usertype 登录用户 级别
		urlPars.append("&usertype=${usertype}");
		// tmpbankcode 默认值2
		urlPars.append("&tmpbankcode=${tmpbankcode}");
		// sinputbankcode、sinputbankname
		urlPars.append("&sinputbankcode=&sinputbankname=");
		// formvo.sbanktypecode 行别 城市商业银行
		urlPars.append("&sbanktypecode=${sbanktypecode}");
		// 要下载的银行行号
		urlPars.append("&groupbankcodecol=${groupbankcodecol}");
		// 年检年份 当前年份减1
		urlPars.append("&scheckyear=${scheckyear}");
		// 年检状况 0 全部 1 已进行年检 2未进行年检
		urlPars.append("&scheckyeartype=${scheckyeartype}");
		String tempURlPars = urlPars.toString();
		tempURlPars = Utils.format(tempURlPars, map);
		// 调用查询方法
		url = auth.getDomain() + "/ams/query005.do?method=forShowResult";
		List<Header> heads = HeadsCache.getRhzgHeader("queryAnnual_down");
		result = HttpUtils.post(url, tempURlPars, HttpConfig.getEncoding(), HttpConfig.HTTP_SOCKET_TIMEOUT * 2, auth.getCookie(), heads, null);
		result.makeHtml(HttpConfig.getEncoding());
		html = result.getHtml();
		if (html.indexOf("提示信息") > -1) {
			logger.error("机构" + task.getBankId() + "下载年检账户excel时异常,返回的结果:" + html);
			throw new SyncException("下载年检账户excel时，查询账户信息失败");
		} else if (html.indexOf("共0行") > -1 && !map.get("userbankcode").equals(task.getBankId())) {
			throw new SyncException("该银行机构不是当前银行机构辖属的银行机构");
		}
		// 调用下载方法
		url = auth.getDomain() + "/ams/query005.do?method=forShowResult";
		url = url + "&format=excel&" + tempURlPars;
		result = HttpUtils.get(url, 2000 * 60 * 5, auth.getCookie(), heads, null);
		heads = result.getHeaders();
		boolean isExl = false;
		for (Header head : heads) {
			String val = head.getValue();
			if (val.indexOf("application/vnd.ms-excel") > -1) {
				isExl = true;
			} else if (val.indexOf("text/html") > -1) {
				isExl = false;
				break;
			}
		}
		if (isExl) {
			byte[] bytes = result.getBuffer();
			IOUtils.buildFile(getFilePath(task.getFolderPath(), task.getBankId()), bytes);
		} else {
			html = result.makeHtml(HttpConfig.getEncoding());
			logger.info("下载{}excel失败:\n{}", new Object[] { task.getBankId(), html });
			if (html.indexOf("error") > -1) {
				doc = Jsoup.parse(html);
				String errorInfo = doc.select("div[class=error]").html();
				throw new SyncException("下载excel时，下载方法失败" + errorInfo);
			}
		}
	}

	private void validaDownAnnualAccount(AmsDownAnnualTask task) throws SyncException {
		if (task == null) {
			throw new SyncException("年检下载任务对象为空");
		}
		if (StringUtils.isBlank(task.getAnnualYear())) {
			throw new SyncException("年检年份不能为空");
		}
		try {
			Integer.valueOf(task.getAnnualYear());
		} catch (NumberFormatException e) {
			throw new SyncException("年检年份格式不正确");
		}
		if (StringUtils.isBlank(task.getBankId())) {
			throw new SyncException("银行机构代码不能为空");
		}
		if (StringUtils.isBlank(task.getFolderPath())) {
			throw new SyncException("下载的文件夹路径不能为空");
		}
	}

	/**
	 * 根据文件夹和bankId成要下载文件的文件名
	 * 
	 * @param folderPath
	 * @param bankId
	 * @return
	 */
	private String getFilePath(String folderPath, String bankId) {
		String[] lastChar = { "/", "\\" };
		if (!ArrayUtils.contains(lastChar, folderPath.substring(folderPath.length() - 1))) {
			return folderPath + File.separator + bankId + ".xls";
		} else {
			return folderPath + bankId + ".xls";
		}
	}


}
