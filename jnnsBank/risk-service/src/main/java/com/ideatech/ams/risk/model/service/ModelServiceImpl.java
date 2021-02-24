package com.ideatech.ams.risk.model.service;


import com.ideatech.ams.risk.Constant.MysqlSQLConstant;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.model.dao.ModelDao;
import com.ideatech.ams.risk.model.dto.ModeAndKindlDto;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.dto.ModelSearchDto;
import com.ideatech.ams.risk.model.entity.Model;
import com.ideatech.ams.risk.modelKind.dao.RiskLevelDao;
import com.ideatech.ams.risk.modelKind.dao.RiskTypeDao;
import com.ideatech.ams.risk.rule.service.RuleConfigService;
import com.ideatech.ams.risk.util.RiskUtil;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Service
public class ModelServiceImpl implements ModelService {

    /**
     * 总行机构码
     */
    @Value("${ams.szsm.ods.orgId:SZSM}")
    private String _orgId;
    /**
     * 总行机构名称
     */
    @Value("${ams.szsm.ods.orgName:总行}")
    private String _orgName;
    @Autowired
    private ModelDao modelDao;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrganizationDao organizationDao;



    @Autowired
    RiskTypeDao riskTypeDao;

    @Autowired
    RiskLevelDao riskLevelDao;

    @Autowired
    RuleConfigService ruleConfigService;

    @Override
    public void saveModel(ModelDto modelDto) {
        Model model = new Model();
        if (null != modelDto.getId()) {
            model = modelDao.findOne(modelDto.getId());
            if (model == null) {
                model = new Model();
            }
        }
        ConverterService.convert(modelDto, model);
        // model.setCorporateBank(RiskUtil.getOrganizationCode());
        modelDao.save(model);
    }


    @Override
    public ModelDto findById(Long id) {
        Model one = modelDao.findOne(id);
        ModelDto modelDto = new ModelDto();
        if (one != null) {
            BeanUtils.copyProperties(one, modelDto);
        }
        return modelDto;
    }

    @Override
    public void enable(Long id) {
        Model one = modelDao.findOne(id);
        one.setStatus("1");
        modelDao.save(one);
    }

    @Override
    public void disabld(Long id) {
        Model one = modelDao.findOne(id);
        one.setStatus("0");
        modelDao.save(one);
    }

    @Override
    public void deleteById(Long id) {
        modelDao.delete(id);
    }

    public ModelDto findByName(String name) {
        ModelDto modelDto = new ModelDto();
        Model byName = modelDao.findByNameAndCorporateBank(name, RiskUtil.getOrganizationCode());
        return ConverterService.convert(byName, modelDto);
    }

    public ModelSearchDto findModel(ModelSearchDto modelSearchDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchDto.getOffset() - 1, 0), modelSearchDto.getLimit());

        String sql = OracleSQLConstant.findModelSql;
        // String sql = MysqlSQLConstant.findModelSql;
//        sql +="AND m.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
//        sql +=" AND rr.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";

        String countSql = OracleSQLConstant.findModelCountSql;
        // String countSql = MysqlSQLConstant.findModelCountSql;
