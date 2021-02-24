package com.ideatech.common.entity;

import com.ideatech.common.entity.util.Comment;
import com.ideatech.common.entity.util.DealBaseListener;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author yangwz
 * @Description
 * @date 2019-10-30 21:24
 */
@Data
@EntityListeners(DealBaseListener.class)
@MappedSuperclass
public class DealBase {

    /**
     * ID
     */
    @Id
    @GenericGenerator(name = "ideaIdGenerator", strategy = "com.ideatech.common.entity.id.IdeaIdGenerator")
    @GeneratedValue(generator = "ideaIdGenerator")
    protected Long id;
    @Comment("采集日期")
    @Column(name = "cjrq", columnDefinition = " varchar(255) ")
    private String cjrq;//采集日期
    @Comment("风险编号")
    @Column(name = "risk_Id", columnDefinition = " varchar(255) ")
    private String riskId;// 风险编号
    @Comment("风险金额")
    @Column(name = "risk_Amt", columnDefinition = "number")
    private double riskAmt;// 风险金额
    @Comment("客户编号")
    @Column(name = "kh_Id", columnDefinition = " varchar(255)  ")
    private String khId;// 客户编号
    @Comment("客户名称")
    @Column(name = "kh_Name", columnDefinition = " varchar(255) ")
    private String khName;// 客户名称
    @Comment("下发机构")
    @Column(name = "org_Id", columnDefinition = " varchar(255) ")
    private String orgId;// 下发机构
    @Comment("机构层级")
    @Column(name = "corporate_Full_Id", columnDefinition = " varchar(255) ")
    private String corporateFullId;//机构层级
    @Comment("法人机构行标识")
    @Column(name = "corporate_Bank", columnDefinition = " varchar(255) ")
    private String corporateBank;//法人机构行标识
    @Comment("模型描述")
    @Column(name = "risk_Desc", columnDefinition = " varchar(255) ")
    private String riskDesc;//模型描述
}
