package com.ideatech.ams.risk.account.service;

import com.ideatech.ams.risk.account.dto.AccountTransactionRiskSearchDto;
import com.ideatech.ams.risk.model.dto.ModelAndCountDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yinjie
 * @Date: 2019/5/9 17:21
 */
@Service
public class AccountRiskServiceImpl implements AccountRiskService {

    @Autowired
    EntityManager entityManager;

    /**
     * @author:yinjie
     * @date:2019/5/11
     * @time:11:14
     * @description:
     * 账户风险监测---账户交易风险监测
     * 可携带搜索条件的表格数据查询
     */
    @Override
    public AccountTransactionRiskSearchDto queryAccountRisk(AccountTransactionRiskSearchDto accountTransactionRiskSearchDto) {
        Pageable pageable = new PageRequest(Math.max(accountTransactionRiskSearchDto.getOffset() - 1, 0), accountTransactionRiskSearchDto.getLimit());
        String sql = " SELECT a.yd_cjrq        AS \"cjrq\",\n" +
                "               a.yd_id          AS \"id\",\n" +
                "               a.yd_risk_id     AS \"riskId\",\n" +
                "               a.yd_risk_table  AS \"riskTable\",\n" +
                "               a.yd_model_id    AS \"modelId\",\n" +
                "               a.yd_org_id      AS \"orgId\",\n" +
                "               a.yd_kh_id       AS \"khId\",\n" +
                "               a.yd_cn          AS \"cn\",   \n" +
                "               a.yd_kh_name     AS \"khName\",\n" +
                "               a.yd_risk_amt    AS \"riskAmt\",\n" +
                "               o.yd_id          as \"office.id\",\n" +
                "               o.yd_name        as \"office.name\",\n" +
                "               ms.yd_name       as \"modelName\",\n" +
                "               ms.yd_sign_flag  as \"model.signFlag\",\n" +
                "               pro.name_     as \"flowName\"\n" +
                "          FROM yd_risk_model_count a\n" +
                "          LEFT JOIN yd_sys_organization o\n" +
                "            ON a.YD_ORG_ID = o.yd_code\n" +
                "          LEFT JOIN yd_risk_models ms\n" +
                "            on a.yd_model_id = ms.yd_model_id\n" +
                "          LEFT JOIN yd_model_flow_configer MFC\n" +
                "            ON MFC.yd_MODEL_ID = MS.yd_ID\n" +
                "          LEFT JOIN (select id_ as key_, max(version_) value, name_\n" +
                "                      from ACT_RE_PROCDEF\n" +
                "                     group by id_, name_) PRO\n" +
                "            ON MFC.yd_FLOW_KEY = PRO.KEY_\n" +
                "         WHERE 1 = 1\n" +
                "           AND a.yd_deleted = '0'\n" +
                "           AND ms.yd_deleted = '0'\n" +
                "           AND o.yd_deleted = '0'\n" +
                "           and MFC.yd_deleted = '0'\n ";

        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getOfficeName())){
            sql+=" and o.yd_name like ?1";
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getKhName())){
            sql+=" and a.yd_kh_name like ?2";
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getModelName())){
            sql+=" and ms.yd_name like ?3";
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getDataDate())){
            sql+=" and a.yd_cjrq >=?4";
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getEndDate())){
            sql+=" and a.yd_cjrq <= ?5";
        }
        sql+=" order by a.yd_cjrq desc, o.yd_id, a.yd_risk_id, a.yd_kh_id, a.yd_kh_name ";

        Query query = entityManager.createNativeQuery(sql);

        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getOfficeName())) {
            query.setParameter(1, "%" + accountTransactionRiskSearchDto.getOfficeName() + "%");
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getKhName())) {
            query.setParameter(2, "%" + accountTransactionRiskSearchDto.getKhName() + "%");
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getModelName())) {
            query.setParameter(3, "%" + accountTransactionRiskSearchDto.getModelName() + "%");
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getDataDate())) {
            query.setParameter(4,accountTransactionRiskSearchDto.getDataDate()+"" );
        }
        if (StringUtils.isNotBlank(accountTransactionRiskSearchDto.getEndDate())) {
            query.setParameter(5,accountTransactionRiskSearchDto.getEndDate()+"" );
        }

        List resultList1 = query.getResultList();
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> resultList = query.getResultList();

        Long count;
        if (resultList1.size()==0){
            count = 0L;
        }else {
            count =(long)resultList1.size();
        }

        List<ModelAndCountDto> list = new ArrayList<>();

        for(Object[] o:resultList){
            ModelAndCountDto m = new ModelAndCountDto(
                    //补充判空
                    (o[0]==null)?"":o[0].toString(),
                    (o[1]==null)?"":o[1].toString(),
                    (o[2]==null)?"":o[2].toString(),
                    (o[3]==null)?"":o[3].toString(),
                    (o[4]==null)?"":o[4].toString(),
                    (o[5]==null)?"":o[5].toString(),
                    (o[6]==null)?"":o[6].toString(),
                    (o[7]==null)?"":o[7].toString(),
                    (o[8]==null)?"":o[8].toString(),
                    (o[9]==null)?"":o[9].toString(),
                    (o[10]==null)?"":o[10].toString(),
                    (o[11]==null)?"":o[11].toString(),
                    (o[12]==null)?"":o[12].toString(),
                    "",
                    ""
                  );

            list.add(m);
        }

        accountTransactionRiskSearchDto.setList(list);
        accountTransactionRiskSearchDto.setTotalPages((int) Math.ceil(count.intValue()/accountTransactionRiskSearchDto.getLimit()));
        accountTransactionRiskSearchDto.setTotalRecord(count);
        return accountTransactionRiskSearchDto;
    }

}
