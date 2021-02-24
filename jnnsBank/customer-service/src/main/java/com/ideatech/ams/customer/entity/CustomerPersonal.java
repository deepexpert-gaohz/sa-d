package com.ideatech.ams.customer.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 对私客户信息表
 *
 * @author RJQ
 *
 */
@Entity
@Table(name = "CUSTOMER_PERSONAL")
@Data
public class CustomerPersonal extends BaseMaintainablePo implements
        Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_CUSTOMER_PERSONAL";

    /*
     *
     *
     *
     *
     * @Column(length =22)
     *
     * @OneToMany(mappedBy = "customerPersonal") private
     * Set<CustomerPersonalLog> customerPersonalLogs = new HashSet<>();
     */
    /**
     * 客户ID
     */
	/*
	
	@Column(length = 22)
	private Long customerId;*/


    private Long customersAllId;
    /**
     * 客户类别(01:中国居民；02:军人；03:武警；04:香港、澳门、台湾地区居民；05:外国公民；06：定居国外的中国公民)
     */
    @Column(length = 100)
    private String customerCategory;
    /**
     * 发证机关所在地的地区代码
     */
    @Column(length = 100)
    private String cardAreaCode;
    /**
     * 国籍
     */
    @Column(length = 30)
    private String country;
    /**
     * 联系电话
     */
    @Column(length = 50)
    private String telephone;
    /**
     * 邮政编码
     */
    @Column(length = 6)
    private String zipcode;
    /**
     * 地址(省市区)
     */
    @Column(length = 255)
    private String address;
    /**
     * 详细地址
     */
    @Column(length = 255)
    private String addressDetail;
    /**
     * 性别
     */


    @Column(length = 14)
    private String sex;
    /**
     * 代理人名称
     */
    @Column(length = 128)
    private String agentName;
    /**
     * 代理人身份证件种类
     */
    @Column(length = 10)
    private String agentIdType;
    /**
     * 代理人身份证件号码
     */
    @Column(length = 30)
    private String agentIdNo;
    /**
     * 代理人国籍
     */
    @Column(length = 30)
    private String agentCountry;
    /**
     * 代理人电话
     */
    @Column(length = 50)
    private String agentTelephone;
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

    /*
     *
     *
     *  private Long customerPersonalMidId;
     */

    // 验证唯一约束
    public String getSqlCheckMap(String errorMessageString) {
        return "";
    }

    public void Validate() {
        // [客户ID]的必填验证
		/*if (this.getCustomerId() == null) {
			throw new RuntimeException("[客户ID]不能为空");
		}*/
        // [创建人 ]的必填验证
        if (this.getCreatedBy() == null) {
            throw new RuntimeException("[创建人 ]不能为空");
        }
    }
}
