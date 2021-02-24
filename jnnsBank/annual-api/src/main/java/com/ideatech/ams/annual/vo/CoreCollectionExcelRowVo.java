package com.ideatech.ams.annual.vo;

import com.ideatech.common.excel.util.annotation.ExcelField;
import lombok.Data;

/**
 * @author wanghongjie
 * @create 2018-08-07 下午4:26
 **/
@Data
public class CoreCollectionExcelRowVo {
    @ExcelField(title = "账号", column = 0)
    private String acctNo;
    @ExcelField(title = "企业名称", column = 1)
    private String depositorName;
    @ExcelField(title = "法人姓名", column = 2)
    private String legalName;
    @ExcelField(title = "工商注册号", column = 3)
    private String regNo;
    @ExcelField(title = "组织机构代码证", column = 4)
    private String orgCode;
    @ExcelField(title = "机构代码(行内机构号)", column = 5)
    private String organCode;
    @ExcelField(title = "注册资金", column = 6)
    private String registeredCapitalStr;
    @ExcelField(title = "企业地址", column = 7)
    private String regFullAddress;
    @ExcelField(title = "法人证件种类", column = 8)
    private String legalIdcardType;
    @ExcelField(title = "法人证件号码", column = 9)
    private String legalIdcardNo;
    @ExcelField(title = "注册币种", column = 10)
    private String regCurrencyType;
    @ExcelField(title = "国税登记证", column = 11)
    private String stateTaxRegNo;
    @ExcelField(title = "地税登记证", column = 12)
    private String taxRegNo;
    @ExcelField(title = "经营范围", column = 13)
    private String businessScope;
    @ExcelField(title = "账户性质", column = 14)
    private String acctType;
}