//        countSql +="AND m.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
//        countSql +=" AND rr.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
        if (StringUtils.isNotBlank(modelSearchDto.getModelId())) {
            sql += " and m.YD_MODEL_ID like ?1 ";
            countSql += " and m.YD_MODEL_ID like ?1 ";
        }
        if (StringUtils.isNotBlank(modelSearchDto.getName())) {
            sql += " and m.YD_NAME like ?2 ";
            countSql += " and m.YD_NAME like ?2 ";
        }
        if (StringUtils.isNotBlank(modelSearchDto.getTypeId())) {
            sql += " and m.YD_TYPE_ID = ?3 ";
            countSql += " and m.YD_TYPE_ID = ?3 ";
        }
        if (StringUtils.isNotBlank(modelSearchDto.getLevelId())) {
            sql += " and m.YD_LEVEL_ID = ?4 ";
            countSql += " and m.YD_LEVEL_ID = ?4 ";
        }
        if (StringUtils.isNotBlank(modelSearchDto.getStatus())) {
            sql += " and m.YD_STATUS = ?5 ";
            countSql += " and m.YD_STATUS = ?5 ";
        }
        //添加deptId
        if (StringUtils.isNotBlank(modelSearchDto.getDeptId())) {
            sql += " and m.deptId = ?6 ";
            countSql += " and m.deptId = ?6 ";
        }
        countSql += OracleSQLConstant.findModelEndCountSql;
        //countSql+= MysqlSQLConstant.findModelEndCountSql;


        sql += OracleSQLConstant.findModelEndSql;
        // sql += MysqlSQLConstant.findModelEndSql;

        Query nativeQuery = entityManager.createNativeQuery(sql);
        Query nativeQueryCount = entityManager.createNativeQuery(countSql);

        if (StringUtils.isNotBlank(modelSearchDto.getModelId())) {
            nativeQuery.setParameter(1, "%" + modelSearchDto.getModelId() + "%");
            nativeQueryCount.setParameter(1, "%" + modelSearchDto.getModelId() + "%");
        }
        if (StringUtils.isNotBlank(modelSearchDto.getName())) {
            nativeQuery.setParameter(2, "%" + modelSearchDto.getName() + "%");
            nativeQueryCount.setParameter(2, "%" + modelSearchDto.getName() + "%");
        }
        if (StringUtils.isNotBlank(modelSearchDto.getTypeId())) {
            nativeQuery.setParameter(3, modelSearchDto.getTypeId());
            nativeQueryCount.setParameter(3, modelSearchDto.getTypeId());
        }

        if (StringUtils.isNotBlank(modelSearchDto.getLevelId())) {
            nativeQuery.setParameter(4, modelSearchDto.getLevelId());
            nativeQueryCount.setParameter(4, modelSearchDto.getLevelId());
        }
        if (StringUtils.isNotBlank(modelSearchDto.getStatus())) {
            nativeQuery.setParameter(5, modelSearchDto.getStatus());
            nativeQueryCount.setParameter(5, modelSearchDto.getStatus());
        }
        //添加deptId
        if (StringUtils.isNotBlank(modelSearchDto.getDeptId())) {
            nativeQuery.setParameter(6, modelSearchDto.getDeptId());
            nativeQueryCount.setParameter(6, modelSearchDto.getDeptId());
        }

        List<Object[]> resultList = nativeQuery.getResultList();
        List<Object[]> resultList1 = nativeQueryCount.getResultList();
        Long count;
        if (resultList.size() == 0) {
            count = 0L;
        } else {
            count = (long) resultList.size();
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> resultList2 = nativeQuery.getResultList();
        List<ModeAndKindlDto> modeAndKindlDtos = new ArrayList<>();
        for (Object[] o : resultList2) {
            ModeAndKindlDto modeAndKindlDto = new ModeAndKindlDto(
                    Long.parseLong(o[0].toString()),
                    (o[1] == null) ? "" : o[1].toString(),
                    (o[2] == null) ? "" : o[2].toString(),
                    (o[3] == null) ? "" : o[3].toString(),
                    (o[4] == null) ? "" : o[4].toString(),
                    (o[5] == null) ? "" : o[5].toString(),
                    (o[6] == null) ? "" : o[6].toString(),
                    (o[7] == null) ? "" : o[7].toString(),
                    (o[8] == null) ? "" : o[8].toString()
            );
            modeAndKindlDtos.add(modeAndKindlDto);
        }
        modelSearchDto.setList(modeAndKindlDtos);
        modelSearchDto.setTotalRecord(count);
        modelSearchDto.setTotalPages((int) Math.ceil(count.intValue() / modelSearchDto.getLimit()));
        return modelSearchDto;
    }

    public ModelDto findByModelIdAndCode(String modelId) {
        ModelDto modelDto = new ModelDto();
        Model byId = modelDao.findByModelId(modelId);
        return ConverterService.convert(byId, modelDto);
    }

    /**
     * @return
     * @Description
     * @author yangwz 跑批
     * @date 2019-11-06 16:25
     * @params * @param null
     */
    public ModelDto findByModelId(String modelId) {
        ModelDto modelDto = new ModelDto();
        Model byId = modelDao.findByModelId(modelId);
        return ConverterService.convert(byId, modelDto);
    }

    public ModelDto findByModelId(String modelId, String code) {
        ModelDto modelDto = new ModelDto();
        Model byId = modelDao.findByModelId(modelId);
        return ConverterService.convert(byId, modelDto);
    }

    /**
     * @author:yinjie
     * @date:2019/7/2
     * @time:10:54
     * @description: 查询交易监测类型的模型名称
     */
    public ModelSearchDto findTypeNameAsRisk(ModelSearchDto modelSearchDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchDto.getOffset() - 1, 0), modelSearchDto.getLimit());
        String sql = OracleSQLConstant.findTypeNameAsRiskSql;
        //String sql = MysqlSQLConstant.findTypeNameAsRiskSql;
        sql += "AND m.yd_corporate_bank = '" + RiskUtil.getOrganizationCode() + "'";
        sql += "AND rr.yd_corporate_bank = '" + RiskUtil.getOrganizationCode() + "'";
        sql += "AND rc.yd_corporate_bank = '" + RiskUtil.getOrganizationCode() + "'";
        return getModelTypeName(modelSearchDto, pageable, sql);
    }

    public ModelSearchDto findTypeNameAsCode(ModelSearchDto modelSearchDto, String code) {
        Pageable pageable = new PageRequest(Math.max(modelSearchDto.getOffset() - 1, 0), modelSearchDto.getLimit());
        String sql = OracleSQLConstant.findTypeNameAsRiskSql;

        return getModelTypeName(modelSearchDto, pageable, sql);
    }

    /**
     * @author:yinjie
     * @date:2019/7/2
     * @time:10:54
     * @description: 查询开户变更类型的模型名称
     */
    public ModelSearchDto findTypeNameAsChange(ModelSearchDto modelSearchDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchDto.getOffset() - 1, 0), modelSearchDto.getLimit());

        String sql = OracleSQLConstant.findTypeNameAsChangeSql;
        //String sql = MysqlSQLConstant.findTypeNameAsChangeSql;
