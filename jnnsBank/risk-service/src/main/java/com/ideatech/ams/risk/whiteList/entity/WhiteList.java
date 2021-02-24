package com.ideatech.ams.risk.whiteList.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: yinjie
 * @Date: 2019/5/27 9:57
 * @description
 * 白名单
 */

@Data
@Entity
@Table(name = "risk_white_list")
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "update yd_risk_white_list set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
public class WhiteList extends BaseMaintainablePo {

    //账户号
    private String accountId;

    //账户名称
    private String accountName;

    //社会统一编码
    private String socialUnifiedCode;

    //删除标记
    private Boolean deleted = Boolean.FALSE;
}
