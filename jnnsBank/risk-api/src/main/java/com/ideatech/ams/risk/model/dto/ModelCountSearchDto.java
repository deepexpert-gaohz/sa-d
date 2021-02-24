package com.ideatech.ams.risk.model.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

/**
 * 发起流程的搜索条件
 * @Author: yinjie
 * @Date: 2019/4/30 9:39
 */

@Data
public class ModelCountSearchDto extends PagingDto<ModelAndCountDto>{

   /**
     * 模型名称
     */
    private String modelName;

   /**
     * 机构名称
     */
    private String bankCode;

    /**
     * 风险点编号
     */
    private String riskId;

    /**
     * 账户号
     */
    private String accountNo;

    /**
     *客户名称
     */
    private String accountName;

    private String dataDate;//数据日期
    private String endDate;
    private String key;//流程标识
    private String status;

}
