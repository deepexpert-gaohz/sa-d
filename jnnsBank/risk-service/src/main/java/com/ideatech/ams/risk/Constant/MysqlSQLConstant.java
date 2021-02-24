package com.ideatech.ams.risk.Constant;

import com.ideatech.ams.risk.util.RiskUtil;

/**
 * @Author: yinjie
 * @Date: 2019/7/12 11:11
 * @description Mysql的Sql语句类
 */
public abstract class MysqlSQLConstant {


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:10:45
     * @description: ModelServiceImpl---findModel
     */
    public static final String findModelSql = "SELECT\n" +
            "\tm.YD_ID,\n" +
            "\tm.YD_MODEL_ID,\n" +
            "\tm.YD_NAME,\n" +
            "\tm.YD_MDESC,\n" +
            "\trl.YD_LEVEL_NAME,\n" +
            "\trt.YD_TYPE_NAME," +

            "\tCONCAT(\n" +
            "\t\tGROUP_CONCAT(rr.yd_field_name,rc.yd_con_and_val)\n" +
            "\t\t)\n" +
            "\t AS ruleName,\n" +
            "\tm.YD_STATUS,\n" +
            "\trt.YD_ID as typeId\n" +
            "FROM\n" +
            "\tYD_RISK_MODELS m\n" +
            "LEFT JOIN YD_RISK_MODEL_LEVEL rl ON m.YD_LEVEL_ID = rl.YD_ID\n" +
            "AND rl.YD_DELETED = 0\n" +
            "LEFT JOIN YD_RISK_MODEL_TYPE rt ON m.YD_TYPE_ID = rt.YD_ID\n" +
            "AND rt.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_FIELD rr ON m.yd_model_id = rr.yd_model_id\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_CONFIGURATION rc ON rr.yd_id = rc.yd_rule_id\n" +
            "AND rc.yd_deleted = '0'\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "WHERE\n" +
            "\tm.YD_DELETED = '0'\n";

    public static final String findModelEndSql = "GROUP BY\n" +
            "\tm.YD_ID,\n" +
            "\tm.YD_MODEL_ID,\n" +
            "\tm.YD_NAME,\n" +
            "\tm.YD_MDESC,\n" +
            "\trl.YD_LEVEL_NAME,\n" +
            "\trt.YD_TYPE_NAME,\n" +
            "\tm.YD_STATUS,rt.YD_ID\n" +
            "ORDER BY\n" +
            "\tm.YD_ID";

    public static final String findModelCountSql = "SELECT COUNT(*) FROM\n" +
            "(SELECT\n" +
            "\tm.YD_ID,\n" +
            "\tm.YD_MODEL_ID,\n" +
            "\tm.YD_NAME,\n" +
            "\tm.YD_MDESC,\n" +
            "\trl.YD_LEVEL_NAME,\n" +
            "\trt.YD_TYPE_NAME,\n" +
            "\tCONCAT(\n" +
            "\t\tGROUP_CONCAT(rr.yd_field_name,rc.yd_con_and_val)\n" +
            "\t\t)\n" +
            "\t AS ruleName,\n" +
            "\tm.YD_STATUS\n" +
            "FROM\n" +
            "\tYD_RISK_MODELS m\n" +
            "LEFT JOIN YD_RISK_MODEL_LEVEL rl ON m.YD_LEVEL_ID = rl.YD_ID\n" +
            "AND rl.YD_DELETED = 0\n" +
            "LEFT JOIN YD_RISK_MODEL_TYPE rt ON m.YD_TYPE_ID = rt.YD_ID\n" +
            "AND rt.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_FIELD rr ON m.yd_model_id = rr.yd_model_id\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_CONFIGURATION rc ON rr.yd_id = rc.yd_rule_id\n" +
            "AND rc.yd_deleted = '0'\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "WHERE\n" +
            "\tm.YD_DELETED = '0'\n";

    public static final String findModelEndCountSql = "GROUP BY\n" +
            "\tm.YD_ID,\n" +
            "\tm.YD_MODEL_ID,\n" +
            "\tm.YD_NAME,\n" +
            "\tm.YD_MDESC,\n" +
            "\trl.YD_LEVEL_NAME,\n" +
            "\trt.YD_TYPE_NAME,\n" +
            "\tm.YD_STATUS)AS a";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:03
     * @description: 查询交易监测类型的模型名称
     * ModelServiceImpl---findTypeNameAsRisk
     */
    public static final String findTypeNameAsRiskSql = "SELECT\n" +
            "\tm.YD_ID,\n" +
            "\tm.YD_MODEL_ID,\n" +
            "\tm.YD_NAME,\n" +
            "\tm.YD_MDESC,\n" +
            "\trl.YD_LEVEL_NAME,\n" +
            "\trt.YD_TYPE_NAME,\n" +
            "\tCONCAT(\n" +
            "\t\tGROUP_CONCAT(\n" +
            "\t\t\trr.yd_field_name,rc.yd_con_and_val\n" +
            "\t\t)\n" +
            "\t) AS ruleName,\n" +
            "\tm.YD_STATUS\n" +
            "FROM\n" +
            "\tYD_RISK_MODELS m\n" +
            "LEFT JOIN YD_RISK_MODEL_LEVEL rl ON m.YD_LEVEL_ID = rl.YD_ID\n" +
            "AND rl.YD_DELETED = 0\n" +
            "LEFT JOIN YD_RISK_MODEL_TYPE rt ON m.YD_TYPE_ID = rt.YD_ID\n" +
            "AND rt.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_FIELD rr ON m.yd_model_id = rr.yd_model_id\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_CONFIGURATION rc ON rr.yd_id = rc.yd_rule_id\n" +
            "AND rc.yd_deleted = '0'\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "WHERE\n" +
            "\tm.YD_DELETED = '0'\n" +
            " and rt.yd_id = '1001'\n";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:06
     * @description: 查询开户变更类型的模型名称
     * ModelServiceImpl---findTypeNameAsChange
     */
    public static final String findTypeNameAsChangeSql = "SELECT\n" +
            "\tm.YD_ID,\n" +
            "\tm.YD_MODEL_ID,\n" +
            "\tm.YD_NAME,\n" +
            "\tm.YD_MDESC,\n" +
            "\trl.YD_LEVEL_NAME,\n" +
            "\trt.YD_TYPE_NAME,\n" +
            "\tCONCAT(\n" +
            "\t\tGROUP_CONCAT(\n" +
            "\t\t\trr.yd_field_name,rc.yd_con_and_val\n" +
            "\t\t)\n" +
            "\t) AS ruleName,\n" +
            "\tm.YD_STATUS\n" +
            "FROM\n" +
            "\tYD_RISK_MODELS m\n" +
            "LEFT JOIN YD_RISK_MODEL_LEVEL rl ON m.YD_LEVEL_ID = rl.YD_ID\n" +
            "AND rl.YD_DELETED = 0\n" +
            "LEFT JOIN YD_RISK_MODEL_TYPE rt ON m.YD_TYPE_ID = rt.YD_ID\n" +
            "AND rt.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_FIELD rr ON m.yd_model_id = rr.yd_model_id\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "LEFT JOIN YD_RISK_RULE_CONFIGURATION rc ON rr.yd_id = rc.yd_rule_id\n" +
            "AND rc.yd_deleted = '0'\n" +
            "AND rr.YD_DELETED = '0'\n" +
            "WHERE\n" +
            "\tm.YD_DELETED = '0'\n" +
            "and rt.yd_id = '1002'\n";


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


