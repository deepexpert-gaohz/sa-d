package com.ideatech.ams.system.proof.entity;

import com.ideatech.ams.system.proof.enums.CompanyAcctType;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import javax.persistence.*;

/**
 *校验费用表
 */
@Entity
@Data
public class ProofReport extends BaseMaintainablePo {
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户性质
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CompanyAcctType acctType;
    /**
     * 企业名称
     */
    private String entname;
    /**
     * 电话号码
     */
    @Column(length =50)
    private String phone;
    /**
     * 开户行
     */
    private String openBankName;
    /**
     * 尽调机构
     */
    private String proofBankName;
    /**
     * 尽调人
     */
    private String username;
    /**
     * 最后一次尽调时间
     */
    private String dateTime;
    /**
     * 是否尽调
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CompanyIfType kycFlag;
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProofType type;
    /**
     * 类型细分
     */
    private String typeDetil;
    /**
     * 尽调机构fullid
     */
    private String organFullId;
    /**
     * 价格
     */
    private String price;
    /**
     * 校验结果
     */
    private String result;


    private String accountKey;

    private String regAreaCode;

}
