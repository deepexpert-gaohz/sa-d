package com.ideatech.ams.pbc.spi.syncParms;

import com.ideatech.ams.pbc.dto.AllAcct;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.EncodeUtils;
import org.springframework.stereotype.Component;



/**
 * 人行账管系统预算户开户接口参数
 * 
 * @author zoulang
 *
 */
@Component
public class AmsYusuanOpenSyncParameter extends AbstractSyncParameter {

	@Override
	protected String getSyncParams(AllAcct allAcct) throws SyncException {
		StringBuffer urlPars = new StringBuffer();
		try {
			/**
			 * accNotCheckedEntDepositorInfo.icheckkind 检查开户类型 0正常 2特殊开户 accNotCheckedEntDepositorInfo.sacckind 存款帐户类型 预算存款账户 usertype登录用户类型 2普通登录用户 1人民银行
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.icheckkind=0&zmbh=");
			urlPars.append("&usertype=2&sacckind=4&accNotCheckedEntDepositorInfo.sacckind=4");
			/**
			 * sdepregareahidden 开户行注册地地区代码
			 */
			urlPars.append("&sdepregareahidden=").append(allAcct.getRegAreaCode());
			/**
			 * managesign 判断 添加 或者 修改 1添加 2修改
			 */
			urlPars.append("&managesign=1");
			/**
			 * accNotCheckedEntDepositorInfo.saccbaselicno 基本存款账户开户许可证核准号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbaselicno=").append(allAcct.getAccountKey());
			/**
			 * accNotCheckedEntDepositorInfo.saccbasearea 基本存款账户开户地地区代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbasearea=").append(allAcct.getRegAreaCode());
			/**
			 * accNotCheckedEntDepositorInfo.saccbankcode 银行机构代码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccbankcode=").append(allAcct.getBankCode());
			/**
			 * accNotCheckedEntDepositorInfo.saccno 帐号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccno=").append(allAcct.getAcctNo());
			/**
			 * accNotCheckedEntDepositorInfo.imode 账户名称构成方式
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.imode=").append(allAcct.getAccountNameFrom());

			if ("2".equals(allAcct.getAccountNameFrom())) { // 选择 存款人名称加资金性质
				/**
				 * 前缀后缀选项
				 */
				urlPars.append("&prefix=").append("&suffix=");
				/**
				 * saccprefix 前缀值
				 */
				urlPars.append("&saccprefix=");
				urlPars.append(EncodeUtils.encodStr(allAcct.getSaccprefix(), amsChart));
				/**
				 * saccpostfix 后缀值
				 */
				urlPars.append("&saccpostfix=");
				urlPars.append(EncodeUtils.encodStr(allAcct.getSaccpostfix(), amsChart));
			}
			/**
			 * accNotCheckedEntDepositorInfo.saccfundkind 资金性质
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfundkind=").append(allAcct.getCapitalProperty());
			/**
			 * accNotCheckedEntDepositorInfo.saccfiletype1 证明文件1种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype1=").append(allAcct.getAccountFileType());
			/**
			 * accNotCheckedEntDepositorInfo.saccfilecode1 证明文件1编号 accNotCheckedEntDepositorInfo.daccbegindate 开户时间
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode1=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccfiletype2 证明文件2种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfiletype2=").append(allAcct.getAccountFileType2());
			/**
			 * accNotCheckedEntDepositorInfo.saccfilecode2 证明文件2编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccfilecode2=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getAccountFileNo2(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.daccbegindate 开户时间
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.daccbegindate=").append(allAcct.getAcctCreateDate());
			/**
			 * saccdepcrekind资金管理人身份证件种类
			 */
			urlPars.append("&saccdepcrekind=").append(allAcct.getMoneyManagerCtype());
			/**
			 * saccdepcrecode资金管理人身份证件编号
			 */
			urlPars.append("&saccdepcrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getMoneyManagerCno(), amsChart));
			/**
			 * saccdepname资金管理人姓名
			 */
			urlPars.append("&saccdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getMoneyManager(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccdepname 内设部门名称
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideDepartmentName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccdepmanname 负责人姓名
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepmanname=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideSaccdepmanName(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccdepcrekind 负责人身份证件种类
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrekind=").append(allAcct.getInsideSaccdepmanKind());
			/**
			 * accNotCheckedEntDepositorInfo.saccdepcrecode 负责人身份证件编号
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepcrecode=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideSaccdepmanNo(), amsChart));
			/**
			 * accNotCheckedEntDepositorInfo.saccdeptel 电话
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeptel=").append(allAcct.getInsideTelphone());
			/**
			 * accNotCheckedEntDepositorInfo.saccdeppostcode 邮政编码
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdeppostcode=").append(allAcct.getInsideZipCode());
			/**
			 * accNotCheckedEntDepositorInfo.saccdepaddress 地址
			 */
			urlPars.append("&accNotCheckedEntDepositorInfo.saccdepaddress=");
			urlPars.append(EncodeUtils.encodStr(allAcct.getInsideAddress(), amsChart));
			urlPars.append("&accNotCheckedEntDepositorInfo.scurtype=1");
			urlPars.append("&cruArray=1");
			// 打印传递的参数
		} catch (Exception e) {
			logger.error(allAcct.getAcctNo() + "参数拼接失败", e);
			throw new SyncException(getAmsThrowExceptionStr(allAcct));
		}
		return urlPars.toString();
	}

}
