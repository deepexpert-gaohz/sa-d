package com.ideatech.ams.domain;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;


/**
 * 交换号
 */
@Data
@Entity
public class JnnsAccountJhh extends BaseMaintainablePo implements Serializable {
    private static final long serialVersionUID = 4214870702757570454L;
    /**
     * 账号(带交换号),账号前3位是交换号
     */
    @Column(length = 150)
    private String jhhAcctNo;

    /**
     * 账号，不带交换换号
     */
    @Column(length = 150)
    private String acctNo;


}
