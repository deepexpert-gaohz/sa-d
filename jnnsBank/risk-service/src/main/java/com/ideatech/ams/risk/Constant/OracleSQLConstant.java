package com.ideatech.ams.risk.Constant;

/**
 * @Author: yinjie
 * @Date: 2019/7/9 10:40
 * @description Oracle的Sql语句类
 */
public abstract class OracleSQLConstant {


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:10:45
     * @description: ModelServiceImpl---findModel
     */
    public static final String findModelSql = "select m.YD_ID,\n" +
            "               m.YD_MODEL_ID,\n" +
            "               m.YD_NAME,\n" +
            "               m.YD_MDESC,\n" +
            "               rl.YD_LEVEL_NAME,\n" +
            "               rt.YD_TYPE_NAME,\n" +
            "            LISTAGG(rr.yd_field_name||rc.yd_con_and_val,',') within group(order by rr.yd_id) as ruleName,\n" +
            "\tm.YD_STATUS,\n" +
            "\trt.YD_ID as typeId\n" +
            "          from YD_RISK_MODELS m\n" +
            "          left join YD_RISK_MODEL_LEVEL rl\n" +
            "            on m.YD_LEVEL_ID = rl.YD_ID\n" +
            "           and rl.YD_DELETED = 0\n" +
            "          left join YD_RISK_MODEL_TYPE rt\n" +
            "            on m.YD_TYPE_ID = rt.YD_ID\n" +
            "           and rt.YD_DELETED = '0'\n" +
            "          left join YD_RISK_RULE_FIELD rr\n" +
            "            on m.yd_model_id = rr.yd_model_id and rr.YD_DELETED = '0'\n" +
            "            left join YD_RISK_RULE_CONFIGURATION rc\n" +
            "            on rr.yd_id = rc.yd_rule_id and rc.yd_deleted='0'\n" +
            "           and rr.YD_DELETED = '0'\n" +
            "         where m.YD_DELETED = '0'" +
            "           ";

    public static final String findModelCountSql = "select count(1) from (\n" +
            "select m.YD_ID,\n" +
            "               m.YD_MODEL_ID,\n" +
            "               m.YD_NAME,\n" +
            "               m.YD_MDESC,\n" +
            "               rl.YD_LEVEL_NAME,\n" +
            "               rt.YD_TYPE_NAME,\n" +
            "               LISTAGG(rr.yd_field_name||rc.yd_con_and_val,',') within group(order by rr.yd_id) as ruleName,\n" +
            "               m.YD_STATUS\n" +
            "          from YD_RISK_MODELS m\n" +
            "          left join YD_RISK_MODEL_LEVEL rl\n" +
            "            on m.YD_LEVEL_ID = rl.YD_ID\n" +
            "           and rl.YD_DELETED = '0'\n" +
            "          left join YD_RISK_MODEL_TYPE rt\n" +
            "            on m.YD_TYPE_ID = rt.YD_ID\n" +
            "           and rt.YD_DELETED = '0'\n" +
            "          left join YD_RISK_RULE_FIELD rr\n" +
            "            on m.yd_model_id = rr.yd_model_id and rr.YD_DELETED = '0' \n" +
            "            left join YD_RISK_RULE_CONFIGURATION rc\n" +
            "            on rr.yd_id = rc.yd_rule_id and rc.yd_deleted='0'\n" +
            "         where m.YD_DELETED = '0'\n" +
            "        " +
            "     ";

    public static final String findModelEndCountSql = "  group by  m.YD_ID," +
            "  m.YD_MODEL_ID," +
            "  m.YD_NAME," +
            "  m.YD_MDESC," +
            "  rl.YD_LEVEL_NAME," +
            "  rt.YD_TYPE_NAME, m.YD_STATUS, rt.YD_ID" +
            "   )";

