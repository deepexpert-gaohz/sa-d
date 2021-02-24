package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;



/**
 * 人行账管系统预算单位专用存款变更报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsYusuanChangeSyncValidater extends AbstractSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
		if (StringUtils.isBlank(allAcct.getAccountNameFrom())) {
			throw new SyncException("账户构成方式不能为空");
		}

		// 证明文件1类型判断
		if (StringUtils.isNotBlank(allAcct.getAccountFileType())) {
			//证明文件1类型判断
			String[] accountFileType = new String[] { "09", "10","11"};
			if (!ArrayUtils.contains(accountFileType, allAcct.getAccountFileType())) {
				logger.info("预算户证明文件1类型值当前值：" + allAcct.getAccountFileType());
				logger.info("预算户证明文件1类型值应为：\"09\", \"10\",\"11\"");
				throw new SyncException("预算户证明文件1类型值不正确");
			}
		}
		// 证明文件2种类与编号
		if (StringUtils.isBlank(allAcct.getAccountFileType2()) || StringUtils.isBlank(allAcct.getAccountFileNo2())) {
			if (StringUtils.isNotEmpty(allAcct.getAccountFileNo2()) || StringUtils.isNotEmpty(allAcct.getAccountFileType2())) {
				throw new SyncException("证明文件2编号与证明文件2种类必须全部为空、或全部不为空!");
			}
			if(StringUtils.isNotBlank(allAcct.getAccountFileType2())){
				//证明文件1类型判断
				String[] accountFileType2 = new String[] { "08"};
				if (!ArrayUtils.contains(accountFileType2, allAcct.getAccountFileType2())) {
					logger.info("预算户证明文件2类型值当前值：" + allAcct.getAccountFileType2());
					logger.info("预算户证明文件2类型值应为：\"08\"");
					throw new SyncException("预算户证明文件2类型值不正确");
				}
			}
		}

		//预算户账户名称构成方式类型值不正确
		String[] accountNameFrom = new String[]{ "0","1","2"};
		if(StringUtils.isNotBlank(allAcct.getAccountNameFrom())){
			if (!ArrayUtils.contains(accountNameFrom, allAcct.getAccountNameFrom())) {
				logger.info("预算户账户名称构成方式类型当前值：" + allAcct.getAccountNameFrom());
				logger.info("预算户账户名称构成方式类型值应为：\"0\",\"1\",\"2\"");
				throw new SyncException("预算户账户名称构成方式类型值不正确");
			}
		}

		//预算户资金性质类型值不正确
		String[] capitalProperty = new String[]{ "01","02","03","04","05","06","07","08","09","10","11","12","13","14","16"};
		if(StringUtils.isNotBlank(allAcct.getCapitalProperty())){
			if (!ArrayUtils.contains(capitalProperty, allAcct.getCapitalProperty())) {
				logger.info("预算户资金性质类型当前值：" + allAcct.getCapitalProperty());
				logger.info("预算户资金性质类型值应为：\"01\",\"02\",\"03\",\"04\",\"05\",\"06\",\"07\",\"08\",\"09\",\"10\",\"11\",\"12\",\"13\",\"14\",\"16\"");
				throw new SyncException("预算户资金性质类型值不正确");
			}
		}


		//资金管理人身份证件类型值不正确
		String[] moneyManagerCtype = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		if(StringUtils.isNotBlank(allAcct.getMoneyManagerCtype())){
			if (!ArrayUtils.contains(moneyManagerCtype, allAcct.getMoneyManagerCtype())) {
				logger.info("资金管理人身份证件类型当前值：" + allAcct.getMoneyManagerCtype());
				logger.info("资金管理人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("资金管理人身份证件类型值不正确");
			}
		}

		if(StringUtils.isNotBlank(allAcct.getInsideSaccdepmanKind())){
			if (!ArrayUtils.contains(moneyManagerCtype, allAcct.getInsideSaccdepmanKind())) {
				logger.info("内设部门身份证件类型当前值：" + allAcct.getInsideSaccdepmanKind());
				logger.info("内设部门身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
				throw new SyncException("内设部门身份证件类型值不正确");
			}
		}
	}
}
