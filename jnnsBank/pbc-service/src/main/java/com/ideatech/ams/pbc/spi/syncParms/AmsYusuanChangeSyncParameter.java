package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import org.springframework.stereotype.Component;


/**
 * 人行账管系统预算户变更接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsYusuanChangeSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			// 存款帐户类型
			urlPars.append("accNotCheckedEntDepositorInfo.sacckind=4");
			// 预算存款账户 usertype登录用户类型 2普通登录用户 1人民银行
			urlPars.append("&accNotCheckedEntDepositorInfo.icheckkind=1");
			urlPars.append("&usertype=2&managesign=1&accchoosesign=");
			// 账号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
			// 开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbaselicno=");
			// urlPars.append(allAcct.getAccountKey());
			// 银行机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=");
			urlPars.append(allAcct.getBankCode());
			urlPars.append("&zmbh=");
			// 证明文件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=");
			urlPars.append(allAcct.getAccountFileType());
			// 证明文件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo(), amsChart));
			// 证明文件种类2
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype2=");
			urlPars.append(allAcct.getAccountFileType2());
			// 证明文件编号2
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode2=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo2(), amsChart));
			// 账户名称构成方式
			urlPars.append("&accNotCheckedEntDepositorInfo.imode=");
			urlPars.append(allAcct.getAccountNameFrom());
			// 账户名称
			urlPars.append("&accNotCheckedEntDepositorInfo.saccname=");
			// 资金性质
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfundkind=");
			urlPars.append(allAcct.getCapitalProperty());
			urlPars.append("&accountNameType=");
			// 前缀值
			urlPars.append("&saccprefix=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getSaccprefix(), amsChart));
			// 后缀值
			urlPars.append("&saccpostfix=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getSaccpostfix(), amsChart));
			// 资金管理人名称
			urlPars.append("&saccdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getMoneyManager(), amsChart));
			// 资金管理人身份证种类
			urlPars.append("&saccdepcrekind=");
			urlPars.append(allAcct.getMoneyManagerCtype());
			// 资金管理人身份证件编号
			urlPars.append("&saccdepcrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getMoneyManagerCno(), amsChart));
			// 内设部门信息
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideDepartmentName(), amsChart));
			// 内设部门负责人名称
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideSaccdepmanName(), amsChart));
			// 负责人身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrekind=");
			urlPars.append(allAcct.getInsideSaccdepmanKind());
			// 负责人身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideSaccdepmanNo(), amsChart));
			// 电话
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeptel=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideTelphone(), amsChart));
			// 邮政编码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeppostcode=");
			urlPars.append(allAcct.getInsideZipCode());
			// 地址
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideAddress(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
