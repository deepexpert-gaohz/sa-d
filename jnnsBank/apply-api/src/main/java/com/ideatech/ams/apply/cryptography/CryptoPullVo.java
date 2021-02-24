package com.ideatech.ams.apply.cryptography;

import lombok.Data;

import java.util.Date;

@Data
public class CryptoPullVo {
	/**
	 * 易账户端数据创建时间
	 */
    private String ezhCreateddate;
    /**
     * 账户状态
     * 目前没有用到，选填
     */
    private String accountstatus;
    /**
     * 预约编号 : APPLY447389244407676928
     * 易账户生成的预约号，具有唯一性，后续会改造成更具有意义的便于识别的编号
     */
    private String applyid; 
    /**
     * 预约回执
     * 行方填入的对于该预约的反馈，目前没有用到，选填
     */
    private String applynote;
    /**
     * 预约机构FullID : BASEROOTNODE-ORPHANNODE00
     * 发起申请者的组织机构树id
     */
    private String applyorganfullid;
    /**
     * 预约机构 : ORPHANNODE00
     * 发起申请者组织机构的唯一id
     */
    private String applyorganid;
    /**
     * 预约时间 ： 2018-05-20
     * 发起预约的日期
     */ 
    private String applytime;
    /**
     * 地区 ： 320113
     * 省市区 中的区的国标编码
     */ 
    private String area;
    /**
     * 银行名称 ： 南京银行
     * 预约的总行的中文名称
     */ 
    private String bank;
    /**
     * 银行客服 ： 
     * 目前没有用到
     */ 
    private Long bankcustomerid;
    /**
     * 银行受理时间
     * 目前没有用到
     */ 
    private String banktime;
    /**
     * 银行网点 ： 仙林支行
     * 预约的支行网点的中文名称
     */ 
    private String branch;

    /**
     * 城市 ： 320100
     * 省市区中的市的国标编码
     */ 
    private String city;

    /**
     * 是否有影像 0无 1有
     * 是否上传过影响文件
     */ 
    private String hasocr;

    /**
     * 企业名称 ： 南京小蓝鲸信息科技有限公司
     * 企业的中文全名
     */ 
    private String name;

    /**
     * 预约人员 ： 李响
     * 发起预约的开户企业相关联系人
     */ 
     private String operator;
    /**
     * 银行FullId：BASEROOTNODE-313301008887-vwnt3bRd1iex-jYZUghhueiUV-SzOvmDqDoXlu
     * 预约的目标网点的组织机构fullid, 
     */ 
    private String organfullid;
    /**
     * 银行机构号 ： SzOvmDqDoXlu
     * 预约的目标网点的组织机构唯一id
     */ 
    private String organid;
    /**
     * 预约手机 ： 15951893232
     */
    private String phone;
    /**
     * 省份 ： 320000
     */
    private String province;
    /**
     * 预约状态 ： UnComplete
     */
    private String status;

    private String statusName;
    /**
     * 提交时间
     * 目前没有用到
     */
    private Date submitdate;
    /**
     * 受理时间（小时）
     * 目前没有用到
     */
    private String times;
    /**
     * 预约类型（基本户/一般户）： jiben
     */
    private String type;
    /** 自增的id，作为游标*/
    private Long uuid;
    /**
     * 附加信息
     */
    private String additional;
    /**
     * 基本户开户许可核准号
     */
    private String accountKey;

    /**
     * 预约操作类型
     */
    private String billType;
}
