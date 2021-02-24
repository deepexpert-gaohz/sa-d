package com.ideatech.ams.account.entity.bill;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 流水操作锁定表
 * 防止多人对同一个流水进行操作
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class BillOperateLock extends BaseMaintainablePo implements Serializable {
    /**
     * 流水id
     */
    private Long billId;

}