    public static final String findModelEndSql = "   group by  m.YD_ID," +
            "  m.YD_MODEL_ID," +
            "  m.YD_NAME," +
            "   m.YD_MDESC," +
            "  rl.YD_LEVEL_NAME," +
            "  rt.YD_TYPE_NAME, m.YD_STATUS , rt.YD_ID order by m.YD_ID";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:03
     * @description: 查询交易监测类型的模型名称
     * ModelServiceImpl---findTypeNameAsRisk
     */
    public static final String findTypeNameAsRiskSql = "select m.YD_ID,\n" +
            "               m.YD_MODEL_ID,\n" +
            "               m.YD_NAME,\n" +
            "               m.YD_MDESC,\n" +
            "               rl.YD_LEVEL_NAME,\n" +
            "               rt.YD_TYPE_NAME,\n" +
            "             LISTAGG(rr.yd_field_name||rc.yd_con_and_val,',') within group(order by rr.yd_id) as ruleName,\n" +
            "               m.YD_STATUS\n" +
            "          from YD_RISK_MODELS m\n" +
            "          left join YD_RISK_MODEL_LEVEL rl\n" +
            "            on m.YD_LEVEL_ID = rl.YD_ID\n" +
            "           and rl.YD_DELETED = 0\n" +
            "          left join YD_RISK_MODEL_TYPE rt\n" +
            "            on m.YD_TYPE_ID = rt.YD_ID\n" +
            "           and rt.YD_DELETED = '0'\n" +
            "          left join YD_RISK_RULE_FIELD rr\n" +
            "            on m.yd_model_id = rr.yd_model_id and rr.YD_DELETED = '0'\n" +
            "            left join YD_RISK_RULE_CONFIGURATION rc\n" +
            "            on rr.yd_id = rc.yd_rule_id and rc.yd_deleted='0'\n" +
            "           and rr.YD_DELETED = '0'\n" +
            "         where m.YD_DELETED = '0' and rt.yd_id = '1001' " +
            "           ";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:06
     * @description: 查询开户变更类型的模型名称
     * ModelServiceImpl---findTypeNameAsChange
     */
    public static final String findTypeNameAsChangeSql = "select m.YD_ID,\n" +
            "               m.YD_MODEL_ID,\n" +
            "               m.YD_NAME,\n" +
            "               m.YD_MDESC,\n" +
            "               rl.YD_LEVEL_NAME,\n" +
            "               rt.YD_TYPE_NAME,\n" +
            "                LISTAGG(rr.yd_field_name||rc.yd_con_and_val,',') within group(order by rr.yd_id) as ruleName,\n" +
            "               m.YD_STATUS\n" +
            "          from YD_RISK_MODELS m\n" +
            "          left join YD_RISK_MODEL_LEVEL rl\n" +
            "            on m.YD_LEVEL_ID = rl.YD_ID\n" +
            "           and rl.YD_DELETED = 0\n" +
            "          left join YD_RISK_MODEL_TYPE rt\n" +
            "            on m.YD_TYPE_ID = rt.YD_ID\n" +
            "           and rt.YD_DELETED = '0'\n" +
            "          left join YD_RISK_RULE_FIELD rr\n" +
            "            on m.yd_model_id = rr.yd_model_id and rr.YD_DELETED = '0'\n" +
            "            left join YD_RISK_RULE_CONFIGURATION rc\n" +
            "            on rr.yd_id = rc.yd_rule_id and rc.yd_deleted='0'\n" +
            "           and rr.YD_DELETED = '0'\n" +
            "         where m.YD_DELETED = '0' and rt.yd_id = '1002' " +
            "           ";

    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:06
     * @description: 查询对账类型的模型名称
     * ModelServiceImpl---findTypeNameAsChange
     */
    public static final String findTypeNameAsDzSql = "select m.YD_ID,\n" +
            "               m.YD_MODEL_ID,\n" +
            "               m.YD_NAME,\n" +
            "               m.YD_MDESC,\n" +
            "               rl.YD_LEVEL_NAME,\n" +
            "               rt.YD_TYPE_NAME,\n" +
            "                LISTAGG(rr.yd_field_name||rc.yd_con_and_val,',') within group(order by rr.yd_id) as ruleName,\n" +
            "               m.YD_STATUS\n" +
            "          from YD_RISK_MODELS m\n" +
            "          left join YD_RISK_MODEL_LEVEL rl\n" +
            "            on m.YD_LEVEL_ID = rl.YD_ID\n" +
            "           and rl.YD_DELETED = 0\n" +
            "          left join YD_RISK_MODEL_TYPE rt\n" +
            "            on m.YD_TYPE_ID = rt.YD_ID\n" +
            "           and rt.YD_DELETED = '0'\n" +
            "          left join YD_RISK_RULE_FIELD rr\n" +
            "            on m.yd_model_id = rr.yd_model_id and rr.YD_DELETED = '0'\n" +
            "            left join YD_RISK_RULE_CONFIGURATION rc\n" +
            "            on rr.yd_id = rc.yd_rule_id and rc.yd_deleted='0'\n" +
            "           and rr.YD_DELETED = '0'\n" +
            "         where m.YD_DELETED = '0' and rt.yd_id = '1003' " +
            "           ";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:09
     * @description: ModelServiceImpl---getModelTypeName
     */
    public static final String getModelTypeNameSql = "   group by  m.YD_ID," +
            "  m.YD_MODEL_ID," +
            "  m.YD_NAME," +
            "   m.YD_MDESC," +
            "  rl.YD_LEVEL_NAME," +
            "  rt.YD_TYPE_NAME, m.YD_STATUS order by m.YD_ID";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:10
     * @description: ModelServiceImpl---findAllModel
     */
    public static final String findAllModelSql = " SELECT distinct a.yd_name,a.yd_type_id,a.yd_ID,a.yd_model_id \n" +
            "    from yd_risk_models a \n " +
            "    left join yd_risk_model_type b on b.yd_id = a.yd_type_id\n" +
            " where a.yd_deleted='0'  and  b.yd_deleted='0' ";




    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:48
     * @description: ModelCountFileServiceImpl---queryModelCountFile2
     */
    public static final String queryModelCountFile2Sql = "SELECT a.yd_id as \"modelsId\",a.yd_name as \"modelName\", T.yd_RISK_ID AS \"modelId\",SUM( T.yd_TJJG) AS \"tjjg\",o12.yd_type_name AS \"typeName\"\n" +
            "  FROM yd_risk_tjjg T \n" +
            "  LEFT JOIN yd_risk_models A \n" +
            "    ON T.yd_RISK_ID = A.yd_MODEL_ID \n" +
            "  LEFT JOIN yd_sys_organization O \n" +
            "    ON O.yd_code = T.yd_ORG_ID\n" +
            "  LEFT JOIN yd_risk_model_type o12 \n" +
            "    ON o12.yd_id = a.yd_type_id\n where 1=1 ";

    public static final String queryModelCountFile2CountSql = "SELECT count(*)" +
            "  FROM yd_risk_tjjg T\n" +
            "  LEFT JOIN yd_risk_models A\n" +
            "    ON T.yd_RISK_ID = A.yd_MODEL_ID\n" +
            "  LEFT JOIN yd_sys_organization O\n" +
            "    ON O.yd_code = T.yd_ORG_ID\n" +
            "  LEFT JOIN yd_risk_model_type o12\n" +
            "    ON o12.yd_id = a.yd_type_id\n where 1=1 ";

    public static final String queryModelCountFile2EndSql2 = " GROUP BY a.yd_id, T.yd_RISK_ID,A.yd_NAME,o12.yd_type_name order by T.yd_RISK_ID ";

    public static final String queryModelCountFile2CountSql1 = " GROUP BY a.yd_id, T.yd_RISK_ID,A.yd_NAME,o12.yd_type_name order by T.yd_RISK_ID ";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:55
     * @description: ModelCountFileServiceImpl---getAllModelTypeList
     */
    public static final String getAllModelTypeListSql = "    SELECT DISTINCT M.yd_MODEL_ID, T.yd_type_NAME\n" +
            "      FROM yd_risk_models M\n" +
            "      LEFT JOIN yd_risk_model_type T\n" +
            "        ON M.yd_TYPE_ID = T.yd_ID\n" +
            "     WHERE M.yd_deleted = '0'\n" +
            "       AND T.yd_deleted = '0'";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:12:02
     * @description: ModelCountFileServiceImpl---findModelExtendList
     */
    public static final String findModelExtendListSql = "select * from yd_risk_models where 1=1 ";

    public static final String findModelExtendListEndSql = " AND yd_table_name like ?1 ";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:16:37
     * @description: ModelCountFileServiceImpl---getOrgByRiskList
     */
    public static final String getOrgByRiskListSql = "SELECT distinct\n" +
            "      o.yd_code,o.yd_name\n" +
            "    FROM yd_sys_organization o RIGHT JOIN yd_risk_tjjg r on  o.yd_code=r.yd_org_id \n" +
            "\t\tWHERE  o.yd_deleted = '0' order by yd_code ";


    public static final String queryTradeRiskDataSqlCQ = "select rownum as ordNum , yd_model_id,yd_risk_date,yd_name,yd_risk_desc,yd_type_name,yd_risk_point,yd_status,yd_risk_cnt from \n" +
            "                (select distinct  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ,\n" +
            "                t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt \n" +
            "                  from yd_risk_models t1\n" +
            "                  left join yd_risk_model_type t2 \n" +
            "                    on t1.yd_type_id = t2.yd_id \n" +
            "                 right join yd_risk_trade_info t3 \n" +
            "                  on t1.yd_model_id = t3.yd_risk_id \n" +
            "                 right join yd_accounts_all t4 \n" +
            "                   on t3.yd_account_no = t4.yd_acct_no \n" +
            "                 where t2.yd_deleted = 0";
    public static final String queryTradeRiskDataCountSql = "select count(1)\n" +
            "                 from yd_risk_models t1\n" +
            "                  left join yd_risk_model_type t2\n" +
            "                    on t1.yd_type_id = t2.yd_id\n" +
            "                 right join yd_risk_trade_info t3\n" +
            "                    on t1.yd_model_id = t3.yd_risk_id\n" +
            "                 right join yd_accounts_all t4 \n" +
            "                   on t3.yd_account_no = t4.yd_acct_no \n" +
            "                 where t2.yd_deleted = 0\n";


