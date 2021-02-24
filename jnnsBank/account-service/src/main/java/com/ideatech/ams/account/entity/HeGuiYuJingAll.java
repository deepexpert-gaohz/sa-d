package com.ideatech.ams.account.entity;


import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class  HeGuiYuJingAll extends BaseMaintainablePo implements Serializable {



    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 54541121212135342L;
    public static String baseTableName = "YD_He_Gui_Yu_Jing_All";


    //公共字段开始
    /**
     * 上级核心机构号代码
     */
    @Column(length = 500)
    private String bankCodeParent;
    /**
     * 上级核心机构号名称
     */
    @Column(length = 500)
    private String bankCodeName;

    /**
     * 预警标识字段
     * 1-临时户到期
     * 2-法人证件到期日
     * 3-证明文件到期日
     * 4-账户对账预警
     * 5-账户开户检测
     * 6-严重失信预警
     * 7-变更项监控表
     * 8-法律涉案账户预警
     * 9-税务处罚预警
     */
    @Column(length = 50)
    private String yuJingType;
    //公共字段结束
    @Column(length = 50)
    private int keyid;

    @Column(length = 50)
    private int typet;

    @Column(length = 500)
    private String title;

    @Column(length = 500)
    private String name;

    @Column(length = 100)
    private String cid;

    @Column(length = 500)
    private String organ;

    @Column(length = 500)
    private String caseno;

    @Column(length = 1000)
    private String origin;

    @Column(length = 500)
    private String eresult;

    @Column(length = 500)
    private String judegeresult;


    @Column(length = 500)
    private String datatype;

    @Column(length = 100)
    private String datatinme;


    @Column(length = 500)
    private String remark;


    @Column(length = 500)
    private String court;


    @Column(length = 500)
    private String context;

    @Column(length = 50)
    private String dateType;


    @Column(length = 100)
    private String state;


    @Column(length = 50)
    private String taxtype;

    @Column(length = 50)
    private String period;

    @Column(length = 500)
    private String balance;

    //临时户开始

    /**
     * 账号
     */@Column(length = 50)
    private String acctNo;
    /**
     * 账户名称
     */
    @Column(length = 500)
    private String acctName;
    /**
     * 账户开户日期
     */
    @Column(length = 50)
    private String acctCreateDate;
    /**
     * 开户银行金融机构编码
     */
    @Column(length = 50)
    private String bankCode;
    /**
     * 开户银行名称
     */
    @Column(length = 50)
    private String bankName;
    /**
     * 完整机构ID
     */
    @Column(length = 50)
    private String organFullId;
    /**
     * 账户有效期(临时账户)是否超期
     */
    @Column(length = 50)
    private Boolean isEffectiveDateOver;

    /**
     * 账户有效期(临时账户)
     */
    @Column(length = 500)
    private String effectiveDate;
    /**
     * 操作状态
     */
    @Column(length = 50)
    private String ckeckStatus;
    //临时户结束


    //法人证件到期开始
    /**
     * 存款人名称
     */
    @Column(length = 500)
    private String depositorName;

    /**
     * 法人类型（法定代表人、单位负责人）
     */
    @Column(length = 50)
    private String legalType;
    /**
     * 法人姓名
     */
    @Column(length = 50)
    private String legalName;
    /**
     * 法人证件类型
     */
    @Column(length = 50)
    private String legalIdcardType;
    /**
     * 法人证件编号
     */
    @Column(length = 500)
    private String legalIdcardNo;
   
    /**
     * 法人证件到期日
     */
    @Column(length = 500)
    private String legalIdcardDue;
    /**
     * 法人证件到期日是否超期
     */
    @Column(length = 50)
    private Boolean isLegalIdcardDueOver;
    // 法人证件到期结束


    //证明文件到期日开始
    /**
     * 证明文件到期日是否超期
     */
    @Column(length = 50)
    private Boolean isFileDueOver;

    /**
     * 证明文件1编号(工商注册号)
     */
    @Column(length = 500)
    private String fileNo;
    /**
     * 证明文件1种类(工商注册类型)
     */
    @Column(length = 50)
    private String fileType;

    /**
     * 证明文件1设立日期
     */
    @Column(length = 50)
    private String fileSetupDate;
    /**
     * 证明文件1到期日
     */
    @Column(length = 50)
    private String fileDue;
    /**
     * 核实状态
     */
    @Column(length = 50)
    private String checkStatus;
    //证明文件到期日结束

    //变更监控表开始
    @Column(length = 500)
    private  String customerId;//客户号
    @Column(length = 500)
    private  String alterItem;//变更项
    @Column(length = 500)
    private  String alterBefore;//变更前
    @Column(length = 500)
    private  String alterAfter;//变更后
    @Column(length = 50)
    private  String etlDate;//跑批日期
    @Column(length = 500)
    private  String warnTime;//预警时间
    @Column(length = 50)
    private  String dataDt;//数据日期
    //变更监控表结束

    //账户对账预警开始
    //和开户检测开始  字段一样
    //账户对账预警结束

    //账户开户检测开始
    @Column(length = 50)
    private String accountNo;//账号
    @Column(length = 50)
    private String riskDate;//数据日期
    @Column(length = 50)
    private String riskType; //风险类型
    @Column(length = 500)
    private String riskPoint;

    //账户开户检测结束

    //严重失信开始
    @Column(length = 50)
    private String customerNo;
    //严重失信结束
    @Column(length = 50)
    private String organCode;//机构号









    /**
     * 财务负责人姓名
     */
    @Column(length = 50)
    private String financeName;
    /**
     *财务负责人联系电话
     */
    @Column(length = 50)
    private String financeTelephone;
    /**
     * 财务负责人身份证号
     */
    @Column(length = 50)
    private String financeIdcardNo;


    /**
     * 经办人姓名
     */
    @Column(length = 100)
    private String operatorName;

    /**
     * 经办人证件号码
     */
    @Column(length = 50)
    private String operatorIdcardNo;

    /**
     * 经办人联系电话
     */
    @Column(length = 50)
    private String operatorTelephone;
	
	
	 @Column(length = 50)
     private String legalTelephone;


    //S数字化管理平台需要的字段
    @Column(length = 50)
    private String dmpOrganCode;


    @Column(length = 100)
    private String riskDesc;

    @Column(length = 500)
    private String regAdress;

    @Column(length = 500)
    private String acctType;

    @Column(length = 100)
    private String workAdress;



}
