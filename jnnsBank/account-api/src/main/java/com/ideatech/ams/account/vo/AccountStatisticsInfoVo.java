package com.ideatech.ams.account.vo;

import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import lombok.Data;

import java.util.List;

/**
 * @Description TODO
 * @Author wanghongjie
 * @Date 2019/1/9
 **/
@Data
public class AccountStatisticsInfoVo {
    //账户类型
    private CompanyAcctType acctType;
    //是否是存量:1-是，其他为不是
    private String string003;
    //账户状态
    private AccountStatus accountStatus;
    //是否是存量:1-是，其他为不是
    private List<String> string003s;
    //机构
    private String organFullId;
}