    public static final String queryRiskStaticDataSqlCQ = "select rownum as ordNum , yd_model_id,yd_risk_date,yd_name,yd_risk_desc,yd_type_name,yd_risk_point,yd_status,yd_risk_cnt,yd_account_no, yd_account_name,yd_organ_code ,yd_operator_telephone,yd_operator_idcard_no, yd_operator_name, yd_finance_idcard_no, yd_finance_name, yd_finance_telephone,yd_legal_telephone, yd_legal_idcard_no, yd_legal_name from " +
            "(select  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ," +
            "t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_account_name,t3.yd_organ_code, \n" +
            " t5.yd_operator_telephone,t5.yd_operator_idcard_no,t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,t7.yd_legal_telephone, t7.yd_legal_idcard_no, t7.yd_legal_name \n"+
           " from yd_risk_record_info t3 \n"+
            " left join yd_accounts_all t4 on t4.yd_acct_no = t3.yd_account_no \n"+
            " left join yd_account_public t5 on t5.yd_account_id = t4.yd_id \n"+
            " left join yd_customers_all t6 on t6.yd_customer_no = t4.yd_customer_no \n"+
            " left join yd_customer_public t7 on t7.yd_customer_id = t6.yd_id \n"+
            " left join yd_risk_models t1 on t1.yd_model_id = t3.yd_risk_id \n"+
            " left join yd_risk_model_type t2 on t1.yd_type_id = t2.yd_id \n"+
            " where t2.yd_deleted = 0 \n";
    public static final String queryRiskStaticDataSqlCQYUJING= "select rownum as ordNum , yd_model_id,yd_risk_date,yd_name,yd_risk_desc,yd_type_name,yd_risk_point,yd_status,yd_risk_cnt,yd_account_no, yd_account_name,yd_organ_code ,yd_operator_telephone,yd_operator_idcard_no, yd_operator_name, yd_finance_idcard_no, yd_finance_name, yd_finance_telephone,yd_legal_telephone, yd_legal_idcard_no, yd_legal_name, yd_dmp_organ_code,yd_reg_address,yd_acct_type,yd_customer_id,yd_work_address from " +
            "(select distinct  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ," +
            "t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_account_name,t3.yd_organ_code, \n" +
            " t5.yd_operator_telephone,t5.yd_operator_idcard_no,t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,a.yd_legal_telephone, a.yd_legal_idcard_no, a.yd_legal_name, t8.yd_dmp_organ_code,a.yd_reg_address,a.yd_acct_type, t3.yd_customer_id ,a.yd_work_address\n"+
            " from yd_risk_record_info t3 \n"+
            " left join yd_accounts_all t4 on t4.yd_acct_no = t3.yd_account_no \n"+
            " left join yd_account_public t5 on t5.yd_account_id = t4.yd_id \n"+
            " left join yd_customers_all t6 on t6.yd_customer_no = t4.yd_customer_no \n"+
            " left join yd_customer_public t7 on t7.yd_customer_id = t6.yd_id \n"+
            " left join yd_risk_models t1 on t1.yd_model_id = t3.yd_risk_id \n"+
            " left join yd_risk_model_type t2 on t1.yd_type_id = t2.yd_id \n"+
            " left join yd_organization_map_po t8 on t8.yd_organcode = t3.yd_organ_code \n"+
             " left join yd_account_all_data a on t3.yd_account_no = a.yd_acct_no \n"+
            " where t2.yd_deleted = 0 \n";
    public static final String queryRiskDzDataSqlCQ = "select rownum as ordNum , " +
            "yd_model_id," +
            "yd_risk_date," +
            "yd_name," +
            "yd_risk_desc," +
            "yd_type_name," +
            "yd_risk_point," +
            "yd_status," +
            "yd_risk_cnt," +
            "yd_account_no,yd_organ_code from " +
            "(select distinct  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ," +
            "t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_organ_code \n" +
            "  from yd_risk_models t1\n" +
            "  left join yd_risk_model_type t2\n" +
            "    on t1.yd_type_id = t2.yd_id\n" +
            " right join yd_risk_check_info t3\n" +
            "    on t1.yd_model_id = t3.yd_risk_id\n" +
            " where t2.yd_deleted = 0";
    public static final String queryRiskDzDataSqlCQForYUJING = "select rownum as ordNum , " +
            "yd_model_id," +
            "yd_risk_date," +
            "yd_name," +
            "yd_risk_desc," +
            "yd_type_name," +
            "yd_risk_point," +
            "yd_status," +
            "yd_risk_cnt," +
            "yd_account_no,yd_organ_code, yd_dmp_organ_code from " +
            "(select distinct  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ," +
            "t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_organ_code,t4.yd_dmp_organ_code \n" +
            "  from yd_risk_models t1\n" +
            "  left join yd_risk_model_type t2\n" +
            "    on t1.yd_type_id = t2.yd_id\n" +
            " right join yd_risk_check_info t3\n" +
            "    on t1.yd_model_id = t3.yd_risk_id\n" +
            "  left join yd_organization_map_po t4 on t4.yd_organcode = t3.yd_organ_code \n"+
            " where t2.yd_deleted = 0";
    public static final String queryRiskStaticDataCountSql = "select count(1)\n" +
            "  from yd_risk_models t1\n" +
            "  left join yd_risk_model_type t2\n" +
            "    on t1.yd_type_id = t2.yd_id\n" +
            " right join yd_risk_check_info t3\n" +
            "    on t1.yd_model_id = t3.yd_risk_id\n" +
            " where t2.yd_deleted = 0\n";


    public static final String queryRiskStaticDataCountSqlYUJING = "select count(1)\n" +
            "  from yd_risk_models t1\n" +
            "  left join yd_risk_model_type t2\n" +
            "    on t1.yd_type_id = t2.yd_id\n" +
            " right join yd_risk_check_info t3\n" +
            "    on t1.yd_model_id = t3.yd_risk_id\n" +
            " where t2.yd_deleted = 0\n";

    public static final String queryRiskStaticCheckDataCountSql = "select * from " +
            "(select distinct  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ," +
            "t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_account_name,t3.yd_organ_code, \n" +
            " t5.yd_operator_telephone,t5.yd_operator_idcard_no,t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,t7.yd_legal_telephone, t7.yd_legal_idcard_no, t7.yd_legal_name \n"+
            "  from yd_risk_models t1\n" +
            "  left join yd_risk_model_type t2\n" +
            "    on t1.yd_type_id = t2.yd_id\n" +
            " right join yd_risk_record_info t3\n" +
            "    on t1.yd_model_id = t3.yd_risk_id\n" +
            " right join yd_accounts_all t4\n" +
            "    on t4.yd_acct_no = t3.yd_account_no\n" +
            "left join yd_account_public t5 on t5.yd_account_id = t4.yd_id \n"+
            "left join yd_customers_all t6 on t6.yd_customer_no =  t4.yd_customer_no \n"+
            "left join yd_customer_public t7 on t7.yd_customer_id = t6.yd_id \n"+
            " where t2.yd_deleted = 0";


