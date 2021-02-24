package com.ideatech.ams.account.service.pbc;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.dto.*;
import com.ideatech.ams.pbc.enums.AmsAnnualResultStatus;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;

import java.util.List;

public interface PbcAmsService {

	/**
	 * 根据基本户开户许可证、地区代码校验基于基本户开户的账户是否满足开户条件
	 *
	 * @param accountKey     基本户开户许可证
	 * @param regAreaCode    基本户注册地地区代码
	 * @return 校验满足开后，校验结果
	 * @throws Exception 接口异常
	 */
	AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(String orgCode, String accountKey, String regAreaCode)
			throws Exception;

	/**
	 * 根据基本户开户许可证、地区代码校验基于基本户开户的账户是否满足开户条件
	 *
	 * @param accountKey     基本户开户许可证
	 * @param regAreaCode    基本户注册地地区代码
	 * @return 校验满足开后，校验结果
	 * @throws Exception 接口异常
	 */
	AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCode(String orgCode, String accountKey, String regAreaCode, String username)
			throws Exception;

	/**
	 * 根据基本户开户许可证、地区代码校验基于基本户开户的账户是否满足开户条件
	 * (开一般户时使用：用于检测基本户和一般户是否开在统一机构下)
	 *
	 * @param accountKey     基本户开户许可证
	 * @param regAreaCode    基本户注册地地区代码
	 * @return 校验满足开后，校验结果
	 * @throws Exception 接口异常
	 */
	AmsCheckResultInfo checkPbcByAccountKeyAndRegAreaCodeForYiBanOpen(String orgCode, String accountKey, String regAreaCode)
			throws Exception;

	/**
	 * 人行账管系统报备（开户、变更、久悬、销户<一般户\非预算专用户>）
	 *
	 * @param billsPublic     接口传入对象
	 * @throws Exception
	 */
	void amsAccountSync(AllBillsPublicDTO billsPublic) throws Exception;

	void amsAccountSync(PbcAccountDto pbcAccountDto, AllBillsPublicDTO billsPublic) throws Exception;

	/**
	 * 获取登录人行系统的账号
	 * @param orgCode 核心机构号(OrganizationPo的code)
	 * @param acctNo 账户
	 * @return
	 * @throws Exception
	 */
	AmsAccountInfo getAmsAccountInfoByAcctNo(String orgCode, String acctNo) throws Exception;

	/**
	 * 获取登录人行系统的账号
	 * @param organId 所属机构ID(OrganizationPo的id)
	 * @param acctNo 账户
	 * @return
	 * @throws Exception
	 */
	AmsAccountInfo getAmsAccountInfoByAcctNo(Long organId, String acctNo) throws Exception;

	AmsAccountInfo getAmsAccountInfoByAcctNo(PbcAccountDto pbcAccountDto, String acctNo) throws Exception;

	AmsAccountInfo getAmsAccountInfoByAcctNoAndAcctTypeToRevoke(OrganizationDto organizationDto, String acctNo,String acctType) throws Exception;

	AmsAccountInfo getAmsAccountInfoByAcctNoFromChangeHtml(OrganizationDto organizationDto, AllAcct allAcct) throws Exception;
	/**
	 * 提交年检结果
	 */
	AmsAnnualResultStatus sumitAnnualAccount(Long organId, String acctNo) throws Exception;

	AmsAnnualResultStatus sumitAnnualAccount(String organFullId, String acctNo) throws Exception;

	/**
	 * 删除上报核准类
	 * @param pbcAccountDto
	 * @param acctNo
	 * @return
	 * @throws Exception
	 */
	boolean deleteAccount(PbcAccountDto pbcAccountDto,String acctNo) throws Exception;

	/**
	 * 检查人行待核准列表有无该账户
	 * @param pbcAccountDto
	 * @param acctNo
	 * @return
	 * @throws Exception
	 */
	boolean existAccountInCheckList(PbcAccountDto pbcAccountDto,String acctNo) throws Exception;

	/**
	 * 查询存款人的所有账户信息
	 * @param pbcAccountDto
	 * @param depositorName
	 * @param accountKey
	 * @param selectPwd
	 * @throws Exception
	 */
	List<AmsPrintInfo> searchAllAccount(PbcAccountDto pbcAccountDto, String depositorName, String accountKey, String selectPwd) throws Exception;


	/**
	 * 查询基本户唯一性
	 * @param amsJibenUniqueCheckCondition
	 * @param organCode
	 * @return 返回不为空则有异常
	 */
	String  jiBenUniqueCheck(AmsJibenUniqueCheckCondition amsJibenUniqueCheckCondition, String organCode);

    /**
     * 人行变更记录留存 基于基本户的开户的账户在未变更原有账户信息的情况下进行变更的接口
     * 根据账号查找账户的信息 用查回来的信息重新上报人行
     * @param organizationDto
     * @param acctNo
     * @throws Exception
     */
    void amsAccountSyncChangeAgain(OrganizationDto organizationDto, String acctNo) throws Exception;
}
