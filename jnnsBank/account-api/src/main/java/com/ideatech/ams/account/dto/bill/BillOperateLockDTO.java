package com.ideatech.ams.account.dto.bill;

import lombok.Data;

import java.util.Date;

/**
 * 流水操作锁定表DTO
 * 防止多人对同一个流水进行操作
 */
@Data
public class BillOperateLockDTO {
    private static final long serialVersionUID = 5454155825314635342L;

    private Long id;
    private String createdBy;
    private Date createdDate;
    private String lastUpdateBy;//最好操作人员id
    private Date lastUpdateDate;//最后操作时间

    /**
     * 流水id
     */
    private Long billId;

}
