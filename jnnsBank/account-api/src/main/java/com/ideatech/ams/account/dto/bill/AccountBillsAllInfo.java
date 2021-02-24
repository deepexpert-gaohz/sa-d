package com.ideatech.ams.account.dto.bill;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

/**
 * 流水表
 *
 * @author clxry
 */


@Data
public class AccountBillsAllInfo extends BaseMaintainableDto implements Serializable {

    private Long id;
    /**
     * 账户id
     */
    
    private Long accountId;
    /**
     * 客户id
     */
    
    private Long customerLogId;
    /**
     * 单据编号
     */
    
    private String billNo;
    /**
     * 单据日期
     */
    
    private String billDate;
    /**
     * 单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）
     */
    
    
    private BillType billType;
    /**
     * 单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)
     */
    
    
    private BillStatus status;
    /**
     * 审核人id
     */
    
    private Long approver;
    /**
     * 审核日期
     */
    
    private String approveDate;
    /**
     * 审核/驳回说明
     */
    
    private String approveDesc;
    /**
     * 退回原因
     */
    
    private String denyReason;
    /**
     * 人行核准状态(01待审核、02审核通过、03无需审核)
     */
    
    
    private CompanyAmsCheckStatus pbcCheckStatus;
    /**
     * 人行核准日期
     */
    
    private String pbcCheckDate;
    /**
     * 账户ID
     */
    /**
     * 账号
     */
    
    private String acctNo;
    /**
     * 客户号
     */
    
    private String customerNo;
    /**
     * 完整机构ID
     */
    
    private String organFullId;
    /**
     * 描述
     */
    
    private String description;
    /**
     * 上报人行账管状态（01成功；02失败；03无需上报）
     */
    
    
    private CompanySyncStatus pbcSyncStatus;
    /**
     * 上报人行账管错误信息
     */
    
    private String pbcSyncError;
    /**
     * 上报操作人
     */
    
    private Long pbcOperator;
    /**
     * 上报人行账管时间
     */
    
    private String pbcSyncTime;
    /**
     * 校验账户报备是否成功
     */
    
    
    private SyncCheckStatus pbcSyncCheck;
    /**
     * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
     */
    
    
    private CompanySyncOperateType pbcSyncMethod;
    /**
     * 上报信用代码证状态（01成功；02失败；03无需上报）
     */
    
    
    private CompanySyncStatus eccsSyncStatus;
    /**
     * 上报信用代码证错误信息
     */
    
    private String eccsSyncError;
    /**
     * 上报操作人
     */
    
    private Long eccsOperator;
    /**
     * 上报信用代码证时间
     */
    
    private String eccsSyncTime;
    /**
     * 账户报备成功后，再次校验是否报备成功
     */

    
    private SyncCheckStatus eccsSyncCheck;
    /**
     * T+1是否来自核心(0否1是)
     */
    
    private CompanyIfType acctIsFromCore;
    /**
     * T+1数据是否完整(0否1是)
     */
    
    
    private CompanyIfType coreDataCompleted;
    /**
     * T+1账户是否需要手工处理(0否1是)
     */
    
    
    private CompanyIfType handingMark;
    /**
     * 流水最终状态标识(0否1是)
     */
    
    
    private CompanyIfType finalStatus;
    /**
     * 流水初始化待补录(完整性)状态(0-无需补录、1-待补录、2-已补录)
     */
    
    private String initFullStatus;
    /**
     * 流水初始化备注信息
     */
    
    private String initRemark;
    /**
     * 流水来源(预填单、核心T+0、核心T+1、AMS)
     */
    
    
    private BillFromSource fromSource;
    /**
     * 原始流水id（变更销户久悬时，存前一笔流水id）
     */
    
    private Long originalBillId;
    /**
     * 变更字段的流水是否需要上报人行(0:无上报字段  1：包含上报字段)
     */
    
    private String changeFieldIsPbcSync;
    /**
     * 变更字段的流水是否需要上报信用机构(0:无上报字段  1：包含上报字段)
     */
    
    private String changeFieldIsEccsSync;

    /**
     * 核心同步状态
     */
    private CompanySyncStatus coreSyncStatus;

    /**
     * 上报人行账管错误信息
     */
    private String coreSyncError;
    /**
     * 上报操作人
     */
    private Long coreOperator;
    /**
     * 上报人行账管时间
     */
    private String coreSyncTime;
    /**
     * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
     */
    private CompanySyncOperateType coreSyncMethod;

    /**
     * 预约编号
     */
    private String preOpenAcctId;

    ////新增字段(用于列表条件筛选)
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    private CompanyAcctType acctType;

    /**
     *存款人名称
     */
    private String depositorName;

    /**
     * 本地异地标识(0本地、1异地)
     */
    private OpenAccountSiteType openAccountSiteType;

    /**
     * 扩展字段1（1给核心推送完毕,空则没有推送）
     */
    
    private String string001;
    /**
     * 扩展字段2
     */
    
    private String string002;
    /**
     * 扩展字段3
     */
    
    private String string003;
    /**
     * 扩展字段4
     */
    
    private String string004;
    /**
     * 扩展字段5
     */
    
    private String string005;
    /**
     * 扩展字段6
     */
    
    private String string006;
    /**
     * 扩展字段7
     */
    
    private String string007;
    /**
     * 扩展字段8
     */
    
    private String string008;
    /**
     * 扩展字段9
     */
    
    private String string009;
    /**
     * 扩展字段10
     */
    
    private String string010;


    //新增字段(用于列表条件筛选)
    private List<BillStatus> statuses;


    private List<CompanySyncStatus> eccsSyncStatuses;

    private List<CompanySyncStatus> pbcSyncStatuses;

    private BillType billTypeEx;

    private String whiteList;

    /**
     * 是否取消核准标上报标志位
     */
    private Boolean cancelHeZhun;

    /**
     * 查询密码
     */
    private String selectPwd;
    /**
     * 开户许可证号
     */
    private String accountKey;

    /**
     * 开户许可证号（新）（取消核准）
     */
    private String openKey;

    /**
     * 影像批次号（甘肃）
     */
    private String imageBatchNo;

    /**
     * 临时存款账户核准号保存字段
     * @param errorMessageString
     * @return
     */
    private String accountLicenseNo;

    /**
     * 原基本开户许可证号
     */
    private String oldAccountKey;

    /**
     * 存款人类别
     */
    private String depositorType;


    /**
     * 证明文件1
     */
    private String fileNo;



    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [主键ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[主键ID]不能为空");
        }
        // [单据编号]的必填验证
        if (this.getBillNo() == null || this.getBillNo().equals("")) {
            throw new RuntimeException("[单据编号]不能为空");
        }
        // [单据日期]的必填验证
        if (this.getBillDate() == null || this.getBillDate().equals("")) {
            throw new RuntimeException("[单据日期]不能为空");
        }
        // [单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）]的必填验证
        if (this.getBillType() == null || this.getBillType().equals("")) {
            throw new RuntimeException("[单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）]不能为空");
        }
        // [单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)]的必填验证
        if (this.getStatus() == null || this.getStatus().equals("")) {
            throw new RuntimeException("[单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)]不能为空");
        }
        // [账户ID]的必填验证
        /*
         * if (this.getAccountId() == null) { throw new RuntimeException("[账户ID]不能为空"); }
         */
        // [客户ID]的必填验证
        //		if (this.getCustomerId() == null) {
        //			throw new RuntimeException("[客户ID]不能为空");
        //		}
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