    public static final String queryModelCountEndSql = " order by t.yd_risk_date desc";


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

    //使用类中的拼接方式
    public static final String queryModelCountFile2EndSql1 = "AND a.yd_deleted = '0'\n" +
            "AND o12.yd_deleted = '0'";

    public static final String queryModelCountFile2EndSql2 = " GROUP BY a.yd_id, T.yd_RISK_ID,A.yd_NAME,o12.yd_type_name order by T.yd_RISK_ID ";

    public static final String queryModelCountFile2CountSql = "SELECT count(*)\n" +
            "              FROM yd_risk_tjjg T\n" +
            "              LEFT JOIN yd_risk_models A\n" +
            "                ON T.yd_RISK_ID = A.yd_MODEL_ID\n" +
            "              LEFT JOIN yd_sys_organization O\n" +
            "               ON O.yd_code = T.yd_ORG_ID\n" +
            "             LEFT JOIN yd_risk_model_type o12\n" +
            "                ON o12.yd_id = a.yd_type_id where 1=1 ";

    public static final String queryModelCountFile2CountSql1 = " GROUP BY a.yd_id, T.yd_RISK_ID,A.yd_NAME,o12.yd_type_name order by T.yd_RISK_ID ";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:11:55
     * @description: ModelCountFileServiceImpl---getAllModelTypeList
     */
    public static final String getAllModelTypeListSql = "SELECT DISTINCT\n" +
            "\tM.yd_MODEL_ID,\n" +
            "\tT.yd_type_NAME\n" +
            "FROM\n" +
            "\tyd_risk_models M\n" +
            "LEFT JOIN yd_risk_model_type T ON M.yd_TYPE_ID = T.yd_ID\n" +
            "WHERE\n" +
            "\tM.yd_deleted = '0'\n" +
            "AND T.yd_deleted = '0'";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:12:02
     * @description: ModelCountFileServiceImpl---findModelExtendList
     */
    public static final String findModelExtendListSql = "select * from yd_risk_models where 1=1 ";

    //这个？在mysql里不能直接跑，所以暂时没改
    public static final String findModelExtendListEndSql = " AND yd_model_id like ?1 ";


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


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:14:55
     * @description: ModelFlowConfigerServiceImpl---searchData
     */
    public static final String searchDataSql = " SELECT\n" +
            "\t mfc.YD_ID,\n" +
            "\t m.YD_MODEL_ID,\n" +
            "\t m.YD_NAME,\n" +
            "\t rp.KEY_,\n" +
            "\t rp.NAME_,\n" +
            "\t mfc.YD_IS_AUTO,\n" +
            "\t mfc.YD_LAST_UPDATE_DATE \n" +
            " FROM\n" +
            "\t YD_MODEL_FLOW_CONFIGER mfc \n" +
            "\t left join YD_RISK_MODELS m on mfc.YD_MODEL_ID = m.YD_ID \n" +
            "\t right join (select key_ as key_, max(version_) value, name_ from ACT_RE_PROCDEF group by key_, name_) rp on mfc.yd_flow_key=rp.key_  \n " +
            " WHERE 1=1\n" +
            "  and mfc.YD_DELETED = '0'";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:16:18
     * @description: ModelFlowConfigerServiceImpl---getModels
     */
    public static final String getModelsSql = "select  ms.yd_id,ms.yd_name from yd_risk_models ms " +
            " where ms.yd_id  not in (select yd_model_id from yd_model_flow_configer c where c.yd_deleted='0') and ms.yd_deleted='0'";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:16:41
     * @description: ModelFlowConfigerServiceImpl---findFlowkeyAndNameByModelId
     */
    public static final String findFlowkeyAndNameByModelIdSql = "select t.id_ as yd_flow_key ,t.name_,t.key_ from \n" +
            "  (select t1.id_,t1.name_,t1.key_ from ACT_RE_PROCDEF t1 , act_re_deployment t2 \n" +
            "  where t1.deployment_id_ = t2.id_ and t1.key_='";

    public static final String findFlowkeyAndNameByModelIdEndSql = "'  order by t2.deploy_time_ DESC) t limit 1  ";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:18:01
     * @description: RiskDataServiceImpl---queryCheckAcctData
     */
    //直接在mysql中使用是没问题的，但是在hibernate下执行会报：Space is not allowed after parameter prefix ':'
    //原因是因为语句中有:= 需要转义\\:=
    //sql条件中有Oracle的to_char()函数，在mysql中需要改成DATE_FORMAT()函数
    public static final String queryCheckAcctDataSql = "SELECT\n" +
            "\t@rowno\\:=@rowno+1 as rownum,\n" +
            "\tyd_model_id,\n" +
            "\tyd_risk_date,\n" +
            "\tyd_name,\n" +
            "\tyd_risk_desc,\n" +
            "\tyd_type_name,\n" +
            "\tyd_risk_point,\n" +
            "\tyd_status\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT DISTINCT\n" +
            "\t\t\tt1.yd_model_id,\n" +
            "\t\t\tDATE_FORMAT (\n" +
            "\t\t\t\tt3.yd_risk_date,\n" +
            "\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t) AS yd_risk_date,\n" +
            "\t\t\tt1.yd_name,\n" +
            "\t\t\tt3.yd_risk_desc,\n" +
            "\t\t\tt2.yd_type_name,\n" +
            "\t\t\tt3.yd_risk_point,\n" +
            "\t\t\tt3.yd_status\n" +
            "\t\tFROM\n" +
            "\t\t\tyd_risk_models t1\n" +
            "\t\tLEFT JOIN yd_risk_model_type t2 ON t1.yd_type_id = t2.yd_id\n" +
            "\t\tRIGHT JOIN yd_risk_check_info t3 ON t1.yd_model_id = t3.yd_risk_id\n" +
            "\t\tWHERE\n" +
            "\t\t\tt2.yd_deleted = 0";

