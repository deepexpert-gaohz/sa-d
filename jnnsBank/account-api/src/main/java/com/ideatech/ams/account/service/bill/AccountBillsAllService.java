package com.ideatech.ams.account.service.bill;

import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.OuterSysCode;
import com.ideatech.ams.account.enums.bill.*;

import java.util.Date;
import java.util.List;

/**
 * @author vantoo
 * @date 10:08 2018/5/28
 */
public interface AccountBillsAllService {

    AccountBillsAllInfo getOne(Long id);

    void save(AccountBillsAllInfo accountBillsAllInfo);

    /**
     * 根据账户找到未完成流水
     *
     * @param accountId
     * @return
     */
    long countUnfinishedByAccountId(Long accountId);

    /**
     * 查找未完成流水（相同业务类型）
     * @param acctNo
     * @return
     */
    AccountBillsAllInfo findLatestUnfinishedByAcctNo(String acctNo, BillType billType);

    AccountBillsAllInfo findLatestFinishedByAcctNo(String acctNo);

    /**
     * 根据账号查询未完成的流水
     * @param acctNo
     * @return
     */
    AccountBillsAllInfo findLatestUnFinishedByAcctNo(String acctNo);

    long countUnfinishedByAcctNo(String acctNo);

    long count(BillType billType, String organFullId, String billBeginDate, String billEndDate,
                 List<CompanySyncStatus> pbcSyncStatusList, List<CompanySyncStatus> eccsSyncStatusList, List<CompanyAmsCheckStatus> pbcCheckStatusList,String acctType);

    long countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(BillType billType, String organFullId, String createddatestart, String createddateend);

    long countByBillTypeAndOrganFullIdStartsWith(BillType billType, String organFullId);

    int updateFinalStatus(Long id);

    long countById(Long id);

    /**
     * 根据日期添加3个工作日
     *
     * @return
     */
    String getActiveDate(Date date);

    /**
     * @param billId 流水id, sysCode 系统标识编码 , status 同步状态, SyncMethod 上报方式 msg 同步信息
     * @author jogy.he
     * @description 更新系统同步状态 pbc人行 eccs信用代码证系统 core核心系统
     */
    void updateSyncStatus(Long billId, OuterSysCode sysCode, CompanySyncStatus status, CompanySyncOperateType syncMethod, String msg, Long userId);

    /**
     * 更新人行核准状态
     * @param billId
     * @param status
     */
    void updatePbcCheckStatus(Long billId, CompanyAmsCheckStatus status);

    void updatePbcEccsCheckStatus(Long billId,CompanySyncStatus pbcSyncStatus,CompanySyncStatus eccsSyncStatus,CompanyAmsCheckStatus pbcCheckStatus);

    AccountBillsAllInfo getByCustomerNo(String customerNo);

    AccountBillsAllInfo getByBillNo(String billNo);

    /**
     * @param billId 流水id, status 流水状态,userId 用户Id,description审核备注信息
     * @author jogy.he
     * @description 更新流水状态
     */
    void updateBillStatus(Long billId, BillStatus status, Long userId, String description);

    List<AccountBillsAllInfo> getAllAccountBillsAllInfo();

    Boolean isCheckAcctNoAndCustomerNo(String acctNo, String customerNo, String billNo);

    /**
     * 根据当前流水表中，获取指定数据定时报送
     */
    void autoSync();

    /**
     * 查找未上报成功的流水
     * @return
     */
    List<AccountBillsAllInfo> getUnSyncSuccessBill();

    int updateCustomerLogId(Long id, Long customerLogid);

    int updateCancelHezhun(Long id,String selectPwd,String openKey,String accountKey);

    long updateAcctTypeFromPbc();

    /**
     * 更新存量账户（人行覆盖核心），页面触发。
     * @return
     */
    long updateBills();

    AccountBillsAllInfo findFirstByAcctNoOrderByCreatedDateDesc(String acctNo);

    void updateApplyStatus();

    /**
     * 首页相关流水信息统计
     * @return
     */
    JSONObject getIndexBillsCounts();

    /**
     * 删除未完成的流水和账号
     * @param id
     */
    boolean deleteBillsAndAccount(Long id) throws Exception;

    /**
     * 删除流水
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBills(Long id) throws Exception;

    boolean deleteBillsCgsb(Long id) throws Exception;

    List<AccountBillsAllInfo> findByOrganFullIdLike(String organFullId);

    List<AccountBillsAllInfo> findByAccountId(Long accountId);

    List<AccountBillsAllInfo> findByAcctNo(String acctNo);

    List<AccountBillsAllInfo> findByAccountIdIn(List<Long> accountIds);

    List<AccountBillsAllInfo> findByIdIn(List<Long> ids);

    /**
     * 根据customerLogId集合获取流水信息
     * @param customerLogId
     * @return
     */
    List<AccountBillsAllInfo> findByCustomerLogIdIn(List<Long> customerLogId);

    /**
     * 获取指定账户的第一笔流水id
     * @param accountId
     * @return
     */
    AccountBillsAllInfo getFirstBillByAccountId(Long accountId);

    /**
     * 根据CustomerPublicLogId获取第一条流水。
     */
    AccountBillsAllInfo getFirstBillByCustomerLogId(Long customerLogId);

//    Object getAcctNoAndAccountStatus(String applyId,String depositorName);

    List<AccountBillsAllInfo> findByPreOpenAcctIdAndDepositorName(String applyId,String depositorName);

    /**
     * 统计开户成功的数据（需要上报的数据）
     *
     * @param depositorType 存款人类型
     * @param acctType      账户性质
     * @param organFullId   机构fullId（机构权限控制）
     * @param beginDate     开始时间
     * @param endDate       结束时间
     */
    long countOpenSuccess(String depositorType, String acctType, String organFullId, String beginDate, String endDate);

    void updateSelectPwd(AllBillsPublicDTO billsPublic);

    void updateImageSyncStatus(Long billId,CompanySyncStatus imgaeSyncStatus);

    //加入通过账号模糊查询流水，处理jnns因存在交换号匹配不到
    List<AccountBillsAllInfo> findByAcctNoLike(String acctNo);


    void updateDownstatus(String id);


    void updateUploadstatus(Long id);




    AccountBillsAllInfo findById(Long id);


    /**
	     * 江南银行江苏影像开发要求
	 * 根据机构号查出当日该机构下的
	 * 所有流水表中的信息     */

	    List<AccountBillsAllInfo> findByOrganFullIdAndBillId(String organFullID,String billDate);
//    List<AccountBillsAllInfo> find

	
	/**
     * 判断对应流水的账号是否唯一
	 * @param billNo
     * @param acctNo
     * @return
     */
    Long isCheckBillNoAndAcctNo(String billNo, String acctNo);
}
