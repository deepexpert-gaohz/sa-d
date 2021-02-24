package com.ideatech.ams.kyc.entity.idcard;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 身份证核查
 */
@Data
@Entity
@Table(name = "IdCardBatch")
public class IdCardBatch extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 5454155825314635342L;
    /**
     * 身份证号码
     */
    @Column(length = 18)
    private String idCardNo;
    /**
     * 姓名
     */
    private String idCardName;
    /**
     * 核查结果
     */
    private String checkResult;
}