    public static final String queryCheckAcctDataCountSql = "select count(1)\n" +
            "  from yd_risk_models t1\n" +
            "  left join yd_risk_model_type t2\n" +
            "    on t1.yd_type_id = t2.yd_id\n" +
            " right join yd_risk_check_info t3\n" +
            "    on t1.yd_model_id = t3.yd_risk_id\n" +
            " where t2.yd_deleted = 0\n";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:18:56
     * @description: RiskDataServiceImpl---queryTradeRiskData
     */
    public static final String queryTradeRiskDataSql = "SELECT\n" +
            "\t@rowno\\:=@rowno+1 as rownum,\n" +
            "\tyd_model_id,\n" +
            "\tyd_risk_date,\n" +
            "\tyd_name,\n" +
            "\tyd_risk_desc,\n" +
            "\tyd_type_name,\n" +
            "\tyd_risk_point,\n" +
            "\tyd_status\n" +
            "FROM\n" +
            "\t(select @rowno\\:=0) r,(\n" +
            "\t\tSELECT DISTINCT\n" +
            "\t\t\tt1.yd_model_id,\n" +
            "\t\t\tDATE_FORMAT(\n" +
            "\t\t\t\tt3.yd_risk_date,\n" +
            "\t\t\t\t'%Y-%m-%d'\n" +
            "\t\t\t) AS yd_risk_date,\n" +
            "\t\t\tt1.yd_name,\n" +
            "\t\t\tt3.yd_risk_desc,\n" +
            "\t\t\tt2.yd_type_name,\n" +
            "\t\t\tt3.yd_risk_point,\n" +
            "\t\t\tt3.yd_status\n" +
            "\t\tFROM\n" +
            "\t\t\tyd_risk_models t1\n" +
            "\t\tLEFT JOIN yd_risk_model_type t2 ON t1.yd_type_id = t2.yd_id\n" +
            "\t\tRIGHT JOIN yd_risk_trade_info t3 ON t1.yd_model_id = t3.yd_risk_id\n" +
            "\t\tWHERE\n" +
            "\t\t\tt2.yd_deleted = 0";

    public static final String queryTradeRiskDataExSql = "SELECT\n" +
            "\tyd_model_id,\n" +
            "\tyd_risk_date,\n" +
            "\tyd_name,\n" +
            "\tyd_risk_desc,\n" +
            "\tyd_type_name,\n" +
            "\tyd_risk_point\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT DISTINCT\n" +
            "\t\t\tt1.yd_model_id,\n" +
            "\t\t\tDATE_FORMAT(\n" +
            "\t\t\t\tt3.yd_risk_date,\n" +
            "\t\t\t\t'%Y-%m-%d'\n" +
            "\t\t\t) AS yd_risk_date,\n" +
            "\t\t\tt1.yd_name,\n" +
            "\t\t\tt3.yd_risk_desc,\n" +
            "\t\t\tt2.yd_type_name,\n" +
            "\t\t\tt3.yd_risk_point,\n" +
            "\t\t\tt3.yd_status\n" +
            "\t\tFROM\n" +
            "\t\t\tyd_risk_models t1\n" +
            "\t\tLEFT JOIN yd_risk_model_type t2 ON t1.yd_type_id = t2.yd_id\n" +
            "\t\tRIGHT JOIN yd_risk_trade_info t3 ON t1.yd_model_id = t3.yd_risk_id\n" +
            "\t\tWHERE\n" +
            "\t\t\tt2.yd_deleted = 0";

    public static final String queryTradeRiskDataCountSql = "select count(1)\n" +
            "                             from yd_risk_models t1\n" +
            "                              left join yd_risk_model_type t2\n" +
            "                                on t1.yd_type_id = t2.yd_id\n" +
            "                             right join yd_risk_trade_info t3\n" +
            "                                on t1.yd_model_id = t3.yd_risk_id\n" +
            "                             where t2.yd_deleted = 0";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:19:00
     * @description: RiskDataServiceImpl---queryRiskStaticData
     */
    public static final String queryRiskStaticDataSql = "select @rowno\\:=@rowno+1 as rownum, yd_model_id,yd_risk_date,yd_name,yd_risk_desc,yd_type_name,yd_risk_point,yd_status from\n" +
            "            (select @rowno\\:=0) r,(select distinct  t1.yd_model_id,DATE_FORMAT(t3.yd_risk_date,'%Y-%m-%d') as yd_risk_date ,\n" +
            "            t1.yd_name,t3.yd_risk_desc,t2.yd_type_name,t3.yd_risk_point,t3.yd_status\n" +
            "              from yd_risk_models t1\n" +
            "              left join yd_risk_model_type t2\n" +
            "                on t1.yd_type_id = t2.yd_id\n" +
            "             right join yd_risk_record_info t3\n" +
            "                on t1.yd_model_id = t3.yd_risk_id\n" +
            "             where t2.yd_deleted = 0";

    public static final String queryRiskStaticDataCountSql = "select count(1)\n" +
            "              from yd_risk_models t1\n" +
            "              left join yd_risk_model_type t2\n" +
            "                on t1.yd_type_id = t2.yd_id\n" +
            "             right join yd_risk_record_info t3\n" +
            "                on t1.yd_model_id = t3.yd_risk_id\n" +
            "             where t2.yd_deleted = 0";
    /**
     * @author:ywz
     * @date:2019/9/20
     * @time:19:00
     * @description: RiskDataServiceImpl---queryRiskStaticData
     */
    public static final String queryRiskidsql = "select yd_risk_id,yd_risk_desc from yd_risk_record_info where yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"' GROUP BY yd_risk_id";

    /**
     * @author:ywz
     * @date:2019/10/22
     * @time:19:00
     * @description: RiskDataServiceImpl---queryRiskStaticData
     */
    public static final String queryDealRiskidsql = "select yd_risk_id,yd_risk_desc from yd_risk_trade_info where yd_corporate_bank = '"+ RiskUtil.getOrganizationCode() +"' GROUP BY yd_risk_id";

    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:17:51
     * @description: RiskDataServiceImpl---getModelSearchExtendDto
     */
    // public static final String getModelSearchExtendDtoSql = " group by  t1.yd_model_id, t3.yd_risk_date,t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status) as a\n";
    public static final String getModelSearchExtendDtoSql = " ) as a\n ORDER BY yd_risk_date desc";

    public static final String getModelSearchExtendDtoCountSql = " group by  t1.yd_model_id,DATE_FORMAT(t3.yd_risk_date,'yyyy-MM-dd'),t1.yd_name,t3.yd_risk_desc, t2.yd_type_name,t3.yd_risk_point,t3.yd_status\n";