    public static final String queryRiskStaticCheckDataCountSqlYUJING = "select * from " +
            "(select distinct  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd') as yd_risk_date ," +
            "t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_account_name,t3.yd_organ_code, \n" +
            " t5.yd_operator_telephone,t5.yd_operator_idcard_no,t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,a.yd_legal_telephone, a.yd_legal_idcard_no, a.yd_legal_name,t8.yd_dmp_organ_code,a.yd_reg_address,a.yd_acct_type,t3.yd_customer_id,a.yd_work_address \n"+
            " from yd_risk_record_info t3 \n"+
            " left join yd_accounts_all t4 on t4.yd_acct_no = t3.yd_account_no \n"+
            " left join yd_account_public t5 on t5.yd_account_id = t4.yd_id \n"+
            " left join yd_customers_all t6 on t6.yd_customer_no = t4.yd_customer_no \n"+
            " left join yd_customer_public t7 on t7.yd_customer_id = t6.yd_id \n"+
            " left join yd_risk_models t1 on t1.yd_model_id = t3.yd_risk_id \n"+
            " left join yd_risk_model_type t2 on t1.yd_type_id = t2.yd_id \n"+
            " left join yd_organization_map_po t8 on t8.yd_organcode = t3.yd_organ_code \n"+
            " left join yd_account_all_data a on t3.yd_account_no = a.yd_acct_no \n"+
            " where t2.yd_deleted = 0 \n";



    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:10:12
     * @description: ModelCountServiceImpl---findModelCountByCjrq
     */
    public static final String findModelCountByCjrqSql = " select distinct m.yd_name ,m.yd_model_id  " +
            "from yd_risk_models m where m.yd_model_id in " +
            "(select distinct t.yd_risk_table from YD_RISK_MODEL_COUNT t  \n" +
            " where 1=1 ";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:10:32
     * @description: RuleConfigServiceImp---findByModelId
     */
    public static final String findByModelIdSql = "select f.YD_ID,f.YD_FIELD_NAME from yd_risk_rule_field f where f.yd_id\n" +
            "not in (select t.yd_rule_id from YD_RISK_RULE_CONFIGURATION t where yd_deleted='0') and f.yd_deleted='0' and f.yd_model_id = '";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:10:37
     * @description: RuleConfigServiceImp---findRuleFieldByModelId
     */
    public static final String findRuleFieldByModelIdSql = " select  f.yd_field_name, f.yd_field,t.yd_condition,t.yd_value,t.yd_id  " +
            " from YD_RISK_RULE_CONFIGURATION t ,yd_risk_rule_field f where f.yd_id = t.yd_rule_id and f.YD_DELETED=0 and t.YD_DELETED = 0 ";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:10:42
     * @description: RuleConfigServiceImp---getModelRuleByModelId
     */
    public static final String getModelRuleByModelIdSql = "   select distinct rf.yd_model_id," +
            "   rf.yd_field_name," +
            "   rc.yd_con_and_val," +
            "   rc.yd_condition," +
            "   rc.yd_value," +
            "   rf.yd_field" +
            "   from YD_RISK_RULE_FIELD rf" +
            "  left join YD_RISK_RULE_CONFIGURATION rc" +
            "  on rf.yd_id = rc.yd_rule_id" +
            "   where 1=1 and rc.yd_deleted='0' ";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:07
     * @description: RiskApiServiceImpl---getCustomersInfo
     */
    public static final String getCustomersInfoSql = "select a.yd_customer_no   as customerNo,\n" +
            "       a.yd_acct_no       as acctNo,\n" +
            "       a.yd_organ_code     as bankCode,\n" +
            "       a.yd_work_area     as workArea,\n" +
            "       a.yd_reg_area_code as regAreaCode," +
            "      og.yd_pbc_code,a.yd_acct_name,a.yd_organ_code\n" +
            "  from yd_account_all_data a" +
            " left outer join yd_sys_organization og\n" +
            "  on a.yd_organ_code = og.yd_code\n" +
            "    where og.yd_pbc_code is not null ";

    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:10
     * @description: RiskApiServiceImpl---getPBCAddressInfo
     */
    public static final String getPBCAddressInfoSql = "select t.yd_ip, t.yd_account, t.yd_password,t1.yd_code  \n" +
            "  from YD_SYS_PBC_ACCOUNT t, yd_sys_organization t1\n" +
            " where t1.yd_id = t.yd_org_id\n" +
            "   and t1.yd_code = '";

    public static final String getPBCAddressInfoEndSql = "'\n" +
            "   and t.yd_enabled = 1\n" +
            "   and t1.yd_deleted = 0\n" +
            "   and t1.yd_deleted = 0";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:14
     * @description: RiskApiServiceImpl---getAllCustomersTel
     */
    public static final String getAllCustomersTelSql = " SELECT distinct t1.yd_finance_telephone,t1.yd_legal_telephone,t4.yd_operator_telephone,t2.yd_customer_no,t3.yd_acct_no,t3.yd_acct_name, t5.yd_organ_code FROM yd_customer_public t1 \n" +
            "   left join yd_customers_all t2 on t1.yd_customer_id= t2.yd_id\n" +
            "   left join yd_accounts_all t3 on t2.yd_customer_no=t3.yd_customer_no\n" +
            "   left join yd_account_public t4 on t3.yd_id = t4.yd_account_id \n" +
            " left join yd_account_all_data t5 on t3.yd_acct_no = t5.yd_acct_no \n"+
            "   where 1=1 and t4.yd_acct_type='jiben' \n" +
            "    and t3.yd_acct_no not in(\n" +
            "                 select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t3.yd_acct_no) ";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:16
     * @description: RiskApiServiceImpl---getAccountInfoFromAms
     */
    public static final String getAccountInfoFromAmsSql =
            "select t.yd_account_no,t.yd_customer_id," +
                    " t.yd_risk_desc,t.yd_risk_cnt,t.yd_risk_type,t.yd_risk_id" +
                    "  FROM yd_risk_middle_info t\n" +
                    "  where 1=1 ";



    public static final String getModelSearchExtendDtoSql1 = " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3. yd_account_name,t3.yd_organ_code,t5.yd_operator_telephone,t5.yd_operator_idcard_no,t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,t7.yd_legal_telephone, t7.yd_legal_idcard_no, t7.yd_legal_name)\n";
    public static final String getModelSearchExtendDtoSqlYUJING = " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3. yd_account_name,t3.yd_organ_code,t5.yd_operator_telephone,t5.yd_operator_idcard_no,t5.yd_operator_name,t7.yd_finance_idcard_no, t7.yd_finance_name,t7.yd_finance_telephone,a.yd_legal_telephone, a.yd_legal_idcard_no, a.yd_legal_name, t8.yd_dmp_organ_code,a.yd_reg_address,a.yd_acct_type,t3.yd_customer_id,a.yd_work_address)\n";

