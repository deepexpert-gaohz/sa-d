package com.ideatech.ams.customer.dto.bill;

import com.ideatech.ams.customer.enums.bill.*;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import java.io.Serializable;

/**
 * 流水表
 *
 * @author clxry
 */


@Data
public class CustomerBillsAllInfo extends BaseMaintainableDto implements Serializable {

    private Long id;
    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_CUSTOMER_BILLS_ALL";

    /**
     * 客户id
     */

    private Long customerLogId;

    /**
     * 中间表id
     */
    private Long customerMidId;

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


    private CustomerBillType billType;
    /**
     * 单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)
     */


    private CustomerBillStatus status;
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


    private CustomerBillFromSource fromSource;
    /**
     * 原始流水id（变更销户久悬时，存前一笔流水id）
     */

    private Long originalBillId;

    /**
     * 核心同步状态
     */
    private CustomerSyncStatus coreSyncStatus;

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
    private CustomerSyncOperateType coreSyncMethod;

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

    public void validate() {
        // [主键ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[主键ID]不能为空");
        }
        if (this.getCustomerLogId() == null) {
            throw new RuntimeException("[客户ID]不能为空");
        }
        // [单据编号]的必填验证
        if (this.getBillNo() == null || "".equals(this.getBillNo())) {
            throw new RuntimeException("[单据编号]不能为空");
        }
        // [单据日期]的必填验证
        if (this.getBillDate() == null || "".equals(this.getBillDate())) {
            throw new RuntimeException("[单据日期]不能为空");
        }
        // [单据类型（00存量、01开户申请、02变更申请、03销户申请、04久悬申请）]的必填验证
        if (this.getBillType() == null) {
            throw new RuntimeException("[单据类型（00存量、01开户申请、02变更申请、03销户申请）]不能为空");
        }
        // [单据状态(01新建、02审核中、03已审核、04驳回、05核心开户)]的必填验证
        if (this.getStatus() == null) {
            throw new RuntimeException("[单据状态(01新建、02审核中、03已审核)]不能为空");
        }
        if (this.getCreatedBy() == null || "".equals(this.getCreatedBy())) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
