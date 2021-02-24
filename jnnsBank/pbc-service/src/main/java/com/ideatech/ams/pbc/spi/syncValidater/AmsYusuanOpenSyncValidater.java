package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.PbcBussUtils;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 * 人行账管系统预算单位专用存款账户开户报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsYusuanOpenSyncValidater extends AbstractSyncValidater {

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
		// 资金性质
		if (StringUtils.isBlank(allAcct.getCapitalProperty())) {
			throw new SyncException("资金性质不能为空");
		}
		// 证明文件1种类
		if (StringUtils.isBlank(allAcct.getAccountFileType())) {
			throw new SyncException("证明文件1类型不能为空");
		}
		// 证明文件1编号
		if (!allAcct.getAccountFileType().equals("10")) {
			if (StringUtils.isBlank(allAcct.getAccountFileNo())) {
				throw new SyncException("开户证明文件1编号不能为空");
			}
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
		// 账户构成方式
		if (StringUtils.isBlank(allAcct.getAccountNameFrom())) {
			throw new SyncException("账户构成方式不能为空");
		} else if (allAcct.getAccountNameFrom().equals("1")) { // 加内设部门
			if (StringUtils.isBlank(allAcct.getInsideAddress())) {
				throw new SyncException("内设部门地址不能为空");
			}
			if (StringUtils.isBlank(allAcct.getInsideDepartmentName())) {
				throw new SyncException("内设部门名称不能为空");
			}
			if (StringUtils.isBlank(allAcct.getInsideSaccdepmanKind())) {
				throw new SyncException("内设部门负责人身份证件类型不能为空");
			}
			if (StringUtils.isBlank(allAcct.getInsideSaccdepmanName())) {
				throw new SyncException("内设部门负责人姓名不能为空");
			}
			if (StringUtils.isBlank(allAcct.getInsideSaccdepmanNo())) {
				throw new SyncException("内设部门负责人身份编号不能为空");
			}
			if (StringUtils.isBlank(allAcct.getInsideTelphone())) {
				throw new SyncException("内设部门联系电话不能为空");
			}
			if (StringUtils.isBlank(allAcct.getInsideZipCode())) {
				throw new SyncException("内设部门邮政编码不能为空");
			}

			//预算户负责人身份证件类型值判断
			String[] insideSaccdepmanKind = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
			if(StringUtils.isNotBlank(allAcct.getInsideSaccdepmanKind())){
				if (!ArrayUtils.contains(insideSaccdepmanKind, allAcct.getInsideSaccdepmanKind())) {
					logger.info("预算户负责人身份证件类当前值：" + allAcct.getInsideSaccdepmanKind());
					logger.info("预算户负责人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
					throw new SyncException("预算户负责人身份证件类型值不正确");
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