    public static final String getModelSearchExtendDtoSqlqudiaokouhao = " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t4.yd_acct_name,t3.yd_organ_code)\n";
    public static final String getModelSearchExtendDtoSqlqudiaokouhao11 = " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_organ_code,t4.yd_dmp_organ_code)\n";
    //group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status
    public static final String getModelSearchExtendDtoCountSql = " group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no\n";
    public static final String getModelSearchExtendDtoCountSql1 = " group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3. yd_account_name,t3.yd_organ_code\n";
    public static final String getModelSearchExtendDtoCountSqlYUJING = " group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3. yd_account_name,t3.yd_organ_code,t5.yd_operator_telephone,t5.yd_operator_idcard_no, t5.yd_operator_name,t7.yd_finance_idcard_no,t7.yd_finance_name,t7.yd_finance_telephone, a.yd_legal_telephone,a.yd_legal_idcard_no, a.yd_legal_name, t8.yd_dmp_organ_code,a.yd_reg_address,a.yd_acct_type,t3.yd_customer_id,a.yd_work_address)\n";

    public static final String getModelSearchExtendDtoCountSqlqudiaokouhao = " group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t4.yd_acct_name,t3.yd_organ_code\n";
    public static final String getModelSearchExtendDtoCountSqlqudiaokouhao11 = " group by  t1.yd_model_id,to_char(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status,t3.yd_risk_cnt,t3.yd_account_no,t3.yd_organ_code\n";


    //RiskHandleInfoServiceImpl---getRiskDataSearchDto
    public static final String getRiskDataSearchDtoTOCHAR1 = " and to_char(t1.yd_risk_date,'yyyyMMdd') >= ?5";
    public static final String getRiskDataSearchDtoTOCHAR2 = " and to_char(t1.yd_risk_date,'yyyyMMdd') <= ?6";
    public static final String getRiskDataSearchDtoTOCHAR3 = " and to_char(t1.yd_handle_date,'yyyyMMdd') >= ?5";
    public static final String getRiskDataSearchDtoTOCHAR4 = " and to_char(t1.yd_handle_date,'yyyyMMdd') <= ?6";

    //RiskDataServiceImpl---getModelSearchExtendDto
    public static final String getModelSearchExtendDtoTOCHAR1 = " and to_char(t3.yd_risk_date,'yyyyMMdd') >= ?4";
    public static final String getModelSearchExtendDtoTOCHAR2 = " and to_char(t3.yd_risk_date,'yyyyMMdd') <= ?5";


    /**
     * @author:yinjie
     * @date:2019/7/16
     * @time:14:24
     * @description: 搬运RiskSQLConstant中的SQL
     */
    public static final String RISK2002SQL = "select t.yd_id,\n" +
            "       t.yd_customer_id,\n" +
            "       a.yd_depositor_name,\n" +
            "       t.yd_risk_date,\n" +
            "       a.yd_acct_name,\n" +
            "       a.yd_acct_create_date,\n" +
            "       a.yd_bank_name,\n" +
            "       a.yd_acct_no,\n" +
            "       a.yd_account_status,\n" +
            "       t.yd_risk_desc,\n" +
            "       t.yd_risk_cnt,\n" +
            "       t.yd_risk_type,\n" +
            "       t.yd_risk_id,\n" +
            "       a.yd_reg_address,\n" +
            "       a.yd_work_address,\n" +
            "       a.yd_acct_type\n" +
            "  FROM yd_risk_record_info t\n" +
            "  left join yd_account_all_data a\n" +
            "  on t.yd_account_no = a.yd_acct_no\n" +
            " where 1 = 1\n";

    public static final String RISK3000SQL = "select distinct t.yd_id,\n" +
            "                a.yd_legal_idcard_no,\n" +
            "                a.yd_legal_name,\n" +
            "                t.yd_customer_id,\n" +
            "                a.yd_depositor_name,\n" +
            "                t.yd_risk_date,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_bank_name,\n" +
            "                a.yd_acct_no,\n" +
            "                a.yd_account_status,\n" +
            "                t.yd_risk_desc,\n" +
            "                t.yd_risk_cnt,\n" +
            "                t.yd_risk_type,\n" +
            "                t.yd_risk_id,\n" +
            "                a.yd_reg_address,\n" +
            "                a.yd_work_address,\n" +
            "                a.yd_acct_type\n" +
            "  FROM yd_risk_record_info t\n" +
            "  left join yd_account_all_data a\n" +
            "  on t.yd_account_no = a.yd_acct_no\n" +
            " where 1 = 1 \n";

    public static final String RISK4000SQL = " select distinct t.yd_id,\n" +
            "                dc.yd_legal_idcard_no,\n" +
            "                dc.yd_legal_name,\n" +
            "                t.yd_customer_id,\n" +
            "                dc.yd_depositor_name,\n" +
            "                t.yd_risk_date,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_bank_code,\n" +
            "                a.yd_bank_name,\n" +
            "                t.yd_account_no,\n" +
            "                a.yd_account_status,\n" +
            "                t.yd_risk_desc,\n" +
            "                t.yd_risk_cnt,\n" +
            "                t.yd_risk_type,\n" +
            "                t.yd_risk_id,\n" +
            "                ba.yd_bill_type,\n" +
            "                dc.yd_reg_address,\n" +
            "                dc.yd_work_address,\n" +
            "                a.yd_acct_type\n" +
            "  FROM yd_risk_check_info t\n" +
            "  left join yd_account_bills_all ba\n" +
            "    on t.yd_account_no = ba.yd_acct_no\n" +
            "  left outer join yd_accounts_all a\n" +
            "    on t.yd_account_no = a.yd_acct_no\n" +
            "  left outer join yd_customers_all c\n" +
            "    on a.yd_customer_no = c.yd_customer_no\n" +
            "  left outer join yd_customer_public dc\n" +
            "    on c.yd_id = dc.yd_customer_id\n" +
            " where 1 = 1";

    public static final String RISK2001SQL = " select distinct t.yd_id,\n" +
            "                t.yd_customer_id,\n" +
            "                dc.yd_depositor_name,\n" +
            "                t.yd_risk_date,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_bank_code,\n" +
            "                a.yd_bank_name,\n" +
            "                a.yd_acct_no,\n" +
            "                a.yd_account_status,\n" +
            "                t.yd_risk_desc,\n" +
            "                t.yd_risk_cnt,\n" +
            "                t.yd_risk_type,\n" +
            "                t.yd_risk_id,\n" +
            "                ba.yd_bill_type,\n" +
            "                dc.yd_reg_address,dc.yd_work_address,a.yd_acct_type\n" +
            "  FROM yd_risk_record_info t\n" +
            "  left join yd_account_bills_all ba\n" +
            "    on t.yd_account_no = ba.yd_acct_no\n" +
            "  left outer join yd_accounts_all a\n" +
            "    on t.yd_account_no = a.yd_acct_no\n" +
            "  left outer join yd_customers_all c\n" +
            "    on a.yd_customer_no = c.yd_customer_no\n" +
            "  left outer join yd_customer_public dc\n" +
            "    on c.yd_id = dc.yd_customer_id\n" +
            " where 1 = 1 ";

//    public static final String RISK2002SQL = RISK2001SQL;//使用RISK2001SQL

