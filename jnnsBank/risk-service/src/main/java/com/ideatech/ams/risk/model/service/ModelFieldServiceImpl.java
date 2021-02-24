package com.ideatech.ams.risk.model.service;

import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.ams.risk.enums.OverFieldsEnums;
import com.ideatech.ams.risk.model.dao.ModelFieldDao;
import com.ideatech.ams.risk.model.dto.ModelDto;
import com.ideatech.ams.risk.model.dto.ModelFieldDto;
import com.ideatech.ams.risk.model.entity.ModelField;
import com.ideatech.ams.risk.riskdata.dto.RiskDetailsSearchDto;
import com.ideatech.ams.risk.riskdata.service.RiskDataService;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.org.dao.OrganizationDao;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelFieldServiceImpl implements ModelFieldService {

    @Autowired
    ModelFieldDao modelFieldDao;
    @Autowired
    RiskDataServiceToExp riskDataServiceToExp;
    @Autowired
    RiskDataService riskDataService;
    @Autowired
    EntityManager em ;
    @Autowired
    ModelService modelService;
    @Autowired
    OrganizationDao organizationDao;
    @Autowired
    private DictionaryService dictionaryService;
    /**
     * 获取数据库名称
     */
    @Value("${ams.szsm.ods.schema:AMS}")
    private String schema;
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
    @Override
    public List<ModelFieldDto> findAllByModelId(String tableName) {
        List<ModelField> allByTableName = modelFieldDao.findAllByModelId(tableName);
        List<ModelFieldDto> modelFieldDtos = ConverterService.convertToList(allByTableName, ModelFieldDto.class);
        return modelFieldDtos;
    }

    @Override
    public void saveModelField(ModelFieldDto modelFieldDto) {
        ModelField modelField = new ModelField();
        if(null != modelFieldDto.getId()){
            modelField = modelFieldDao.findOne(modelFieldDto.getId());
            if (modelField == null){
                modelField = new ModelField();
            }
        }
        ConverterService.convert(modelFieldDto,modelField);
        //modelField.setCorporateBank(RiskUtil.getOrganizationCode());
        modelFieldDao.save(modelField);
    }

    @Override
    public List<Map<String, Object>> getRiskMapList(List list, RiskDetailsSearchDto riskDetailsSearchDto) {
        List<Object[]> list1= new ArrayList<>();
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        RiskDetailsSearchDto fieldDto = riskDataService.createSQLField ( list );
        riskDetailsSearchDto.setOfield(fieldDto.getOfield ()+" ");
        //把page中每个list中的map转换成list,并保持和zh相同的顺序
        riskDetailsSearchDto = riskDataServiceToExp.findRiskListDetails(riskDetailsSearchDto);
        list1=riskDetailsSearchDto.getList();

        //将字段名和值组装成map
        Object obj = list.get(1);
        String[] key = obj.toString().split(",");
        //拼装表数据,标题与数据按顺序对应
        for (int i = 0 ; i<=list1.size()-1;i++){
            Map<String, Object> map =new HashMap<String, Object> () ;
            Object[] objects = list1.get(i);
            for (int j=0;j<= key.length-1;j++){
//                if(j==5){
//                    objects[5]=dictionaryService.getNameByValue("cr_dr_maint_ind", (objects[5] == null) ? "" : objects[5].toString());
//                }
//                if(j==12){
//                    objects[12]=dictionaryService.getNameByValue("tran_channel", (objects[12] == null) ? "" : objects[12].toString());
//                }
                //借贷标志
                if(key[j].equals("YD_JDBZ")){
                    objects[j]=dictionaryService.getNameByValue("cr_dr_maint_ind", (objects[j] == null) ? "" : objects[j].toString());
                }
                //交易渠道
                if(key[j].equals("YD_TRAN_CHANNEL")){
                    objects[j]=dictionaryService.getNameByValue("tran_channel", (objects[j] == null) ? "" : objects[j].toString());
                }
                //现转标志
                if(key[j].equals("YD_CASH_TRAN_IND")){
                    objects[j]=dictionaryService.getNameByValue("cash_tran_ind", (objects[j] == null) ? "" : objects[j].toString());
                }
                //冲抹标志
                if(key[j].equals("YD_REVERSAL_INDL")){
                    objects[j]=dictionaryService.getNameByValue("reversal_indi", (objects[j] == null) ? "" : objects[j].toString());
                }
                //币种
                if(key[j].equals("YD_CCY")){
                    objects[j]=dictionaryService.getNameByValue("ccy",(objects[j] == null) ? "" : objects[j].toString());
                }
                //交易类型
                if(key[j].equals("YD_TRAN_TYPE")){
                    objects[j]=dictionaryService.getNameByValue("tran_type", (objects[j] == null) ? "" : objects[j].toString());
                }
                //证件类型
                if(key[j].equals("YD_DEPUTY_DOCUMENT_TYPE")){
                    objects[j]=dictionaryService.getNameByValue("global_id_type", (objects[j] == null) ? "" : objects[j].toString());
                }
                //证件类型
                if(key[j].equals("yd_acct_type")){
                    objects[j]=dictionaryService.getNameByValue("账户性质(加未知)", (objects[j] == null) ? "" : objects[j].toString());
                }
                //操作类型
                if(key[j].equals("yd_bill_type")){
                    objects[j]=dictionaryService.getNameByValue("业务操作类型", (objects[j] == null) ? "" : objects[j].toString());
                }
                //交易金额乘以-1
                if(key[j].equals("YD_JYJE")){
                    if(StringUtils.isNotBlank((String) objects[j])){
                        String jyje= (String) objects[j];
                        BigDecimal a=new BigDecimal(jyje);
                        BigDecimal b=new BigDecimal("-1");
                        objects[j]= a.multiply(b);
                    }
                }
                map.put(key[j],objects[j]);
            }
            System.out.print(map);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 删除模型配置字段
     * @yangcq
     * @date 20190622
     * @address wulmq
     * @param modelFieldDto
     */
    @Override
    public void delete(ModelFieldDto modelFieldDto) {
        ModelField dto = new ModelField();
        if(modelFieldDto.getId ()!=null){
            dto = modelFieldDao.findOne ( modelFieldDto.getId () );
        }
        modelFieldDao.delete ( dto );
    }

    @Override
    public boolean modelFieldInit() {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        try {
            List<ModelDto>  list = modelService.findAllMstModel (  );
                /*初始化交易模型字段*/
                    for(ModelDto dto:  list){
                        if(StringUtils.equalsIgnoreCase (dto.getTypeId (),"1001")){
                            if(!StringUtils.equalsIgnoreCase (dto.getModelId (),"RISK_1001")&&!StringUtils.equalsIgnoreCase (dto.getModelId (),"RISK_1002" )){
                                this.initModelFields (dto.getModelId ());
                            }else{
                                modelFieldDao.deleteByModelId ( dto.getModelId () );
                            }
                        }else{
                            modelFieldDao.deleteByModelId ( dto.getModelId () );
                        }
                    }
                    initModelField();
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 初始化交易风险模型字段
     * @author yangcq
     * @param modelId
     */
    @Override
    public void initModelFields(String modelId) {

        //String sql = MysqlSQLConstant.getModelFieldsSql;
        String sql = OracleSQLConstant.getModelFieldsSql;
        String tableName = "YD_"+modelId;
        //==========MySql============
        //sql = sql +"\n and table_name = '"+tableName.trim ()+"'\n and table_schema='"+schema.trim ()+"'";
        //==========Oracle===========
        sql+="\nWHERE a.TABLE_NAME = '"+tableName.trim()+"'\n" +
                " and b.TABLE_NAME = '"+tableName.trim()+"'\n" +
                "    and a.column_name = b.column_name";
        Query query = em.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();
        List<ModelField> resultList = new ArrayList<>();
        int order =0;
        OverFieldsEnums[]  overFields= OverFieldsEnums.values ();
        for(Object[] o:list){
            boolean flag = false;
            for (int i = 0; i< overFields.length ; i++) {
                OverFieldsEnums enums = overFields[i];
                String displayName = enums.getDisplayName ();
                if(StringUtils.equalsIgnoreCase ( displayName,(o[0]==null)?"":o[0].toString()  )){
                    flag = true;
                }
            }
            if(flag){
                continue;
            }
            order++;
            ModelField m = new ModelField();
            ModelFieldDto dto = new ModelFieldDto();
            dto.setModelId ( modelId );
            dto.setOrderFlag ( order );
            dto.setFieldsZh ( (o[1]==null)?"":o[1].toString() );
            dto.setFieldsEn ( (o[0]==null)?"":o[0].toString() );
            dto.setStatus ( 1 );
            dto.setExportFlag ( 0 );
            dto.setShowFlag ( 0 );
            ConverterService.convert (dto,m  );
            resultList.add(m);
        }
        modelFieldDao.deleteByModelId ( modelId );
        modelFieldDao.save ( resultList );

    }

    /**
     * 初始化开开户风险字段
     * @author yangcq 20191122
     * @param
     */
    public void initModelField(){
        createModelField("yd_acct_no", "账户号", "RISK_1001", 0, 0, 1);
        createModelField("yd_acct_name", "账户名称", "RISK_1001", 0, 0, 2);
        createModelField("yd_trade_time", "交易时间", "RISK_1001", 0, 0, 3);
        createModelField("yd_amount", "交易金额", "RISK_1001", 0, 0, 4);


        createModelField("yd_acct_no", "账户号", "RISK_1002", 0, 0, 1);
        createModelField("yd_acct_name", "账户名称", "RISK_1002", 0, 0, 2);
        createModelField("yd_amount", "交易金额", "RISK_1002", 0, 0, 3);
        createModelField("yd_trade_date", "交易日期", "RISK_1002", 0, 0, 4);
        createModelField("yd_start_date", "印鉴卡启用日期", "RISK_1002", 0, 0, 5);
        createModelField("yd_serial_Id", "交易流水号", "RISK_1002", 0, 0, 6);


        createModelField("yd_customer_id", "客户号", "RISK_2001", 0, 0, 1);
        createModelField("yd_acct_no", "账户号", "RISK_2001", 0, 0, 2);
        createModelField("yd_depositor_name", "客户名称", "RISK_2001", 0, 0, 3);
        createModelField("yd_acct_name", "账户名称", "RISK_2001", 0, 0, 4);

        createModelField("yd_customer_id", "客户号", "RISK_2002", 0, 0, 1);
        createModelField("yd_depositor_name", "客户名称", "RISK_2002", 0, 0, 2);
        createModelField("yd_reg_address", "注册地址", "RISK_2002", 0, 0, 3);


        createModelField("yd_work_address", "经营地地址", "RISK_2003", 0, 0, 2);
        createModelField("yd_depositor_name", "客户名称", "RISK_2003", 0, 0, 0);
        createModelField("yd_acct_create_date", "开户日期", "RISK_2003", 0, 0, 2);
        createModelField("yd_reg_address", "注册地址", "RISK_2003", 0, 0, 1);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2003", 0, 0, 1);
        createModelField("yd_reg_address", "注册地址", "RISK_2003", 0, 0, 2);
        createModelField("yd_bank_name", "开户机构", "RISK_2003", 0, 0, 3);
        createModelField("yd_legal_name", "法人姓名", "RISK_2003", 0, 0, 5);
        createModelField("yd_bank_name", "开户机构", "RISK_2003", 0, 0, 6);
        createModelField("yd_finance_idcard_no", "财务主管证件号", "RISK_2003", 0, 0, 8);

        createModelField("yd_legal_name", "法人姓名", "RISK_2005", 0, 0, 0);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2005", 0, 0, 4);
        createModelField("yd_bank_name", "开户机构", "RISK_2005", 0, 0, 1);
        createModelField("yd_acct_name", "账户名称", "RISK_2005", 0, 0, 3);
        createModelField("yd_acct_no", "账户号", "RISK_2005", 0, 0, 2);

        createModelField("yd_legal_name", "法人姓名", "RISK_2006", 0, 0, 0);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2006", 0, 0, 1);


        createModelField("yd_legal_telephone", "法人电话", "RISK_2007", 0, 0, 8);
        createModelField("yd_legal_name", "法人姓名", "RISK_2007", 0, 0, 5);
        createModelField("yd_finance_telephone", "财务负责人电话", "RISK_2007", 0, 0, 10);
        createModelField("yd_finance_name", "财务负责人姓名", "RISK_2007", 0, 0, 11);
        createModelField("yd_finance_idcard_no", "财务负责人证件号", "RISK_2007", 0, 0, 12);
        createModelField("yd_depositor_name", "客户名称", "RISK_2007", 0, 0, 0);
        createModelField("yd_customer_id", "客户号", "RISK_2007", 0, 0, 1);
        createModelField("yd_bank_name", "开户机构", "RISK_2007", 0, 0, 2);
        createModelField("yd_acct_create_date", "开户日期", "RISK_2007", 0, 0, 3);
        createModelField("yd_operator_name", "经办人姓名", "RISK_2007", 0, 0, 7);
        createModelField("yd_operator_telephone", "经办人电话", "RISK_2007", 0, 0, 9);
        createModelField("yd_operator_idcard_no", "经办人证件号", "RISK_2007", 0, 0, 12);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_2007", 0, 0, 14);

        createModelField("yd_reg_address", "注册地址", "RISK_2008", 0, 0, 9);
        createModelField("yd_depositor_name", "客户名称", "RISK_2008", 0, 0, 0);
        createModelField("yd_customer_id", "客户号", "RISK_2008", 0, 0, 1);
        createModelField("yd_operator_idcard_no", "经办人证件号", "RISK_2008", 0, 0, 2);
        createModelField("yd_legal_name", "法人姓名", "RISK_2008", 0, 0, 3);
        createModelField("yd_legal_telephone", "法人电话", "RISK_2008", 0, 0, 4);
        createModelField("yd_finance_idcard_no", "财务主管证件号", "RISK_2008", 0, 0, 5);
        createModelField("yd_finance_name", "财务主管名称", "RISK_2008", 0, 0, 6);
        createModelField("yd_operator_name", "经办人姓名", "RISK_2008", 0, 0, 8);
        createModelField("yd_finance_idcard_no", "财务负责人证件号", "RISK_2008", 0, 0, 5);
        createModelField("yd_finance_telephone", "财务负责人电话", "RISK_2008", 0, 0, 7);
        createModelField("yd_reg_address", "注册地址", "RISK_2003", 0, 0, 0);
        createModelField("yd_operator_telephone", "经办人电话", "RISK_2008", 0, 0, 9);
        createModelField("yd_bank_name", "开户机构", "RISK_2008", 0, 0, 7);
        createModelField("yd_finance_name", "财务负责人姓名", "RISK_2008", 0, 0, 6);

        //新增账户性质
        createModelField("yd_acct_type", "账户性质", "RISK_1001", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_1002", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2001", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2002", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2003", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2005", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2006", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2007", 0, 0, 14);
        createModelField("yd_acct_type", "账户性质", "RISK_2008", 0, 0, 14);

        //新加模型展示字段
        createModelField("yd_depositor_name", "客户名称", "RISK_3001", 0, 0, 0);
        createModelField("yd_legal_name", "法人姓名", "RISK_3001", 0, 0, 1);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_3001", 0, 0, 2);

        createModelField("yd_depositor_name", "客户名称", "RISK_3002", 0, 0, 0);
        createModelField("yd_legal_name", "法人姓名", "RISK_3002", 0, 0, 1);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_3002", 0, 0, 2);
        createModelField("yd_reg_address", "注册地址", "RISK_3002", 0, 0, 3);

        createModelField("yd_depositor_name", "客户名称", "RISK_3003", 0, 0, 0);
        createModelField("yd_reg_address", "注册地址", "RISK_3003", 0, 0, 1);
        createModelField("yd_work_address", "经营地地址", "RISK_3003", 0, 0, 2);

        createModelField("yd_depositor_name", "客户名称", "RISK_3004", 0, 0, 0);
        createModelField("yd_reg_address", "注册地址", "RISK_3004", 0, 0, 1);
        createModelField("yd_bank_name", "开户机构", "RISK_3004", 0, 0, 2);

        createModelField("yd_depositor_name", "客户名称", "RISK_3005", 0, 0, 0);
        createModelField("yd_acct_no", "账户号", "RISK_3005", 0, 0, 1);
        createModelField("yd_acct_create_date", "开户日期", "RISK_3005", 0, 0, 2);
        createModelField("yd_legal_name", "法人姓名", "RISK_3005", 0, 0, 3);
        createModelField("yd_legal_idcard_no", "法人证件号", "RISK_3005", 0, 0, 4);
    }
    //保存开户类模型详细字段
    public void createModelField(String fieldsEn, String fieldsZh, String modelId, Integer showFlag , Integer exportFlag , Integer orderFlag){
        ModelField modelField = new ModelField();
        modelField.setFieldsEn(fieldsEn);
        modelField.setFieldsZh(fieldsZh);
        modelField.setModelId(modelId);
        modelField.setShowFlag(showFlag);
        modelField.setExportFlag(exportFlag);
        modelField.setOrderFlag(orderFlag);
        modelFieldDao.save(modelField);
    }
    @Override
    public List<ModelFieldDto> findAllByModelIdAndExportFlagOrderByOrderFlag(ModelFieldDto modelFieldDto) {
        List<ModelField> mfList = modelFieldDao.findAllByModelIdAndExportFlagAndShowFlagOrderByOrderFlag(modelFieldDto.getModelId().toUpperCase(),0,0);
        return  ConverterService.convertToList(mfList, ModelFieldDto.class);
    }

    @Override
    public List findRiskDataFieldZH(ModelFieldDto modelFieldDto) {
        Sort sort = new Sort(Sort.Direction.ASC,"orderFlag");
        List<ModelField> mfList = modelFieldDao.findAllByModelIdAndShowFlag(modelFieldDto.getModelId().toUpperCase(),0,sort);
        List<ModelFieldDto> modelFieldDtoList = ConverterService.convertToList(mfList, ModelFieldDto.class);
        List li = new ArrayList();
        if(modelFieldDtoList.size() > 0){
            List<String> zh = new ArrayList<String>();
            String en = "";
            String dataSnField ="";
            String allField = "";//所有字段,用于判断是否有机构字段
            for(ModelFieldDto m : modelFieldDtoList){
                allField += m.getFieldsEn()+",";
                if(m.getShowFlag()!=null){
                    if("0".equals(String.valueOf ( m.getShowFlag() )))//显示的情况才加入
                    {
                        zh.add(m.getFieldsZh()+"");
                        en += m.getFieldsEn()+",";
                    }
                }
                if(m.getOrderFlag ()!=null){
                    if("0".equals(String.valueOf ( m.getOrderFlag ())))//拼接排序字段
                    {
                        dataSnField += m.getFieldsEn()+",";
                    }
                }
            }
            en = en.substring(0,en.length()-1);
            allField = allField.substring(0,allField.length()-1);
            if(!"".equals(dataSnField))
            {
                dataSnField = dataSnField.substring(0, dataSnField.length()-1);
            }
            li.add(0, zh);
            li.add(1, en);
            li.add(2, dataSnField);
            li.add(3, allField);
        }
        return li;
    }
}
