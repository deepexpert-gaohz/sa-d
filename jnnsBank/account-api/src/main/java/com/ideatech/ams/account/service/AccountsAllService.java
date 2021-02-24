package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.AccountsAllSearchInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.vo.AnnualAccountVo;
import com.ideatech.ams.pbc.dto.AmsPrintInfo;
import com.ideatech.common.dto.PagingDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * @author vantoo
 * @date 15:52 2018/5/28
 */
public interface AccountsAllService {

    AccountsAllInfo getOne(Long accountId);

    void save(AccountsAllInfo accountsAllInfo);

    AccountsAllInfo findByRefBillId(Long refBillId);

    List<AccountsAllInfo> findByAccountKey(String accountKey,CompanyAcctType acctType);

    AccountsAllInfo findByAcctNo(String acctNo);

    /**
     * 根据客户名称获取该客户基本户信息
     * 若没有基本户，返回null
     */
    AccountsAllInfo findByDepositorName(String depositorName) throws Exception;

    List<AccountsAllInfo> findByCustomerLogId(Long customerLogId);

    AccountsAllInfo findByAcctNoAndIdNot(String acctNo,Long id);

    AccountsAllInfo findByAcctTypeAndDepositorName(CompanyAcctType acctType, String depositorName);

    Boolean getChangeAuthority(String depositorName);

    Long countByAcctNo(String acctNo);

    /**
     * 获取年检条件所有账户客户数据
     * @return
     */
    List<AnnualAccountVo> getAnnualAccountsAll(String endDay, String organFullId);

    /**
    *@Description: 获取全部的账号集合
    *@Param: []
    *@return: java.util.Set<java.lang.String>
    *@Author: wanghongjie
    *@date: 2018/9/16
    */
    Set<String> findAcctNoAllInSet();

    Long countTempAcctBefore(String s, String before, String organFullId, Boolean tempAcctOverConfigEnabled) throws ParseException;

    @Transactional
    PagingDto<AccountsAllInfo> listTempAcctBefore(AccountsAllSearchInfo customerPublicInfo, String fullId, String before, PagingDto pagingDto) throws Exception;

    @Transactional
    PagingDto<AccountsAllInfo> listTempAcctBefore1(AccountsAllSearchInfo customerPublicInfo, String fullId, String before, PagingDto pagingDto) throws Exception;
    /**
     * 临时户（非临时机构临时存款账户、临时机构临时存款账户、临时存款账户）到期超期查询
     * （在定时短信提醒时使用）
     * @param afterDateStr
     * @param beforeDateStr
     * @param tempAcctOverConfigEnabled
     * @return
     */
    List<AccountsAllInfo> getTempAcctDueAndOver(String afterDateStr, String beforeDateStr, Boolean tempAcctOverConfigEnabled);

    void deleteById(Long id);

    List<AccountsAllInfo> findByOrganFullIdLike(String organFullId);

    List<AccountsAllInfo> findByCustomerNo(String customerNo);

    /**
     * 获取指定客户的第一笔流水id
     * @param customerNo
     * @return
     */
    AccountBillsAllInfo getFirstBillByCustomerNo(String customerNo);

    /**
     * 获取指定客户id的第一笔流水id
     * @param customerId
     * @return
     */
    AccountBillsAllInfo getFirstBillByCustomerId(Long customerId);

    boolean isHeZhun2BaoBei(AllBillsPublicDTO allBillsPublicDTO);

    int updateCancelHezhun(Long id, String selectPwd, String openKey, String accountKey);

    HSSFWorkbook exportExcel(String depositorName, String accountKey, String cancelDate, List<AmsPrintInfo> apiList);

    Set<String> findAcctNoFromAccountStatus(AccountStatus accountStatus);
}
