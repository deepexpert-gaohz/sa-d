package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统非临时户开户接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiOpenSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			// urlPars.append("accType=4"); // 4 非临时机构临时存款账户
			/**
			 * accNotCheckedEntDepositorInfo.sacckind = 6 还不确定是什么意思 usertype 用户类型等级 （第几级操作员） 用户登录后的存在cookie里的用户类型 managesign 判断是否 添加 还是修改 1 是添加 2是修改 tradeanddomainhiddentemp 行业归属
			 */
			urlPars.append("accNotCheckedEntDepositorInfo.sacckind=6");
			// 用户登录后的级别
			urlPars.append("&usertype=2&managesign=1");
			// 前缀值，后缀值
			urlPars.append("&saccprefix=&saccpostfix=");
			// 基本存款账户开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbaselicno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountKey(), amsChart));
			// 基本存款账户开户地地区代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbasearea=").append(allAcct.getRegAreaCode());
			// 银行机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=").append(allAcct.getBankCode());
			// 账号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=").append(allAcct.getAcctNo());
			// 开户日期
			urlPars.append("&accNotCheckedEntDepositorInfo.daccbegindate=").append(allAcct.getAcctCreateDate());
			// 有效日期
			urlPars.append("&accNotCheckedEntDepositorInfo.daccvailddate=").append(allAcct.getEffectiveDate());
			// 申请开户原因
			urlPars.append("&accNotCheckedEntDepositorInfo.saccreason=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getCreateAccountReason(), amsChart));
			// 证明文件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=").append(allAcct.getAccountFileType());
			// 证明文件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo(), amsChart));
			// 项目部名称
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFlsProjectName(), amsChart));
			// 负责人姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrLegalName(), amsChart));
			// 身份证种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrekind=").append(allAcct.getFlsFzrLegalIdcardType());
			// 身份证编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrecode=").append(allAcct.getFlsFzrLegalIdcardNo());
			// 电话
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeptel=").append(allAcct.getFlsFzrTelephone());
			// 邮政编码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeppostcode=").append(allAcct.getFlsFzrZipCode());
			// 地址
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFlsFzrAddress(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
