package com.ideatech.ams.pbc.dto;

import lombok.Data;

@Data
public class AmsPrintInfo {
    //开户许可证号
    private String openKey;
    //基本开户许可证号编号
    private String accountKey;
    //账户名称
    private String acctName;
    //查询密码
    private String selectPwd;
    //账号
    private String acctNo;
    //开户银行
    private String bankName;
    //法定代表人（单位负责人）姓名
    private String legalName;
    //日期
    private String printDate;
    //开户地地区代码
    private String bankAreaCode;
    //账户性质
    private String acctType;
    //账户状态
    private String accountStatus;
    //开户日期
    private String acctCreateDate;
    //销户日期
    private String cancelDate;
    //久悬日期
    private String acctSuspenDate;
    //序号
    private String num;
}
