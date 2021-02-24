package com.ideatech.ams.risk.whiteList.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @Author: yinjie
 * @Date: 2019/5/27 11:37
 * @description
 */

@Data
public class WhiteListSearchDto extends PagingDto<WhiteListDto> {

    //账户号
    private String accountId;

    //账户名称
    private String accountName;

    //社会统一编码
    private String socialUnifiedCode;

    //法人机构号
    private String corporateBank;

}