    /**
     * @author:yinjie
     * @date:2019/7/9
     * @time:19:19
     * @description: OaAuditHeadOfficeServiceImp---findHisFLowByUserId
     */
    public static final String findHisFLowByUserIdSql = "SELECT b.yd_count_id AS countId,\n" +
            "             b.yd_flow_bis_name  AS flowBisName,\n" +
            "             b.yd_flow_user_type     AS flowUserType,\n" +
            "             b.yd_proc_ins_id  AS procInsId,\n" +
            "             t.proc_def_id_      AS procDefId,\n" +
            "             b.yd_status    AS status,\n" +
            "             b.yd_user_id      AS userId,\n" +
            "             b.yd_deal_advice      AS dealAdvice,\n" +
            "             c.name_ AS  flowName,\n" +
            "             t.name_ AS assigner\n" +
            "             from ACT_HI_TASKINST t,YD_OA_AUDIT_HEAD_OFFICE b,ACT_RE_PROCDEF c\n" +
            "             where t.proc_inst_id_ = b.yd_proc_ins_id  and t.proc_def_id_ = c.id_ and t.assignee_='";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:10:06
     * @description: OaAuditHeadOfficeServiceImp---findByUserId
     */
    public static final String findByUserIdSql = "SELECT b.yd_count_id AS countId,\n" +
            "             b.yd_flow_bis_name  AS flowBisName,\n" +
            "             b.yd_flow_user_type     AS flowUserType,\n" +
            "             b.yd_proc_ins_id  AS procInsId,\n" +
            "             t.proc_def_id_      AS procDefId,\n" +
            "             b.yd_status    AS status,\n" +
            "             b.yd_user_id      AS userId,\n" +
            "             b.yd_deal_advice      AS dealAdvice,\n" +
            "             t.create_time_      AS exeDate,\n" +
            "             c.name_ AS  flowName,\n" +
            "             t.name_ AS assigner\n" +
            "             from ACT_RU_TASK t,YD_OA_AUDIT_HEAD_OFFICE b,ACT_RE_PROCDEF c\n" +
            "             where t.proc_inst_id_ = b.yd_proc_ins_id  and t.proc_def_id_ = c.id_ and t.assignee_='";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:10:10
     * @description: OaAuditHeadOfficeServiceImp---findOaAuditByProcInsId
     */
    public static final String findOaAuditByProcInsIdSql = "select * from act_ru_task t where t.proc_inst_id_ =  '";


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
     * @time:11:11
     * @description: RiskDataServiceImpl---getAllAccountsByRiskIdAndRIskPoint
     */
    public static final String getAllAccountsByRiskIdAndRIskPointSql = "select t1.yd_id,t2.yd_acct_no,t1.yd_risk_desc,t2.yd_customer_no\n" +
            "  from yd_risk_record_info t1\n" +
            "  left outer join yd_accounts_all t2\n" +
            "    on t1.yd_customer_id = t2.yd_customer_no\n" +
            " where t1.yd_risk_id = '";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:11:28
     * @description: AccountRiskServiceImpl---queryAccountRisk
     */
    //没有js入口，暂时不改
    public static final String queryAccountRiskSql = " SELECT a.yd_cjrq        AS \"cjrq\",\n" +
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

    public static final String queryAccountRiskEndSql = " order by a.yd_cjrq desc, o.yd_id, a.yd_risk_id, a.yd_kh_id, a.yd_kh_name ";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:07
     * @description: RiskApiServiceImpl---getCustomersInfo
     */
    public static final String getCustomersInfoSql = "select a.yd_customer_no   as customerNo,\n" +
            "       a.yd_acct_no       as acctNo,\n" +
            "       a.yd_bank_code     as bankCode,\n" +
            "       c.yd_work_area     as workArea,\n" +
            "       c.yd_reg_area_code as regAreaCode," +
            "      og.yd_code\n" +
            "  from yd_accounts_all a\n" +
            "  left outer join yd_customers_all b\n" +
            "    on a.yd_customer_no = b.yd_customer_no\n" +
            "  left outer join yd_customer_public c\n" +
            "    on b.yd_id = c.yd_customer_id\n" +
            "  left outer join yd_account_public ac\n" +
            "  on a.yd_id = ac.yd_account_id" +
            " left outer join yd_sys_organization og\n" +
            "  on a.yd_bank_code = og.yd_pbc_code\n" +
            "    where 1=1 and ac.yd_acct_big_type='jiben ' and og.yd_deleted=1";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:09
     * @description: RiskApiServiceImpl---queryRiskRecordInfoData
     */
    public static final String queryRiskRecordInfoDataSql =
            "select t.yd_id,t.yd_account_no,t.yd_customer_id,t.yd_risk_date,a.yd_acct_name," +
                    "a.yd_acct_create_date,\n" +
                    "  a.yd_bank_code,a.yd_bank_name,\n" +
                    "  a.yd_account_status,t.yd_risk_desc,t.yd_risk_cnt,t.yd_risk_type,t.yd_risk_id\n" +
                    "  FROM yd_risk_record_info t\n" +
                    "  left outer join yd_accounts_all a on t.yd_account_no = a.yd_acct_no\n" +
                    "  left outer join yd_customers_all c on a.yd_customer_no= c.yd_customer_no\n" +
                    "   left outer join yd_customer_public dc on c.yd_id = dc.yd_customer_id where 1=1 and t.yd_deleted='1'";


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
    public static final String getAllCustomersTelSql = " SELECT t1.yd_finance_telephone,t1.yd_legal_telephone,t4.yd_operator_telephone,t2.yd_customer_no,t1.yd_organ_full_id,o.yd_code" +
            " FROM yd_customer_public t1 \n" +
            "   left join yd_customers_all t2 on t1.yd_customer_id= t2.yd_id\n" +
            "   left join yd_accounts_all t3 on t2.yd_customer_no=t3.yd_customer_no\n" +
            "   left join yd_account_public t4 on t3.yd_id = t4.yd_account_id " +
            "   right join yd_sys_organization o on t3.yd_organ_full_id = o.yd_full_id \n" +
            "   where 1=1 and t4.yd_acct_type='jiben' and o.yd_deleted = 0 \n" +
            "    and t3.yd_acct_no not in(\n" +
            "    select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t3.yd_acct_no) ";


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


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:20
     * @description: ReportDataServiceImpl---queryAccount
     */
    public static final String queryAccountSql1 = "  SELECT\n" +
            "\tyd_depositor_type,\n" +
            "\tcurrentMonth,\n" +
            "\tlastMonth,\n" +
            "\tlastYear\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT DISTINCT\n" +
            "\t\t\ttab.yd_depositor_type,\n" +
            "\t\t\ttab1.cnt AS currentMonth,\n" +
            "\t\t\ttab2.cnt AS lastMonth,\n" +
            "\t\t\ttab3.cnt AS lastYear\n" +
            "\t\tFROM\n" +
            "\t\t\tyd_account_public tab\n" +
            "\t\tLEFT OUTER JOIN (\n" +
            "\t\t\tSELECT\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tyd_acct_big_type,\n" +
            "\t\t\t\tcount(1) AS cnt\n" +
            "\t\t\tFROM\n" +
            "\t\t\t\tyd_accounts_all t1\n" +
            "\t\t\tRIGHT JOIN yd_account_public t2 ON t1.yd_id = t2.yd_account_id\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\t1 = 1\n" +
            "\t\t\tAND DATE_FORMAT (\n" +
            "\t\t\t\tDATE_FORMAT(\n" +
            "\t\t\t\t\tt1.yd_acct_create_date,\n" +
            "\t\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t\t),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t) = DATE_FORMAT (NOW(), 'yyyy-MM')\n" +
            "\t\t\tAND t2.yd_acct_type = '";

