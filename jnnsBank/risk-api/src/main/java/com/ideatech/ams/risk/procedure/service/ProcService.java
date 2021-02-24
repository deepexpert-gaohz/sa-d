package com.ideatech.ams.risk.procedure.service;

import com.ideatech.ams.risk.riskdata.entity.RiskCheckInfo;
import com.ideatech.ams.risk.riskdata.entity.RiskRecordInfo;

import java.util.List;

public interface ProcService {

    //同一单位客户在n天内发生x次以上的开户、销户或变更信息操作模型接口
    List<RiskRecordInfo> risk2001Application(String iDate, Boolean isHighRiskApi);


    //多个单位客户预留同一注册地址风险模型接口
    List<RiskRecordInfo> risk2002Application(String iDate, Boolean isHighRiskApi);


    //同一法人或负责人已经开立了多n个账户模型接口
    List<RiskRecordInfo> risk2005Application(String iDate, Boolean isHighRiskApi);

    //同一经办人?已经开立了多n个账户模型接口
   List<RiskRecordInfo> risk2006Application(String iDate, Boolean isHighRiskApi);


    //同一主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码模型接口
    List<RiskRecordInfo> risk2008Appliaction(String iDate, Boolean isHighRiskApi);

    //新模型总的触发跑批方法。
    void risk3000Appliaction(String iDate, Boolean isHighRiskApi);

    String findBankCodeByCode(String bankNo);

    boolean getIdcardNo(String IdcardNo, String rn1, String rn2);

    void saveDzDate(RiskCheckInfo riskCheckInfo);
}