    public static final String RISK2003SQL =
            "select distinct t.yd_id,t.yd_risk_desc,\n" +
                    "                a.*\n" +
                    "  from yd_risk_record_info t\n" +
                    "  left join yd_account_all_data a\n" +
                    "    on t.yd_account_no = a.yd_acct_no\n" +
                    " where 1 = 1\n";

    public static final String RISK2004SQL = "";

    public static final String RISK2005SQL = "select distinct t.yd_id,t.yd_risk_desc, \n" +
            "                a.*\n" +
            "  from yd_risk_record_info t\n" +

            "  left join yd_account_all_data a\n" +
            "  on t.yd_account_no =a.yd_acct_no\n" +
            " where 1 = 1 \n";

    public static final String RISK2006SQL = RISK2005SQL;//使用RISK2005SQL

    public static final String RISK2008SQL = "select distinct t.yd_customer_id,c.yd_bank_name,\n" +
            "       d.yd_operator_telephone,\n" +
            "       d.yd_operator_idcard_no,\n" +
            "       d.yd_operator_name,\n" +
            "       b.yd_finance_idcard_no,\n" +
            "       b.yd_finance_name,\n" +
            "       b.yd_finance_telephone，\n" +
            "       b.yd_legal_telephone,\n" +
            "       b.yd_legal_idcard_no,\n" +
            "       b.yd_legal_name," +
            "       b.yd_depositor_name," +
            "       b.yd_reg_address," +
            "       b.yd_work_address,\n" +
            "       t.yd_risk_point,c.yd_acct_type,t.yd_risk_desc" +
            "  from yd_risk_record_info t\n" +
            "  left join yd_customers_all a\n" +
            "    on t.yd_customer_id = a.yd_customer_no\n" +
            "  left outer join yd_customer_public b\n" +
            "    on a.yd_id = b.yd_customer_id\n" +
            "  left outer join yd_accounts_all c\n" +
            "    on a.yd_customer_no = c.yd_customer_no\n" +
            "  left outer join yd_account_public d\n" +
            "    on c.yd_id = d.yd_account_id\n" +
            " where 1 = 1\n " ;
    public static final String RISK2007SQL = "\n" +
            "\n" +
            "   select t1.yd_customer_id,  t4.yd_acct_create_date,\n" +
            "                        t4.yd_bank_name ,\n" +
            "    t5.yd_operator_telephone,\n" +
            "       t5.yd_operator_idcard_no,\n" +
            "       t5.yd_operator_name,\n" +
            "       t3.yd_finance_idcard_no,\n" +
            "       t3.yd_finance_name,\n" +
            "       t3.yd_finance_telephone，\n" +
            "       t3.yd_legal_telephone,\n" +
            "       t3.yd_legal_idcard_no,\n" +
            "       t3.yd_legal_name,       \n" +
            "       t3.yd_depositor_name,      \n" +
            "       t3.yd_reg_address,       \n" +
            "       t3.yd_work_address,\n" +
            "       t1.yd_risk_point,t4.yd_acct_type,t1.yd_risk_desc\n" +
            "   from yd_risk_record_info t1 \n" +
            "   left join yd_customers_all t2\n" +
            "   on t1.yd_customer_id = t2.yd_customer_no\n" +
            "   left join yd_customer_public t3 \n" +
            "   on  t2.yd_id = t3.yd_customer_id\n" +
            "   left outer join yd_accounts_all t4\n" +
            "    on t2.yd_customer_no = t4.yd_customer_no\n" +
            "      left outer join yd_account_public t5\n" +
            "    on t4.yd_id = t5.yd_account_id " +
            " where 1=1 and t5.yd_acct_type = 'jiben'";

    public static final String RISK1001And1002SQL = "select distinct t1.yd_risk_date,t1.yd_risk_date,t3.yd_acct_no," +
            "t3.yd_acct_name,t2.yd_flag,t2.yd_start_date,\n" +
            "t2.yd_open_date,t2.yd_amount,t2.yd_serial_id,\n" +
            "t2.yd_trade_date,t2.yd_trade_time,t3.yd_acct_type\n" +
            " from yd_risk_trade_info t1\n" +
            "left join yd_risk_core_info t2 on \n" +
            "t1.yd_account_no = t2.yd_account_no\n" +
            "left join yd_accounts_all t3\n" +
            "on t2.yd_account_no = t3.yd_acct_no\n" +
            "left outer join yd_account_public t4 on \n" +
            "t3.yd_id = t4.yd_account_id\n" +
            "where 1=1\n" +
            "and t4.yd_acct_type='jiben'";

    public static final String queryRegisterRiskInfoSQL = "select rownum as ordNum,t1.yd_id,  \n" +
            "                     to_char(t1.yd_risk_date,'yyyy-MM-dd'),\n" +
            "                     m.yd_Name as modelName,\n" +
            "                     t2.yd_acct_no,\n" +
            "                     t2.yd_acct_name,\n" +
            "                     t2.yd_acct_create_date,\n" +
            "                      t1.yd_risk_desc,\n" +
            "                      t1.yd_risk_id,\n" +
            "                      t1.yd_risk_point,\n" +
            "                      og.yd_name,\n" +
            "                      t3.yd_acct_type,\n" +
            "                      t1.yd_status," +
            "                      t1.yd_handle_mode," +
            "                      t1.yd_handle_date," +
            "                      su.yd_cname,t1.yd_dubious_reason\n" +
            "                from  yd_risk_register_info t1\n" +
            "                left join yd_accounts_all t2\n" +
            "                on t1.yd_account_no = t2.yd_acct_no\n" +
            "                left outer join yd_account_public t3\n" +
            "                on t2.yd_id = t3.yd_account_id\n" +
            "                left join yd_sys_organization og\n" +
            "                 on t3.yd_organ_full_id = og.yd_full_id" +
            "               left join yd_sys_user su \n" +
            "                on t1.yd_handler = su.yd_id\n" +
            "                left join yd_risk_models m\n" +
            "                 on t1.yd_risk_id = m.yd_model_id \n" +
            "                 where m.yd_deleted=0 ";

    public static final String queryregisterRiskInfoCountSQL = "select  1 from \n" +
            "                yd_risk_register_info t1\n" +
            "                left join yd_accounts_all t2\n" +
            "                on t1.yd_account_no = t2.yd_acct_no\n" +
            "                left outer join yd_account_public t3\n" +
            "                on t2.yd_id = t3.yd_account_id\n" +
            "                left join yd_sys_organization og\n" +
            "                 on t3.yd_organ_full_id = og.yd_full_id\n" +
            "                left join yd_risk_models m\n" +
            "                 on t1.yd_risk_id = m.yd_model_id \n" +
            "                 where m.yd_deleted=0";

