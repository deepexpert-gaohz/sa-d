package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;



/**
 * 人行账管系统一般户变更报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsYibanChangeSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		// 证明文件1编号
		if ("06".equals(allAcct.getAccountFileType())) {
			if (StringUtils.isBlank(allAcct.getAccountFileNo())) {
				throw new SyncException("文件种类为“借款合同”时,开户证明文件编号为必输项");
			}
		}
		//判断开户证明文件种类下拉框值校验
		String[] accountFileType = new String[] { "06", "07"};
		if(StringUtils.isNotBlank(allAcct.getAccountFileType())){
			if (!ArrayUtils.contains(accountFileType, allAcct.getAccountFileType())) {
				logger.info("开户证明文件编号类型值当前值：" + allAcct.getAccountFileType());
				logger.info("开户证明文件编号类型值应为：\"06\", \"07\"");
				throw new SyncException("开户证明文件编号类型值不正确");
			}
		}
	}
}
