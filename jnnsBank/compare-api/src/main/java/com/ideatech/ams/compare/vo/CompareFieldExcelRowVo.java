package com.ideatech.ams.compare.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * 比对字段导入excel  Vo
 */
@Data
public class CompareFieldExcelRowVo {

    @ExcelField(title = "账号",column = 0)
    private String acctNo;
    @ExcelField(title = "存款人名称",column = 1)
    private String depositorName;
    @ExcelField(title = "组织机构代码",column = 2)
    private String orgCode;
    @ExcelField(title = "机构代码",column = 3)
    private String organCode;
    @ExcelField(title = "法人姓名",column = 4)
    private String legalName;
    @ExcelField(title = "工商注册号",column = 5)
    private String regNo;
    @ExcelField(title = "经营范围",column = 6)
    private String businessScope;
    @ExcelField(title = "注册地址",column = 7)
    private String regAddress;
    @ExcelField(title = "注册资金",column = 8)
    private String registeredCapital;
    @ExcelField(title = "账户状态",column = 9)
    private String accountStatus;
    @ExcelField(title = "国税登记证号",column = 10)
    private String stateTaxRegNo;
    @ExcelField(title = "地税登记证号",column = 11)
    private String taxRegNo;
    @ExcelField(title = "开户银行名称",column = 12)
    private String bankName;
    @ExcelField(title = "账户性质",column = 13)
    private String acctType;
    @ExcelField(title = "开户日期",column = 14)
    private String acctCreateDate;
    @ExcelField(title = "开户许可核准号",column = 15)
    private String accountKey;
    @ExcelField(title = "证明文件类型",column = 16)
    private String fileType;
    @ExcelField(title = "证明文件编号",column = 17)
    private String fileNo;
    @ExcelField(title = "联系电话",column = 18)
    private String telephone;
    @ExcelField(title = "邮政编码",column = 19)
    private String zipcode;
    @ExcelField(title = "存款人类别",column = 20)
    private String depositorType;
    @ExcelField(title = "证件类型",column = 21)
    private String legalIdcardType;
    @ExcelField(title = "法人证件号码",column = 22)
    private String legalIdcardNo;
    @ExcelField(title = "行业归属",column = 23)
    private String industryCode;
    @ExcelField(title = "经济行业分类",column = 24)
    private String economyIndustry;
    @ExcelField(title = "开户地区代码",column = 25)
    private String regAreaCode;
    @ExcelField(title = "登记机关",column = 26)
    private String regOffice;
    @ExcelField(title = "销户日期",column = 27)
    private String cancelDate;
    @ExcelField(title = "成立日期",column = 28)
    private String opendate;
    @ExcelField(title = "营业执照到期日",column = 29)
    private String enddate;
}
