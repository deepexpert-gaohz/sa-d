package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统临时户变更报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsLinshiChangeSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		//法人类型值判断
		String[] legalType = new String[] { "1", "2"};
		if(StringUtils.isNotBlank(allAcct.getLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getLegalType())) {
				logger.info("临时户法人类型当前值：" + allAcct.getLegalType());
				logger.info("临时户法人类型值应为：\"1\", \"2\"");
				throw new SyncException("临时户法人类型值不正确");
			}
		}

		//上级法人类型值判断
		if(StringUtils.isNotBlank(allAcct.getParLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getParLegalType())) {
				logger.info("临时户上级法人类型当前值：" + allAcct.getLegalType());
				logger.info("临时户上级法人类型值应为：\"1\", \"2\"");
				throw new SyncException("临时户上级法人类型值不正确");
			}
		}
		//法人证件类型值判断
		String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		if(StringUtils.isNotBlank(allAcct.getLegalIdcardTypeAms())){
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getLegalIdcardTypeAms())) {
				logger.info("临时户法人证件类型当前值：" + allAcct.getLegalIdcardTypeAms());
				logger.info("临时户法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("临时户法人证件类型值不正确");
			}
		}


		//上级法人证件类型值判断
		if(StringUtils.isNotBlank(allAcct.getParLegalIdcardType())){
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getParLegalIdcardType())) {
				logger.info("临时户上级法人证件类型当前值：" + allAcct.getParLegalIdcardType());
				logger.info("临时户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("临时户上级法人证件类型值不正确");
			}
		}

		//证明文件种类1类型值判断
		String[] fileType = new String[] { "01", "09"};
		if(StringUtils.isNotBlank(allAcct.getFileType())){
			if (!ArrayUtils.contains(fileType, allAcct.getFileType())) {
				logger.info("临时户证明文件种类1证件类型当前值：" + allAcct.getParLegalIdcardType());
				logger.info("临时户证明文件种类1证件类型值应为：\"01\", \"09\"");
				throw new SyncException("临时户证明文件种类1证件类型值不正确");
			}
		}

		//临时户币种类型值判断
		String[] regCurrencyTypeAms = new String[] { "1","2","3","4","5","A","B","C","D","E","F"};
		if(StringUtils.isNotBlank(allAcct.getRegCurrencyTypeAms())){
			if (!ArrayUtils.contains(regCurrencyTypeAms, allAcct.getRegCurrencyTypeAms())) {
				logger.info("临时户币种类型当前值：" + allAcct.getRegCurrencyTypeAms());
				logger.info("临时户币种类型值应为：\"1\",\"2\",\"3\",\"4\",\"5\",\"A\",\"B\",\"C\",\"D\",\"E\",\"F\"");
				throw new SyncException("临时户币种类型值不正确");
			}
		}
	}
}
