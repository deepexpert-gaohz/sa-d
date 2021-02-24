package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CollectState;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @Description TODO
 * @Author wanghongjie
 * @Date 2018/8/13
 **/
@Data
public class PbcCollectAccountDto  extends BaseMaintainableDto {
    private Long id;
    /**
     * 采集时间 yyyyMMdd hh:mm:ss
     */
    private String parDate;

    /**
     * 账号
     */
    private String acctNo;
    /**
     * 采集状态
     */
    private CollectState collectState;

    /**
     * 机构信用代码证系统采集状态
     */
    private CollectState eccsCollectState;

    /**
     * 信用代码证采集时间
     */
    private String eccsParDate;

    /**
     * 信用代码证采集失败完整
     */
    private String eccsParErrorMsg;

    /**
     * 采集失败原因
     */
    private String parErrorMsg;

    /**
     * 账户状态
     */
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    /**
     * 开户日期
     */
    private String acctCreateDate;

    /**
     * 企业名称
     */
    private String acctName;

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 采集人行机构集合ID
     */
    private Long collectOrganId;

    /**
     * 年检task的ID
     */
    private Long annualTaskId;

    /**
     * 收集task的ID
     */
    private Long collectTaskId;
}
