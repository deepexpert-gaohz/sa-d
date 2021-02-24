package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.riskdata.RiskRegisterInfoDto;
import com.ideatech.ams.risk.riskdata.dao.RiskRegisterInfoDao;
import com.ideatech.ams.risk.riskdata.dto.RiskDataSearchDto;
import com.ideatech.ams.risk.riskdata.dto.RiskHandleInfoDto;
import com.ideatech.ams.risk.riskdata.entity.RiskRegisterInfo;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RiskRegisterInfoServiceImpl implements RiskRegisterInfoService {

    @Autowired
    RiskRegisterInfoDao riskRegisterInfoDao;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    RiskHandleInfoService riskHandleInfoService;
    /**
     * 保存信息到风险处理登记簿下
     * @author yangcq
     * @date 20190714
     * @address nanj
     * @param riskRegisterInfoDto
     */
    @Override
    public void saveRegisterInfo(RiskRegisterInfoDto riskRegisterInfoDto) {
        RiskRegisterInfo riskRegisterInfo = new RiskRegisterInfo();
        if(null != riskRegisterInfoDto.getId()){
            riskRegisterInfo = riskRegisterInfoDao.findOne(riskRegisterInfoDto.getId());
            if (riskRegisterInfo == null){
                riskRegisterInfo = new RiskRegisterInfo();
            }
        }
        ConverterService.convert(riskRegisterInfoDto,riskRegisterInfo);
        riskRegisterInfoDao.save ( riskRegisterInfo );
    }

    @Override
    public RiskRegisterInfoDto findById(String id) {
        RiskRegisterInfoDto riskRegisterInfoDto = new RiskRegisterInfoDto();
        Long idL = Long.parseLong ( id );
        RiskRegisterInfo info = riskRegisterInfoDao.findOne ( idL);
        ConverterService.convert(info,riskRegisterInfoDto);
        return riskRegisterInfoDto;
    }
    /**
     * 查询处理结果登记簿
     * @param riskDataSearchDto
     * @return
     */
    @Override
    public RiskDataSearchDto queryRegisterRiskInfo(RiskDataSearchDto riskDataSearchDto) {
        //Pageable pageable = new PageRequest (Math.max(riskDataSearchDto.getOffset() - 1, 0), riskDataSearchDto.getLimit());
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        //String sql = RiskSQLConstant.queryRegisterRiskInfoSQL;
         String sql = OracleSQLConstant.queryRegisterRiskInfoSQL;
        //String sql = MysqlSQLConstant.queryRegisterRiskInfoSQL;
        //String countSql = RiskSQLConstant.queryregisterRiskInfoCountSQL;
        String countSql = OracleSQLConstant.queryregisterRiskInfoCountSQL;
        //String countSql = MysqlSQLConstant.queryregisterRiskInfoCountSQL;
        String countStSql = MysqlSQLConstant.queryregisterRiskInfoCountStSQL;
        //String code = RiskUtil.getOrganizationCode();
//        sql = sql +" and og.yd_full_id like '%"+user.getOrgFullId ()+"%' and t1.yd_status!='0' and m.yd_corporate_bank=\'"+code+"\'";
//        countSql = countSql +" and og.yd_full_id like '%"+user.getOrgFullId ()+"%' and t1.yd_status!='0' and m.yd_corporate_bank=\'"+code+"\'";
//        countStSql = countStSql +" and og.yd_full_id like '%"+user.getOrgFullId ()+"%' and t1.yd_status!='0' and m.yd_corporate_bank=\'"+code+"\'";
        RiskDataSearchDto dto = riskHandleInfoService.getRiskDataSearchDto ( riskDataSearchDto, sql, countSql,countStSql,"handleList" );
        return dto;
    }


    @Override
    public RiskDataSearchDto countRegisterRiskInfo(RiskDataSearchDto riskDataSearchDto) {
        RiskHandleInfoDto dto = new RiskHandleInfoDto ();
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser ();
        String code = RiskUtil.getOrganizationCode();
        String sql = "select 1\n" +
                "  from yd_risk_register_info t1\n" +
                "  left join yd_accounts_all t2\n" +
                "    on t1.yd_account_no = t2.yd_acct_no\n" +
                "  left outer join yd_account_public t3\n" +
                "    on t2.yd_id = t3.yd_account_id\n" +
                "  left join yd_sys_organization og \n" +
                "    on t3.yd_organ_full_id = og.yd_full_id\n" +
                "  left join yd_risk_models m\n" +
                "    on t1.yd_risk_id = m.yd_model_id\n" +
                " where m.yd_deleted = 0 and t1.yd_status !='0' \n";

        Query nativeQuery = entityManager.createNativeQuery ( sql );
        int count=0;
        count = nativeQuery.getResultList().size ();
        riskDataSearchDto.setTotalRecord( Long.parseLong(String.valueOf ( count )));
        return riskDataSearchDto;
    }

    /**
     * 根据账号查询该账户的详细信息
     * @param accountNo
     * @return
     */
    @Override
    public String getBankCodeByAccount(String accountNo) {
        String sql = "select distinct og.yd_code,og.yd_full_id " +
                "  from  yd_accounts_all t2\n" +
                "  left join yd_sys_organization og \n" +
                "    on t2.yd_organ_full_id = og.yd_full_id\n" +
                " where og.yd_deleted = 0 \n" +
                " and t2.yd_acct_no= '"+accountNo+"'";
        Query nativeQuery = entityManager.createNativeQuery ( sql );
        List<Object[]> list = nativeQuery.getResultList();
        String bankCode = "";
        if(list!=null){
            Object[] obj=  list.get ( 0 );
            bankCode = (obj[0] == null) ? "" : obj[0].toString ();
        }
        return bankCode;
    }

    @Override
    public List<RiskRegisterInfo> findAllByAccountNoAndStatusAndCorporateBank(String accountNo, String status, String corporateBank) {
        return riskRegisterInfoDao.findAllByAccountNoAndStatusAndCorporateBank(accountNo,status, corporateBank);
    }
}
