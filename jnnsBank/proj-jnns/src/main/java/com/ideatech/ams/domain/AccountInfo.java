package com.ideatech.ams.domain;

import com.ideatech.ams.account.enums.CompanyAcctType;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.*;



/**
 * @version V1.0
 * @author: sunhui
 * @date: 2019/12/24 20:05
 * @description:
 */

/**
 * 需要拉取的账户信息表
 */
@Data
@Entity
public class AccountInfo {



    /**
     *关联目录名称
     */
    @Id
    private String imgDir;

    /**
     * 账户性质
     */

    private String accKind;

    /**
     * 业务类型
     */
    private String bizKind;

    /**
     * 存款人类别
     */
    private String depKind;

    /**
     * 账户账号
     */
    private String accNo;

    /**
     * 账户名称
     */
    private String accName;

    /**
     * 存款人名称
     */
    private String depName;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 开户银行机构代码
     */
    private String accBankCode;

    /**
     * 经办人
     */
    private String agent;

    /**
     * 经办人身份证件号
     */
    private String agentIdno;

    /**
     * 经办人电话
     */
    private String agentTel;
}
