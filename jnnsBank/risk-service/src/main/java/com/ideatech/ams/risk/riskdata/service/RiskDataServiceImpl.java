package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.account.dao.HeGuiYuJingAllDao;
import com.ideatech.ams.account.entity.HeGuiYuJingAll;
import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.model.dto.AcctModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.AcctModelsExtendDto;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelsExtendDto;
import com.ideatech.ams.risk.riskdata.dao.RiskApiDao;
import com.ideatech.ams.risk.riskdata.dto.RiskDataInfoDto;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.ams.risk.riskdata.dto.RiskRecordInfoDto;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.constant.IdeaConstant;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author yangcq
 * @date 20190422
 * @desc 查询风险数据实现层
 */
@Service
@Slf4j
public class RiskDataServiceImpl implements RiskDataService {


    @Autowired
    private EntityManager em;

    @Autowired
    private RiskApiDao riskApiDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private HeGuiYuJingAllDao heGuiYuJingAllDao;

    /**
     * 查询交易监测风险数据
     *
     * @param modelSearchExtendDto
     * @return
     * @author yangcq
     * @date 2019-05-31
     */
    public ModelSearchExtendDto queryTradeRiskData(ModelSearchExtendDto modelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchExtendDto.getOffset() - 1, 0), modelSearchExtendDto.getLimit());
