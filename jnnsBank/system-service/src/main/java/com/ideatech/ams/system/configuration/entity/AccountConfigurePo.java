package com.ideatech.ams.system.configuration.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;

/**
 * 取消核准账户类型配置
 *
 */
@Entity
@Table(name = "sys_account_configure")
@Data
public class AccountConfigurePo extends BaseMaintainablePo {

    /**
     * 账户类型
     */
    private String acctType;

    /**
     * 存款人性质
     */
    private String depositorType;

    /**
     * 业务类型
     */
    private String operateType;
}
