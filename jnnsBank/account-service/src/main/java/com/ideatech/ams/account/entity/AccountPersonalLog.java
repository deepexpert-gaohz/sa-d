package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 对私账户日志表
 * @author
 */

@Entity
@Table(name = "ACCOUNT_PERSONAL_LOG")
@Data
public class AccountPersonalLog extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     *
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ACCOUNT_PERSONAL_LOG";

    /**
     * 前一笔日志记录ID
     */
    @Column(length = 22)
    private Long preLogId;
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
     * 扩展字段6
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
     * 日志ID
     */
    /*
    
    @Column(length =22)
    private Long id;*/
    /**
     * 账户ID
     */
    @Column(length = 22)
    private Long accountId;
    /**
     * 账号
     */
    @Column(length = 32)
    private String acctNo;

    /**
     * 账户名称
     */
    @Column(length = 255)
    private String acctName;
    /**
     * 账户分类(1个人2对公3金融)
     */
    @Column(length = 10)
    private String accountClass;
    /**
     * 账户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AccountStatus accountStatus;
    /**
     * 币种
     */
    @Column(length = 10)
    private String currencyType;
    /**
     * 账户开户日期
     */
    @Column(length = 10)
    private String acctCreateDate;
    /**
     * 账户有效期(临时账户)
     */
    @Column(length = 10)
    private String effectiveDate;
    /**
     * 开户原因
     */
    @Column(length = 255)
    private String acctCreateReason;
    /**
     * 账户销户日期
     */
    @Column(length = 10)
    private String acctCancelDate;
    /**
     * 销户原因
     */
    @Column(length = 255)
    private String acctCancelReason;
    /**
     * 开户银行金融机构编码
     */
    @Column(length = 14)
    private String bankCode;
    /**
     * 开户银行名称
     */
    @Column(length = 100)
    private String bankName;
    /**
     * 客户ID
     */
    @Column(length = 22)
    private Long customerId;
    /**
     * 客户号
     */
    @Column(length = 50)
    private String customerNo;
    /**
     * 账户性质(01:借记结算账户；02贷记结算账户；03非结算账户)
     */
    @Column(length = 50)
    private String acctType;
    /**
     * 账户分类(01：I类，02：II类，03：III类)
     */
    @Column(length = 50)
    private String acctCategory;
    /**
     * 开户渠道(01：柜面；02：互联网网页；03：APP客户端；04：自助机具（人工参与审核）；05：自助机具（无人工审核）06：其他)
     */
    @Column(length = 10)
    private String acctChannel;
    /**
     * 卡号（卡子信息）
     */
    @Column(length = 314)
    private String cardNo;
    /**
     * 卡到期日（卡子信息）
     */
    @Column(length = 134)
    private String cardDue;
    /**
     * 账户介质（卡子信息）(01银行卡、02存折、03存单、04手机、05无介质、06其他)
     */
    @Column(length = 44)
    private String media;
    /**
     * 销卡日期(卡子信息)
     */
    @Column(length = 134)
    private String cancalCardDate;
    /**
     * 卡状态（卡子信息）(01正常、02注销)
     */
    @Column(length = 44)
    private String cardStatus;
    /**
     * 绑定I类账户账号
     */
    @Column(length = 329)
    private String iAcctNo;
    /**
     * 绑定I类账户开户银行金融机构编码(绑定I类账户子信息)
     */
    @Column(length = 149)
    private String iBankCode;
    /**
     * 是否为军人保障卡( 01:是；02:不是)
     */
    @Column(length = 2)
    private String militaryCard;
    /**
     * 是否为社会保障卡( 01:是；02:不是)
     */
    @Column(length = 2)
    private String socialCard;
    /**
     * 核实结果(01:未核实；02:真实；03假名；04:匿名；05:无法核实；)
     */
    @Column(length = 2)
    private String checkStatus;
    /**
     * 无法核实原因(01:无法联系存款人；02:存款人提供证明文件有疑义待进一步核实；03: 存款人在规定时间内无法提供相关证明；04:存款人拒绝提
     * 供证明)
     */
    @Column(length = 2)
    private String checkFailReason;
    /**
     * 处置方法(01:未作处理；02:报送当地人民银行分支机构；03:报送反洗钱监测中心；04:报送当地公安机关；05:中止交易；06:关闭网银；0
     * 7:关闭手机电话银行；08:关闭ATM取现；09:关闭ATM转账；10:其他)
     */
    @Column(length = 2)
    private String procMethod;
    /**
     * 备注
     */
    @Column(length = 128)
    private String remark;
    /**
     * 最新关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [日志ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[日志ID]不能为空");
        }
        // [账户ID]的必填验证
        if (this.getAccountId() == null) {
            throw new RuntimeException("[账户ID]不能为空");
        }
        // [账户分类(1个人2对公3金融)]的必填验证
        if (this.getAccountClass() == null || this.getAccountClass().equals("")) {
            throw new RuntimeException("[账户分类(1个人2对公3金融)]不能为空");
        }
        // [账户状态]的必填验证
        /*if (this.getStatus() == null || this.getStatus().equals("")) {
            throw new RuntimeException("[账户状态]不能为空");
        }*/
        // [最新关联单据ID]的必填验证
        if (this.getRefBillId() == null) {
            throw new RuntimeException("[最新关联单据ID]不能为空");
        }
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
