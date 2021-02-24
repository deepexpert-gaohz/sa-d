package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.enums.SyncAcctType;
import com.ideatech.ams.pbc.enums.SyncOperateType;
import com.ideatech.ams.pbc.enums.SyncSystem;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.spi.SyncValidater;
import com.ideatech.ams.pbc.utils.DateUtils;
import com.ideatech.ams.pbc.utils.IDCardUtils;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.ams.pbc.utils.RegexUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public abstract class AbstractSyncValidater implements SyncValidater {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void validater(AllAcct allAcct, SyncSystem syncSystem) throws SyncException, Exception {
		valdiateCommon(allAcct);
		doValidater(allAcct);
		logger.info("账号" + allAcct.getAcctNo() + "报备" + syncSystem.getFullName() + "规则校验结束，满足上报规则");
	}

	protected abstract void doValidater(AllAcct allAcct) throws SyncException, Exception;

	/**
	 * 公共校验器
	 * 
	 * @param allAcct
	 * @throws SyncException
	 */
	protected void valdiateCommon(AllAcct allAcct) throws SyncException {
		StringBuilder sb = new StringBuilder();
		if (allAcct == null) {
			sb.append("上报对象不能为空");
			throw new SyncException(sb.toString());
		}
		if (StringUtils.isBlank(allAcct.getAcctNo())) {
			sb.append("账号不能为空");
			throw new SyncException(sb.toString());
		} else if (allAcct.getAcctNo().getBytes().length > 32) {
			throw new SyncException("账号不能超过32个字符");
		}
		if (allAcct.getAcctType() == null) {
			sb.append("账户性质不能为空");
			throw new SyncException(sb.toString());
		}
		if (StringUtils.isBlank(allAcct.getBankCode())) {
			throw new SyncException("人行机构代码不能为空");
		} else if (allAcct.getBankCode().getBytes().length != 12) {
			throw new SyncException("银行机构代码长度不正确，应12位");
		} else if (!RegexUtils.isNumberOrLetter(allAcct.getBankCode())) {
			throw new SyncException("银行机构代码应为12位数字");
		}
		if (allAcct.getOperateType() == null) {
			sb.append("业务类型不能为空");
			throw new SyncException(sb.toString());
		}
		// 开户日期 若为空默认当前时间
		if (allAcct.getOperateType() == SyncOperateType.ACCT_OPEN) {
			if (StringUtils.isBlank(allAcct.getAcctCreateDate())) {
				allAcct.setAcctCreateDate(DateUtils.getNowDateShort());
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (format.parse(allAcct.getAcctCreateDate()).getTime() > new Date().getTime()) {
					throw new SyncException("开户日期不能大于当前日期");
				}
			} catch (ParseException e) {
				throw new SyncException("开户日期格式错误");
			}
		}
		if (allAcct.getOperateType() == SyncOperateType.ACCT_REVOKE) {
			validateRevokeAcctType(allAcct.getAcctType(),allAcct);
		}
	}

	/**
	 * 校验填写客户信息common开户校验(基本户、临时机构临时户、特殊单位专用户)
	 * 
	 * @param allAcct
	 * @return
	 * @exception
	 */
	protected void validateCustomerInfoCommonOpen(AllAcct allAcct) throws SyncException,Exception {
		// 证件类型、编号
		if (StringUtils.isBlank(allAcct.getFileType())) {
			throw new SyncException("证明文件1种类不能为空");
		}
		if (StringUtils.isBlank(allAcct.getFileNo())) {
			throw new SyncException("证明文件1编号不能为空");
		}
		if ((StringUtils.isNotEmpty(allAcct.getFileType2()) || StringUtils.isNotEmpty(allAcct.getFileNo2()))
				&& (StringUtils.isBlank(allAcct.getFileType2()) || StringUtils.isBlank(allAcct.getFileNo2()))) {
			throw new SyncException("证明文件2证据类型与证件编号必须同时填写");
		}
		// 存款人名称
		if (StringUtils.isBlank(allAcct.getDepositorName())) {
			throw new SyncException("存款人名称不能为空");
		} else if (allAcct.getDepositorName().getBytes("GBK").length > 128) {
			throw new SyncException("款人名称超长(128个字符或64个汉字),请重新录入");
		}
		// 法人类型
		if (StringUtils.isBlank(allAcct.getLegalType())) {
			throw new SyncException("法人类型不能为空");
		}
		// 法人姓名
		if (StringUtils.isBlank(allAcct.getLegalName())) {
			throw new SyncException("法人姓名不能为空");
		}
		// 法人证件类型、编号
		if (StringUtils.isBlank(allAcct.getLegalIdcardNo())) {
			throw new SyncException("法人身份编号不能为空");
		} else if (allAcct.getLegalIdcardNo().getBytes().length > 18) {
			throw new SyncException("法人身份编号长度不能大于18位");
		}
		if (StringUtils.isBlank(allAcct.getLegalIdcardTypeAms())) {
			throw new SyncException("法人证件类型不能为空");
		} else if (allAcct.getLegalIdcardTypeAms().equals("1") && StringUtils.isNotEmpty(IDCardUtils.IDCardValidate(allAcct.getLegalIdcardNo()))) {
			throw new SyncException("请录入正确的法定代表人或负责人身份证件编号");
		}
		// 国地税
		if (StringUtils.isNotEmpty(allAcct.getNoTaxProve()) && allAcct.getNoTaxProve().getBytes().length > 32) {
			throw new SyncException("无需办理税务登记证的文件或税务机关出具的证明不能超过32个字符或16个汉字,请重新录入!");
		}
		// 当存款人类别为企业法人、非企业法人、个体工商户时 国地税获与无需办理税务登记证明必须有个必填
		if (StringUtils.isBlank(allAcct.getNoTaxProve())) {
			String[] arrays = { "01", "02", "13", "14" };
			if (ArrayUtils.contains(arrays, allAcct.getDepositorType())) {
				if (StringUtils.isBlank(allAcct.getStateTaxRegNo()) && StringUtils.isBlank(allAcct.getTaxRegNo())) {
					throw new SyncException("请录入国税登记证号或地税登记证号,或填写无需办理税务登记证的文件或税务机关出具的证明!");
				}
			}
		}
		// 开户时间
		if (StringUtils.isBlank(allAcct.getAcctCreateDate())) {// 默认当前时间
			allAcct.setAcctCreateDate(DateUtils.getNowDateShort());
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (format.parse(allAcct.getAcctCreateDate()).getTime() > new Date().getTime()) {
				throw new SyncException("开户日期不能大于当前日期");
			}
		} catch (ParseException e) {
			throw new SyncException("开户日期格式错误,格式应为YYYY-MM-DD");
		}
		// 工商有效日期
		if (StringUtils.isNotEmpty(allAcct.getEffectiveDate())) {
			try {
				if (format.parse(allAcct.getEffectiveDate()).getTime() < new Date().getTime()) {
					throw new SyncException("有效期不能小于当前日期");
				}
			} catch (ParseException e) {
				throw new SyncException("证件有效日期格式错误,格式应为YYYY-MM-DD");
			}
		}
		// 注册地地区代码
		String validResult=PbcBussUtils.valiDateRegAreaCode(allAcct.getRegAreaCode());
		if(StringUtils.isNotBlank(validResult)){
			throw new SyncException(validResult);
		}
		// 行业归属
		if (StringUtils.isBlank(allAcct.getIndustryCode())) {
			throw new SyncException("行业归属不能为空");
		}
		// 工商注册地址（上报人行）
		if (allAcct.getAcctType() != null && allAcct.getAcctType() == SyncAcctType.jiben && StringUtils.isBlank(allAcct.getIndusRegArea())) {
			throw new SyncException("工商注册地址不能为空");
		}
		// 注册资金
		if (StringUtils.isBlank(allAcct.getIsIdentification())) {
			throw new SyncException("未标明注册资金不能为空");
		} else {
			//未选择未标明时需要校验必填
			if (!"1".equals(allAcct.getIsIdentification())) {
				if (StringUtils.isBlank(allAcct.getRegisteredCapital())) {
					throw new SyncException("注册资金不能为空");
				}
				if (StringUtils.isBlank(allAcct.getRegCurrencyTypeAms())) {
					throw new SyncException("注册资金币种不能为空");
				}
			}
			//有值必须校验格式
			if (StringUtils.isNotBlank(allAcct.getRegisteredCapital())) {
				if (!RegexUtils.isDecimal(allAcct.getRegisteredCapital())) {
					throw new SyncException("请录入正确的注册资金金额");
				}
				if (StringUtils.isBlank(allAcct.getRegCurrencyTypeAms())) {
					throw new SyncException("注册资金有值时，注册资金币种不能为空");
				}
			}

			if (StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms())) {
				if (StringUtils.isBlank(allAcct.getRegisteredCapital())) {
					throw new SyncException("注册资金币种有值时，注册资金不能为空");
				} else {
					if (!RegexUtils.isDecimal(allAcct.getRegisteredCapital())) {
						throw new SyncException("请录入正确的注册资金金额");
					}
				}
			}
		}

		// 经营范围
		if (StringUtils.isBlank(allAcct.getBusinessScope()) && allAcct.getFileType().equals("01")) {
			throw new SyncException("经营范围不能为空");
		} else if (allAcct.getBusinessScope().length() > 489) {
			throw new SyncException("经营范围超长(978个字符或489个汉字),请重新录入");
		}
		// 联系电话
		if (StringUtils.isBlank(allAcct.getTelephone())) {
			throw new SyncException("联系电话不能为空");
		}
		// 邮政编码
		if (StringUtils.isBlank(allAcct.getZipCode())) {
			throw new SyncException("邮政编码不能为空");
		} else if (!RegexUtils.isZipCOde(allAcct.getZipCode())) {
			throw new SyncException("邮政编码格式不正确");
		}
		// 组织机构代码
		if (StringUtils.isNotEmpty(allAcct.getOrgCode())) {
			if (allAcct.getOrgCode().getBytes().length != 9) {
				throw new SyncException("组织机构代码长度不正确，应为9位");
			}
		}
		/* 上级信息 */
		if (StringUtils.isNotEmpty(allAcct.getParLegalName()) && allAcct.getParLegalName().getBytes().length > 32) {
			throw new SyncException("上级法人及主管单位的法定代表人或单位负责人姓名不能超过32个字符或16个汉字,请重新录入");
		}
		if (StringUtils.isNotEmpty(allAcct.getParLegalName()) || StringUtils.isNotEmpty(allAcct.getParCorpName())) {
			if (StringUtils.isBlank(allAcct.getParLegalType())) {
				throw new SyncException("您录入了上级法人及主管单位的法定代表人或单位负责人姓名,请再选择法定代表人或单位负责人及相关信息");
			}
		}
		if (StringUtils.isNotEmpty(allAcct.getParLegalName()) || (StringUtils.isNotEmpty(allAcct.getParLegalIdcardType()) && (!allAcct.getParLegalIdcardType().equals(" , ")))
				|| StringUtils.isNotEmpty(allAcct.getParLegalIdcardNo())) {
			if (StringUtils.isBlank(allAcct.getParLegalName()) || StringUtils.isBlank(allAcct.getParLegalIdcardType()) || StringUtils.isBlank(allAcct.getParLegalIdcardNo())) {
				throw new SyncException("上级法人及主管单位的法定代表人或负责人姓名、身份证明文件种类及其编号三个数据项,必须全部为空、或全部不为空!");
			}
		}
		if (StringUtils.isNotEmpty(allAcct.getParAccountKey()) && (!allAcct.getParAccountKey().substring(0, 1).equalsIgnoreCase("J")) && allAcct.getParAccountKey().getBytes().length != 14) {
			throw new SyncException("请录入正确的上级法人或主管单位信息基本存款账户开户许可证核准号!");
		}
		if (StringUtils.isNotEmpty(allAcct.getParAccountKey())) {
			if (StringUtils.isBlank(allAcct.getParLegalName())) {
				throw new SyncException("上级开户许可证不为空时上级法定代表人或单位负责人姓名不能为空!");
			}
			if (StringUtils.isBlank(allAcct.getParCorpName())) {
				throw new SyncException("上级开户许可证不为空时上级主管单位名称不能为空!");
			}
		}
		if (StringUtils.isNotEmpty(allAcct.getParOrgCode())) {
			if (StringUtils.isBlank(allAcct.getParLegalName())) {
				throw new SyncException("上级组织机构代码不为空时上级法定代表人或单位负责人姓名不能为空!");
			}
			if (StringUtils.isBlank(allAcct.getParCorpName())) {
				throw new SyncException("上级组织机构代码不为空时上级主管单位名称不能为空!");
			}
			if (StringUtils.isBlank(allAcct.getParAccountKey())) {
				throw new SyncException("上级组织机构代码不为空请录入上级主管单位基本存款账户开户许可证核准号!");
			}
		}
		if (StringUtils.isNotEmpty(allAcct.getParLegalIdcardNo()) && allAcct.getParLegalIdcardNo().getBytes().length > 18) {
			throw new SyncException("上级法人证件编号长度不能大于18位");
		}
	}


	/**
	 * 判断基本户开户许可证与基本户注册地地区代码数字前4位是否相同，若不相同则用基本户开户许可证前6位数字
	 * @param allAcct
	 */
	@Deprecated
	protected void setRegAreaCodeByAccountKey(AllAcct allAcct) {
		if (StringUtils.isNotEmpty(allAcct.getRegAreaCode()) && StringUtils.isNotBlank(allAcct.getAccountKey())) {
			if (!allAcct.getAccountKey().substring(1, 5).equals(allAcct.getRegAreaCode().substring(0, 4))) {
				allAcct.setRegAreaCode(allAcct.getAccountKey().substring(1, 7));
			}
		}
	}

	/**
	 * 判断账户哪些账户性质可以在人行账管系统中进行报备
	 * 
	 * @param acctType
	 * @throws SyncException
	 */
	protected void validateRevokeAcctType(SyncAcctType acctType,AllAcct allAcct) throws SyncException {
		if (acctType == null) {
			throw new SyncException("账户性质不能为空");
		}
		boolean isCanRevoke = false;
		if (acctType == SyncAcctType.feiyusuan) {
			isCanRevoke = true;
		} else if (acctType == SyncAcctType.yiban) {
			isCanRevoke = true;
		} else if (acctType == SyncAcctType.jiben || acctType == SyncAcctType.feilinshi) {
			//基本非临时取消核准判断  取消核准可以进行人行销户备案
			if(allAcct.getCancelHeZhun() != null && allAcct.getCancelHeZhun()){
				isCanRevoke = true;
			}
		} else {
			isCanRevoke = false;
		}
		if (!isCanRevoke) {
			throw new SyncException("人行系统只支持报备类账户进行销户，核准类账户请直接到当地人行直接进行销户");
		}
	}

	/**
	 * 校验临时户的账户有效期
	 * 
	 * @param allAcct
	 * @throws SyncException
	 */
	protected void validateEffectiveDate(AllAcct allAcct) throws SyncException {

		if (StringUtils.isNotBlank(allAcct.getAcctCreateDate()) && StringUtils.isNotBlank(allAcct.getEffectiveDate())) {
			// 有效日期
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				long createDate = format.parse(allAcct.getAcctCreateDate()).getTime();
				long effectiveDate = format.parse(allAcct.getEffectiveDate()).getTime();
				if (createDate > System.currentTimeMillis()) {
					throw new SyncException("开户日期不能大于当前日期");
				}
				if (createDate > effectiveDate) {
					throw new SyncException("有效日期不能早于开户日期!");
				}
				if (createDate == effectiveDate) {
					throw new SyncException("有效日期和开户日期不能是同一天!");
				}
				long days = (effectiveDate - createDate) / (1000 * 60 * 60 * 24);
				if (days - 731 > 0) {
					throw new SyncException("有效日期不能大于开户日期2年以上!");
				}
			} catch (ParseException e) {
				throw new SyncException("有效日期格式错误");
			}
		}
	}
}
