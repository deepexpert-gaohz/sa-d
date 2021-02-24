package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.PagingDto;
import lombok.Data;

@Data
public class KycSearchHistorySearchDto extends PagingDto<KycSearchHistoryDto> {
    /**
     * 查询关键字
     */
    private String key;
    /**
     * 起始日期
     */
    private String beginDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 尽调结果（是否有对应的内部工商ID，有为成功，没有为失败）
     */
    private String flag;
    private String orgFullId;
    /**
     * 排序字段
     */
    private String column;
    /**
     * 升序倒叙标识
     */
    private String orderStr;

    /**
     * 关联批次号
     */
    private String batchNo;
}