    public static final String querytodoRiskInfoSQL =
            "                   select   rownum as ordNum, t1.yd_id,  \n" +
                    "                     to_char(t1.yd_risk_date,'yyyy-MM-dd'),\n" +
                    "                     m.yd_Name as modelName,\n" +
                    "                     t2.yd_acct_no,\n" +
                    "                     t2.yd_acct_name,\n" +
                    "                     t2.yd_acct_create_date,\n" +
                    "                      t1.yd_risk_desc,\n" +
                    "                      t1.yd_risk_id,\n" +
                    "                      t1.yd_risk_point,\n" +
                    "                      og.yd_name,\n" +
                    "                      t3.yd_acct_type,\n" +
                    "                      t1.yd_status\n" +
                    "                from  yd_risk_handle_info t1\n" +
                    "                left join yd_accounts_all t2\n" +
                    "                on t1.yd_account_no = t2.yd_acct_no\n" +
                    "                left outer join yd_account_public t3\n" +
                    "                on t2.yd_id = t3.yd_account_id\n" +
                    "                left join yd_sys_organization og\n" +
                    "                 on t3.yd_organ_full_id = og.yd_full_id" +

                    "                left join yd_risk_models m\n" +
                    "                 on t1.yd_risk_id = m.yd_model_id \n" +
                    "                 where m.yd_deleted=0 ";

    public static final String querytodoRiskInfoCountSQL = "" +
            "               select 1 \n" +
            "                from yd_risk_handle_info t1\n" +
            "                left join yd_accounts_all t2\n" +
            "                on t1.yd_account_no = t2.yd_acct_no\n" +
            "                left outer join yd_account_public t3\n" +
            "                on t2.yd_id = t3.yd_account_id\n" +
            "                left join yd_sys_organization og\n" +
            "                 on t3.yd_organ_full_id = og.yd_full_id\n" +
            "                left join yd_risk_models m\n" +
            "                 on t1.yd_risk_id = m.yd_model_id \n" +
            "                 where m.yd_deleted=0 ";

    public static final String getSqlProc = "select b.YD_CON_AND_VAL from YD_RISK_RULE_FIELD a\n" +
            "  left outer join YD_RISK_RULE_CONFIGURATION b on a.yd_id=b.yd_rule_id\n" +
            "  where a.yd_deleted='0' and b.yd_deleted='0'\n";

    public static final String getAccountChangeReportRule = "to_date( yd_acct_Create_Date, 'yyyyMMdd' )";

    public static final String getModelFieldsSql = " select \n" +
            "a.column_name ,\n" +
            "b.comments \n" +
            " FROM user_tab_columns a, user_col_comments b";
    public static final String getHighTradeInfo = "SELECT\n" +
            "\tc.yd_customer_no,\n" +
            "\tc.yd_legal_name,\n" +
            "\tc.yd_legal_idcard_no,\n" +
            "\tc.yd_legal_idcard_type,\n" +
            "\tc.yd_depositor_name,\n" +
            "\tc.yd_depositorcard_no,\n" +
            "\tc.yd_depositorcard_type,\n" +
            "\tTO_CHAR ( t.yd_risk_date, 'yyyy-mm-dd' ) yd_risk_date,\n" +
            "\tt.yd_risk_desc,\n" +
            "\tt.yd_risk_id,\n" +
            "\tt.yd_account_no \n" +
            "FROM\n" +
            "\tyd_risk_trade_info t\n" +
//            "\tJOIN yd_accounts_all a ON t.yd_account_no = a.yd_acct_no\n" +
            "\tJOIN yd_risk_high_customer c ON t.yd_customer_id = c.yd_customer_no \n" +
            "WHERE\n" +
            "\t1 = 1";

    public static final String getHighTradeModelInfo = "SELECT\n" +
            "\tt.yd_customer_id,\n" +
            "\tc.yd_legal_name,\n" +
            "\tc.yd_legal_idcard_no,\n" +
            "\tc.yd_legal_idcard_type,\n" +
            "\tca.yd_depositor_name,\n" +
            "\trc.yd_company_certificate_no depositorcard_no,\n" +
            "\tca.yd_depositor_type,\n" +
            "\tTO_CHAR (t.yd_risk_date, 'yyyy-mm-dd') yd_risk_date,\n" +
            "\tt.yd_risk_desc,\n" +
            "\tt.yd_risk_id,\n" +
            "\tt.yd_account_no\n" +
            "FROM\n" +
            "\tyd_risk_trade_info t\n" +
            "LEFT JOIN yd_customers_all ca ON t.yd_customer_id = ca.yd_customer_no\n" +
            "LEFT JOIN yd_customer_public c ON ca.yd_id = c.yd_customer_id\n" +
            "LEFT JOIN yd_relate_company rc ON c.yd_id = rc.yd_customer_public_id\n" +
            "WHERE\n" +
            "\t1 = 1";

    public static final String getHighRiskList = "SELECT\n" +
            "\tROWNUM AS ordNum,\n" +
            "\tT .yd_id,\n" +
            "\tT .yd_customer_no,\n" +
            "\tT .YD_DEPOSITOR_NAME,\n" +
            "\tA .yd_account_management yd_status,\n" +
            "\tT .yd_account_no,\n" +
            "\tA .YD_ACCT_TYPE,\n" +
            "\tA .yd_bank_name,\n" +
            "\tA .yd_acct_create_date,\n" +
            "\tT .yd_depositorcard_no,\n" +
            "\tT .yd_risk_desc\n" +
            "FROM\n" +
            "\tyd_risk_high_info T\n" +
            "LEFT JOIN yd_accounts_all A ON T .yd_account_no = A .yd_acct_no\n" +
            "WHERE\n" +
            "\t1 = 1";

    public static final String getHighRisk1 = "SELECT\n" +
            "\tROWNUM AS ordNum,\n" +
            "\tT .yd_customer_no,\n" +
            "\tdc.YD_DEPOSITOR_NAME,\n" +
            "\tdc.yd_legal_name,\n" +
            "\tc.YD_DEPOSITOR_type yd_depositorcard_type,\n" +
            "\trc.yd_company_certificate_no yd_depositorcard_no,\n" +
            "\tdc.yd_legal_idcard_type,\n" +
            "\tdc.yd_legal_idcard_no,\n" +
            "\tT .yd_risk_desc,\n" +
            "\t'0' AS status,\n" +
            "\tT .yd_risk_date,\n" +
            "\tT .yd_id,\n" +
            "\tT .sta\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT DISTINCT\n" +
            "\t\t\tMAX (T .yd_id) yd_id,\n" +
            "\t\t\tT .yd_customer_no,\n" +
            "\t\t\tMAX (T .YD_REASON) yd_risk_date,\n" +
            "\t\t\tlistagg (M .yd_name, '-,') WITHIN GROUP (ORDER BY M .yd_name) AS yd_risk_desc,\n" +
            "\t\t\tlistagg (\n" +
            "\t\t\t\tA .yd_account_management,\n" +
            "\t\t\t\t','\n" +
            "\t\t\t) WITHIN GROUP (\n" +
            "\n" +
            "\t\t\t\tORDER BY\n" +
            "\t\t\t\t\tA .yd_account_management\n" +
            "\t\t\t) AS sta\n" +
            "\t\tFROM\n" +
            "\t\t\tyd_risk_high_info T\n" +
            "\t\tLEFT JOIN yd_risk_models M ON T .yd_risk_id = M .yd_model_id\n" +
            "\t\tLEFT JOIN yd_accounts_all A ON T .yd_customer_no = A .yd_customer_no\n" +
            "\t\tWHERE\n" +
            "\t\t\t1 = 1";

