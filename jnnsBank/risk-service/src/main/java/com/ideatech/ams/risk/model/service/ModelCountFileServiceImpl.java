package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.model.dao.ModelCountFileDao;
import com.ideatech.ams.risk.model.dto.ModelSearchExtendDto;
import com.ideatech.ams.risk.model.dto.ModelsExtendDto;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.entity.OrganizationPo;
import com.ideatech.ams.system.user.dto.UserDto;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ModelCountFileServiceImpl implements ModelCountFileService {
    @PersistenceContext
    private EntityManager em; //注入EntityManager
    @Autowired
    private ModelCountFileDao modelCountFileDao;
    @Autowired
    private OrganizationDao organizationDao;


    public ModelSearchExtendDto queryModelCountFile2(final ModelSearchExtendDto modelSearchExtendDto){
        Pageable pageable = new PageRequest(Math.max(modelSearchExtendDto.getOffset() - 1, 0), modelSearchExtendDto.getLimit());

//        String sql = MysqlSQLConstant.queryModelCountFile2Sql;
        String sql = OracleSQLConstant.queryModelCountFile2Sql;

//        String countSql = MysqlSQLConstant.queryModelCountFile2CountSql;
        String countSql = OracleSQLConstant.queryModelCountFile2CountSql;


        if (StringUtils.isNotBlank(modelSearchExtendDto.getOrgName())){
            sql+=" and O.yd_name like ?1";
            countSql+=" and O.yd_name like ?1";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())){
            sql+=" and A.yd_name like ?2";
            countSql+=" and A.yd_name like ?2";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getRiskType())){
            sql+=" and o12.yd_type_name like ?3";
            countSql+=" and o12.yd_type_name like ?3";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getDataDate())){
            sql+=" and T.YD_CJRQ >=?4";
            countSql+=" and T.YD_CJRQ >= ?4";
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getEndDate())){
            sql+=" and T.YD_CJRQ <= ?5";
            countSql+=" and T.YD_CJRQ <= ?5";
        }
        sql+=" and a.yd_deleted= "+ "0" +" and o12.yd_deleted= "+"0";
        //sql+= OracleSQLConstant.queryModelCountFile2EndSql1;
        //sql+= MysqlSQLConstant.queryModelCountFile2EndSql1;

        //sql+=" GROUP BY a.yd_id, T.yd_RISK_ID,A.yd_NAME,o12.yd_type_name order by T.yd_RISK_ID ";
        sql+= OracleSQLConstant.queryModelCountFile2EndSql2;
        //sql+= MysqlSQLConstant.queryModelCountFile2EndSql2;

        //countSql+=" GROUP BY a.yd_id, T.yd_RISK_ID,A.yd_NAME,o12.yd_type_name order by T.yd_RISK_ID ";
        countSql+= OracleSQLConstant.queryModelCountFile2CountSql1;
        //countSql+= MysqlSQLConstant.queryModelCountFile2CountSql1;

        Query query = em.createNativeQuery(sql);
        Query queryCount= em.createNativeQuery(countSql);

        if (StringUtils.isNotBlank(modelSearchExtendDto.getOrgName())) {
            query.setParameter(1, "%" + modelSearchExtendDto.getOrgName() + "%");
            queryCount.setParameter(1, "%" + modelSearchExtendDto.getOrgName() + "%");
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getModelName())) {
            query.setParameter(2, "%" + modelSearchExtendDto.getModelName() + "%");
            queryCount.setParameter(2, "%" + modelSearchExtendDto.getModelName() + "%");
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getRiskType())) {
            query.setParameter(3, "%" + modelSearchExtendDto.getRiskType() + "%");
            queryCount.setParameter(3, "%" + modelSearchExtendDto.getRiskType() + "%");
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getDataDate())) {
            query.setParameter(4,modelSearchExtendDto.getDataDate()+"" );
            queryCount.setParameter(4,modelSearchExtendDto.getDataDate()+"");
        }
        if (StringUtils.isNotBlank(modelSearchExtendDto.getEndDate())) {
            query.setParameter(5,modelSearchExtendDto.getEndDate()+"" );
            queryCount.setParameter(5,modelSearchExtendDto.getEndDate()+"");
        }
        List resultList1 = query.getResultList();

        List<Object[]> resultList = query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        Long count;
        if (resultList.size()==0){
            count = 0L;
        }else {
            count =(long)resultList1.size();
        }

        List<ModelsExtendDto> list = new ArrayList<>();

        for(Object[] o:resultList){
            //modelId, modelsId, name,  tjjg, riskType
            ModelsExtendDto m = new ModelsExtendDto(
                    (o[0]==null)?"":o[0].toString(),
                    (o[1]==null)?"":o[1].toString(),
                    (o[2]==null)?"":o[2].toString(),
                    (o[3]==null)?"":o[3].toString(),
                    (o[4]==null)?"":o[4].toString());
            list.add(m);
        }

        modelSearchExtendDto.setList(list);
        modelSearchExtendDto.setTotalPages((int) Math.ceil(count.intValue()/modelSearchExtendDto.getLimit()));
        modelSearchExtendDto.setTotalRecord(count);
        return modelSearchExtendDto;
    }

    @Override
    public List<HashMap<String,String>>  getAllModelTypeList(){
//        String sql = "    SELECT DISTINCT M.yd_MODEL_ID, T.yd_type_NAME\n" +
//                "      FROM yd_risk_models M\n" +
//                "      LEFT JOIN yd_risk_model_type T\n" +
//                "        ON M.yd_TYPE_ID = T.yd_ID\n" +
//                "     WHERE M.yd_deleted = '0'\n" +
//                "       AND T.yd_deleted = '0'";
        String sql = OracleSQLConstant.getAllModelTypeListSql;
        //String sql = MysqlSQLConstant.getAllModelTypeListSql;

        Query query = em.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        for (Object[] o:resultList){
            HashMap<String,String> resMap = new HashMap<String,String>();
            resMap.put(
                    (o[0]==null)?"":o[0].toString(),
                    (o[1]==null)?"":o[1].toString());
            result.add(resMap);
        }
        return result;
    }

    @Override
    public List<ModelsExtendDto> findModelExtendList(ModelsExtendDto modelsExtendDto) {
        //String sql = "select * from yd_risk_models where 1=1 ";
        String sql = OracleSQLConstant.findModelExtendListSql;
        //String sql = MysqlSQLConstant.findModelExtendListSql;
        //sql +=" and t.yd_corporate_bank='"+ RiskUtil.getOrganizationCode()+"'";
        if (StringUtils.isNotBlank(modelsExtendDto.getTableName())){
            //sql+=" AND yd_table_name like ?1 ";
            sql+= OracleSQLConstant.findModelExtendListEndSql;
            //sql+= MysqlSQLConstant.findModelExtendListEndSql;
        }
        Query query = em.createNativeQuery(sql);
        if (StringUtils.isNotBlank(modelsExtendDto.getTableName())) {
            query.setParameter(1, "%" + modelsExtendDto.getTableName() + "%");
        }
        List<Object[]> resultList = query.getResultList();
        List<ModelsExtendDto> list = new ArrayList<>();
        for (Object[] o:resultList){
            ModelsExtendDto m = new ModelsExtendDto();
            m.setName((o[13]==null)?"":o[13].toString());
            list.add(m);
        }
        return list;
    }

    @Override
    public List<ModelsExtendDto> findModelExtendList(ModelsExtendDto modelsExtendDto, String code) {
        //String sql = "select * from yd_risk_models where 1=1 ";
        String sql = OracleSQLConstant.findModelExtendListSql;
       // String sql = MysqlSQLConstant.findModelExtendListSql;
       // sql +=" and yd_corporate_bank='"+ code +"'";
        if (StringUtils.isNotBlank(modelsExtendDto.getTableName())){
            //sql+=" AND yd_table_name like ?1 ";
            sql+= OracleSQLConstant.findModelExtendListEndSql;
            //sql+= MysqlSQLConstant.findModelExtendListEndSql;
        }
        Query query = em.createNativeQuery(sql);
        if (StringUtils.isNotBlank(modelsExtendDto.getTableName())) {
            query.setParameter(1, "%" + modelsExtendDto.getTableName() + "%");
        }
        List<Object[]> resultList = query.getResultList();
        List<ModelsExtendDto> list = new ArrayList<>();
        for (Object[] o:resultList){
            ModelsExtendDto m = new ModelsExtendDto();
            m.setName((o[17]==null)?"":o[17].toString());
            list.add(m);
        }
        return list;
    }
    public List<OrganizationDto> getOrgByRiskList(OrganizationDto organizationDto){
//        String sql = "SELECT distinct\n" +
//                "      o.yd_code,o.yd_name\n" +
//                "    FROM yd_sys_organization o RIGHT JOIN yd_risk_tjjg r on  o.yd_code=r.yd_org_id \n" +
//                "\t\tWHERE  o.yd_deleted = '0' order by yd_code ";
        String sql = OracleSQLConstant.getOrgByRiskListSql;
        //String sql = MysqlSQLConstant.getOrgByRiskListSql;
        Query query = em.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<OrganizationDto> list = new ArrayList<>();
        for (Object[] o:resultList){
            OrganizationDto organizationDto1 = new OrganizationDto();
            organizationDto1.setCode((o[0]==null)?"":o[0].toString());
            organizationDto1.setName((o[1]==null)?"":o[1].toString());
            list.add(organizationDto1);
        }
        return list;
    }
    @Override
    public List<OrganizationDto> findListByRisk(OrganizationDto organizationDto){
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        UserDto userDto = new UserDto();
        OrganizationDto o = new OrganizationDto();
        o.setId(organizationDto.getId());
        //通过id获取机构
        OrganizationPo byId = organizationDao.findById(organizationDto.getId());
        List<OrganizationDto> orgList = new ArrayList<OrganizationDto>();
        //获取机构集合
        if (organizationDto != null){
            orgList = this.getOrgByRiskList(organizationDto);
        }
        return orgList;
    }
}
