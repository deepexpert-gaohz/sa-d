package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;

@Data
public class SaicInfoDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 地址
     */
    private String address;

    /**
     * 最后年检年度 YYYY
     */
    private String ancheyear;

    /**
     * 最后年检年日期
     */
    private String ancheyeardate;

    /**
     * 营业期限终止日期
     */
    private String enddate;

    /**
     * 法人
     */
    private String legalperson;

    /**
     * 法人类型可能的值为“法定代表人”，“经营者”，“负责人”
     */
    private String legalpersontype;

    /**
     * 核准日期
     */
    private String licensedate;

    /**
     * 名称
     */
    private String name;

    /**
     * 开业日期
     */
    private String opendate;

    /**
     * 省
     */
    private String province;

    /**
     * 注册资金
     */
    private String registfund;

    /**
     * 注册资金币种
     */
    private String registfundcurrency;

    /**
     * 注册号
     */
    private String registno;

    /**
     * 登记机关
     */
    private String registorgan;

    /**
     * 全国企业信用信息公示系统代码
     */
    private String saiccode;

    /**
     * 经营范围
     */
    private String scope;

    /**
     * 营业期限起始日期
     */
    private String startdate;

    /**
     * 经营状态
     */
    private String state;

    /**
     * 类型
     */
    private String type;

    /**
     * 数据更新时间
     */
    private Date updatetime;

    /**
     * 失败JSON
     */
    private String failjsonstr;

    /**
     * 内部工商号
     */
    private String idpno;

    /**
     * 消息公告
     */
    private String notice;

    /**
     * 注册地地区
     */
    private String regarea;

    /**
     * 注册地地区代码
     */
    private String regareacode;

    /**
     * 注册地城市
     */
    private String regcity;

    /**
     * 注册地省份
     */
    private String regprovince;

    /**
     * 注销或吊销日期
     */
    private String revokedate;

    /**
     * 工商注册号
     */
    private String unitycreditcode;

    private String idpId;

    /**
     * 最近更新天数
     */
    private Integer lastUpdateDays;

}