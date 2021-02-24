package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 账管系统特殊户变更
 * 
 * @author zoulang
 *
 */
@Component
public class AmsTeshuChangeSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			urlPars.append("sdepkindhidden=&sdepnamehidden=");
			urlPars.append("&rltentname=&rltenthidden=&tradeanddomainhidden=");
			urlPars.append("&accNotCheckedEntDepositorInfo.sacckind=5"); // 存款帐户类型
			// 特殊存款账户
			// usertype登录用户类型 2普通登录用户 1人民银行
			urlPars.append("&usertype=2");
			urlPars.append("&managesign=2");
			urlPars.append("&tradeanddomainhiddentemp=");
			urlPars.append("&accchoosesign="); // 账号 1 开户许可证 2
			urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
			// 账号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
			// 银行机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBankCode(), amsChart));
			// 经 验证
			// 可以不填
			urlPars.append("&zmbh=");
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
			urlPars.append(EncodeUtils.encodStr(allAcct.getOrgCode(), amsChart));
			// 注册币种
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepfundkind=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getRegCurrencyTypeAms(), amsChart));
			// 注册资金（元为单位）
			urlPars.append("&accNotCheckedEntDepositorInfo.fdepfund=");
			urlPars.append(allAcct.getRegisteredCapital());
			// 证明文件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=").append(allAcct.getFileType());
			// 证明文件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
			// 证明文件种类2
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype2=").append(allAcct.getFileType2());
			// 证明文件编号2
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
			if (StringUtils.isNotEmpty(allAcct.getLegalType())) {
				urlPars.append("&accNotCheckedEntDepositorInfo.idepmanagestype=").append(allAcct.getLegalType());
			}
			// 法人姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmanagername=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));
			// 身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrekind=").append(allAcct.getLegalIdcardTypeAms());
			// 身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrecode=").append(allAcct.getLegalIdcardNo());
			// 上级单位名称
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
			// 上级基本存款账户开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptlic=").append(allAcct.getParAccountKey());
			// 上级组织机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanorgcode=").append(allAcct.getParOrgCode());
			// 上级法定代表人或单位负责人
			if (StringUtils.isNotEmpty(allAcct.getParLegalType())) {
				urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmankind=").append(allAcct.getParLegalName());
			}
			// 上级法人姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));
			// 上级身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrekind=").append(allAcct.getParLegalIdcardType());
			// 上级身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrecode=").append(allAcct.getParLegalIdcardNo());
			urlPars.append("&accNotCheckedEntDepositorInfo.sDepManRegion=").append(allAcct.getNationality());
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfundkind=").append(allAcct.getCapitalProperty());
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();

	}

}
