package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019-09-11.
 * 企业信息联网核查业务受理时间通知报文实体
 */

@Entity
@Data
@Table(name = "mivs_business_accept_notice")
public class BusinessAcceptTimeNotice extends MessageHeader {

    /**
     * CurrentSystemDate
     * 系统当前日期
     * <CurSysDt>  [1..1]  ISODate  禁止中文
     */
    private String curSysDt;


    /**
     * NextSystemDate
     * 系统下一日期
     * <NxtSysDt>  [1..1]  ISODate  禁止中文
     */
    private String nxtSysDt;

    /**
     * 扩展字段
     */
    private String String001;
    private String String002;
    private String String003;
    private String String004;
    private String String005;
    private String String006;
    private String String007;
    private String String008;
    private String String009;
    private String String010;
    private String String011;
    private String String012;
    private String String013;
    private String String014;
    private String String015;
}
