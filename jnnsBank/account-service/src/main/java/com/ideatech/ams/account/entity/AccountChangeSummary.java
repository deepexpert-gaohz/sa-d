/**
 *
 */
package com.ideatech.ams.account.entity;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 账户变更概括表
 *
 * @author vantoo
 * @date 2018/5/16 13:53
 *
 */
@Entity
@Data
public class AccountChangeSummary extends BaseMaintainablePo {

//	@ManyToOne
//	private CompanyAccount companyAccount;
//	@ManyToOne
//	private AllBillsPublicDTO allBillsPublic;

    /**
     * 关联单据ID
     */
    private Long refBillId;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 账户性质
     */
    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;

    /**
     * 操作用户
     */
    private String operateName;

    /**
     * organFullId
     */
    private String organFullId;

}
