package com.ideatech.ams.apply.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ideatech.ams.apply.enums.ApplyEnum;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.BillType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Data
public class CompanyPreOpenAccountEntDto extends BaseMaintainableDto {

    private Long id;

	/**
     * 账户状态
     */
    private String accountstatus;

    /**
     * 预约编号
     */
    private String applyid;

    /**
     * 预约回执
     */
    private String applynote;

    /**
     * 预约机构FullID
     */
    private String applyorganfullid;

    /**
     * 预约机构的银行联行号
     */
    private String applyorganid;

    /**
     * 预约时间
     */
    private String applytime;

    /**
     * 地区
     */
    private String area;

    /**
     * 银行名称
     */
    private String bank;

    /**
     * 银行客服
     */
    private Long bankcustomerid;

    /**
     * 银行受理时间(客户临柜时间)
     */
    private String banktime;

    /**
     * 银行网点
     */
    private String branch;

    /**
     * 城市
     */
    private String city;

    /**
     * 是否有影像 0无 1有 2同步完成
     */
    private String hasocr;

    /**
     * 当前同步的图片数量
     */
    private Integer curNum;
    /**
     * 图片的总数
     */
    private Integer totalNum;
    /**
     * 企业名称
     */
    private String name;

    /**
     * 预约人员
     */
    private String operator;

    /**
     * 银行FullId
     */
    private String organfullid;

    /**
     * 银行机构id
     */
    private String organid;

    /**
     * 预约手机
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 预约状态
     */
    private String status;

    /**
     * 预约操作类型
     */
    private String billType;

    private String statusName;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitdate;

    /**
     * 受理时间（小时）
     */
    private String times;

    /**
     * 受理人员
     */
    private String  accepter;

    /**
     * 预约账户类型（基本户/一般户）
     */
    private String type;
    
    /**
     * 预约状态
     */
    private List<String> statuses;

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
     * 预约开始时间
     */
    private String beginDate;

    /**
     * 预约结束时间
     */
    private String endDate;

    /**
     * 申请开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDateApply;

    /**
     * 申请结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateApply;

    /**
     * 接洽开始时间
     */
    private String beginDateAccept;

    /**
     * 接洽结束时间
     */
    private String endDateAccept;

    /**
     * 账号
     */
    private String acctNo;

    /**
     * 是否是存量预约数据
     */
    private Boolean isStockData;


    /**
     * 创建时间 开始
     */
    private String createdDateStart;

    /**
     * 创建时间 结束
     */
    private String createdDateEnd;

    /**
     * 销户理由
     */
    private String cancelReason;
    
    public String getStatusName() {
        return ApplyEnum.getEnum(status).getDisplayName();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * 验证非空项
     */
    public void validate() {
//        if (StringUtils.isBlank(applyid)) {
//            throw new RuntimeException("[预约编号]不能为空");
//        }
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("[企业名称]不能为空");
        }
        if (StringUtils.isBlank(operator)) {
            throw new RuntimeException("[预约人]不能为空");
        }
        if (StringUtils.isBlank(phone)) {
            throw new RuntimeException("[预约手机]不能为空");
        }
        if (StringUtils.isBlank(type)) {
            throw new RuntimeException("[账户性质]不能为空");
        }
    }
}