    public static final String queryAccountSql2 = "'\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tyd_acct_big_type\n" +
            "\t\t) tab1 ON tab.yd_depositor_type = tab1.yd_depositor_type\n" +
            "\t\tLEFT OUTER JOIN (\n" +
            "\t\t\tSELECT\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type,\n" +
            "\t\t\t\tcount(1) AS cnt\n" +
            "\t\t\tFROM\n" +
            "\t\t\t\tyd_accounts_all t1,\n" +
            "\t\t\t\tyd_account_public t2\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\tt1.yd_id = t2.yd_account_id\n" +
            "\t\t\tAND DATE_FORMAT (\n" +
            "\t\t\t\tDATE_FORMAT(\n" +
            "\t\t\t\t\tt1.yd_acct_create_date,\n" +
            "\t\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t\t),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t) = DATE_FORMAT (\n" +
            "\t\t\t\tDATE_ADD(NOW(),INTERVAL -1 MONTH),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t)\n" +
            "\t\t\tAND t2.yd_acct_big_type = '";

    public static final String queryAccountSql3 = "'\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type\n" +
            "\t\t) tab2 ON tab.yd_depositor_type = tab2.yd_depositor_type\n" +
            "\t\tLEFT OUTER JOIN (\n" +
            "\t\t\tSELECT\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type,\n" +
            "\t\t\t\tcount(1) AS cnt\n" +
            "\t\t\tFROM\n" +
            "\t\t\t\tyd_accounts_all t1\n" +
            "\t\t\tLEFT JOIN yd_account_public t2 ON t1.yd_id = t2.yd_account_id\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\t1 = 1\n" +
            "\t\t\tAND DATE_FORMAT (\n" +
            "\t\t\t\tDATE_FORMAT(\n" +
            "\t\t\t\t\tt1.yd_acct_create_date,\n" +
            "\t\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t\t),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t) = DATE_FORMAT (\n" +
            "\t\t\t\tDATE_ADD(NOW(),INTERVAL -12 MONTH),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t)\n" +
            "\t\t\tAND t2.yd_acct_big_type = '";

    public static final String queryAccountSql4 = "'\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type\n" +
            "\t\t) tab3 ON tab.yd_depositor_type = tab3.yd_depositor_type\n" +
            "\t)as a";


    /**
     * @author:yinjie
     * @date:2019/7/10
     * @time:14:25
     * @description: ReportDataServiceImpl---queryStorage
     */
    public static final String queryStorageSql1 = "  SELECT\n" +
            "\tyd_depositor_type,\n" +
            "\tcurrentMonth,\n" +
            "\tlastMonth,\n" +
            "\tlastYear\n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT DISTINCT\n" +
            "\t\t\ttab1.yd_depositor_type,\n" +
            "\t\t\ttab1.cnt AS currentMonth,\n" +
            "\t\t\ttab2.cnt AS lastMonth,\n" +
            "\t\t\ttab3.cnt AS lastYear\n" +
            "\t\tFROM\n" +
            "\t\t\tyd_account_public tab\n" +
            "\t\tLEFT OUTER JOIN (\n" +
            "\t\t\tSELECT\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type,\n" +
            "\t\t\t\tcount(1) AS cnt\n" +
            "\t\t\tFROM\n" +
            "\t\t\t\tyd_accounts_all t1\n" +
            "\t\t\tLEFT JOIN yd_account_public t2 ON t1.yd_id = t2.yd_account_id\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\t1 = 1\n" +
            "\t\t\tAND DATE_FORMAT (\n" +
            "\t\t\t\tDATE_FORMAT (\n" +
            "\t\t\t\t\tt1.yd_acct_create_date,\n" +
            "\t\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t\t),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t) <= DATE_FORMAT (NOW(), 'yyyy-MM')\n" +
            "\t\t\tAND t2.yd_acct_type = '";

    public static final String queryStorageSql2 = "'\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type\n" +
            "\t\t) tab1 ON tab.yd_depositor_type = tab1.yd_depositor_type\n" +
            "\t\tLEFT OUTER JOIN (\n" +
            "\t\t\tSELECT\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type,\n" +
            "\t\t\t\tcount(1) AS cnt\n" +
            "\t\t\tFROM\n" +
            "\t\t\t\tyd_accounts_all t1\n" +
            "\t\t\tLEFT JOIN yd_account_public t2 ON t1.yd_id = t2.yd_account_id\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\t1 = 1\n" +
            "\t\t\tAND DATE_FORMAT (\n" +
            "\t\t\t\tDATE_FORMAT (\n" +
            "\t\t\t\t\tt1.yd_acct_create_date,\n" +
            "\t\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t\t),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t) <= DATE_FORMAT (\n" +
            "\t\t\t\tDATE_ADD(NOW(),INTERVAL -1 MONTH),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t)\n" +
            "\t\t\tAND t2.yd_acct_type = '";

    public static final String queryStorageSql3 = "'\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type\n" +
            "\t\t) tab2 ON tab.yd_depositor_type = tab2.yd_depositor_type\n" +
            "\t\tLEFT OUTER JOIN (\n" +
            "\t\t\tSELECT\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type,\n" +
            "\t\t\t\tcount(1) AS cnt\n" +
            "\t\t\tFROM\n" +
            "\t\t\t\tyd_accounts_all t1\n" +
            "\t\t\tLEFT JOIN yd_account_public t2 ON t1.yd_id = t2.yd_account_id\n" +
            "\t\t\tWHERE\n" +
            "\t\t\t\t1 = 1\n" +
            "\t\t\tAND DATE_FORMAT (\n" +
            "\t\t\t\tDATE_FORMAT (\n" +
            "\t\t\t\t\tt1.yd_acct_create_date,\n" +
            "\t\t\t\t\t'yyyy-MM-dd'\n" +
            "\t\t\t\t),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t) <= DATE_FORMAT (\n" +
            "\t\t\t\tDATE_ADD(NOW(),INTERVAL -12 MONTH),\n" +
            "\t\t\t\t'yyyy-MM'\n" +
            "\t\t\t)\n" +
            "\t\t\tAND t2.yd_acct_type = '";

