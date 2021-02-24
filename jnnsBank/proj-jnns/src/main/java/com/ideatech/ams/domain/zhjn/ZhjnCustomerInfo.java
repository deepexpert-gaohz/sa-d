package com.ideatech.ams.domain.zhjn;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 智慧江南响应参数
 *
 * @auther yfy
 * @create 2020-8-6
 **/
@Entity
@Table(name = "ZHJN_CUSTOMER_INFO")
@Data
public class ZhjnCustomerInfo extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ZHJN_CUSTOMER_INFO";

    @Column(length = 50)
    private String orderId;//任务编号

    @Column(length = 50)
    private String customerNo;//客户号

    @Column(length = 255)
    private String customerName;//客户名称

    @Column(length = 50)
    private String telephone;//联系电话

    @Column(length = 32)
    private String bankCode;//对公账号

    @Column(length = 255)
    private String bankName;//开户行

    @Column(length = 50)
    private String fileType;//证明文件类型

    @Column(length = 100)
    private String fileNo;//证明文件号码

    @Column(length = 50)
    private String legalName;//法定代表人

    @Column(length = 50)
    private String legalTelephone;//法人联系方式

    @Column(length = 1000)
    private String regArea;//注册地址

    @Column(length = 14)
    private String acctType;//账户类型

    @Column(length = 35)
    private String createTime;//业务开始时间

    @Column(length = 1000)
    private String location;//定位

    @Column(length = 1000)
    private String imageNo;//影像批次号
    @Column(length = 50)
    private String clerkNo;//经办人行员号

    @Column(length = 50)
    private String clerkName;//经办人姓名

    @Column(length = 35)
    private String clerkTime;//经办时间

    @Column(length = 50)
    private String checkNo;//有权人行员号

    @Column(length = 50)
    private String checkName;//有权人姓名

    @Column(length = 35)
    private String checkTime;//审核时间

    @Column(length = 14)
    private Long customerStatus;//客户状态

    @Column(length = 1000)
    private String checkMessage;//审批意见

}
