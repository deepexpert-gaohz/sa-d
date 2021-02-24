package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统特殊户变更报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsTeshuChangeSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		//法人类型值判断
		String[] legalType = new String[] { "1", "2"};
		if(StringUtils.isNotBlank(allAcct.getLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getLegalType())) {
				logger.info("特殊户法人类型值当前值：" + allAcct.getLegalType());
				logger.info("特殊户法人类型值应为：\"1\", \"2\"");
				throw new SyncException("特殊户法人类型值不正确");
			}
		}

		//上级法人类型值判断
		if(StringUtils.isNotBlank(allAcct.getParLegalType())){
			if (!ArrayUtils.contains(legalType, allAcct.getParLegalType())) {
				logger.info("特殊户上级法人类型值当前值：" + allAcct.getParLegalType());
				logger.info("特殊户上级法人类型值不正确：\"1\", \"2\"");
				throw new SyncException("特殊户上级法人类型值不正确");
			}
		}
		//法人证件类型值判断
		String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		if(StringUtils.isNotBlank(allAcct.getLegalIdcardTypeAms())){
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getLegalIdcardTypeAms())) {
				logger.info("特殊户法人证件类型当前值：" + allAcct.getLegalIdcardTypeAms());
				logger.info("特殊户法人证件类型值应为确：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("特殊户法人证件类型值不正确");
			}
		}

		//上级法人证件类型值判断
		if(StringUtils.isNotBlank(allAcct.getParLegalIdcardType())){
			if (!ArrayUtils.contains(legalIdcardTypeAms, allAcct.getParLegalIdcardType())) {
				logger.info("特殊户上级法人证件类型值当前值：" + allAcct.getParLegalIdcardType());
				logger.info("特殊户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("特殊户上级法人证件类型值不正确");
			}
		}

		//特殊户证明文件1种类类型值判断
		String[] fileType = new String[] { "12", "17"};
		if(StringUtils.isNotBlank(allAcct.getFileType())){
			if (!ArrayUtils.contains(fileType, allAcct.getFileType())) {
				logger.info("特殊户证明文件1种类类型值当前值：" + allAcct.getFileType());
				logger.info("特殊户证明文件1种类类型值应为：\"12\", \"17\"");
				throw new SyncException("特殊户证明文件1种类类型值不正确");
			}
		}


		//特殊户证明文件2种类类型值判断
		String[] fileType2 = new String[] {"13", "17"};
		if(StringUtils.isNotBlank(allAcct.getFileType2())){
			if (!ArrayUtils.contains(fileType2, allAcct.getFileType2())) {
				logger.info("特殊户证明文件2种类类型值当前值：" + allAcct.getFileType2());
				logger.info("特殊户证明文件2种类类型值应为：\"13\", \"17\"");
				throw new SyncException("特殊户证明文件2种类类型值不正确");
			}
		}
	}


}