    public static final String queryStorageSql4 = "'\n" +
            "\t\t\tGROUP BY\n" +
            "\t\t\t\tyd_depositor_type,\n" +
            "\t\t\t\tt2.yd_acct_type\n" +
            "\t\t) tab3 ON tab.yd_depositor_type = tab3.yd_depositor_type\n" +
            "\t) AS a";


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
            // "                a.yd_bill_type,\n" +
            "                dc.yd_reg_address,dc.yd_work_address\n" +
            "  FROM yd_risk_record_info t\n" +
    /*        "  left join yd_account_bills_all ba\n" +
            "    on t.yd_account_no = ba.yd_acct_no\n" +*/
            "  left outer join yd_accounts_all a\n" +
            "    on t.yd_customer_id = a.yd_customer_no\n" +
            "  left outer join yd_customers_all c\n" +
            "    on a.yd_customer_no = c.yd_customer_no\n" +
            "  left outer join yd_customer_public dc\n" +
            "    on c.yd_id = dc.yd_customer_id\n" +
            " where 1 = 1";

    public static final String RISK2002SQL = RISK2001SQL;//使用RISK2001SQL
    public static final String RISK2003SQL = "";
    public static final String RISK2004SQL = "";

    public static final String RISK2005SQL = "select distinct t.yd_id as id,b.yd_legal_idcard_no,b.yd_work_address," +
            "b.yd_reg_address,b.yd_depositor_name,b.yd_legal_name,a.yd_customer_no as customerNo,c.*\n" +
            "from yd_risk_record_info t\n" +
            "left join yd_customers_all  a on t.yd_customer_id = a.yd_customer_no\n" +
            "left outer join yd_customer_public  b on a.yd_id=b.yd_customer_id\n" +
            "left outer join yd_accounts_all c on a.yd_customer_no = c.yd_customer_no\n" +
            "left outer join yd_account_public ac  on c.yd_id = ac.yd_account_id\n" +
            "where 1 = 1\n" +
            "and ac.yd_acct_type = 'jiben'\n" +
            "and b.yd_legal_idcard_no is not null ";

    public static final String RISK2006SQL = RISK2005SQL;//使用RISK2005SQL

    public static final String RISK2008SQL = "select distinct t.*,\n" +
            "       d.yd_operator_telephone,\n" +
            "       d.yd_operator_idcard_no,\n" +
            "       d.yd_operator_name,\n" +
            "       dc.yd_finance_idcard_no,\n" +
            "       dc.yd_finance_name,\n" +
            "       dc.yd_finance_telephone,\n" +
            "       dc.yd_legal_telephone,\n" +
            "       dc.yd_legal_idcard_no,\n" +
            "       dc.yd_legal_name," +
            "       dc.yd_depositor_name," +
            "       dc.yd_reg_address," +
            "       dc.yd_work_address,\n" +
            "       c.yd_bank_name" +
            "  from yd_risk_record_info t\n" +
            "left join yd_accounts_all c on t.yd_customer_id = c.yd_customer_no\n" +
            "\t\tleft join yd_customer_public dc on c.yd_customer_id = dc.yd_customer_id\n" +
            "\t\tleft join yd_account_public d on c.yd_id = d.yd_account_id"+
            " where 1 = 1\n" +
            "   and dc.yd_finance_telephone is not null\n" +
            "   and dc.yd_legal_telephone is not null\n" +
            "   and d.yd_operator_telephone is not null\n" +
            "   and d.yd_acct_type = 'jiben'" +
            " ";

    public static final String RISK2007SQL = "\n" +
            "\n" +
            "   select t1.yd_customer_id as t1_customer_id,t4.* ,\n" +
            "    t5.yd_operator_telephone,\n" +
            "       t5.yd_operator_idcard_no,\n" +
            "       t5.yd_operator_name,\n" +
            "       t3.yd_finance_idcard_no,\n" +
            "       t3.yd_finance_name,\n" +
            "       t3.yd_finance_telephone,\n" +
            "       t3.yd_legal_telephone,\n" +
            "       t3.yd_legal_idcard_no,\n" +
            "       t3.yd_legal_name,       \n" +
            "       t3.yd_depositor_name,      \n" +
            "       t3.yd_reg_address,       \n" +
            "       t3.yd_work_address,\n" +
            "       t1.yd_risk_point\n" +
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

    public static final String RISK1001And1002SQL = "select distinct t1.yd_risk_date,t3.yd_acct_no," +
            "t3.yd_acct_name,t2.yd_flag,t2.yd_start_date,\n" +
            "t2.yd_open_date,t2.yd_amount,t2.yd_serial_id,\n" +
            "t2.yd_trade_date,t2.yd_trade_time\n" +
            " from yd_risk_trade_info t1\n" +
            "left join yd_risk_core_info t2 on \n" +
            "t1.yd_account_no = t2.yd_account_no\n" +
            "left join yd_accounts_all t3\n" +
            "on t2.yd_account_no = t3.yd_acct_no\n" +
            "where 1=1\n";

    public static final String queryRegisterRiskInfoSQL = "select @rowno\\:=@rowno+1 as ordNum,t1.yd_id,  \n" +
            "                     DATE_FORMAT(t1.yd_risk_date,'%Y-%m-%d'),\n" +
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

    public static final String queryregisterRiskInfoCountSQL = "select count(ts.rn) from ( select  count(1) as rn from \n" +
            "                yd_risk_register_info t1\n" +
            "                left join yd_accounts_all t2\n" +
            "                on t1.yd_account_no = t2.yd_acct_no\n" +
            "                left outer join yd_account_public t3\n" +
            "                on t2.yd_id = t3.yd_account_id\n" +
            "                left join yd_sys_organization og\n" +
            "                 on t3.yd_organ_full_id = og.yd_full_id\n" +
            "                left join yd_risk_models m\n" +
            "                 on t1.yd_risk_id = m.yd_model_id \n" +
            "                 where m.yd_deleted=0 ";

    public static final String queryregisterRiskInfoCountStSQL = "select 1 from \n" +
            "                yd_risk_register_info t1\n" +
            "                left join yd_accounts_all t2\n" +
            "                on t1.yd_account_no = t2.yd_acct_no\n" +
            "                left outer join yd_account_public t3\n" +
            "                on t2.yd_id = t3.yd_account_id\n" +
            "                left join yd_sys_organization og\n" +
            "                 on t3.yd_organ_full_id = og.yd_full_id\n" +
            "                left join yd_risk_models m\n" +
            "                 on t1.yd_risk_id = m.yd_model_id \n" +
            "                 where m.yd_deleted=0 ";

