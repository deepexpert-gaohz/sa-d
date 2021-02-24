package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统一般户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsYibanOpenSyncValidater extends AbstractSyncValidater {

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
		// 证明文件1种类
//		if (StringUtils.isBlank(allAcct.getAccountFileType())) {
//			throw new SyncException("证明文件1类型不能为空");
//		}
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
