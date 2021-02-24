package com.ideatech.ams.customer.entity;

import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 客户信息基础表
 *
 * @author RJQ
 *
 */
@Entity
@Data
@Table(name = "CUSTOMERS_ALL",
        indexes = {@Index(name = "customers_all_dn_idx",columnList = "depositorName")
                    ,@Index(name = "customers_all_rbi_idx",columnList = "refBillId")})
public class CustomersAll extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_CUSTOMERS_ALL";

    /*
     * marked for 逻辑控制关联
     *
     *
     * @OneToMany(mappedBy = "customersAll") private Set<AccountsAll> AccountsAlls = new HashSet<>();
     */

    /*
     * marked for 逻辑控制关联
     *
     *
     * @OneToMany(mappedBy = "customersAll") private Set<BillsAll> billsAlls = new HashSet<>();
     */

    /**
     * 客户号（db2数据库需注释掉unique = true唯一性约束）
     */
    @Column(length = 50, unique = true)
    private String customerNo;
    /**
     * 存款人名称
     */
    @Column(length = 200)
    private String depositorName;
    /**
     * 客户类型(1个人2对公)
     */
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private CustomerType customerClass;
    /**
     * 证件类型
     */
    @Column(length = 30)
    private String credentialType;
    /**
     * 证件号码
     */
    @Column(length = 50)
    private String credentialNo;
    /**
     * 证件到期日
     */
    @Column(length = 10)
    private String credentialDue;
    /**
     * 机构fullId
     */
    private String organFullId;
    /**
     * 最新关联单据ID
     */
    @Column(length = 22)
    private Long refBillId;

    /**
     * 关联客户流水编号
     */
    @Column(length = 22)
    private Long refCustomerBillId;

    /**
     * 最新的日志表ID
     */
    @Column(length = 22)
    private Long refCustomerLogId;


    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [客户ID]的必填验证
        if (this.getId() == null) {
            throw new RuntimeException("[客户ID]不能为空");
        }
        // [存款人名称]的必填验证
        if (this.getDepositorName() == null || this.getDepositorName().equals("")) {
            throw new RuntimeException("[存款人名称]不能为空");
        }
        // [客户类型(1个人2对公3金融)]的必填验证
        if (this.getCustomerClass() == null) {
            throw new RuntimeException("[客户类型(1个人2对公3金融)]不能为空");
        }
        /*// [证件类型]的必填验证
        if (this.getCredentialType() == null || this.getCredentialType().equals("")) {
            throw new RuntimeException("[证件类型]不能为空");
        }
        // [证件号码]的必填验证
        if (this.getCredentialNo() == null || this.getCredentialNo().equals("")) {
            throw new RuntimeException("[证件号码]不能为空");
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