    public static final String querytodoRiskInfoSQL =
            "                   select   @rowno\\:=@rowno+1 as ordNum, t1.yd_id,  \n" +
                    "                     DATE_FORMAT(t1.yd_risk_date,'%Y-%m-%d'),\n" +
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
                    "                 where m.yd_deleted=0 and t2.yd_account_status<>'revoke' ";

    public static final String querytodoRiskInfoCountSQL = " select count(ts.cn) from (" +
            "               select count(1) as cn ,t2.yd_acct_no,t2.yd_acct_name,t1.yd_risk_date\n" +
            "                from yd_risk_handle_info t1\n" +
            "                left join yd_accounts_all t2\n" +
            "                on t1.yd_account_no = t2.yd_acct_no\n" +
            "                left outer join yd_account_public t3\n" +
            "                on t2.yd_id = t3.yd_account_id\n" +
            "                left join yd_sys_organization og\n" +
            "                 on t3.yd_organ_full_id = og.yd_full_id\n" +
            "                left join yd_risk_models m\n" +
            "                 on t1.yd_risk_id = m.yd_model_id \n" +
            "                 where m.yd_deleted=0 and t2.yd_account_status<>'revoke'";
    public static final String querytodoRiskInfoCountStSQL =
            "               select 1 as cn\n" +
                    "                from yd_risk_handle_info t1\n" +
                    "                left join yd_accounts_all t2\n" +
                    "                on t1.yd_account_no = t2.yd_acct_no\n" +
                    "                left outer join yd_account_public t3\n" +
                    "                on t2.yd_id = t3.yd_account_id\n" +
                    "                left join yd_sys_organization og\n" +
                    "                 on t3.yd_organ_full_id = og.yd_full_id\n" +
                    "                left join yd_risk_models m\n" +
                    "                 on t1.yd_risk_id = m.yd_model_id \n" +
                    "                 where m.yd_deleted=0 and t2.yd_account_status<>'revoke'";

    /**
     *
     */
    //RiskHandleInfoServiceImpl---getRiskDataSearchDtoS
    public static final String getRiskDataSearchDtoSTOCHAR1 = " and DATEDIFF( t1.yd_handle_date, ?5 ) >= 0";
    public static final String getRiskDataSearchDtoSTOCHAR2 = " and DATEDIFF( t1.yd_handle_date, ?6 ) <= 0";

    //RiskHandleInfoServiceImpl---getRiskDataSearchDto
    public static final String getRiskDataSearchDtoTOCHAR1 = " and DATEDIFF( t1.yd_risk_date, ?5 ) >= 0";
    public static final String getRiskDataSearchDtoTOCHAR2 = " and DATEDIFF( t1.yd_risk_date, ?6 ) <= 0";
    public static final String getRiskDataSearchDtoTOCHAR3 = " and DATEDIFF( t1.yd_handle_date, ?5 ) >= 0";
    public static final String getRiskDataSearchDtoTOCHAR4 = " and DATEDIFF( t1.yd_handle_date, ?6 ) <= 0";

    //RiskDataServiceImpl---getModelSearchExtendDto
    public static final String getModelSearchExtendDtoTOCHAR1 = " and DATEDIFF( t3.yd_risk_date, ?4 ) >= 0";
    public static final String getModelSearchExtendDtoTOCHAR2 = " and DATEDIFF( t3.yd_risk_date, ?5 ) <= 0";


    public static final String getSqlProc = "select b.YD_CON_AND_VAL from YD_RISK_RULE_FIELD a\n" +
            "  left outer join YD_RISK_RULE_CONFIGURATION b on a.yd_id=b.yd_rule_id\n" +
            "  where a.yd_deleted='0' and b.yd_deleted='0'\n";


    public static final String getHighRiskData = "SELECT\n" +
            "\tc.yd_customer_no,\n" +
            "\tc.yd_legal_name,\n" +
            "\tc.yd_legal_idcard_no,\n" +
            "\tc.yd_legal_idcard_type,\n" +
            "\tc.yd_depositor_name,\n" +
            "\tc.yd_depositorcard_no,\n" +
            "\tc.yd_depositorcard_type,\n" +
            "\tDATE_FORMAT(t.yd_risk_date,'%Y-%m-%d') yd_risk_date,\n" +
            "\tt.yd_risk_desc,t.yd_risk_id,t.yd_account_no \n" +
            "FROM\n" +
            "\tyd_risk_high_customer c\n" +
            "\tLEFT JOIN yd_accounts_all a ON a.yd_customer_no = c.yd_customer_no\n" +
            "\tLEFT JOIN yd_risk_trade_info t ON t.yd_account_no = a.yd_acct_no \n" +
            "WHERE 1=1\n";

    public static final String getHighRiskByCustomerNo = "select * FROM yd_risk_high_customer WHERE 1=1";

    public static final String getHighRisk = "SELECT\n" +
            "\t@rowno \\:= @rowno + 1 AS ordNum,\n" +
            "\ta.* \n" +
            "FROM\n" +
            "\t( SELECT @rowno \\:= 0 ) r,\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tt.yd_customer_no,\n" +
            "\t\tt.YD_DEPOSITOR_NAME,\n" +
            "\t\tt.yd_legal_name,\n" +
            "\t\tt.yd_depositorcard_type,\n" +
            "\t\tt.yd_depositorcard_no,\n" +
            "\t\tt.yd_legal_idcard_type,\n" +
            "\t\tt.yd_legal_idcard_no,\n" +
            "\t\tgroup_concat(case when m.yd_name   is not null then m.yd_name  else p.yd_api_name end, '-') as yd_risk_desc,\n" +
            "\t\tt.yd_risk_date,\n" +
            "\t\ta.yd_account_management yd_status,\n" +
            "\t\tt.yd_id,group_concat(a.yd_account_management) as sta\n" +
            "\tFROM\n" +
            "\t\tyd_risk_high_info t \n" +
            "\tLEFT JOIN yd_risk_models m ON t .yd_risk_id=m.yd_model_id\n" +
            "\tLEFT JOIN yd_risk_high_api p ON t.yd_risk_id = p.yd_api_no\n" +
            "\t\tLEFT JOIN yd_accounts_all a ON t.yd_account_no = a.yd_acct_no\n" +
            "\tWHERE\n" +
            "\t1 = 1 ";

    public static final String getHighRiskCountSql = "select count(1) from(select count(1),\n" +
            "\t\tgroup_concat( CASE WHEN m.yd_name IS NOT NULL THEN m.yd_name ELSE p.yd_api_name END, '-' ) AS yd_risk_desc  from yd_risk_high_info t LEFT JOIN yd_risk_models m ON t .yd_risk_id=m.yd_model_id \n" +
            "\tLEFT JOIN yd_risk_high_api p ON t.yd_risk_id = p.yd_api_no LEFT JOIN yd_accounts_all a ON t.yd_account_no = a.yd_acct_no where 1=1";


