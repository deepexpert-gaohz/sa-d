package com.ideatech.ams.risk.model.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * @author yangwz
 * @Description 以账户维度搜索风险数据
 * @date 2020/8/20 9:38
 */
@Data
public class AcctModelSearchExtendDto extends PagingDto<AcctModelsExtendDto> {
    private String orgName; //机构名称
    private String endDate;
    private String startEndTime;//开始和结束日期
    private String accountNo;//账号
    private String accountName;//账号名称

    //触发模型数
    private String modelNum;
    //账户开户日期
    private String acctCreateDate;
    //账户销户日期
    private String cancelDate;
    // 账户性质
    private String acctType;
    //采集日期
    private String cjrq;
    //网点机构号
    private String orgNo;
}
