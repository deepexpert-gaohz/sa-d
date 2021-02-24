package com.ideatech.ams.account.entity.bill;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 流水表
 *
 * @author clxry
 */


@Entity
@Table(name = "ACCOUNT_BILLS_ALL",
        indexes = {@Index(name = "account_bills_all_ai_idx",columnList = "accountId"),
                    @Index(name = "account_bills_all_an_idx",columnList = "acctNo")})
@Data
public class AccountBillsAll extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635341L;
    public static String baseTableName = "YD_ACCOUNT_BILLS_ALL";

    /**
     * 账户id
     */

    private Long accountId;
    /**
     * 客户id
     */
    @Column(length = 14)
    private Long customerLogId;
    /**
     * 单据编号
     */
    @Column(length = 50)
    private String billNo;
    /**
     * 单据日期
     */
    @Column(length = 10)
    private String billDate;
    /**
     * 单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BillType billType;
    /**
     * 单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BillStatus status;
    /**
     * 审核人id
     */
    @Column(length = 22)
    private Long approver;
    /**
     * 审核日期
     */
    @Column(length = 10)
    private String approveDate;
    /**
     * 审核/驳回说明
     */
    @Column(length = 255)
    private String approveDesc;
    /**
     * 退回原因
     */
    @Column(length = 255)
    private String denyReason;
    /**
     * 人行核准状态(01待审核、02审核通过、03无需审核)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyAmsCheckStatus pbcCheckStatus;
    /**
     * 人行核准日期
     */
    @Column(length = 30)
    private String pbcCheckDate;
    /**
     * 账户ID
     */
    /**
     * 账号
     */
    @Column(length = 100)
    private String acctNo;
    /**
     * 客户号
     */
    @Column(length = 50)
    private String customerNo;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 描述
     */
    @Column(length = 255)
    private String description;
    /**
     * 上报人行账管状态（01成功；02失败；03无需上报）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncStatus pbcSyncStatus;

    /**
     * 影像上报状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncStatus imgaeSyncStatus;
    /**
     * 上报人行账管错误信息
     */
    @Column(length = 200)
    private String pbcSyncError;
    /**
     * 上报操作人
     */
    @Column(length = 22)
    private Long pbcOperator;
    /**
     * 上报人行账管时间
     */
    @Column(length = 20)
    private String pbcSyncTime;
    /**
     * 校验账户报备是否成功
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SyncCheckStatus pbcSyncCheck;
    /**
     * 账户上报方式(01手工上报02自动上报03手工补录04手工虚拟上报05线下手工报备)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncOperateType pbcSyncMethod;
    /**
     * 上报信用代码证状态（01成功；02失败；03无需上报）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncStatus eccsSyncStatus;
    /**
     * 上报信用代码证错误信息
     */
    @Column(length = 200)
    private String eccsSyncError;
    /**
     * 上报操作人
     */
    @Column(length = 22)
    private Long eccsOperator;
    /**
     * 上报信用代码证时间
     */
    @Column(length = 20)
    private String eccsSyncTime;
    /**
     * 账户报备成功后，再次校验是否报备成功
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private SyncCheckStatus eccsSyncCheck;
    /**
     * T+1是否来自核心(0否1是)
     */
    @Column(length = 10)
    private CompanyIfType acctIsFromCore;
    /**
     * T+1数据是否完整(0否1是)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyIfType coreDataCompleted;
    /**
     * T+1账户是否需要手工处理(0否1是)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyIfType handingMark;
    /**
     * 流水最终状态标识(0否1是)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CompanyIfType finalStatus;
    /**
     * 流水初始化待补录(完整性)状态(0-无需补录、1-待补录、2-已补录)
     */
    @Column(length = 15)
    private String initFullStatus;
    /**
     * 流水初始化备注信息
     */
    @Column(length = 1000)
    private String initRemark;
    /**
     * 流水来源(预填单、核心T+0、核心T+1、AMS)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private BillFromSource fromSource;
    /**
     * 原始流水id（变更销户久悬时，存前一笔流水id）
     */
    @Column(length = 14)
    private Long originalBillId;
    /**
     * 变更字段的流水是否需要上报人行(0:无上报字段  1：包含上报字段)
     */
    @Column(length = 14)
    private String changeFieldIsPbcSync;
    /**
     * 变更字段的流水是否需要上报信用机构(0:无上报字段  1：包含上报字段)
     */
    @Column(length = 14)
    private String changeFieldIsEccsSync;


    /**
     * 核心同步状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
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
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanySyncOperateType coreSyncMethod;

    /**
     * 预约id
     */
    @Column(length = 50)
    private String preOpenAcctId;

    ////新增字段(用于列表条件筛选)
    /**
     * 账户性质(1基本存款账户 2预算单位专用存款账户 3临时机构临时存款账户 4非临时机构临时存款账户 5特殊单位专用存款账户 6一般存款账户 7 非预算单位专用存款账户)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;

    /**
     * 存款人类别
     */
    @Column(length = 30)
    private String depositorType;

    /**
     *存款人名称
     */
    @Column(length = 100)
    private String depositorName;

    /**
     * 扩展字段1
     */
    @Column(length = 255)
    private String string001;
    /**
     * 扩展字段2
     */
    @Column(length = 255)
    private String string002;
    /**
     * 扩展字段3
     */
    @Column(length = 255)
    private String string003;
    /**
     * 扩展字段4
     */
    @Column(length = 255)
    private String string004;
    /**
     * 扩展字段5
     */
    @Column(length = 255)
    private String string005;
    /**
     * 记录基本户编码生成时间
     */
    @Column(length = 255)
    private String string006;
    /**
     * 扩展字段7
     */
    @Column(length = 255)
    private String string007;
    /**
     * 扩展字段8
     */
    @Column(length = 255)
    private String string008;
    /**
     * 扩展字段9
     */
    @Column(length = 255)
    private String string009;
    /**
     * 扩展字段10
     */
    @Column(length = 255)
    private String string010;

    /**
     * 白名单字段   1：白名单
     */
    private String whiteList;

    /**
     * 取消核准的标志位
     */
    @Column(length = 10)
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
     * 本地异地标识
     */
    private OpenAccountSiteType openAccountSiteType;

    /**
     * 影像批次号（甘肃）
     */
    private String imageBatchNo;

    /**
     *
     * 下载状态
     */
    @Column(length = 255)
    private String downloadstatus;

    /**
     *
     * 影像上传状态
     */
    @Column(length = 255)
    private String uploadstatus;



    /**
     *
     * 证明文件1
     */
    @Column(length = 255)
    private String fileNo;




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
        if (this.getBillType() == null) {
            throw new RuntimeException("[单据类型（01开户申请、02变更申请、03销户申请、04久悬申请）]不能为空");
        }
        // [单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)]的必填验证
        if (this.getStatus() == null) {
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
