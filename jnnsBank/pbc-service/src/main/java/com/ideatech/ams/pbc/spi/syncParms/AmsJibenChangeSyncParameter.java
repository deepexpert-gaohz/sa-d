package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统基本户变更接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsJibenChangeSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			urlPars.append("sdepkindhidden=&tradeanddomainhiddentemp=");
			urlPars.append("&sdepnamehidden=&rltentname=&zmbh=");
			urlPars.append("&rltenthidden=&tradeanddomainhidden=");
			// 存款帐户类型
			urlPars.append("&accNotCheckedEntDepositorInfo.sacckind=1");
			// usertype登录用户类型 2普通登录用户 1人民银行
			urlPars.append("&usertype=2");
			urlPars.append("&managesign=2");
			urlPars.append("&accchoosesign=");
            urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
			// 变更时 以账号 或者 开户许可证核准号 来匹配
			// 账号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
			// 开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbaselicno=");
			// urlPars.append(EncodeUtils.encodStr(allAcct.getAccountKey(),
			// amsChart));
			// 银行机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBankCode(), amsChart));

			// 可以不填
			// 以下是变更内容 无变更的空着

			// 存款人姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
			// 地址
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getIndusRegArea(), amsChart));
			// 邮政编码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeppostcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getZipCode(), amsChart));
			// 电话
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptel=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), amsChart));
			// 组织机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeporgcode=");
			urlPars.append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
			// 注册币种
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepfundkind=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getRegCurrencyTypeAms(), amsChart));
			// 注册资金
			urlPars.append("&accNotCheckedEntDepositorInfo.fdepfund=");
			urlPars.append(allAcct.getRegisteredCapital());
			if (StringUtils.isNotEmpty(allAcct.getFileType()) && allAcct.getFileType().equals("07")) {
				allAcct.setFileType("01");
			}
			// 证明文件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileType(), amsChart));
			// 证明文件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype2=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileType2(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode2=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo2(), amsChart));
			// 经营范围
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepwork=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScope(), amsChart));
			// 无需办理税务登记证的文件或税务机关出具的证明
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepnotaxfile=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getNoTaxProve(), amsChart));
			// 国税登记证号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepcountaxcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getStateTaxRegNo(), amsChart));
			// 地税登记证号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepareataxcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getTaxRegNo(), amsChart));
			// 法定代表人或单位负责人
			if (StringUtils.isNotEmpty(allAcct.getLegalType()) && !allAcct.getLegalType().equals("0")) {
				urlPars.append("&accNotCheckedEntDepositorInfo.idepmanagestype=").append(allAcct.getLegalType());
			}
			// 法人姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmanagername=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));
			// 身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrekind=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardTypeAms(), amsChart));
			// 身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
			// 上级单位名称
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
			// 上级基本存款账户开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptlic=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParAccountKey(), amsChart));
			// 上级组织机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanorgcode=");
//			urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgCode(), amsChart));
			urlPars.append(StringUtils.isNotEmpty(allAcct.getParOrgCode()) ? allAcct.getParOrgCode().replace("-", "") : "");
			// 上级法定代表人或单位负责人
			if (StringUtils.isNotEmpty(allAcct.getParLegalType()) && !"0".equals(allAcct.getParLegalType())) {
				urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmankind=").append(allAcct.getParLegalType());

			}
			// 上级姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));

			// 上级身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrekind=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalIdcardType(), amsChart));
			// 上级身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalIdcardNo(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.sDepManRegion=").append(allAcct.getNationality());
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
