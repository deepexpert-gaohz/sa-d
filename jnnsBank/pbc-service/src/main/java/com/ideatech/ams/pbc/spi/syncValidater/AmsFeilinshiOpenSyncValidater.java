package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统非临时机构临时存款账户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiOpenSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		// 开户许可证
		String validResult=PbcBussUtils.valiDateAccountKey(allAcct.getAccountKey());
		if(StringUtils.isNotBlank(validResult)){
			throw new SyncException(validResult);
		}
		// 基本户存款账户地区代码
		validResult=PbcBussUtils.valiDateRegAreaCode(allAcct.getRegAreaCode());
		if(StringUtils.isNotBlank(validResult)){
			throw new SyncException(validResult);
		}
		// 判断基本户开户许可证与基本户注册地地区代码数字前4位是否相同，若不相同则用基本户开户许可证前6位数字
		//super.setRegAreaCodeByAccountKey(allAcct);
		// 账户有效期
		super.validateEffectiveDate(allAcct);
		// 申请开户原因
		if (StringUtils.isBlank(allAcct.getCreateAccountReason())) {
			throw new SyncException("申请开户原因不能为空");
		}
		// 证明文件1种类
		if (StringUtils.isBlank(allAcct.getAccountFileType())) {
			throw new SyncException("证明文件1类型不能为空");
		}
		// 证明文件1编号
		if (StringUtils.isBlank(allAcct.getAccountFileNo())) {
			throw new SyncException("开户证明文件1编号不能为空");
		}

		//非临时户证明文件1种类类型值判断
		String[] fileType = new String[] { "14", "15", "16"};
		if(StringUtils.isNotBlank(allAcct.getAccountFileType())){
			if (!ArrayUtils.contains(fileType, allAcct.getAccountFileType())) {
				logger.info("非临时户证明文件1种类类型当前值：" + allAcct.getAccountFileType());
				logger.info("非临时户证明文件1种类类型值应为：\"14\", \"15\", \"16\"");
				throw new SyncException("非临时户证明文件1种类类型值不正确");
			}
		}

		//非临时户申请开户原因类型值判断
		String[] acctCreateReason = new String[] { "1", "2"};
		if(StringUtils.isNotBlank(allAcct.getCreateAccountReason())){
			if (!ArrayUtils.contains(acctCreateReason, allAcct.getCreateAccountReason())) {
				logger.info("非临时户申请开户原因类型当前值：" + allAcct.getCreateAccountReason());
				logger.info("非临时户申请开户原因类型值应为：\"1\", \"2\"");
				throw new SyncException("非临时户申请开户原因类型值不正确");
			}
		}

		//非临时负责人身份证件类型校验
		if(StringUtils.isNotBlank(allAcct.getFlsFzrLegalIdcardType())){
			//法人证件类型值判断
			String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getFlsFzrLegalIdcardType())) {
				logger.info("非临时负责人身份证件类型当前值：" + allAcct.getFlsFzrLegalIdcardType());
				logger.info("非临时负责人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("非临时负责人身份证件类型值不正确");
			}
		}
	}

}
