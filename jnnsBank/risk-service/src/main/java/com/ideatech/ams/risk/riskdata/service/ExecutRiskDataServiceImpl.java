package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.riskdata.dto.RiskDataInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskRecordInfoDto;
import com.ideatech.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Slf4j
public class ExecutRiskDataServiceImpl implements Runnable  {
    private List<RiskDataInfoDto> telList;
    private HashMap<String ,List<RiskRecordInfoDto>> resultHaspMap;
    List<RiskDataInfoDto> mList;
    private int j;
    private String code;
    public ExecutRiskDataServiceImpl( List<RiskDataInfoDto> telList, List<RiskDataInfoDto> mList , HashMap<String ,List<RiskRecordInfoDto>> resultHaspMap, int j) {
        this.telList = telList;
        this.mList = mList;
        this.resultHaspMap = resultHaspMap;
        this.j =j;

    }

    public boolean compareTelInfo(){
        boolean flag= true;
        try{
            //迭代循环比较结束，返回结果集
            if(j==telList.size ()){
                return true;
            }
            String riskId="RISK_2007";
            for( RiskDataInfoDto dtos: mList){
                List<RiskRecordInfoDto> resultList = new ArrayList();
                /*判断非空*/
                String legTel = StringUtil.nullToString(dtos.getLegTel() );
                String operTel = StringUtil.nullToString(dtos.getOperTel() );
                String finTel = StringUtil.nullToString(dtos.getFinTel() );
                for(int i =0 ;i<telList.size ();i++){
                    RiskDataInfoDto dto = telList.get ( i );
                    if (dtos.getCustomerNo ().equals ( dto.getCustomerNo () )){
                        continue;
                    }
                    RiskRecordInfoDto dtoRisk2007 = new RiskRecordInfoDto();
                    /*判断非空*/
                    String legTel2007 = StringUtil.nullToString(dto.getLegTel() );
                    String operTel2007 = StringUtil.nullToString(dto.getOperTel() );
                    String finTel2007 = StringUtil.nullToString(dto.getFinTel() );
                    String fullId =  StringUtil.nullToString(dto.getFullId () );
                    String accountNo =  StringUtil.nullToString(dto.getAccountNo () );
                    String accountName =  StringUtil.nullToString(dto.getAccountName () );
                    String organCode = StringUtil.nullToString(dto.getOrganCode());
                    if(dtos!=null&&dto!=null) {
                        if((finTel.equals(finTel2007)&&!finTel.equals("")&&!finTel2007.equals("")) || (finTel.equals(legTel2007)&&!finTel.equals("")&&!legTel2007.equals("")) ||(finTel.equals(operTel2007)&&!finTel.equals("")&&!operTel2007.equals(""))){
                            dtoRisk2007.setCustomerId (dto.getCustomerNo() );
                            String risk2007Desc = "多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式(财务负责人联系方式-" + finTel+")";
                            dtoRisk2007.setRiskDesc ( risk2007Desc );
                            dtoRisk2007.setRiskId(riskId);
                            dtoRisk2007.setRiskPoint ( finTel );
                            dtoRisk2007.setStatus ( "0" );
                            dtoRisk2007.setDeleted ( false );
                            dtoRisk2007.setCorporateBank(code);
                            dtoRisk2007.setCorporateFullId ( fullId );
                            dtoRisk2007.setRiskPointDesc("联系方式:"+finTel);
                            dtoRisk2007.setAccountNo(accountNo);
                            dtoRisk2007.setAccountName(accountName);
                            //20201107
                            dtoRisk2007.setOrganCode(organCode);
                            resultList.add ( dtoRisk2007 );
                            continue;
                        }else if((legTel.equals(finTel2007)&&!legTel.equals("")&&!finTel2007.equals(""))||(legTel.equals(legTel2007)&&!legTel.equals("")&&!legTel2007.equals("")) ||(legTel.equals(operTel2007)&&!legTel.equals("")&&!operTel2007.equals(""))){

                            dtoRisk2007.setCustomerId (dto.getCustomerNo());
                            String risk2007Desc = "多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式(法人或负责人联系方式-" +legTel+")";
                            dtoRisk2007.setRiskDesc ( risk2007Desc );
                            dtoRisk2007.setRiskId(riskId);
                            dtoRisk2007.setStatus ( "0" );
                            dtoRisk2007.setRiskPoint ( legTel );
                            dtoRisk2007.setDeleted ( false );
                            dtoRisk2007.setCorporateBank(code);
                            dtoRisk2007.setCorporateFullId ( fullId );
                            dtoRisk2007.setRiskPointDesc("联系方式:"+legTel);
                            dtoRisk2007.setAccountNo(accountNo);
                            dtoRisk2007.setAccountName(accountName);
                            //20201107
                            dtoRisk2007.setOrganCode(organCode);
                            resultList.add ( dtoRisk2007 );
                            continue;
                        }else if((operTel.equals(finTel2007)&&!operTel.equals("")&&!finTel2007.equals(""))||(operTel.equals(legTel2007)&&!operTel.equals("")&&!legTel2007.equals(""))||(operTel.equals(operTel2007)&&!operTel.equals("")&&!operTel2007.equals(""))){
                            dtoRisk2007.setCustomerId (dto.getCustomerNo());
                            String risk2007Desc = "多个主体的法定代表人、财务负责人、财务经办人员预留同一个联系方式(财务经办人联系方式--" + operTel+")";
                            dtoRisk2007.setRiskDesc ( risk2007Desc );
                            dtoRisk2007.setRiskId(riskId);
                            dtoRisk2007.setStatus ( "0" );
                            dtoRisk2007.setRiskPoint ( operTel );
                            dtoRisk2007.setDeleted ( false );
                            dtoRisk2007.setCorporateBank(code);
                            dtoRisk2007.setCorporateFullId ( fullId );
                            dtoRisk2007.setRiskPointDesc("联系方式:"+operTel);
                            dtoRisk2007.setAccountNo(accountNo);
                            dtoRisk2007.setAccountName(accountName);
                            //20201107
                            dtoRisk2007.setOrganCode(organCode);
                            resultList.add ( dtoRisk2007 );
                            continue;
                        }
                    }
                }
                if(resultList.size ()>0){
                    resultHaspMap.put("risk2007"+j,resultList);
                    ++j;
                }
            }
        }
        catch (Exception e )
        {
            log.info(e.getMessage());
            return false;
        }
        return flag;
    }
    @Override

    public void run() {
        synchronized(telList) {
            this.compareTelInfo();
        }
    }

}
