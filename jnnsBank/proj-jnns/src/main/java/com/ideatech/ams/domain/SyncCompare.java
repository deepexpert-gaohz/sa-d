package com.ideatech.ams.domain;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 上报账户实时比对报表类
 *
 * @auther ideatech
 * @create 2018-11-29 9:41 AM
 **/

@Data
@Entity
@Table(name = "Sync_Compare",indexes = {@Index(name = "syncCompare_an_idx", columnList = "acctNo")})
public class  SyncCompare extends BaseMaintainablePo implements Serializable {
    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 54541121212135342L;
    public static String baseTableName = "YD_SYNC_COMPARE";

    /**
     * 账号
     */
    @Column(length = 50)
    private String acctNo;

    /**
     * 核心机构号
     */
    @Column(length = 100)
    private String organCode;

    /**
     * 完整机构号
     */
    private String organFullId;

    /**
     * 开户时间
     */
    private String acctOpenDate;

    /**
     * 企业名称
     */
    @Column(length = 100)
    private String depositorName;

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    /**
     * 账户性质
     */
    @Column(length = 50)
    private String acctType;

    /**
 * 人行上报状态
 */
    @Column(length = 50)
    private String pbcStarts;

    /**
     * 机构信用代码上报状态
     */
    @Column(length = 50)
    private String eccsStarts;

//    /**
//     * 销户日期
//     */
//    @Column(length = 100)
//    private String xiohriqi;
//
//    /**
//     * 开户日期
//     */
//    @Column(length = 100)
//    private String kaihriqi;

    /**
     * 开销户标志
     */
    @Column(length = 100)
    private String kaixhubz;

    /**
     * 业务日期
     */
    private String businessDate;

    public String getPbcStarts() {
        return pbcStarts;
    }

    public void setPbcStarts(String pbcStarts) {
        this.pbcStarts = pbcStarts;
    }

    public String getEccsStarts() {
        return eccsStarts;
    }

    public void setEccsStarts(String eccsStarts) {
        this.eccsStarts = eccsStarts;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }



    public String getKaixhubz() {
        return kaixhubz;
    }

    public void setKaixhubz(String kaixhubz) {
        this.kaixhubz = kaixhubz;
    }
//    public String getXiohriqi() {
//        return xiohriqi;
//    }
//
//    public void setXiohriqi(String xiohriqi) {
//        this.xiohriqi = xiohriqi;
//    }
//
//    public String getKaihriqi() {
//        return kaihriqi;
//    }
//
//    public void setKaihriqi(String kaihriqi) {
//        this.kaihriqi = kaihriqi;
//    }


    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public String getOrganFullId() {
        return organFullId;
    }

    public void setOrganFullId(String organFullId) {
        this.organFullId = organFullId;
    }

    public String getAcctOpenDate() {
        return acctOpenDate;
    }

    public void setAcctOpenDate(String acctOpenDate) {
        this.acctOpenDate = acctOpenDate;
    }

    public String getDepositorName() {
        return depositorName;
    }

    public void setDepositorName(String depositorName) {
        this.depositorName = depositorName;
    }

}