    public static final String getHighRiskList = "select\n" +
            "@rowno \\:= @rowno + 1 AS ordNum,\n" +
            "b.*\n" +
            "from ( SELECT @rowno \\:= 0 ) r,\n" +
            "(SELECT\n" +
            "\t\n" +
            "\tt.yd_id,\n" +
            "\tt.yd_customer_no,\n" +
            "\tt.YD_DEPOSITOR_NAME,\n" +
            "\ta.yd_account_management yd_status,\n" +
            "\tt.yd_account_no,\n" +
            "\ta.YD_ACCT_TYPE,\n" +
            "\ta.yd_bank_name,\n" +
            "\ta.yd_acct_create_date," +
            "\tt.yd_depositorcard_no,\n" +
            "\tt.yd_risk_desc\n" +
            "FROM\n" +
            "\t\n" +
            "\tyd_risk_high_info t\n" +
            "\tLEFT JOIN yd_accounts_all a ON t.yd_account_no = a.yd_acct_no\n" +
            "WHERE\n" +
            "\t1 = 1 ";

    public static final String getHighRiskListCountSql = "SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\t(SELECT\n" +
            "\tcount(1)\n" +
            "FROM\n" +
            "\tyd_risk_high_info t\n" +
            "\tLEFT JOIN yd_accounts_all a ON t.yd_account_no = a.yd_acct_no \n" +
            "WHERE\n" +
            "\t1 = 1 ";

    public static final String findRiskApi = "SELECT\n" +
            "\tyd_id,\n" +
            "\tyd_api_no,\n" +
            "\tyd_api_name,\n" +
            "\tyd_api_url,\n" +
            "\tyd_key_word,\n" +
            "\tyd_ret_data \n" +
            "FROM\n" +
            "\tyd_risk_high_api\n" +
            "\twhere yd_deleted ='0'";


    public static final String getHighRiskAccNo = "SELECT\n" +
            "\tc.yd_customer_no,\n" +
            "\tc.yd_legal_name,\n" +
            "\tc.yd_legal_idcard_no,\n" +
            "\tc.yd_legal_idcard_type,\n" +
            "\tc.yd_depositor_name,\n" +
            "\tc.yd_depositorcard_no,\n" +
            "\tc.yd_depositorcard_type,\n" +
            "\ta.YD_ACCT_NO accountNo,\n" +
            "\ta.yd_account_management\n" +
            "FROM\n" +
            "\tyd_risk_high_customer c\n" +
            "\tLEFT JOIN yd_accounts_all a ON a.yd_customer_no = c.yd_customer_no where 1=1";

    public static final String getHighTradeInfo = "SELECT\n" +
            "\tc.yd_customer_no,\n" +
            "\tc.yd_legal_name,\n" +
            "\tc.yd_legal_idcard_no,\n" +
            "\tc.yd_legal_idcard_type,\n" +
            "\tc.yd_depositor_name,\n" +
            "\tc.yd_depositorcard_no,\n" +
            "\tc.yd_depositorcard_type,\n" +
            "\tDATE_FORMAT ( t.yd_risk_date, '%Y-%m-%d' ) yd_risk_date,\n" +
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
            "\tDATE_FORMAT(t.yd_risk_date, '%Y-%m-%d') yd_risk_date,\n" +
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

    public static final String getModelInfo = "SELECT\n" +
            "\tc.yd_customer_no,\n" +
            "\tcp.yd_legal_name,\n" +
            "\tcp.yd_legal_idcard_no,\n" +
            "\tcp.yd_legal_idcard_type,\n" +
            "\tc.yd_depositor_name,\n" +
            "\trc.yd_company_certificate_no depositorcard_no,\n" +
            "\tc.yd_depositor_type,\n" +
            "\ta.yd_account_management \n" +
            "FROM\n" +
            "\tyd_customers_all c\n" +
            "\tLEFT JOIN yd_customer_public cp ON c.yd_id = cp.yd_customer_id\n" +
            "\tLEFT JOIN yd_relate_company rc ON cp.yd_id = rc.yd_customer_public_id \n" +
            "\tLEFT JOIN yd_accounts_all a ON a.yd_customer_no = c.yd_customer_no\n" +
            "WHERE 1=1";

    public static final String getModelFieldsSql = "select \n"+
            "COLUMN_NAME,\n"+
            "COLUMN_COMMENT\n"+
            "from information_schema.COLUMNS where 1=1";

    public static final   String inancialLiquReportFlowSql = "select t2.YD_DEPOSITOR_TYPE,\n" +
            "sum(case when t1.yd_cr_dr_maint_ind = 'C' then t1.yd_tran_amt else 0 end ) as inflow,\n" +
            "sum(case when t1.yd_cr_dr_maint_ind = 'D' then t1.yd_tran_amt else 0 end ) as outflow   \n" +
            "from yd_intf_ams_tran_hist t1\n" +
            "right join yd_customers_all t2 on t1.yd_client_no = t2.yd_customer_no\n" +
            "where t1.yd_TRAN_DATE>=\n" +
            " concat(date_format(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
            "interval QUARTER(CURDATE())*3-3 month),'%Y%m'),'01') and t1.yd_tran_date<=DATE_FORMAT(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
            "interval QUARTER(CURDATE())*3-1 month),'%Y%m%d')\n";
    public static final String inancialLiquReportPoriedSql_1 = "select tab1.YD_DEPOSITOR_TYPE , tab2.begPoried,tab1.endPoried from (\n" +
            "select t2.YD_DEPOSITOR_TYPE,sum(t1.yd_actual_bal) as endPoried \n" +
            "from yd_intf_ams_tran_hist t1\n" +
            "left join yd_customers_all t2 on t1.yd_client_no = t2.YD_CUSTOMER_NO\n" +
            "where t1.yd_TRAN_DATE= \n" +
            "concat(date_format(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
            "interval QUARTER(CURDATE())*3-3 month),'%Y%m'),'01')  \n" ;

    public static final String inancialLiquReportPoriedSql_2 =  "(select t2.YD_DEPOSITOR_TYPE,sum(t1.yd_actual_bal) as begPoried from yd_intf_ams_tran_hist t1\n" +
            "left join yd_customers_all t2 on t1.yd_client_no = t2.YD_CUSTOMER_NO\n" +
            "where t1.yd_TRAN_DATE= DATE_FORMAT(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
            "interval QUARTER(CURDATE())*3-1 month),'%Y%m%d')" ;


}
