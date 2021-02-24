package com.ideatech.ams.risk.tradeRisk.entity;

import com.ideatech.common.entity.DealBase;
import com.ideatech.common.entity.util.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RISK_2022")
@Getter
@Setter
/**
 * create by yangcq 20191030 于西安神州数码科技园
 */
public class Risk2022 extends DealBase {
    @Comment("机构名称")
    @Column(name = "org_Name", columnDefinition = " varchar(255)")
    private String orgName;//机构名称
    /**
     * 客户统一编号
     */
    @Comment("客户号")
    @Column(name = "khtybh", columnDefinition = " varchar(255) ")
    private String khtybh;
    /**
     * 客户名称
     */
    @Comment("客户名称")
    @Column(name = "khmc", columnDefinition = " varchar(255) ")
    private String khmc;
    /**
     * 内部机构号
     */
    @Comment("内部机构号")
    @Column(name = "nbjgh", columnDefinition = " varchar(255) ")
    private String nbjgh;
    /**
     * 机构名称
     */
    @Comment("机构名称")
    @Column(name = "yxjgmc", columnDefinition = " varchar(255) ")
    private String yxjgmc;
    /**
     * 存款账号
     */
    @Comment("存款账号")
    @Column(name = "ckzh", columnDefinition = " varchar(255) ")
    private String ckzh;
    /**
     * 存款账号类型
     */
    @Comment("存款账号类型")
    @Column(name = "ckzhlx", columnDefinition = " varchar(255) ")
    private String ckzhlx;
    /**
     * 开户日期
     */
    @Comment("开户日期")
    @Column(name = "khrq", columnDefinition = " varchar(255) ")
    private String khrq;
    /**
     * 营业执照有效截止日期
     */
    @Comment("营业执照有效截止日期")
    @Column(name = "yyzzyxjzrq", columnDefinition = " varchar(255) ")
    private String yyzzyxjzrq;
    /**
     * 成立日期
     */
    @Comment("成立日期")
    @Column(name = "clrq", columnDefinition = " varchar(255) ")
    private String clrq;
    @Comment("用途")
    @Column(name = "yt", columnDefinition = " varchar(255) ")
    private String yt;//用途
    @Comment("交易渠道")
    @Column(name = "jyqu", columnDefinition = " varchar(255) ")
    private String jyqu;//交易渠道
    @Comment("交易次数")
    @Column(name = "jycs", columnDefinition = " varchar(255) ")
    private String jycs;//交易次数
}
