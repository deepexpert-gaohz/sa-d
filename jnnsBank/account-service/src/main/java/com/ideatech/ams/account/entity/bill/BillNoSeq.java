package com.ideatech.ams.account.entity.bill;

import com.ideatech.ams.account.enums.bill.BillTypeNo;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@Table(name = "BILL_NO_SEQ")
public class BillNoSeq extends BaseMaintainablePo implements Serializable {
    /**
     * 序列化ID, 缓存需要
     */
    private static final long serialVersionUID = 5454155825314635382L;
    public static String baseTableName = "YD_BILL_NO_SEQ";

    /**
     * 单据类型代码
     */
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private BillTypeNo type;

    /**
     * 日期
     */
    @Column(length = 8)
    private String date;

    /**
     * 机构代码
     */
    @Column(length = 20)
    private String orgCode;

    /**
     * 序号
     */
    @Column(length = 20)
    private Long sequence;
}
