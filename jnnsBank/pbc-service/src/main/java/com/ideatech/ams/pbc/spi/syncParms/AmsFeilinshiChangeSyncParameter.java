package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import org.springframework.stereotype.Component;


/**
 * 账户管理系统非临时户变更接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsFeilinshiChangeSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			urlPars.append("&accNotCheckedEntDepositorInfo.sacckind=6"); // 存款帐户类型
			// 非临时存款账户
			urlPars.append("&usertype=2"); // usertype登录用户类型 2普通登录用户 1人民银行
			urlPars.append("&managesign=1");
			urlPars.append("&saccprefix="); // 前缀
			urlPars.append("&saccpostfix="); // 后缀
			// 前缀\后缀选项
			urlPars.append("&prefix=&suffix=");
			urlPars.append("&accchoosesign="); // 账号 1 开户许可证 2 经 验证不用填值
			// 变更时 以账号 或者 开户许可证核准号 来匹配
			// 账号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAcctNo(), amsChart));
			// 开户许可证核准号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbaselicno=");
			// urlPars.append(allAcct.getAccountKey());
			// 银行机构代码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=");
			urlPars.append(allAcct.getBankCode());
			// 以下是变更内容 无变更的空着
			// 申请开户原因
			urlPars.append("&accNotCheckedEntDepositorInfo.saccreason=");
			urlPars.append(allAcct.getCreateAccountReason());
			// 证明文件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=");
			urlPars.append(allAcct.getAccountFileType());
			// 证明文件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo(), amsChart));
			// 项目部名称
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getFlsProjectName(), amsChart));
			// 负责人身份证件种类
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrekind=");
			urlPars.append(allAcct.getLegalIdcardTypeAms());
			// 负责人身份证件编号
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getLegalIdcardNo(), amsChart));
			// 电话
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeptel=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getTelephone(), amsChart));
			// 邮政编码
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeppostcode=");
			urlPars.append(allAcct.getZipCode());
			// 地址
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getRegAddress(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
