package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 账管系统特殊户开户接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsTeshuOpenSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			// urlPars.append("accType=5"); // 5 特殊单位专用存款账户
			/**
			 * accNotCheckedEntDepositorInfo.sacckind = 6 还不确定是什么意思 usertype
			 * 用户类型等级 （第几级操作员） 用户登录后的存在cookie里的用户类型 managesign 判断是否 添加 还是修改 1
			 * 是添加 2是修改 tradeanddomainhiddentemp 行业归属
			 */
			urlPars.append("sdepkindhidden=&sdepnamehidden=&rltentname=&rltenthidden=");
			urlPars.append("&tradeanddomainhidden=&accNotCheckedEntDepositorInfo.sacckind=5");
			urlPars.append("&usertype=2&managesign=1");
			urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
			// 存款人名称
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
			// 电话
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptel=").append(allAcct.getTelephone());
			// 地址
			StringBuffer deatialAddress = new StringBuffer("");
			deatialAddress.append(allAcct.getIndusRegArea());
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepaddress=");
			urlPars.append(EncodeUtils.encodStr(deatialAddress.toString(), amsChart));
			// 邮政编码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeppostcode=").append(allAcct.getZipCode());
			// 存款人类别
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepkind=").append(allAcct.getDepositorType());
			// 组织机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeporgcode=").append(allAcct.getOrgCode());
			// 法定代表人或单位负责人
			urlPars.append("&accNotCheckedEntDepositorInfo.idepmanagestype=").append(allAcct.getLegalType());
			// 姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmanagername=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));
			// 身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrekind=").append(allAcct.getLegalIdcardTypeAms());
			// 身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrecode=").append(allAcct.getLegalIdcardNo());
			// 注册地区代码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepregarea=").append(allAcct.getRegAreaCode());
			// 注册资金币种
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepfundkind=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getRegCurrencyTypeAms(), amsChart));
			// 注册资金
			urlPars.append("&accNotCheckedEntDepositorInfo.fdepfund=");
			//urlPars.append(PbcBussUtils.getAmsRegisteredCapital(allAcct.getRegisteredCapital()));
			//单位统一为元
			urlPars.append(allAcct.getRegisteredCapital());
			// 未标明注册资金
			urlPars.append("&bregfundsign=").append(allAcct.getIsIdentification() == "1" ? "on" : "");
			// 证明文件1种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=").append(allAcct.getFileType());
			// 证明文件1编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
			// 证明文件2种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype2=").append(allAcct.getFileType2());
			// 证明文件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode2=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo2(), amsChart));
			// 无需办理税务登记证的文件或税务机关出具的证明
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepnotaxfile=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getNoTaxProve(), amsChart));
			// 国税登记证号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepcountaxcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getStateTaxRegNo(), amsChart));
			// 地税登记证号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepareataxcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getTaxRegNo(), amsChart));
			urlPars.append("&bcashsign=").append(allAcct.getEnchashmentType()); // 取现标识
			// 上级法人或主管单位信息
			// 上级单位名称
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
			// 基本存款账户开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptlic=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParAccountKey(), amsChart));
			// 组织机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanorgcode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParOrgCode(), amsChart));
			// 法定代表人或单位负责人
			if (StringUtils.isNotEmpty(allAcct.getParLegalType())) {
				urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmankind=").append(allAcct.getParLegalType());
			}
			// 上级法人姓名
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));
			// 上级身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrekind=").append(allAcct.getParLegalIdcardType());
			// 上级身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrecode=").append(allAcct.getParLegalIdcardNo());
			// 开户银行机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=").append(allAcct.getBankCode());
			// 账号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=").append(allAcct.getAcctNo());
			// 开户日期
			urlPars.append("&accNotCheckedEntDepositorInfo.daccbegindate=").append(allAcct.getAcctCreateDate());
			//经营范围
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepwork=").append(EncodeUtils.encodStr(allAcct.getBusinessScope(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
			urlPars.append("&accNotCheckedEntDepositorInfo.sDepManRegion=").append(allAcct.getNationality());
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfundkind=").append(allAcct.getCapitalProperty());
			
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
