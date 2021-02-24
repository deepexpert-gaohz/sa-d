package com.ideatech.ams.risk.model.dto;


import lombok.Data;

@Data
public class ModelsExtendDto {
    private String modelId;        // 模型编号
    private String modelsId;    //models表中id
    private String name;        //
    private String mdesc;        // 模型描述
    private String status;        // 模型状态
    private String raiseName;    //提出人
    private String raiseTime;//提出时间
    private String tableName;        // 数据表名
    private String procName;        // 过程名称
    private String dataImg;//数据流图
    private String modelProc; //模型代码
    private String disModel;//模型分配状态
    private String tjjg;//统计结果
    private String modelName;
    private String ogId;//add zhudd 机构 备用
    private String checkedListId;//add by yangcq 用户保存已勾选的checkbox，用于翻页功能
    private String signFlag;//add by mengxp 用户勾选指定模型需求功能，（去重，误报，风险关系图）,1表示已勾选
    private String riskType; //风险类型
    private String ordNum;//序号
    private String minDate;  //开始时间
    private String maxDate;   //结束时间
    private String orgId; //机构编号
    private String riskDesc;
    private String riskDate;
    private String riskPoint;
    private String riskCnt;
    private String riskPointDesc;//风险点描述
    private String accountNo;//账号
    private String acctName;//账号
    private String organCode;//机构号




    /**
     * 财务负责人姓名
     */
   
    private String financeName;
    /**
     *财务负责人联系电话
     */
   
    private String financeTelephone;
    /**
     * 财务负责人身份证号
     */
   
    private String financeIdcardNo;


    /**
     * 经办人姓名
     */
   
    private String operatorName;

    /**
     * 经办人证件号码
     */
  
    private String operatorIdcardNo;

    /**
     * 经办人联系电话
     */
   
    private String operatorTelephone;

    /**
     * 法人姓名
     */
   
    private String legalName;

    /**
     * 法人证件编号
     */
   
    private String legalIdcardNo;
    /**
     * 法人联系电话
     */
    
    private String legalTelephone;


    //S数字化管理平台需要的字段
    private String dmpOrganCode;
    //z注册地址
    private String regAdress;
    //账户性质
    private String acctType;
    //客户号
    private String customerId;

     //经营地址
    private String workAdress;


    public ModelsExtendDto(String modelsId, String name, String modelId, String tjjg, String riskType) {
        this.modelId = modelId;
        this.modelsId = modelsId;
        this.name = name;
        this.tjjg = tjjg;
        this.riskType = riskType;
    }

    public ModelsExtendDto() {
    }
}
