package com.ideatech.ams.risk.riskdata.service;

import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.entity.ModelCount;
import com.ideatech.ams.risk.riskdata.dao.ModelCountDao;
import com.ideatech.ams.risk.riskdata.dto.ModelCountDto;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.common.converter.ConverterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ModelCountServiceImpl implements ModelCountService {

    @Autowired
    ModelCountDao modelCountDao;
    @Autowired
    EntityManager em ;
    @Autowired
    ModelCountService modelCountService;

    @Override
    public List<ModelDto> findModelCountByCjrq(String cjrq, String orgId, String khId) {
        //List<ModelCount> list = modelCountDao.findAllByCjrqAndOrgIdAndKhId(cjrq, orgId, khId);
//       String sql = " select distinct m.yd_name ,m.yd_model_id  " +
//               "from yd_risk_models m where m.yd_model_id in " +
//               "(select distinct t.yd_risk_table from YD_RISK_MODEL_COUNT t  \n" +
//               " where 1=1 ";
         String sql = OracleSQLConstant.findModelCountByCjrqSql;
        //String sql = MysqlSQLConstant.findModelCountByCjrqSql;

        if (StringUtils.isNotBlank(cjrq)){
            sql+=" and t.yd_cjrq= ?1 ";
        }
        if (StringUtils.isNotBlank(orgId)){
            sql+=" and t.yd_org_id= ?2 ";
        }
        if (StringUtils.isNotBlank(khId)){
            sql+=" and t.yd_kh_id= ?3 ";
        }

        sql+=" ) and m.yd_deleted= "+" 0 ";
        Query query = em.createNativeQuery(sql);

        if(StringUtils.isNotBlank(cjrq)){
            query.setParameter(1,cjrq+"");
        }
        if(StringUtils.isNotBlank(orgId)){
            query.setParameter(2,orgId+"");
        }
        if(StringUtils.isNotBlank(khId)){
            query.setParameter(3,khId+"");
        }
        List<Object[]> list = query.getResultList();
        List<ModelDto> resultList = new ArrayList<>();

        for(Object[] o:list){
            ModelDto m = new ModelDto();
            m.setName((o[0]==null)?"":o[0].toString());
            m.setModelId((o[1]==null)?"":o[1].toString());
            resultList.add(m);
        }
        return resultList;
    }

    @Override
    public ModelCountDto findModelCountAndModelsId(Long id) {
        String sql = " select m.YD_ID as \"mId\",c.YD_RISK_AMT,t.yd_id as \"tId\",l.YD_ID,mr.YD_ID as \"mrId\" "  +
                " from YD_RISK_MODEL_COUNT c left join YD_RISK_MODELS m on  m.YD_MODEL_ID=  c.YD_MODEL_ID" +
                "   left join YD_RISK_MODEL_TYPE t on m.yd_type_id = t.yd_id \n" +
                "\t    left join YD_RISK_MODEL_LEVEL l on m.yd_level_id = l.yd_id\n" +
                "\t    left join YD_RISK_MODEL_RULE mr on m.yd_rule_id = mr.yd_id"+
                " where m.YD_DELETED='0' and c.YD_DELETED='0' ";

        if (id!=null){
            sql+=" and c.yd_id=?1 ";
        }
        Query query = em.createNativeQuery(sql);
        if (id!=null){
            query.setParameter(1,id);
        }
        List<Object[]> resultList = query.getResultList();
        ModelCountDto modelCountDto = new ModelCountDto();
        modelCountDto.setModelId(resultList.get(0)[0].toString());
        modelCountDto.setRiskAmt(resultList.get(0)[1].toString());
        modelCountDto.setTypeId(resultList.get(0)[2].toString());
        modelCountDto.setLevelId(resultList.get(0)[3].toString());
        modelCountDto.setRuleId(resultList.get(0)[4].toString());
        return modelCountDto;
    }

    @Override
    public ModelCountDto findById(Long id) {
        ModelCountDto modelCountDto = new ModelCountDto();
        ModelCount modelCount = modelCountDao.findOne(id);
        ConverterService.convert(modelCount,modelCountDto);
        return modelCountDto;
    }

    @Override
    public void upDate(ModelCountDto modelCountDto) {
        ModelCount modelCount = new ModelCount ();
        ConverterService.convert(modelCount,modelCountDto);
        modelCountDao.save(modelCount);
    }


    /**
     * 获取交易风险中最大日期
     * @return
     */
    @Override
    public ModelCountDto findTradeNearDate() {
        ModelCountDto modelCountDto = new ModelCountDto();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = null;
        String  cjrq = modelCountDao.findNearDate(RiskUtil.getOrganizationCode());
        if(cjrq == null){
            Date currentTime = new Date();
            dateString = fmt.format(currentTime);
        }else{
            Calendar calendar =Calendar.getInstance();
            Date da = null;
            try {
                da = formatter.parse(cjrq);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.setTime(da);
            calendar.add(calendar.DATE,1);
            da =calendar.getTime();
            dateString = fmt.format(da);
        }
        modelCountDto.setCjrq(dateString);
        return modelCountDto;
    }
}
