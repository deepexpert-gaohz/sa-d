package com.ideatech.ams.risk.riskdata.entity;

public abstract class RiskSQLConstant {

    public static final String RISK2001SQL =" select distinct t.yd_id,\n" +
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
            "                dc.yd_reg_address,dc.yd_work_address\n" +
            "  FROM yd_risk_record_info t\n" +
            "  left join yd_account_bills_all ba\n" +
            "    on t.yd_customer_id = ba.yd_customer_no\n" +
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
    public static final String RISK2005SQL = "select distinct t.yd_id,b.yd_legal_idcard_no,b.yd_work_address,b.yd_reg_address,b.yd_depositor_name,b.yd_legal_name,a.yd_customer_no,c.*\n" +
            "from yd_risk_record_info t\n" +
            "left join yd_customers_all  a on t.yd_customer_id = a.yd_customer_no\n" +
            "left outer join yd_customer_public  b on a.yd_id=b.yd_customer_id\n" +
            "left outer join yd_accounts_all c on a.yd_customer_no = c.yd_customer_no\n" +
            "left outer join yd_account_public ac  on c.yd_id = ac.yd_account_id\n" +
            "where 1 = 1\n" +
            "and ac.yd_acct_type = 'jiben'\n" +
            "and b.yd_legal_idcard_no is not null ";

    public static final String RISK2006SQL = RISK2005SQL;//使用RISK2005SQL
    public static final String RISK2008SQL = "select distinct t.yd_customer_id,c.*,\n" +
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
            "       t.yd_risk_point" +
            "  from yd_risk_record_info t\n" +
            "  left join yd_customers_all a\n" +
            "    on t.yd_customer_id = a.yd_customer_no\n" +
            "  left outer join yd_customer_public b\n" +
            "    on a.yd_id = b.yd_customer_id\n" +
            "  left outer join yd_accounts_all c\n" +
            "    on a.yd_customer_no = c.yd_customer_no\n" +
            "  left outer join yd_account_public d\n" +
            "    on c.yd_id = d.yd_account_id\n" +
            " where 1 = 1\n" +
            "   and b.yd_finance_telephone is not null\n" +
            "   and b.yd_legal_telephone is not null\n" +
            "   and d.yd_operator_telephone is not null\n" +
            "   and d.yd_acct_type = 'jiben'" +
            " ";
    public static final String RISK2007SQL = "\n" +
            "\n" +
            "   select t1.yd_customer_id,t4.* ,\n" +
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
            "left outer join yd_account_public t4 on \n" +
            "t3.yd_id = t4.yd_account_id\n" +
            "where 1=1\n" +
            "and t4.yd_acct_type='jiben'";

    public static final String queryRegisterRiskInfoSQL = "select rownum as ordNum,t1.yd_id,  \n"+
            "                     to_char(t1.yd_risk_date,'yyyy-MM-dd'),\n"+
            "                     m.yd_Name as modelName,\n"+
            "                     t2.yd_acct_no,\n"+
            "                     t2.yd_acct_name,\n"+
            "                     t2.yd_acct_create_date,\n"+
            "                      t1.yd_risk_desc,\n"+
            "                      t1.yd_risk_id,\n"+
            "                      t1.yd_risk_point,\n"+
            "                      og.yd_name,\n"+
            "                      t3.yd_acct_type,\n"+
            "                      t1.yd_status," +
            "                      t1.yd_handle_mode," +
            "                      t1.yd_handle_date," +
            "                      su.yd_cname,t1.yd_dubious_reason\n"+
            "                from  yd_risk_register_info t1\n"+
            "                left join yd_accounts_all t2\n"+
            "                on t1.yd_account_no = t2.yd_acct_no\n"+
            "                left outer join yd_account_public t3\n"+
            "                on t2.yd_id = t3.yd_account_id\n"+
            "                left join yd_sys_organization og\n"+
            "                 on t3.yd_organ_full_id = og.yd_full_id" +
            "               left join yd_sys_user su \n" +
            "                on t1.yd_handler = su.yd_id\n"+
            "                left join yd_risk_models m\n"+
            "                 on t1.yd_risk_id = m.yd_model_id \n"+
            "                 where m.yd_deleted=0 ";

    public static final String  queryregisterRiskInfoCountSQL="select  1 from \n"+
            "                yd_risk_register_info t1\n"+
            "                left join yd_accounts_all t2\n"+
            "                on t1.yd_account_no = t2.yd_acct_no\n"+
            "                left outer join yd_account_public t3\n"+
            "                on t2.yd_id = t3.yd_account_id\n"+
            "                left join yd_sys_organization og\n"+
            "                 on t3.yd_organ_full_id = og.yd_full_id\n"+
            "                left join yd_risk_models m\n"+
            "                 on t1.yd_risk_id = m.yd_model_id \n"+
            "                 where m.yd_deleted=0";
    public static final String querytodoRiskInfoSQL =
            "                   select   rownum as ordNum, t1.yd_id,  \n"+
            "                     to_char(t1.yd_risk_date,'yyyy-MM-dd'),\n"+
            "                     m.yd_Name as modelName,\n"+
            "                     t2.yd_acct_no,\n"+
            "                     t2.yd_acct_name,\n"+
            "                     t2.yd_acct_create_date,\n"+
            "                      t1.yd_risk_desc,\n"+
            "                      t1.yd_risk_id,\n"+
            "                      t1.yd_risk_point,\n"+
            "                      og.yd_name,\n"+
            "                      t3.yd_acct_type,\n"+
            "                      t1.yd_status\n"+
            "                from  yd_risk_handle_info t1\n"+
            "                left join yd_accounts_all t2\n"+
            "                on t1.yd_account_no = t2.yd_acct_no\n"+
            "                left outer join yd_account_public t3\n"+
            "                on t2.yd_id = t3.yd_account_id\n"+
            "                left join yd_sys_organization og\n"+
            "                 on t3.yd_organ_full_id = og.yd_full_id" +

            "                left join yd_risk_models m\n"+
            "                 on t1.yd_risk_id = m.yd_model_id \n"+
            "                 where m.yd_deleted=0 ";

    public static final String  querytodoRiskInfoCountSQL="" +
                "               select 1 \n"+
                "                from yd_risk_handle_info t1\n"+
                "                left join yd_accounts_all t2\n"+
                "                on t1.yd_account_no = t2.yd_acct_no\n"+
                "                left outer join yd_account_public t3\n"+
                "                on t2.yd_id = t3.yd_account_id\n"+
                "                left join yd_sys_organization og\n"+
                "                 on t3.yd_organ_full_id = og.yd_full_id\n"+
                "                left join yd_risk_models m\n"+
                "                 on t1.yd_risk_id = m.yd_model_id \n"+
                "                 where m.yd_deleted=0 ";

}

