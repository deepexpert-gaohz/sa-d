package com.ideatech.ams.pbc.spi.syncValidater;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统非预算单位专用存款账户变更报备校验器
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeiyusuanChangeSyncValidater extends AmsYusuanOpenSyncValidater {

	@Override
	protected void doValidater(AllAcct allAcct) throws SyncException {
        //预算户负责人身份证件类型值判断
        String[] insideSaccdepmanKind = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(StringUtils.isNotBlank(allAcct.getInsideSaccdepmanKind())){
            if (!ArrayUtils.contains(insideSaccdepmanKind, allAcct.getInsideSaccdepmanKind())) {
                logger.info("非预算户负责人身份证件类当前值：" + allAcct.getInsideSaccdepmanKind());
                logger.info("非预算户负责人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("非预算户负责人身份证件类型值不正确");
            }
        }

        //非预算户账户名称构成方式类型值不正确
        String[] accountNameFrom = new String[]{ "0","1","2"};
        if(StringUtils.isNotBlank(allAcct.getAccountNameFrom())){
            if (!ArrayUtils.contains(accountNameFrom, allAcct.getAccountNameFrom())) {
                logger.info("非预算户账户名称构成方式类型当前值：" + allAcct.getAccountNameFrom());
                logger.info("非预算户账户名称构成方式类型值应为：\"0\",\"1\",\"2\"");
                throw new SyncException("非预算户账户名称构成方式类型值不正确");
            }
        }

        //非预算户资金性质类型值不正确
        String[] capitalProperty = new String[]{ "01","02","03","04","05","06","07","08","09","10","11","12","13","14","16"};
        if(StringUtils.isNotBlank(allAcct.getCapitalProperty())){
            if (!ArrayUtils.contains(capitalProperty, allAcct.getCapitalProperty())) {
                logger.info("非预算户资金性质类型当前值：" + allAcct.getCapitalProperty());
                logger.info("非预算户资金性质类型值应为：\"01\",\"02\",\"03\",\"04\",\"05\",\"06\",\"07\",\"08\",\"09\",\"10\",\"11\",\"12\",\"13\",\"14\",\"16\"");
                throw new SyncException("非预算户资金性质类型值不正确");
            }
        }

        //资金管理人身份证件类型值不正确
        String[] moneyManagerCtype = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(StringUtils.isNotBlank(allAcct.getMoneyManagerCtype())){
            if (!ArrayUtils.contains(moneyManagerCtype, allAcct.getMoneyManagerCtype())) {
                logger.info("非预算资金管理人身份证件类型当前值：" + allAcct.getMoneyManagerCtype());
                logger.info("非预算资金管理人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("非预算资金管理人身份证件类型值不正确");
            }
        }

        if(StringUtils.isNotBlank(allAcct.getInsideSaccdepmanKind())){
            if (!ArrayUtils.contains(moneyManagerCtype, allAcct.getInsideSaccdepmanKind())) {
                logger.info("非预算内设部门身份证件类型当前值：" + allAcct.getInsideSaccdepmanKind());
                logger.info("非预算内设部门身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("非预算内设部门身份证件类型值不正确");
            }
        }
	}

}