    public static final String getHighRisk2 = "GROUP BY\n" +
            "\t\t\tT .yd_customer_no\n" +
            "\t) T\n" +
            "LEFT OUTER JOIN yd_customers_all c ON T .yd_customer_no = c.yd_customer_no\n" +
            "LEFT OUTER JOIN yd_customer_public dc ON c.yd_id = dc.yd_customer_id\n" +
            "LEFT JOIN yd_relate_company rc ON dc.yd_id = rc.yd_customer_public_id\n" +
            "WHERE\n" +
            "\t1 = 1";

    public static final String getHighRiskCountSql = "select count(1) from(select count(1),\n" +
            "\t\tlistagg (M .yd_name, '-,') WITHIN GROUP (ORDER BY M .yd_name) AS yd_risk_desc  from yd_risk_high_info t LEFT JOIN yd_risk_models m ON t .yd_risk_id=m.yd_model_id \n" +
            "\tLEFT JOIN yd_risk_high_api p ON t.yd_risk_id = p.yd_api_no LEFT JOIN yd_accounts_all a ON t.yd_account_no = a.yd_acct_no where 1=1";


    public static final String queryRiskidsql = "select yd_risk_id,max(yd_risk_desc)  from yd_risk_record_info GROUP BY yd_risk_id";
    public static final String queryDealRiskidsql = "select yd_risk_id,max(yd_risk_desc)  from yd_risk_trade_info GROUP BY yd_risk_id";

    public static final String queryTradeRiskDataByAcctNoSql = "select rownum as ordNum , yd_acct_no,yd_acct_name, yd_bank_name,yd_acct_type,\n" +
            "yd_account_status,yd_acct_create_date,yd_cancel_date,num,yd_risk_date from \n" +
            " (select distinct a.yd_acct_no,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_bank_name,\n" +
            "                a.yd_acct_type,\n" +
            "                a.yd_account_status,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_cancel_date,\n" +
            "                table1.num,\n" +
            "                max(t.yd_risk_date) as yd_risk_date\n" +
            "  from yd_accounts_all a\n" +
            "  left join yd_risk_trade_info t\n" +
            "    on a.yd_acct_no = t.yd_account_no\n" +
            "  left join (select att.yd_account_no, count(1) num\n" +
            "               from (select at.yd_account_no, at.yd_risk_id\n" +
            "                       from yd_risk_trade_info at\n" +
            "                      group by at.yd_account_no, at.yd_risk_id) att\n" +
            "              group by att.yd_account_no) table1\n" +
            "    on a.yd_acct_no = table1.yd_account_no where 1=1 \n";

    public static final String queryTradeRiskDataCountByAcctNoSql = "select rownum as ordNum , yd_acct_no,yd_acct_name, yd_bank_name,yd_acct_type,\n" +
            "yd_account_status,yd_acct_create_date,yd_cancel_date,num from \n" +
            " (select distinct a.yd_acct_no,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_bank_name,\n" +
            "                a.yd_acct_type,\n" +
            "                a.yd_account_status,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_cancel_date,\n" +
            "                table1.num\n" +
            "  from yd_accounts_all a\n" +
            "  left join yd_risk_trade_info t\n" +
            "    on a.yd_acct_no = t.yd_account_no\n" +
            "  left join (select att.yd_account_no, count(1) num\n" +
            "               from (select at.yd_account_no, at.yd_risk_id\n" +
            "                       from yd_risk_trade_info at\n" +
            "                      group by at.yd_account_no, at.yd_risk_id) att\n" +
            "              group by att.yd_account_no) table1\n" +
            "    on a.yd_acct_no = table1.yd_account_no where 1=1 \n";
    public static final String queryRiskStaticDataByAcctNoSql = "select rownum as ordNum , yd_acct_no,yd_acct_name, yd_bank_name,yd_acct_type,\n" +
            "yd_account_status,yd_acct_create_date,yd_cancel_date,num,yd_risk_date from \n" +
            " (select distinct a.yd_acct_no,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_bank_name,\n" +
            "                a.yd_acct_type,\n" +
            "                a.yd_account_status,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_cancel_date,\n" +
            "                table1.num,\n" +
            "                max(t.yd_risk_date) as yd_risk_date\n" +
            "  from yd_accounts_all a\n" +
            "  left join yd_risk_record_info t\n" +
            "    on a.yd_acct_no = t.yd_account_no\n" +
            "  left join (select att.yd_account_no, count(1) num\n" +
            "               from (select at.yd_account_no, at.yd_risk_id\n" +
            "                       from yd_risk_record_info at\n" +
            "                      group by at.yd_account_no, at.yd_risk_id) att\n" +
            "              group by att.yd_account_no) table1\n" +
            "    on a.yd_acct_no = table1.yd_account_no where 1=1 \n";
    public static final String queryRiskStaticDataCountByAcctNoSql = "select rownum as ordNum , yd_acct_no,yd_acct_name, yd_bank_name,yd_acct_type,\n" +
            "yd_account_status,yd_acct_create_date,yd_cancel_date,num,yd_risk_date from \n" +
            " (select distinct a.yd_acct_no,\n" +
            "                a.yd_acct_name,\n" +
            "                a.yd_bank_name,\n" +
            "                a.yd_acct_type,\n" +
            "                a.yd_account_status,\n" +
            "                a.yd_acct_create_date,\n" +
            "                a.yd_cancel_date,\n" +
            "                table1.num,\n" +
            "                max(t.yd_risk_date) as yd_risk_date\n" +
            "  from yd_accounts_all a\n" +
            "  left join yd_risk_record_info t\n" +
            "    on a.yd_acct_no = t.yd_account_no\n" +
            "  left join (select att.yd_account_no, count(1) num\n" +
            "               from (select at.yd_account_no, at.yd_risk_id\n" +
            "                       from yd_risk_record_info at\n" +
            "                      group by at.yd_account_no, at.yd_risk_id) att\n" +
            "              group by att.yd_account_no) table1\n" +
            "    on a.yd_acct_no = table1.yd_account_no where 1=1 \n";
}
