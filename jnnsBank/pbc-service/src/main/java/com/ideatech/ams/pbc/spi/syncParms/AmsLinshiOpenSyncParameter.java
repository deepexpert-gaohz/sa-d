package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统临时户开户接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsLinshiOpenSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			/**
			 * accType 账户类型 3为临时存款账户
			 */
			// urlPars += "accType=3";
			/**
			 * sdepkindhidden; sdepnamehidden; rltentname;tradeanddomainhidden
			 * accNotCheckedEntDepositorInfo.sacckind 临时存款账户 usertype 登录用户类型
			 * 2普通登录用户 1人民银行
			 */
			urlPars.append("sdepkindhidden=&sdepnamehidden=&rltentname=&rltenthidden=&tradeanddomainhidden=1");
			urlPars.append("&accNotCheckedEntDepositorInfo.sacckind=7&usertype=2&FD_DomainCode=");
			/**
			 * managesign判断是否是添加方法或修改方法 1 添加方法 2 修改方法
			 * tradeanddomainhiddentemp行业归属
			 */
			urlPars.append("&managesign=1&tradeanddomainhiddentemp=undefined");
			/**
			 * sdepregareahidden 登录机构所在地地区代码
			 */
			if (StringUtils.isEmpty(allAcct.getParCorpName())) {
				urlPars.append("&sdepregareahidden=fuck%21");
			} else {
				urlPars.append("&sdepregareahidden=").append(allAcct.getRegAreaCode());
			}
			urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
			/**
			 * sdepregareahidden accNotCheckedEntDepositorInfo.sdepname存款人名称
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptel电话
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptel=").append(allAcct.getTelephone());

			/**
			 * accNotCheckedEntDepositorInfo.sdepaddress 注册地址
			 */
			StringBuffer deatialAddress = new StringBuffer("");
			deatialAddress.append(allAcct.getIndusRegArea());
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepaddress=");
			urlPars.append(EncodeUtils.encodStr(deatialAddress.toString(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeppostcode邮政编码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeppostcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getZipCode(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeporgcode组织机构代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeporgcode=");
			urlPars.append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");

			/**
			 * accNotCheckedEntDepositorInfo.
			 * idepmanagestype法定代表人或单位负责人1法定代表人2单位负责人
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.idepmanagestype=").append(allAcct.getLegalType());
			/**
			 * accNotCheckedEntDepositorInfo.sdepmanagername法人姓名
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmanagername=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepmancrekind身份证件种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrekind=");
			urlPars.append(allAcct.getLegalIdcardTypeAms());
			/**
			 * accNotCheckedEntDepositorInfo.sdepmancrecode身份证编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepregarea注册地地区代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepregarea=").append(allAcct.getRegAreaCode());
			/**
			 * bregfundsign未标明注册资金
			 */
			if (StringUtils.isEmpty(allAcct.getRegisteredCapital()) && StringUtils.isEmpty(allAcct.getRegCurrencyTypeAms())) {
				urlPars.append("&bregfundsign=on");
			} else {
				/**
				 * accNotCheckedEntDepositorInfo.sdepfundkind注册资金币种
				 */
				urlPars.append("&accNotCheckedEntDepositorInfo.sdepfundkind=");
				urlPars.append(EncodeUtils.encodStr(allAcct.getRegCurrencyTypeAms(), amsChart));
				/**
				 * accNotCheckedEntDepositorInfo.fdepfund注册资金
				 */
				urlPars.append("&accNotCheckedEntDepositorInfo.fdepfund=");
				//urlPars.append(PbcBussUtils.getAmsRegisteredCapital(allAcct.getRegisteredCapital()));
				//单位统一为元
				urlPars.append(allAcct.getRegisteredCapital());
			}
			/**
			 * accNotCheckedEntDepositorInfo.saccfiletype1证明文件1种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileType(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccfilecode1证明文件1编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));

			/**
			 * accNotCheckedEntDepositorInfo.sdepwork经营范围
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepwork=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScope(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepnotaxfile无需办理税务登记证的文件或税务机关出具的证明
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepnotaxfile=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getNoTaxProve(), amsChart));

			/**
			 * accNotCheckedEntDepositorInfo.sdepcountaxcode国税登记证号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepcountaxcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getStateTaxRegNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepareataxcode地税登记证号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepareataxcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getTaxRegNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptname上级主管单位单位名称
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptlic上级基本存款账户开户许可证核准号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptlic=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParAccountKey(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmanorgcode上级组织机构代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanorgcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgCode(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmankind法定代表人或单位负责人
			 */
			if (allAcct.getParLegalType() != null && StringUtils.isNotEmpty(allAcct.getParLegalName())) {
				urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmankind=");
				urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalType(), amsChart));
			}
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmanname姓名
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmancrekind身份证件种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrekind=");
			urlPars.append(allAcct.getParLegalIdcardType().split(",")[0]);
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmancrecode 身份证件编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalIdcardNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccbankcode开户银行代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBankCode(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccno账号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.daccbegindate开户日期
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.daccbegindate=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctCreateDate(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.daccvailddate有效日期
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.daccvailddate=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getEffectiveDate(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
			urlPars.append("&accNotCheckedEntDepositorInfo.sDepManRegion=").append(allAcct.getNationality());
			urlPars.append("&add=%26nbsp%3B%C8%B7%26nbsp%3B%26nbsp%3B%B6%A8%26nbsp%3B&check=%26nbsp%3B%B7%B5%26nbsp%3B%26nbsp%3B%BB%D8%26nbsp%3B");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}
}
