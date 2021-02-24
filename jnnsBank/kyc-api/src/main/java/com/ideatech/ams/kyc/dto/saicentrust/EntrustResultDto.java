package com.ideatech.ams.kyc.dto.saicentrust;

import lombok.Data;

@Data
public class EntrustResultDto {
    /**
     * 委托状态
     * 1：委托成功，2：委托失败，3：已委托，正在更新， 0:查询异常
     */
    private String state;
    //详细描述
    private String details;
}
