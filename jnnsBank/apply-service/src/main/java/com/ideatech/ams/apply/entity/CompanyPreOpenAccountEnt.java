package com.ideatech.ams.apply.entity;

import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.BillType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 预约结果 
 *
 */
@Entity
@Table(name = "yd_companypreopenaccountent")
@Data
public class CompanyPreOpenAccountEnt extends BaseMaintainablePo {
    /**
     * 账户状态
     */
    @Column(name = "yd_accountstatus")
    private String accountstatus;

    /**
     * 预约编号
     */
    @Column(name = "yd_applyid")
    private String applyid;

    /**
     * 预约回执
     */
    @Column(name = "yd_applynote")
    private String applynote;

    /**
     * 预约机构FullID
     */
    @Column(name = "yd_applyorganfullid")
    private String applyorganfullid;

    /**
     * 预约机构的银行联行号
     */
    @Column(name = "yd_applyorganid")
    private String applyorganid;

    /**
     * 预约时间
     */
    @Column(name = "yd_applytime")
    private String applytime;

    /**
     * 地区
     */
    @Column(name = "yd_area")
    private String area;

    /**
     * 银行名称
     */
    @Column(name = "yd_bank")
    private String bank;

    /**
     * 银行客服
     */
    @Column(name = "yd_bankcustomerid")
    private Long bankcustomerid;

    /**
     * 银行受理时间
     */
    @Column(name = "yd_banktime")
    private String banktime;

    /**
     * 银行网点
     */
    @Column(name = "yd_branch")
    private String branch;

    /**
     * 城市
     */
    @Column(name = "yd_city")
    private String city;

    /**
     * 是否有影像 0无 1有
     */
    @Column(name = "yd_hasocr")
    private String hasocr;

    /**
     * 当前同步的图片数量
     */
    @Column(name = "yd_curnum")
    private Integer curNum;
    /**
     * 图片的总数
     */
    @Column(name = "yd_totalnum")
    private Integer totalNum;

    /**
     * 企业名称
     */
    @Column(name = "yd_name")
    private String name;

    /**
     * 预约人员
     */
    @Column(name = "yd_operator")
    private String operator;

    /**
     * 银行FullId
     */
    @Column(name = "yd_organfullid")
    private String organfullid;

    /**
     * 银行机构id
     */
    @Column(name = "yd_organid")
    private String organid;

    /**
     * 预约手机
     */
    @Column(name = "yd_phone")
    private String phone;

    /**
     * 省份
     */
    @Column(name = "yd_province")
    private String province;

    /**
     * 预约状态
     */
    @Column(name = "yd_status")
    private String status;

    /**
     * 预约操作类型
     */
    @Column(name = "yd_billType")
    private String billType;

    @Transient
    private String statusName;

    /**
     * 提交时间
     */
    @Column(name = "yd_submitdate")
    private Date submitdate;

    /**
     * 受理时间（小时）
     */
    @Column(name = "yd_times")
    private String times;

    /**
     * 受理人员
     */
    @Column(name = "yd_accepter")
    private String  accepter;

    /**
     * 预约账户类型（基本户/一般户）
     */
    @Column(name = "yd_type")
    private String type;

    /**
     * 基本户核准号
     */
    private String accountKey;

    /**
     * 基本户地区代码
     */
    private String regAreaCode;

    /**
     * 开户状态
     */
    private String acctOpenStatus;

    /**
     * 受理按钮点击时间
     */
    private String acceptTimes;

    /**
     * 枚举在字典表“预约渠道类型”中维护
     */
//    @Column(columnDefinition="varchar(100) COMMENT '预约渠道'")
    private String channel;

    /**
     * 是否是存量预约数据
     */
    private Boolean isStockData;

    /**
     * 账号
     */
    @Column(length = 100)
    private String acctNo;

    /**
     * 销户理由
     */
    @Column(length = 255)
    private String cancelReason;

    public String getStatusName() {
        return ApplyEnum.getEnum(status).getDisplayName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}