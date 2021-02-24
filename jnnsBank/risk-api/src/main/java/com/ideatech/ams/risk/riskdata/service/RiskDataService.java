package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.model.dto.AcctModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelsExtendDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDataInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.ams.risk.riskdata.dto.RiskRecordInfoDto;

import javax.transaction.Transactional;
import java.util.List;

public interface RiskDataService {

    ModelSearchExtendDto queryTradeRiskData(ModelSearchExtendDto modelSearchExtendDto);

    @Transactional
    ModelSearchExtendDto queryRiskStaticData(ModelSearchExtendDto modelSearchExtendDto);

    @Transactional
    ModelSearchExtendDto queryRiskDzData(ModelSearchExtendDto modelSearchExtendDto);


    List<RiskDataInfoDto> getAllAccountsByRiskIdAndRIskPoint(RiskDetailsSearchDto riskDetailsSearchDto);

    RiskDetailsSearchDto createSQLField(List list);

    ModelSearchExtendDto queryRiskDateForExp(ModelSearchExtendDto modelSearchExtendDto);

    ModelSearchExtendDto queryRiskid();

    ModelSearchExtendDto queryDealRiskid();

    RiskRecordInfoDto findAccountNearDate();

    long findByCorporateBankAndRiskIdAndCjrqIn1001(String code, ModelsExtendDto modelsExtendDto);

    long findByCorporateBankAndRiskIdAndCjrqInAnd1002(String code, ModelsExtendDto modelsExtendDto);

    /**
     * @Description ÕËºÅÎ¬¶ÈÊý¾Ý(½»Ò×)
     * @author yangwz
     * @date 2020/8/19 19:34
     */
    AcctModelSearchExtendDto queryRiskDataByAcctNo(AcctModelSearchExtendDto acctModelSearchExtendDto);

    /**
     * @Description ÕËºÅÎ¬¶ÈÊý¾Ý(¿ª»§)
     * @author yangwz
     * @date 2020/8/19 19:34
     */
    AcctModelSearchExtendDto queryRiskStaticDataByAcctNo(AcctModelSearchExtendDto acctModelSearchExtendDto);



    @Transactional
    ModelSearchExtendDto queryRiskStaticData1(ModelSearchExtendDto modelSearchExtendDto);

    @Transactional
    ModelSearchExtendDto queryRiskDzData1(ModelSearchExtendDto modelSearchExtendDto);
}
