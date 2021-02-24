package com.ideatech.ams.dto;

import lombok.Data;



@Data
public class ZhjnCustomerDto {

    private String orderId;//任务编号


    private String customerNo;//客户号


    private String customerName;//客户名称


    private String telephone;//联系电话


    private String bankCode;//对公账号

    private String bankName;//开户行


    private String fileType;//证明文件类型


    private String fileNo;//证明文件号码

    private String legalName;//法定代表人


    private String legalTelephone;//法人联系方式


    private String regArea;//注册地址


    private String acctType;//账户类型


    private String createTime;//业务开始时间


    private String location;//定位


    private String imageNo;//影像批次号


    private String clerkNo;//经办人行员号

    private String clerkName;//经办人姓名


    private String clerkTime;//经办时间


    private String checkNo;//有权人行员号


    private String checkName;//有权人姓名


    private String checkTime;//审核时间


    private Long customerStatus;//客户状态


    private String checkMessage;//审批意见

}
