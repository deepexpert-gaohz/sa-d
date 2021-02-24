package com.ideatech.ams.apply.entity;

import com.ideatech.ams.apply.enums.OpenAccountStatus;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.validator.EnumValidator;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hammer on 2018/3/13.
 */

@Table(name = "apply_open_account_log")
@Data
@Entity
public class OpenAccountLog extends BaseMaintainablePo {
    /**
     * 开户行的code
     */
    @Column(name = "open_bank")
    private String openBank;

    /**
     * 对此开户进行操作的人员的username 即手机号码
     */
    @Column(name = "operator")
    private String operator;

    /**
     * 来源
     * 1 预约
     * 2 客户尽调
     */
    @Column(name = "source")
    private String source;

    /**
     * 来源相关联的id
     * 仅用于开户来源于预约的情况，会关联到预约记录的主键
     */
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 开户操作的状态
     *
     * 开户中 processing
     * 开户成功 success
     * 开户失败 failed
     */
    @EnumValidator(enumClazz = OpenAccountStatus.class, acceptEmptyString = false, attribute = "value",
            message = "传入的开户流水的状态值不存在")
    @Column(name = "status")
    private String status;

    /**
     * 公司名称
     */
    @Column(name = "acct_name")
    private String acctName;

    /**
     * 公司账户类型
     *
     * 基本户
     * 一般户
     */
    @Column(name = "acct_type")
    private String acctType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 开户银行的fullid
     */
    @Column(name = "open_bank_fullid")
    private String openBankFullid;
}