//        String sql = OracleSQLConstant.queryTradeRiskDataSql;
        //重庆银行修改
        String sql = OracleSQLConstant.queryTradeRiskDataSqlCQ;
        //String sql = MysqlSQLConstant.queryTradeRiskDataSql;
      /*  sql +=  " AND t3.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";
        sql +=  " AND t1.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";*/
        String countSql = OracleSQLConstant.queryTradeRiskDataCountSql;
        //String countSql = MysqlSQLConstant.queryTradeRiskDataCountSql;
       /* countSql +=  " AND t3.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";
        countSql +=  " AND t1.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";*/
        return getModelSearchExtendDto(modelSearchExtendDto, pageable, sql, countSql);
    }

    @NotNull
    private ModelSearchExtendDto getModelSearchExtendDto(ModelSearchExtendDto modelSearchExtendDto, Pageable pageable, String sql, String countSql) {
        //sql += " and to_char(t3.yd_risk_date,'yyyyMMdd') >= ?4";
        //sql += MysqlSQLConstant.getModelSearchExtendDtoTOCHAR1;
        //countSql += " and to_char(t3.yd_risk_date,'yyyyMMdd') >= ?4";
        //countSql += MysqlSQLConstant.getModelSearchExtendDtoTOCHAR1;
        //sql += " and to_char(t3.yd_risk_date,'yyyyMMdd') <= ?5";
        //sql += MysqlSQLConstant.getModelSearchExtendDtoTOCHAR2;
        //countSql += " and to_char(t3.yd_risk_date,'yyyyMMdd') <= ?5";
        //countSql += MysqlSQLConstant.getModelSearchExtendDtoTOCHAR2;
//        sql +=  " AND t3.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";
//        sql+=" and t3.yd_corporate_full_id like '%"+SecurityUtils.getCurrentOrgFullId()+"%'";
//        sql +=  " AND t1.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";
//
//        countSql+=" and t3.yd_corporate_full_id like '%"+SecurityUtils.getCurrentOrgFullId()+"%'";
//        countSql +=  " AND t1.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";
//        countSql +=  " AND t3.yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"'";

        //sql += " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status)\n";
        //sql+=MysqlSQLConstant.getModelSearchExtendDtoSql;
        //countSql += " group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status\n";
        //countSql+= MysqlSQLConstant.getModelSearchExtendDtoCountSql;


        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            sql += " and t1.yd_model_id like ?1";
            countSql += " and t1.yd_model_id like ?1";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getRiskType())) {
            sql += " and t2.yd_id = ?2";
            countSql += " and t2.yd_id = ?2";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStatus())) {
            sql += " and t3.yd_status = ?3";
            countSql += " and t3.yd_status = ?3";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            sql += OracleSQLConstant.getModelSearchExtendDtoTOCHAR1;
            countSql += OracleSQLConstant.getModelSearchExtendDtoTOCHAR1;
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            sql += OracleSQLConstant.getModelSearchExtendDtoTOCHAR2;
            countSql += OracleSQLConstant.getModelSearchExtendDtoTOCHAR2;
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getAccountNo())) {
            sql += " and t3.yd_account_no = ?6";
            countSql += " and t3.yd_account_no = ?6";
        }
        sql += OracleSQLConstant.getModelSearchExtendDtoSqlqudiaokouhao;
        countSql += OracleSQLConstant.getModelSearchExtendDtoCountSqlqudiaokouhao;
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            nativeQuery.setParameter(1, modelSearchExtendDto.getModelName());
            nativeQueryCount.setParameter(1, modelSearchExtendDto.getModelName());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getRiskType())) {
            nativeQuery.setParameter(2, modelSearchExtendDto.getRiskType());
            nativeQueryCount.setParameter(2, modelSearchExtendDto.getRiskType());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStatus())) {
            nativeQuery.setParameter(3, modelSearchExtendDto.getStatus());
            nativeQueryCount.setParameter(3, modelSearchExtendDto.getStatus());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            nativeQuery.setParameter(4, modelSearchExtendDto.getStartEndTime().split("~")[0].trim().replace("-", ""));
            nativeQueryCount.setParameter(4, modelSearchExtendDto.getStartEndTime().split("~")[0].trim().replace("-", ""));
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            nativeQuery.setParameter(5, modelSearchExtendDto.getStartEndTime().split("~")[1].trim().replace("-", ""));
            nativeQueryCount.setParameter(5, modelSearchExtendDto.getStartEndTime().split("~")[1].trim().replace("-", ""));
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getAccountNo())) {
            nativeQuery.setParameter(6, modelSearchExtendDto.getAccountNo());
            nativeQueryCount.setParameter(6, modelSearchExtendDto.getAccountNo());
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());

        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object[]> resultList1 = nativeQueryCount.getResultList();
        Long count;
        if (resultList1 == null || resultList1.size() <= 0) {
            count = 0L;
        } else {
            count = Long.parseLong(String.valueOf(resultList1.size()));
        }
        List<ModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            ModelsExtendDto e = new ModelsExtendDto();
            String orm = (obj[0] == null) ? "" : obj[0].toString();
            if (orm.indexOf(".") != -1) {
                orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
            }
            e.setOrdNum(orm);
            e.setModelId((obj[1] == null) ? "" : obj[1].toString());
            e.setRiskDate((obj[2] == null) ? "" : obj[2].toString());
            e.setName((obj[3] == null) ? "" : obj[3].toString());
            e.setRiskDesc((obj[4] == null) ? "" : obj[4].toString());
            e.setRiskType((obj[5] == null) ? "" : obj[5].toString());
            e.setRiskPoint((obj[6] == null) ? "" : obj[6].toString());
            e.setStatus((obj[7] == null) ? "" : obj[7].toString());
            e.setRiskCnt((obj[8] == null) ? "0" : obj[8].toString());
            e.setAccountNo((obj[9] == null) ? "0" : obj[9].toString());
            e.setAcctName((obj[10] == null) ? "0" : obj[10].toString());
            list.add(e);
        }
        modelSearchExtendDto.setList(list);
        modelSearchExtendDto.setTotalPages((int) Math.ceil(count.intValue() / modelSearchExtendDto.getLimit()));
        modelSearchExtendDto.setTotalRecord(count);
        return modelSearchExtendDto;
    }

    /**
     * 查询开户监测风险数据
     *
     * @param modelSearchExtendDto
     * @return
     * @authority yangcq
     * @date 20190618
     */
    @Override
    public ModelSearchExtendDto queryRiskStaticData(ModelSearchExtendDto modelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchExtendDto.getOffset() - 1, 0), modelSearchExtendDto.getLimit());
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        //重庆银行修改
        String sql = OracleSQLConstant.queryRiskStaticDataSqlCQ;
        String countSql = "select count(1)\n" +
                " from yd_risk_record_info t3 \n"+
                " left join yd_accounts_all t4 on t4.yd_acct_no = t3.yd_account_no \n"+
                " left join yd_account_public t5 on t5.yd_account_id = t4.yd_id \n"+
                " left join yd_customers_all t6 on t6.yd_customer_no = t4.yd_customer_no \n"+
                " left join yd_customer_public t7 on t7.yd_customer_id = t6.yd_id \n"+
                " left join yd_risk_models t1 on t1.yd_model_id = t3.yd_risk_id \n"+
                " left join yd_risk_model_type t2 on t1.yd_type_id = t2.yd_id \n"+
                " where t2.yd_deleted = 0 \n";
        return getModelSearchExtendSraricDto(modelSearchExtendDto, pageable, sql, countSql);
    }

    @NotNull
    private ModelSearchExtendDto getModelSearchExtendSraricDto(ModelSearchExtendDto modelSearchExtendDto, Pageable pageable, String sql, String countSql) {
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            sql += " and t1.yd_model_id like ?1";
            countSql += " and t1.yd_model_id like ?1";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getAccountNo())) {
            sql += " and t3.yd_account_no = ?6";
            countSql += " and t3.yd_account_no = ?6";
        }
        if(StringUtils.isNotBlank(modelSearchExtendDto.getOrganCode())){
            sql += " and t3.yd_organ_code = ?7";
            countSql += " and t3.yd_organ_code = ?7";
        }
        sql += " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_account_name,t3.yd_organ_code, t5.yd_operator_telephone,t5.yd_operator_idcard_no, t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,t7.yd_legal_telephone,t7.yd_legal_idcard_no,t7.yd_legal_name)\n";
        countSql += OracleSQLConstant.getModelSearchExtendDtoCountSql1;
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            nativeQuery.setParameter(1, modelSearchExtendDto.getModelName());
            nativeQueryCount.setParameter(1, modelSearchExtendDto.getModelName());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getAccountNo())) {
            nativeQuery.setParameter(6, modelSearchExtendDto.getAccountNo());
            nativeQueryCount.setParameter(6, modelSearchExtendDto.getAccountNo());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getOrganCode())) {
            nativeQuery.setParameter(7, modelSearchExtendDto.getOrganCode());
            nativeQueryCount.setParameter(7, modelSearchExtendDto.getOrganCode());
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object[]> resultList1 = nativeQueryCount.getResultList();
        Long count;
        if (resultList1 == null || resultList1.size() <= 0) {
            count = 0L;
        } else {
            count = Long.parseLong(String.valueOf(resultList1.size()));
        }
        List<ModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            ModelsExtendDto e = new ModelsExtendDto();
            String orm = (obj[0] == null) ? "" : obj[0].toString();
            if (orm.indexOf(".") != -1) {
                orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
            }
            e.setOrdNum(orm);
            e.setModelId((obj[1] == null) ? "" : obj[1].toString());
            e.setRiskDate((obj[2] == null) ? "" : obj[2].toString());
            e.setName((obj[3] == null) ? "" : obj[3].toString());
            e.setRiskDesc((obj[4] == null) ? "" : obj[4].toString());
            e.setRiskType((obj[5] == null) ? "" : obj[5].toString());
            e.setRiskPoint((obj[6] == null) ? "" : obj[6].toString());
            e.setStatus((obj[7] == null) ? "" : obj[7].toString());
            e.setRiskCnt((obj[8] == null) ? "0" : obj[8].toString());
            e.setAccountNo((obj[9] == null) ? "0" : obj[9].toString());
            e.setAcctName((obj[10] == null) ? "0" : obj[10].toString());
            e.setOrganCode((obj[11] == null) ? "0" : obj[11].toString());
            list.add(e);
        }
        modelSearchExtendDto.setList(list);
        modelSearchExtendDto.setTotalPages((int) Math.ceil(count.intValue() / modelSearchExtendDto.getLimit()));
        modelSearchExtendDto.setTotalRecord(count);
        return modelSearchExtendDto;
    }


    /**
     * 查询对账监测风险数据
     *
     * @param
     * @return
     */
    @Override
    public ModelSearchExtendDto queryRiskDzData(ModelSearchExtendDto modelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchExtendDto.getOffset() - 1, 0), modelSearchExtendDto.getLimit());

        String sql = OracleSQLConstant.queryRiskDzDataSqlCQ;

        String countSql = OracleSQLConstant.queryRiskStaticDataCountSql;
        return getModelSearchDZDto(modelSearchExtendDto, pageable, sql, countSql);
    }

    @NotNull
    private ModelSearchExtendDto getModelSearchDZDto(ModelSearchExtendDto modelSearchExtendDto, Pageable pageable, String sql, String countSql) {
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            sql += " and t1.yd_model_id like ?1";
            countSql += " and t1.yd_model_id like ?1";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getAccountNo())) {
            sql += " and t3.yd_account_no = ?6";
            countSql += " and t3.yd_account_no = ?6";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getOrganCode())) {
            sql += " and t3.yd_organ_code = ?7";
            countSql += " and t3.yd_organ_code = ?7";
        }
        sql += " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_organ_code)\n";
        countSql += OracleSQLConstant.getModelSearchExtendDtoCountSql;
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            nativeQuery.setParameter(1, modelSearchExtendDto.getModelName());
            nativeQueryCount.setParameter(1, modelSearchExtendDto.getModelName());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getAccountNo())) {
            nativeQuery.setParameter(6, modelSearchExtendDto.getAccountNo());
            nativeQueryCount.setParameter(6, modelSearchExtendDto.getAccountNo());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getOrganCode())) {
            nativeQuery.setParameter(7, modelSearchExtendDto.getOrganCode());
            nativeQueryCount.setParameter(7, modelSearchExtendDto.getOrganCode());
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());

        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object[]> resultList1 = nativeQueryCount.getResultList();

        Long count;
        if (resultList1 == null || resultList1.size() <= 0) {
            count = 0L;
        } else {
            count = Long.parseLong(String.valueOf(resultList1.size()));
        }
        List<ModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            ModelsExtendDto e = new ModelsExtendDto();
            String orm = (obj[0] == null) ? "" : obj[0].toString();
            if (orm.indexOf(".") != -1) {
                orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
            }
            e.setOrdNum(orm);
            e.setModelId((obj[1] == null) ? "" : obj[1].toString());
            e.setRiskDate((obj[2] == null) ? "" : obj[2].toString());
            e.setName((obj[3] == null) ? "" : obj[3].toString());
            e.setRiskDesc((obj[4] == null) ? "" : obj[4].toString());
            e.setRiskType((obj[5] == null) ? "" : obj[5].toString());
            e.setRiskPoint((obj[6] == null) ? "" : obj[6].toString());
            e.setStatus((obj[7] == null) ? "" : obj[7].toString());
            e.setRiskCnt((obj[8] == null) ? "0" : obj[8].toString());
            e.setAccountNo((obj[9] == null) ? "0" : obj[9].toString());
            e.setOrganCode((obj[10] == null) ? "0" : obj[10].toString());
            list.add(e);
        }
        modelSearchExtendDto.setList(list);
        modelSearchExtendDto.setTotalPages((int) Math.ceil(count.intValue() / modelSearchExtendDto.getLimit()));
        modelSearchExtendDto.setTotalRecord(count);
        return modelSearchExtendDto;
    }

    /**
     * 获取风险数据中的account_no账号
     *
     * @param riskDetailsSearchDto
     * @return
     * @yangcq
     * @20190622
     * @wulmq
     */
    @Override
    public List<RiskDataInfoDto> getAllAccountsByRiskIdAndRIskPoint(RiskDetailsSearchDto riskDetailsSearchDto) {
        //获取查询语句
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        //String sql = OracleSQLConstant.getAllAccountsByRiskIdAndRIskPointSql + riskDetailsSearchDto.getModelId ()+"'\n" +
//                "   and t1.yd_risk_point = '"+riskDetailsSearchDto.getRiskPoint ()+"'\n";
        String sql = MysqlSQLConstant.getAllAccountsByRiskIdAndRIskPointSql + riskDetailsSearchDto.getModelId() + "'\n" +
                "   and t1.yd_risk_point = '" + riskDetailsSearchDto.getRiskPoint() + "' and t1.yd_corporate_bank = '" + RiskUtil.getOrganizationCode() + "'\n";
        Query query = em.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        List<RiskDataInfoDto> resultList = new ArrayList<>();
        for (Object[] o : list) {
            RiskDataInfoDto m = new RiskDataInfoDto();
            m.setId(Long.parseLong((o[0] == null) ? "" : o[0].toString()));
            m.setAccountNo((o[1] == null) ? "" : o[1].toString());
            m.setRiskDesc((o[2] == null) ? "" : o[2].toString());
            resultList.add(m);
        }
        return resultList;
    }

    /**
     * 拼装查询字段
     *
     * @param fieldlist
     * @return
     * @ author yangcq
     * @ date 20190623
     */
    @Override
    public RiskDetailsSearchDto createSQLField(List fieldlist) {
        RiskDetailsSearchDto dto = new RiskDetailsSearchDto();
        StringBuffer ofieldDeal = new StringBuffer();
        String str = (String) (fieldlist.get(1));
        String fields[] = str.split(",");
        for (int i = 0; i < fields.length; i++) {
            //添加别名  防止ofield中出现同名字段影响jpa原生查询
            ofieldDeal.append("T.").append(fields[i]).append(" AS" + i).append(",");//sql语句添加别名进行查询
        }
        String ofield = ofieldDeal.toString().substring(0, ofieldDeal.toString().length() - 1);
        String[] split = ofield.split(",");
        dto.setOfield(ofield + " ");
        return dto;
    }


    /**
     * 查询所有开户风险数据
     *
     * @param modelSearchExtendDto
     * @return
     * @author liuz 2019-9-9
     */
    public ModelSearchExtendDto queryRiskDateForExp(ModelSearchExtendDto modelSearchExtendDto) {
        String sql = MysqlSQLConstant.queryRiskStaticDataSql;
        sql += " AND t3.yd_corporate_bank = '" + RiskUtil.getOrganizationCode() + "'";
        sql += " AND t1.yd_corporate_bank = '" + RiskUtil.getOrganizationCode() + "'";
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            sql += " and t1.yd_model_id like ?1";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getRiskType())) {
            sql += " and t2.yd_id = ?2";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStatus())) {
            sql += " and t3.yd_status = ?3";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            sql += MysqlSQLConstant.getModelSearchExtendDtoTOCHAR1;
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            sql += MysqlSQLConstant.getModelSearchExtendDtoTOCHAR2;
        }
        sql += MysqlSQLConstant.getModelSearchExtendDtoSql;


        Query nativeQuery = em.createNativeQuery(sql);
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            nativeQuery.setParameter(1, modelSearchExtendDto.getModelName());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getRiskType())) {
            nativeQuery.setParameter(2, modelSearchExtendDto.getRiskType());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStatus())) {
            nativeQuery.setParameter(3, modelSearchExtendDto.getStatus());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            nativeQuery.setParameter(4, modelSearchExtendDto.getStartEndTime().split("~")[0].trim());
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getStartEndTime())) {
            nativeQuery.setParameter(5, modelSearchExtendDto.getStartEndTime().split("~")[1].trim());
        }
        List<Object[]> resultList = nativeQuery.getResultList();
        List<ModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            ModelsExtendDto e = new ModelsExtendDto();
            e.setOrdNum((obj[0] == null) ? "" : obj[0].toString());
            e.setModelId((obj[1] == null) ? "" : obj[1].toString());
            e.setRiskDate((obj[2] == null) ? "" : obj[2].toString());
            e.setName((obj[3] == null) ? "" : obj[3].toString());
            e.setRiskDesc((obj[4] == null) ? "" : obj[4].toString());
            e.setRiskType((obj[5] == null) ? "" : obj[5].toString());
            e.setRiskPoint((obj[6] == null) ? "" : obj[6].toString());
            e.setStatus((obj[7] == null) ? "" : obj[7].toString());
            list.add(e);
        }
        modelSearchExtendDto.setList(list);
        return modelSearchExtendDto;
    }

    /**
     * 查询所有开户模型
     *
     * @return
     * @author ywz 2019-9-20
     */
    public ModelSearchExtendDto queryRiskid() {
        ModelSearchExtendDto modelSearchExtendDto = new ModelSearchExtendDto();
        //String sql = MysqlSQLConstant.queryRiskidsql;
        String sql = OracleSQLConstant.queryRiskidsql;
        Query nativeQuery = em.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<ModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            ModelsExtendDto e = new ModelsExtendDto();
            e.setModelId((obj[0] == null) ? "" : obj[0].toString());
            list.add(e);
        }
        modelSearchExtendDto.setList(list);
        return modelSearchExtendDto;
    }

    /**
     * 查询所有交易模型
     *
     * @return
     * @author ywz 2019-10-22
     */
    public ModelSearchExtendDto queryDealRiskid() {
        ModelSearchExtendDto modelSearchExtendDto = new ModelSearchExtendDto();
//        String sql = MysqlSQLConstant.queryDealRiskidsql;
        String sql = OracleSQLConstant.queryDealRiskidsql;
        Query nativeQuery = em.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        List<ModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            ModelsExtendDto e = new ModelsExtendDto();
            e.setModelId((obj[0] == null) ? "" : obj[0].toString());
            list.add(e);
        }
        modelSearchExtendDto.setList(list);
        return modelSearchExtendDto;
    }

    /**
     * 获取交易风险中最大日期
     *
     * @return
     */
    @Override
    public RiskRecordInfoDto findAccountNearDate() {
        RiskRecordInfoDto dto = new RiskRecordInfoDto();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = null;
        String riskDate = riskApiDao.findTradeNearDate(RiskUtil.getOrganizationCode());
        if (riskDate == null) {
            Date currentTime = new Date();
            dateString = formatter.format(currentTime);
        } else {
            Calendar calendar = Calendar.getInstance();
            Date dt = null;
            try {
                dt = formatter.parse(riskDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar.setTime(dt);
            //calendar.add(calendar.DATE,1);
            dt = calendar.getTime();
            dateString = formatter.format(dt);
        }
        dto.setRiskDate(dateString);
        return dto;
    }

    @Override
    public long findByCorporateBankAndRiskIdAndCjrqIn1001(String code, ModelsExtendDto modelsExtendDto) {
        long count = 0;
        String sql = "select count(1) from yd_risk_trade_info t where  yd_risk_id='" + modelsExtendDto.getModelId() + "' ";
        if (StringUtils.isNotBlank(modelsExtendDto.getMaxDate())) {
            String maxDate = modelsExtendDto.getMaxDate();
            sql = sql + " and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
        }
        if (StringUtils.isNotBlank(modelsExtendDto.getMinDate())) {
            String minDate = modelsExtendDto.getMinDate();
            sql = sql + " and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
        }
        Query nativeQuery = em.createNativeQuery(sql);
        Object object = nativeQuery.getSingleResult();
        if (object != null && StringUtils.isNotBlank(object.toString())) {
            count = Long.parseLong(object.toString());
        }
        return count;
    }

    @Override
    public long findByCorporateBankAndRiskIdAndCjrqInAnd1002(String code, ModelsExtendDto modelsExtendDto) {
        long count = 0;
        String sql = "select count(1) from yd_risk_record_info t where  yd_risk_id='" + modelsExtendDto.getModelId() + "' ";
        if (StringUtils.isNotBlank(modelsExtendDto.getMaxDate())) {
            String maxDate = modelsExtendDto.getMaxDate();
            sql = sql + " and to_char(t.yd_risk_date,'yyyy-MM-dd') <= '" + maxDate.trim() + "'";
        }
        if (StringUtils.isNotBlank(modelsExtendDto.getMinDate())) {
            String minDate = modelsExtendDto.getMinDate();
            sql = sql + " and to_char(t.yd_risk_date,'yyyy-MM-dd') >= '" + minDate.trim() + "'";
        }
        Query nativeQuery = em.createNativeQuery(sql);
        Object object = nativeQuery.getSingleResult();
        if (object != null && StringUtils.isNotBlank(object.toString())) {
            count = Long.parseLong(object.toString());
        }
        return count;
    }

    /**
     * @Description 账号维度数据(交易)
     * @author yangwz
     * @date 2020/8/19 19:34
     */
    @Override
    public AcctModelSearchExtendDto queryRiskDataByAcctNo(AcctModelSearchExtendDto acctModelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(acctModelSearchExtendDto.getOffset() - 1, 0), acctModelSearchExtendDto.getLimit());
        String sql = OracleSQLConstant.queryTradeRiskDataByAcctNoSql;
        String countSql = OracleSQLConstant.queryTradeRiskDataCountByAcctNoSql;
        return getAcctModelSearchExtendDto(acctModelSearchExtendDto, pageable, sql, countSql);
    }

    /**
     * @Description 账号维度数据(开户)
     * @author yangwz
     * @date 2020/8/19 19:34
     */
    @Override
    public AcctModelSearchExtendDto queryRiskStaticDataByAcctNo(AcctModelSearchExtendDto acctModelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(acctModelSearchExtendDto.getOffset() - 1, 0), acctModelSearchExtendDto.getLimit());
        String sql = OracleSQLConstant.queryRiskStaticDataByAcctNoSql;
        String countSql = OracleSQLConstant.queryRiskStaticDataCountByAcctNoSql;
        return getAcctModelSearchExtendDto(acctModelSearchExtendDto, pageable, sql, countSql);
    }

    @NotNull
    private AcctModelSearchExtendDto getAcctModelSearchExtendDto(AcctModelSearchExtendDto acctModelSearchExtendDto, Pageable pageable, String sql, String countSql) {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        OrganizationDto organ = organizationService.findById(user.getOrgId());
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getOrgNo())) {
            sql += " and a.yd_bank_code = ?10";
            countSql += " and a.yd_bank_code = ?10";
        } else {
            if (organ != null) {
                if (!(IdeaConstant.ORG_ROOT_CODE.equalsIgnoreCase(organ.getCode()) || "9998".equalsIgnoreCase(organ.getCode()))) {
                    sql += " and a.yd_bank_code in (?10)";
                    countSql += " and a.yd_bank_code in (?10)";
                }
            }
        }

        //账号
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAccountNo())) {
            sql += " and a.yd_acct_no like ?1";
            countSql += " and a.yd_acct_no like ?1";
        }
        //账号名称
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAccountName())) {
            sql += " and a.yd_acct_name like ?2";
            countSql += " and a.yd_acct_name like ?2";
        }
        //采集日期
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getCjrq())) {
            sql += " and to_char(t.yd_risk_date,'yyyyMMdd') >= ?3 and to_char(t.yd_risk_date,'yyyyMMdd') <= ?4";
            countSql += " and to_char(t.yd_risk_date,'yyyyMMdd') >= ?3  and to_char(t.yd_risk_date,'yyyyMMdd') <= ?4";
        }
        //开户日期
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAcctCreateDate())) {
            sql += " and a.yd_acct_create_date >= ?5 and a.yd_acct_create_date <= ?6";
            countSql += " and a.yd_acct_create_date >= ?5  and a.yd_acct_create_date <= ?6";
        }
        //销户日期
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getCancelDate())) {
            sql += " and a.yd_cancel_date >= ?7 and a.yd_cancel_date <= ?8";
            countSql += " and a.yd_cancel_date >= ?7  and a.yd_cancel_date <= ?8";
        }
        //账号性质
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAcctType())) {
            sql += " and a.yd_acct_type like ?9";
            countSql += " and a.yd_acct_type like ?9";
        }
        //触发模型数量
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getModelNum())) {
            sql += " and table1.num = ?11";
            countSql += " and table1.num = ?11";
        }
        sql += " and table1.num is not null group by a.yd_acct_no,a.yd_acct_name,\n" +
                "                a.yd_bank_name,\n" +
                "                a.yd_acct_type,\n" +
                "                a.yd_account_status,\n" +
                "                a.yd_acct_create_date,\n" +
                "                a.yd_cancel_date,\n" +
                "                table1.num) order by yd_risk_date,num desc";
        countSql += " and table1.num is not null group by a.yd_acct_no,a.yd_acct_name,\n" +
                "                a.yd_bank_name,\n" +
                "                a.yd_acct_type,\n" +
                "                a.yd_account_status,\n" +
                "                a.yd_acct_create_date,\n" +
                "                a.yd_cancel_date,\n" +
                "                table1.num)";
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAccountNo())) {
            nativeQuery.setParameter(1, "%" + acctModelSearchExtendDto.getAccountNo().trim() + "%");
            nativeQueryCount.setParameter(1, "%" + acctModelSearchExtendDto.getAccountNo().trim() + "%");
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAccountName())) {
            nativeQuery.setParameter(2, "%" + acctModelSearchExtendDto.getAccountName().trim() + "%");
            nativeQueryCount.setParameter(2, "%" + acctModelSearchExtendDto.getAccountName().trim() + "%");
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getCjrq())) {
            nativeQuery.setParameter(3, acctModelSearchExtendDto.getCjrq().split("~")[0].trim().replace("-", ""));
            nativeQueryCount.setParameter(3, acctModelSearchExtendDto.getCjrq().split("~")[0].trim().replace("-", ""));
            nativeQuery.setParameter(4, acctModelSearchExtendDto.getCjrq().split("~")[1].trim().replace("-", ""));
            nativeQueryCount.setParameter(4, acctModelSearchExtendDto.getCjrq().split("~")[1].trim().replace("-", ""));
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAcctCreateDate())) {
            nativeQuery.setParameter(5, acctModelSearchExtendDto.getAcctCreateDate().split("~")[0].trim());
            nativeQueryCount.setParameter(5, acctModelSearchExtendDto.getAcctCreateDate().split("~")[0].trim());
            nativeQuery.setParameter(6, acctModelSearchExtendDto.getAcctCreateDate().split("~")[1].trim());
            nativeQueryCount.setParameter(6, acctModelSearchExtendDto.getAcctCreateDate().split("~")[1].trim());
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getCancelDate())) {
            nativeQuery.setParameter(7, acctModelSearchExtendDto.getCancelDate().split("~")[0].trim().replace("-", ""));
            nativeQueryCount.setParameter(7, acctModelSearchExtendDto.getCancelDate().split("~")[0].trim().replace("-", ""));
            nativeQuery.setParameter(8, acctModelSearchExtendDto.getCancelDate().split("~")[1].trim().replace("-", ""));
            nativeQueryCount.setParameter(8, acctModelSearchExtendDto.getCancelDate().split("~")[1].trim().replace("-", ""));
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getAcctType())) {
            nativeQuery.setParameter(9, acctModelSearchExtendDto.getAcctType());
            nativeQueryCount.setParameter(9, acctModelSearchExtendDto.getAcctType());
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getOrgNo())) {
            nativeQuery.setParameter(10, acctModelSearchExtendDto.getOrgNo());
            nativeQueryCount.setParameter(10, acctModelSearchExtendDto.getOrgNo());
        } else {
            if (organ != null) {
                if (!(IdeaConstant.ORG_ROOT_CODE.equalsIgnoreCase(organ.getCode()) || "9998".equalsIgnoreCase(organ.getCode()))) {
                    List<OrganizationDto> orgList = organizationService.findAllByFullId(organ.getFullId());
                    List<String> que = new ArrayList<>();
//                String ii = "";
                    for (OrganizationDto organizationDto : orgList) {
//                    if (ii != "") {
//                        ii += ",";
//                    }
//                    ii += organizationDto.getCode();
                        que.add(organizationDto.getPbcCode());
                    }
                    nativeQuery.setParameter(10, que);
                    nativeQueryCount.setParameter(10, que);
                }
            }
        }
        if (StringUtils.isNotBlank(acctModelSearchExtendDto.getModelNum())) {
            nativeQuery.setParameter(11, acctModelSearchExtendDto.getModelNum());
            nativeQueryCount.setParameter(11, acctModelSearchExtendDto.getModelNum());
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object> resultList1 = nativeQueryCount.getResultList();
        Long count;
        if (resultList1 == null || resultList1.size() <= 0) {
            count = 0L;
        } else {
            count = Long.parseLong(String.valueOf(resultList1.size()));
        }
        List<AcctModelsExtendDto> list = new ArrayList();
        for (Object[] obj : resultList) {
            AcctModelsExtendDto e = new AcctModelsExtendDto();
            String orm = (obj[0] == null) ? "" : obj[0].toString();
            if (orm.indexOf(".") != -1) {
                orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
            }
            e.setOrdNum(orm);
            e.setAcctNo((obj[1] == null) ? "" : obj[1].toString());
            e.setAcctName((obj[2] == null) ? "" : obj[2].toString());
            e.setBankName((obj[3] == null) ? "" : obj[3].toString());
            e.setAcctType(dictionaryService.getNameByValue("账户性质(加未知)", (obj[4] == null) ? "" : obj[4].toString()));
            e.setAccountStatus(dictionaryService.getNameByValue("账户状态", (obj[5] == null) ? "" : obj[5].toString()));
            e.setAcctCreateDate((obj[6] == null) ? "" : obj[6].toString());
            e.setCancelDate((obj[7] == null) ? "" : obj[7].toString());
            e.setModelNum((obj[8] == null) ? "" : obj[8].toString());
            e.setCjrq((obj[9] == null) ? "" : obj[9].toString());
            list.add(e);
        }
        acctModelSearchExtendDto.setList(list);
        acctModelSearchExtendDto.setTotalPages((int) Math.ceil(count.intValue() / acctModelSearchExtendDto.getLimit()));
        acctModelSearchExtendDto.setTotalRecord(count);
        return acctModelSearchExtendDto;
    }

    @Override
    public ModelSearchExtendDto queryRiskStaticData1(ModelSearchExtendDto modelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchExtendDto.getOffset() - 1, 0), modelSearchExtendDto.getLimit());
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        String sql = OracleSQLConstant.queryRiskStaticDataSqlCQYUJING;
        String countSql = OracleSQLConstant.queryRiskStaticCheckDataCountSqlYUJING;
        return getModelSearchExtendDto1(modelSearchExtendDto, pageable, sql, countSql);
    }

    @Override
    public ModelSearchExtendDto queryRiskDzData1(ModelSearchExtendDto modelSearchExtendDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchExtendDto.getOffset() - 1, 0), modelSearchExtendDto.getLimit());
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        String sql = OracleSQLConstant.queryRiskDzDataSqlCQForYUJING;
        String countSql = OracleSQLConstant.queryRiskStaticDataCountSqlYUJING;
        return getModelSearchExtendDtoduizhang1(modelSearchExtendDto, pageable, sql, countSql);
    }

    @NotNull
    private ModelSearchExtendDto getModelSearchExtendDto1(ModelSearchExtendDto modelSearchExtendDto, Pageable pageable, String sql, String countSql) {
        log.info("开户监测---");
        modelSearchExtendDto.setAccountNo("");
        modelSearchExtendDto.setModelName(" ");
        sql += OracleSQLConstant.getModelSearchExtendDtoSqlYUJING;
        countSql += OracleSQLConstant.getModelSearchExtendDtoCountSqlYUJING;
        Query nativeQuery = em.createNativeQuery(sql);
        log.info("执行sql" + sql);
        log.info("统计" + countSql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        nativeQuery.setFirstResult(pageable.getOffset());
        if (true) {
           // nativeQuery.setMaxResults(1000000);
            List<Object[]> resultList = nativeQuery.getResultList();
            List<ModelsExtendDto> list = new ArrayList();
            for (Object[] obj : resultList) {
                ModelsExtendDto e = new ModelsExtendDto();
                String orm = (obj[0] == null) ? "" : obj[0].toString();
                if (orm.indexOf(".") != -1) {
                    orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
                }
                e.setOrdNum(orm);
                e.setModelId((obj[1] == null) ? "" : obj[1].toString());
                e.setRiskDate((obj[2] == null) ? "" : obj[2].toString());
                e.setName((obj[3] == null) ? "" : obj[3].toString());
                e.setRiskDesc((obj[4] == null) ? "" : obj[4].toString());
                e.setRiskType((obj[5] == null) ? "" : obj[5].toString());
                e.setRiskPoint((obj[6] == null) ? "" : obj[6].toString());
                e.setStatus((obj[7] == null) ? "" : obj[7].toString());
                e.setRiskCnt((obj[8] == null) ? "0" : obj[8].toString());
                e.setAccountNo((obj[9] == null) ? "0" : obj[9].toString());
                e.setAcctName((obj[10] == null) ? "0" : obj[10].toString());
                e.setOrganCode((obj[11] == null) ? "0" : obj[11].toString());
                e.setOperatorTelephone((obj[12] == null) ? "" : obj[12].toString());
                e.setOperatorIdcardNo((obj[13] == null) ? "" : obj[13].toString());
                e.setOperatorName((obj[14] == null) ? "" : obj[14].toString());
                e.setFinanceIdcardNo((obj[15] == null) ? "" : obj[15].toString());
                e.setFinanceName((obj[16] == null) ? "" : obj[16].toString());
                e.setFinanceTelephone((obj[17] == null) ? "" : obj[17].toString());
                e.setLegalTelephone((obj[18] == null) ? "" : obj[18].toString());
                e.setLegalIdcardNo((obj[19] == null) ? "" : obj[19].toString());
                e.setLegalName((obj[20] == null) ? "" : obj[20].toString());
                e.setDmpOrganCode((obj[21] == null) ? "" : obj[21].toString());
                e.setRegAdress((obj[22] == null) ? "" : obj[22].toString());
                e.setAcctType((obj[23] == null) ? "" : obj[23].toString());
                e.setCustomerId((obj[24] == null) ? "" : obj[24].toString());
                e.setWorkAdress((obj[25] == null) ? "" : obj[25].toString());
                list.add(e);

            }
            heGuiYuJingAllDao.deleteAllByYuJingType("5");
            log.info("保存开户监测数据开始---");
            if (list != null && list.size() > 0) {
                for (ModelsExtendDto dtoo : list) {
                    HeGuiYuJingAll heGuiYuJingAll = new HeGuiYuJingAll();
                    if (StringUtils.isNotBlank(dtoo.getAccountNo())) {
                        heGuiYuJingAll.setAccountNo(dtoo.getAccountNo());
                    }
                    if (StringUtils.isNotBlank(dtoo.getAcctName())) {
                        heGuiYuJingAll.setAcctName(dtoo.getAcctName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getRiskType())) {
                        heGuiYuJingAll.setRiskType(dtoo.getRiskType());
                    }
                    if (StringUtils.isNotBlank(dtoo.getRiskDate())) {
                        heGuiYuJingAll.setRiskDate(dtoo.getRiskDate());
                    }
                    if (StringUtils.isNotBlank(dtoo.getName())) {
                        heGuiYuJingAll.setName(dtoo.getName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getRiskPoint())) {
                        heGuiYuJingAll.setRiskPoint(dtoo.getRiskPoint());
                    }
                    if (StringUtils.isNotBlank(dtoo.getOrganCode())) {
                        heGuiYuJingAll.setOrganCode(dtoo.getOrganCode());
                    }
                    if (StringUtils.isNotBlank(dtoo.getLegalIdcardNo())) {
                        heGuiYuJingAll.setLegalIdcardNo(dtoo.getLegalIdcardNo());
                    }
                    if (StringUtils.isNotBlank(dtoo.getLegalName())) {
                        heGuiYuJingAll.setLegalName(dtoo.getLegalName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getLegalTelephone())) {
                        heGuiYuJingAll.setLegalTelephone(dtoo.getLegalTelephone());
                    }
                    if (StringUtils.isNotBlank(dtoo.getOperatorIdcardNo())) {
                        heGuiYuJingAll.setOperatorIdcardNo(dtoo.getOperatorIdcardNo());
                    }
                    if (StringUtils.isNotBlank(dtoo.getOperatorName())) {
                        heGuiYuJingAll.setOperatorName(dtoo.getOperatorName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getOperatorTelephone())) {
                        heGuiYuJingAll.setOperatorTelephone(dtoo.getOperatorTelephone());
                    }

                    if (StringUtils.isNotBlank(dtoo.getFinanceIdcardNo())) {
                        heGuiYuJingAll.setFinanceTelephone(dtoo.getFinanceIdcardNo());
                    }
                    if (StringUtils.isNotBlank(dtoo.getFinanceName())) {
                        heGuiYuJingAll.setFinanceName(dtoo.getFinanceName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getFinanceTelephone())) {
                        heGuiYuJingAll.setFinanceTelephone(dtoo.getFinanceTelephone());
                    }
                    if(StringUtils.isNotBlank(dtoo.getDmpOrganCode())){
                        heGuiYuJingAll.setDmpOrganCode(dtoo.getDmpOrganCode());
                    }

                    if(StringUtils.isNotBlank(dtoo.getRiskDesc())){
                        heGuiYuJingAll.setRiskDesc(dtoo.getRiskDesc());
                    }

                    if(StringUtils.isNotBlank(dtoo.getRegAdress())){
                        heGuiYuJingAll.setRegAdress(dtoo.getRegAdress());
                    }
                    if(StringUtils.isNotBlank(dtoo.getAcctType())){
                        heGuiYuJingAll.setAcctType(dtoo.getAcctType());
                    }

                    if(StringUtils.isNotBlank(dtoo.getCustomerId())){
                        heGuiYuJingAll.setCustomerId(dtoo.getCustomerId());
                    }
                    if(StringUtils.isNotBlank(dtoo.getWorkAdress())){
                        heGuiYuJingAll.setWorkAdress(dtoo.getWorkAdress());
                    }


                    heGuiYuJingAll.setYuJingType("5");
                    heGuiYuJingAllDao.save(heGuiYuJingAll);
                }
            }
            log.info("保存开户监测数据结束-----");
        }

        return modelSearchExtendDto;

    }


    @NotNull
    private ModelSearchExtendDto getModelSearchExtendDtoduizhang1(ModelSearchExtendDto modelSearchExtendDto, Pageable pageable, String sql, String countSql) {
        modelSearchExtendDto.setAccountNo("");
        modelSearchExtendDto.setModelName(" ");
        sql += OracleSQLConstant.getModelSearchExtendDtoSqlqudiaokouhao11;
        countSql += OracleSQLConstant.getModelSearchExtendDtoCountSqlqudiaokouhao11;
        Query nativeQuery = em.createNativeQuery(sql);
        Query nativeQueryCount = em.createNativeQuery(countSql);
        nativeQuery.setFirstResult(pageable.getOffset());
        if (true) {
            //nativeQuery.setMaxResults(10000);
            List<Object[]> resultList = nativeQuery.getResultList();
            List<ModelsExtendDto> list = new ArrayList();
            for (Object[] obj : resultList) {
                ModelsExtendDto e = new ModelsExtendDto();
                String orm = (obj[0] == null) ? "" : obj[0].toString();
                if (orm.indexOf(".") != -1) {
                    orm = (orm == "") ? "" : orm.substring(0, orm.indexOf("."));
                }
                e.setOrdNum(orm);
                e.setModelId((obj[1] == null) ? "" : obj[1].toString());
                e.setRiskDate((obj[2] == null) ? "" : obj[2].toString());
                e.setName((obj[3] == null) ? "" : obj[3].toString());
                e.setRiskDesc((obj[4] == null) ? "" : obj[4].toString());
                e.setRiskType((obj[5] == null) ? "" : obj[5].toString());
                e.setRiskPoint((obj[6] == null) ? "" : obj[6].toString());
                e.setStatus((obj[7] == null) ? "" : obj[7].toString());
                e.setRiskCnt((obj[8] == null) ? "0" : obj[8].toString());
                e.setAccountNo((obj[9] == null) ? "" : obj[9].toString());
                e.setOrganCode((obj[10] == null) ? "" : obj[10].toString());
                //20201112修改
                e.setDmpOrganCode((obj[11] == null) ? "" : obj[11].toString());
                list.add(e);
            }
            heGuiYuJingAllDao.deleteAllByYuJingType("4");
            if (list != null && list.size() > 0) {
                for (ModelsExtendDto dtoo : list) {
                    HeGuiYuJingAll heGuiYuJingAll = new HeGuiYuJingAll();
                    if (StringUtils.isNotBlank(dtoo.getAccountNo())) {
                        heGuiYuJingAll.setAccountNo(dtoo.getAccountNo());
                    }
                    if (StringUtils.isNotBlank(dtoo.getAcctName())) {
                        heGuiYuJingAll.setAcctName(dtoo.getAcctName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getRiskType())) {
                        heGuiYuJingAll.setRiskType(dtoo.getRiskType());
                    }
                    if (StringUtils.isNotBlank(dtoo.getRiskDate())) {
                        heGuiYuJingAll.setRiskDate(dtoo.getRiskDate());
                    }
                    if (StringUtils.isNotBlank(dtoo.getName())) {
                        heGuiYuJingAll.setName(dtoo.getName());
                    }
                    if (StringUtils.isNotBlank(dtoo.getRiskPoint())) {
                        heGuiYuJingAll.setRiskPoint(dtoo.getRiskPoint());
                    }
                    if (StringUtils.isNotBlank(dtoo.getOrganCode())) {
                        heGuiYuJingAll.setOrganCode(dtoo.getOrganCode());
                    }
                    if(StringUtils.isNotBlank(dtoo.getDmpOrganCode())){
                        heGuiYuJingAll.setDmpOrganCode(dtoo.getDmpOrganCode());
                    }
                    heGuiYuJingAll.setYuJingType("4");
                    heGuiYuJingAllDao.save(heGuiYuJingAll);
                }
            }
        }
        log.info("保存全量预警数据对账预警-----");
        return modelSearchExtendDto;
    }

}