//        sql +="AND m.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
//        sql +="AND rr.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
//        sql +="AND rc.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
        return getModelTypeName(modelSearchDto, pageable, sql);
    }


    /**
     * @author:yinjie
     * @date:2019/7/2
     * @time:10:54
     * @description: 查询对账类型的模型名称
     */
    @Override
    public ModelSearchDto findTypeNameAsDz(ModelSearchDto modelSearchDto) {
        Pageable pageable = new PageRequest(Math.max(modelSearchDto.getOffset() - 1, 0), modelSearchDto.getLimit());

        String sql = OracleSQLConstant.findTypeNameAsDzSql;
        //String sql = MysqlSQLConstant.findTypeNameAsChangeSql;
//        sql +="AND m.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
//        sql +="AND rr.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
//        sql +="AND rc.yd_corporate_bank = '"+RiskUtil.getOrganizationCode()+"'";
        return getModelTypeName(modelSearchDto, pageable, sql);
    }

    @NotNull
    private ModelSearchDto getModelTypeName(ModelSearchDto modelSearchDto, Pageable pageable, String sql) {
        sql += OracleSQLConstant.getModelTypeNameSql;
        // sql += MysqlSQLConstant.getModelTypeNameSql;

        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = nativeQuery.getResultList();
        Long count;
        if (resultList.size() == 0) {
            count = 0L;
        } else {
            count = (long) resultList.size();
        }
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());
        List<ModeAndKindlDto> modeAndKindlDtos = new ArrayList<>();
        for (Object[] o : resultList) {
            ModeAndKindlDto modeAndKindlDto = new ModeAndKindlDto(
                    Long.parseLong(o[0].toString()),
                    (o[1] == null) ? "" : o[1].toString(),
                    (o[2] == null) ? "" : o[2].toString(),
                    (o[3] == null) ? "" : o[3].toString(),
                    (o[4] == null) ? "" : o[4].toString(),
                    (o[5] == null) ? "" : o[5].toString(),
                    (o[6] == null) ? "" : o[6].toString(),
                    (o[7] == null) ? "" : o[7].toString(),
                    ""
            );
            modeAndKindlDtos.add(modeAndKindlDto);
        }

        modelSearchDto.setList(modeAndKindlDtos);
        modelSearchDto.setTotalRecord(count);
        modelSearchDto.setTotalPages((int) Math.ceil(count.intValue() / modelSearchDto.getLimit()));
        return modelSearchDto;

    }


    @Override
    public List<ModelDto> findAllModel() {
        String sql = OracleSQLConstant.findAllModelSql;
        //String sql = MysqlSQLConstant.findAllModelSql;
        sql += "and a.yd_status ='1' ";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<ModelDto> list = new ArrayList<>();
        for (Object[] o : resultList) {
            ModelDto m = new ModelDto();
            m.setName((o[0] == null) ? "" : o[0].toString());
            m.setTypeId((o[1] == null) ? "" : o[1].toString());
            m.setId((o[2] == null) ? 0 : Long.parseLong(o[2].toString()));
            m.setModelId((o[3] == null) ? "" : o[3].toString());
            list.add(m);
        }
        return list;
    }

    /*获取所有停用和未停用的模型*/
    @Override
    public List<ModelDto> findAllMstModel() {
        String sql = OracleSQLConstant.findAllModelSql;
        //String sql = MysqlSQLConstant.findAllModelSql;
        //sql +=" and a.yd_corporate_bank='"+RiskUtil.getOrganizationCode()+"'";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<ModelDto> list = new ArrayList<>();
        for (Object[] o : resultList) {
            ModelDto m = new ModelDto();
            m.setName((o[0] == null) ? "" : o[0].toString());
            m.setTypeId((o[1] == null) ? "" : o[1].toString());
            m.setId((o[2] == null) ? 0 : Long.parseLong(o[2].toString()));
            m.setModelId((o[3] == null) ? "" : o[3].toString());
            list.add(m);
        }
        return list;
    }


    @Override
    public List<ModelDto> findByStatus() {
        String sql = MysqlSQLConstant.findAllModelSql;
        sql += "and a.yd_status ='1'";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();
        List<ModelDto> list = new ArrayList<>();
        for (Object[] o : resultList) {
            ModelDto m = new ModelDto();
            m.setName((o[0] == null) ? "" : o[0].toString());
            m.setTypeId((o[1] == null) ? "" : o[1].toString());
            m.setId((o[2] == null) ? 0 : Long.parseLong(o[2].toString()));
            m.setModelId((o[3] == null) ? "" : o[3].toString());
            list.add(m);
        }
        return list;
    }

    @Override
    public boolean modelInit() {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        try {
            ruleConfigService.initRule();
            List<Model> model = modelDao.findAll();
            if (model.size() == 0) {
                initModel();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ModelDto findByModelIdForSechdule(String modelId, String code) {
        ModelDto modelDto = new ModelDto();
        Model byId = modelDao.findByModelId(modelId);
        return ConverterService.convert(byId, modelDto);
    }

    @Override
    public List<ModelDto> findModelNum(String typeId) {
//        return ConverterService.convertToList(modelDao.findByTypeId(typeId), ModelDto.class);
        return ConverterService.convertToList(modelDao.findByTypeIdAndStatus(typeId,"1"), ModelDto.class);
    }

    public void createModel(String name, String modelId, String status, String mdesc, String typeId, String levelId) {
        if (modelId.equals("RISK_1001") || modelId.equals("RISK_1002") || modelId.equals("RISK_2001") || modelId.equals("RISK_2002") || modelId.equals("RISK_2003") || modelId.equals("RISK_2005") || modelId.equals("RISK_2006") || modelId.equals("RISK_2007") || modelId.equals("RISK_2008")) {
          /*  RiskType risk=riskTypeDao.findByTypeName("开户变更");
            typeId =Long.toString(risk.getId());*/
            typeId = "1002";
        } else {
            /*RiskType risk=riskTypeDao.findByTypeName("交易监测");
            typeId =Long.toString(risk.getId());*/
            typeId = "1001";
        }
        // RiskLevel riskLevel = riskLevelDao.findByLevelName("中");
        ///levelId =Long.toString(riskLevel.getId());
        Model model = new Model();
        model.setName(name);
        model.setModelId(modelId);
        model.setStatus(status);
        model.setMdesc(mdesc);
        model.setTypeId(typeId);
        model.setLevelId("1002");
        modelDao.save(model);
    }

    public void initModel() {
        createModel("基本开户人频繁开户、销户或变更账户信息", "RISK_2001", "1", "基本开户人频繁开户、销户或变更账户信息", "1002", "1002");
        createModel("进行对单位同一注册地址被多次使用的账户筛选。", "RISK_2002", "1", "进行对单位同一注册地址被多次使用的账户筛选。", "1002", "1002");
        createModel("进行对双异地（注册地、经营地）单位开立银行账户的筛选。", "RISK_2003", "1", "进行对双异地（注册地、经营地）单位开立银行账户的筛选。", "1002", "1002");
        createModel("进行对同一法定代表人或负责人开立多个账户进行筛选。", "RISK_2005", "1", "进行对同一法定代表人或负责人开立多个账户进行筛选。", "1002", "1002");
        createModel("进行对同一经办人员代理多个主体开立账户进行筛选", "RISK_2006", "1", "进行对同一经办人员代理多个主体开立账户进行筛选", "1002", "1002");
        createModel("多个主体的法定代表人、财务负责人、经办人员预留同一个联系方式等的账户筛选。", "RISK_2007", "1", "多个主体的法定代表人、财务负责人、经办人员预留同一个联系方式等的账户筛选。", "1002", "1002");
        createModel("同一主体的法定代表人、财务负责人、财务经办人预留同一个联系方式", "RISK_2008", "1", "同一主体的法定代表人、财务负责人、财务经办人预留同一个联系方式", "1002", "1002");
        createModel("新开户15日发生100万以上交易", "RISK_1001", "1", "新开户15日发生100万以上交易", "1001", "1002");
        createModel("印鉴卡变更15日内发生50万交易", "RISK_1002", "1", "印鉴卡变更15日内发生50万交易", "1001", "1002");
        createModel("新开立账户短期内有大额资金汇入汇出", "RISK_2019", "1", "新开立账户短期内有大额资金汇入汇出", "1001", "1002");
        createModel("短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "RISK_2012", "1", "短期内资金集中转入、分散转出，尤其是资金来汇往多个地区，多为单次交易", "1001", "1002");
        //createModel("营业执照证件、法人身份证等证件到期，账户还在发生交易行为","RISK_2015","1","营业执照证件、法人身份证等证件到期，账户还在发生交易行为","1001","1002");
        createModel("账户短期内发生频繁或大额交易,后突然停止使用或者销户", "RISK_2020", "1", "账户短期内发生频繁或大额交易,后突然停止使用或者销户", "1001", "1002");
        createModel("短期内资金分散转入、集中转出，尤其是资金来源多个地区", "RISK_2011", "1", "短期内资金分散转入、集中转出，尤其是资金来源多个地", "1001", "1002");
        //createModel("对公客户企业已经被注销或者吊销，但仍存在账户交易行为","RISK_2009","1","对公客户企业已经被注销或者吊销，但仍存在账户交易行为","1001","1002");
        //createModel("单位账户与非日常交易单位账户划转大额资金，且明显不符合常理的","RISK_2010","1","单位账户与非日常交易单位账户划转大额资金，且明显不符合常理的","1001","1002");
        createModel("相同收付款人之间短期内频繁发生资金收付", "RISK_2016", "1", "相同收付款人之间短期内频繁发生资金收付", "1001", "1002");
        createModel("短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "RISK_2023", "1", "短期内频繁收付的大额存款、整收零付或零收整付且金额大致相当的账户", "1001", "1002");
        createModel("长期未发生业务又突然发生大额资金收付的账户", "RISK_2024", "1", "长期未发生业务又突然发生大额资金收付的账户", "1001", "1002");
        //createModel("账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转","RISK_2013","1","账户资金快进快出、过渡性质明显，尤其是资金在极短时间内通过多个账户划转","1001","1002");
        //createModel("公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)","RISK_2014","1","公司资金流水与企业经营规模不符(账户交易与客户身份明显不符)","1001","1002");
        //createModel("存在拆分交易，故意规避交易限额","RISK_2021","1","存在拆分交易，故意规避交易限额","1001","1002");
        //createModel("企业注册时间与开立基本账户间隔超过半年以上","RISK_2022","1","企业注册时间与开立基本账户间隔超过半年以上","1001","1002");
        //createModel("新开单位结算账户，半年内未发生交易","RISK_2017","1","新开单位结算账户，半年内未发生交易","1001","1002");

    }
}
