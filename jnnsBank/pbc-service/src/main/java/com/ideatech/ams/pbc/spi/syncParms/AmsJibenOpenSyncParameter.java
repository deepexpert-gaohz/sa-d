package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import com.ideatech.common.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统基本户开户接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsJibenOpenSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer("");
		try {
			/**
			 * accType 账户类型 1为基础存款账户 accNotCheckedEntDepositorInfo.sacckind =1是基本账户 基本账户为1
			 */
			urlPars.append("sdepkindhidden=&sdepnamehidden=&rltentname=");
			/**
			 * 1 是添加 2是修改 tradeanddomainhiddentemp 行业归属
			 */
			urlPars.append("&rltenthidden=&tradeanddomainhidden=1&tradeanddomainhiddentemp=");
			/**
			 * usertype 用户类型等级 （第几级操作员） 用户登录后的存在cookie里的用户类型 managesign 判断是否 添加 还是修改
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sacckind=1&usertype=2&managesign=1");
			/**
			 * FD_DomainCode 行业归属
			 */
			urlPars.append("&FD_DomainCode=");
			/**
			 * sdepregareahidden 注册地地区代码
			 */
			urlPars.append("&sdepregareahidden=").append(allAcct.getRegAreaCode());
			urlPars.append("&sdepregionhidden=").append(allAcct.getNationality());
			/**
			 * sdepregareahidden accNotCheckedEntDepositorInfo sdepname存款人名称
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getDepositorName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptel 电话
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptel=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepaddress 地址 包含省市区
			 */
			/*StringBuffer deatialAddress = new StringBuffer("");
			if (!StringUtils.contains(allAcct.getRegAddress(), allAcct.getRegProvinceCHName())) {
				deatialAddress.append(allAcct.getRegProvinceCHName());
			}
			if (!StringUtils.contains(allAcct.getRegAddress(), allAcct.getRegCityCHName())) {
				deatialAddress.append(allAcct.getRegCityCHName());
			}
			if(!StringUtils.contains(allAcct.getRegAddress(), allAcct.getRegAreaCHName())){
				deatialAddress.append(allAcct.getRegAreaCHName());
			}
			deatialAddress.append(allAcct.getRegAddress());*/
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getIndusRegArea(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeppostcode 邮政编码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeppostcode=").append(allAcct.getZipCode());
			/**
			 * accNotCheckedEntDepositorInfo.sdepkind 存款人类别
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepkind=").append(allAcct.getDepositorType());
			/**
			 * accNotCheckedEntDepositorInfo.sdeporgcode 组织机构代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeporgcode=");
			urlPars.append(StringUtils.isNotEmpty(allAcct.getOrgCode()) ? allAcct.getOrgCode().replaceAll("-", "") : "");
			/**
			 * accNotCheckedEntDepositorInfo.idepmanagestype 法定代表人或单位负责人
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.idepmanagestype=").append(allAcct.getLegalType());
			/**
			 * accNotCheckedEntDepositorInfo.sdepmanagername 法人姓名
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmanagername=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepmancrekind 身份证件种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrekind=").append(allAcct.getLegalIdcardTypeAms());
			/**
			 * accNotCheckedEntDepositorInfo.sdepmancrecode 身份证件编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepmancrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepregarea 注册地地区代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepregarea=").append(allAcct.getRegAreaCode());
			/**
			 * accNotCheckedEntDepositorInfo.sdepfundkind 注册资金币种
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepfundkind=");
			urlPars.append(allAcct.getRegCurrencyTypeAms());
			/**
			 * accNotCheckedEntDepositorInfo.fdepfund 注册资金
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.fdepfund=");
			//urlPars.append(PbcBussUtils.getAmsRegisteredCapital(allAcct.getRegisteredCapital()));
			//单位统一为元
			urlPars.append(allAcct.getRegisteredCapital());
			/**
			 * accNotCheckedEntDepositorInfo.saccfiletype1 证明文件1种类
			 */
			if (StringUtils.isNotEmpty(allAcct.getFileType()) && allAcct.getFileType().equals("07")) {
				allAcct.setFileType("01");
			}
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=").append(allAcct.getFileType());
			/**
			 * &accNotCheckedEntDepositorInfo.saccfilecode1 证明文件1编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.deffectdate 工商营业执照有效期
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.deffectdate=").append(allAcct.getTovoidDate());
			/**
			 * accNotCheckedEntDepositorInfo.saccfiletype2 证明文件2种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype2=").append(allAcct.getFileType2());
			/**
			 * accNotCheckedEntDepositorInfo.saccfilecode2 证明文件2编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode2=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFileNo2(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepwork 经营范围
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepwork=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getBusinessScope(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepnotaxfile 无需办理税务登记证的文件或税务机关出具的证明
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepnotaxfile=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getNoTaxProve(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdepcountaxcode 国税登记证号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepcountaxcode=").append(allAcct.getStateTaxRegNo());
			/**
			 * accNotCheckedEntDepositorInfo.sdepareataxcode 地税登记证号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdepareataxcode=").append(allAcct.getTaxRegNo());
			/**
			 * accNotCheckedEntDepositorInfo.sdeptname 上级单位名称
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParCorpName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptlic 上级基本存款账户开户许可证核准号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptlic=").append(allAcct.getParAccountKey());
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmanorgcode 上级组织机构代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanorgcode=");
			urlPars.append(StringUtils.isNotEmpty(allAcct.getParOrgCode()) ? allAcct.getParOrgCode().replace("-", "") : "");
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmanname 上级法人的姓名
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmankind 上级法定代表人或单位负责人 1法人 2单位负责人
			 */
			if (allAcct.getParLegalType() != null && StringUtils.isNotEmpty(allAcct.getParLegalType().toString())) {
				urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmankind=").append(allAcct.getParLegalType());
			}
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmancrekind 上级身份证件种类 账户信息：
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrekind=");
			urlPars.append(allAcct.getParLegalIdcardType().split(",")[0]);
			/**
			 * accNotCheckedEntDepositorInfo.sdeptmancrecode 上级身份证件编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.sdeptmancrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getParLegalIdcardNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccbankcode 开户银行代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=").append(allAcct.getBankCode());
			/**
			 * accNotCheckedEntDepositorInfo.saccno 账号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.daccbegindate 开户日期
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.daccbegindate=").append(allAcct.getAcctCreateDate());
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
			urlPars.append("&accNotCheckedEntDepositorInfo.sDepManRegion=").append(allAcct.getNationality());
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
