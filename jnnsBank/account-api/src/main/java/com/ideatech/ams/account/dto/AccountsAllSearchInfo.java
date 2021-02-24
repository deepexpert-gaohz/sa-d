package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.CompanyAcctType;
import lombok.Data;

import java.util.List;

/**
 * Created by houxianghua on 2018/11/28.
 */
@Data
public class AccountsAllSearchInfo extends AccountsAllInfo {
    /**
     * 确定的机构fullid(查询条件为=而不是like)
     */
    private String certainOrganFullId;

    private List<Long> customerLogIdList;

    //是否是存量:1-是，0-不是,-1-全部
    private List<String> string003s;

    private List<String> whiteLists;

    private List<CompanyAcctType> acctTypes;

}
