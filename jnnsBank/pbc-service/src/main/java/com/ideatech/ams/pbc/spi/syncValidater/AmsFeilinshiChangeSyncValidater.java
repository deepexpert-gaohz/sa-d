package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统非临时机构临时存款账户变更报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiChangeSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		/*if (StringUtils.isBlank(allAcct.getCreateAccountReason())) {
			throw new SyncException("申请开户的原因不能为空");
		}*/

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
