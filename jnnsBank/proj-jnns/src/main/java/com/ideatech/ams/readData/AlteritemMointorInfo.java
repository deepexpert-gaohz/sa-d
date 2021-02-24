package com.ideatech.ams.readData;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ALTERITEM_MOINTOR")
@Data
public class AlteritemMointorInfo extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635342L;
    public static String baseTableName = "YD_ALTERITEM_MOINTOR";



    @Column(length = 255)
    private  String customerId;//客户号

    @Column(length = 255)
    private  String creditNo;//统一社会信用号

    @Column(length = 50)
    private  String companyName;//企业名称

    @Column(length = 20)
    private  String isWarning;//是否预警

    @Column(length = 20)
    private  String alterYest;//昨日变更时间

    @Column(length = 20)
    private  String alterNow;//今日变更时间

    @Column(length = 500)
    private  String alterItem;//变更项

    @Column(length = 500)
    private  String alterBefore;//变更前

    @Column(length = 500)
    private  String alterAfter;//变更后

    @Column(length = 100)
    private  String ds;//预警来源

    @Column(length = 50)
    private  String dataDt;//数据日期

    @Column(length = 50)
    private  String etlDate;//跑批日期


    @Column(length = 100)
    private  String fullId;//跑批日期


    @Column(length = 100)
    private  String code;//支行code



    @Column(length = 100)
    private  String organFullId;


    @Column(length = 500)
    private  String warnTime;//预警时间




}
