package com.ideatech.ams.account.dto;

import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.common.dto.PagingDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import java.io.Serializable;

/**
 * 批处理
 *
 * @author fantao
 * @create 2018年10月17日15:33:30
 **/
@Data
public class BatchSuspendSearchDto extends PagingDto<BatchSuspendDto> implements Serializable {

    private String depositorName;

    private CompanyAcctType acctType;
    /**
     * 是否已上报
     */
    private CompanyIfType syncStatus;

    private Long id;

    private String organFullId;

    private String batchNo;

}
