package com.ideatech.ams.risk.Constant;

/**
 * @Author: yinjie
 * @Date: 2019/7/10 9:31
 * @description
 * Oracle的存储过程类
 */
public abstract class OracleProceduresConstant {

    public static final String sp_risk_1001_application = "CREATE OR REPLACE PROCEDURE \"SP_RISK_1001_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description:新开户15日内发生100万交易\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL        VARCHAR2(3000);\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_SQL_2      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  V_SERIAL_ID   VARCHAR2(64);\n" +
            "  V_ACCOUNT_NO  VARCHAR2(64);\n" +
            "  V_CNT         VARCHAR2(64);\n" +
            "\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_1001_application';\n" +
            "  V_RISK_ID   := 'RISK_1001';\n" +
            "\n" +
            "  BEGIN\n" +
            "    V_SQL_1 := ' SELECT risk_record_info_seq.Nextval,TO_DATE(''' || I_DATE ||''',''yyyy-MM-dd''),yd_account_no,yd_customer_id,risk_id,cnt\n" +
            "    ,yd_serial_id,''新开户(''||yd_account_no||'')'||get('' || V_RISK_ID || '', 'ts')||'天内发生 '||get('' || V_RISK_ID || '', 'jyje')||'万以上交易!''';\n" +
            "\n" +
            "    V_SQL_2 := 'select count(1)  ';\n" +
            "    V_SQL := ' FROM ( SELECT distinct a.yd_account_no,t.yd_customer_no as yd_customer_id,''' || V_RISK_ID ||\n" +
            "    ''' as risk_id, a.yd_serial_id,\n" +
            "       count(a.yd_serial_id) over(partition by yd_account_no)  as cnt\n" +
            "        from yd_risk_core_info a\n" +
            "    right join yd_accounts_all t on a.yd_account_no = t.yd_acct_no\n" +
            "    where a.yd_open_date BETWEEN to_char(sysdate -' ||get('' || V_RISK_ID || '', 'ts') || ',''yyyy-MM-dd'')\n" +
            "    and to_char(sysdate,''yyyy-MM-dd'') and a.yd_flag=''1'' and to_number(a.yd_amount)' || get('' || V_RISK_ID || '', 'jyje')||')';\n" +
            "\n" +
            "    /*获取模型定义信息*/\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            "     -- execute immediate V_SQL_2 || V_SQL  into V_CNT;\n" +
            "      dbms_output.put_line( V_SQL_1 || V_SQL);\n" +
            "      commit;\n" +
            "    --获取风险点存入风险结果表中\n" +
            "    open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_ACCOUNT_NO,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT,\n" +
            "               V_SERIAL_ID,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "             dbms_output.put_line( V_CNT);\n" +
            "        delete from yd_risk_trade_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_risk_point = V_SERIAL_ID;\n" +
            "        commit;\n" +
            "          insert into yd_risk_trade_info (yd_id,yd_risk_date,yd_account_no,yd_customer_id,yd_risk_id\n" +
            "          ,yd_risk_desc,yd_status,yd_created_date,yd_risk_point,Yd_Version_Ct,yd_serial_id,Yd_Deleted,yd_risk_cnt)\n" +
            "          values (V_ID,V_RISK_DATE,V_ACCOUNT_NO,V_CUSTOMER_NO,V_RISK_ID,V_RISK_DESC,'0',sysdate,V_ACCOUNT_NO,'0',V_SERIAL_ID,0,V_CNT);\n" +
            "      end loop;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String sp_risk_1002_application = "CREATE OR REPLACE PROCEDURE \"SP_RISK_1002_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description:印鉴卡15天内发生100万以上交易\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL        VARCHAR2(3000);\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_SQL_2      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "  V_CNT        INTEGER;\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_CNT_1       INTEGER;\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  V_SERIAL_ID   VARCHAR2(64);\n" +
            "  V_ACCOUNT_NO  VARCHAR2(64);\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_1002_application';\n" +
            "  V_RISK_ID   := 'RISK_1002';\n" +
            "\n" +
            "  BEGIN\n" +
            "    V_SQL_1 := ' SELECT risk_record_info_seq.Nextval,TO_DATE(''' || I_DATE ||''',''yyyy-MM-dd''),\n" +
            "    yd_account_no,yd_customer_id,risk_id, cnt,\n" +
            "    yd_serial_id,''印鉴卡(''||yd_account_no||'')'||get('' || V_RISK_ID || '', 'ts')||'天内发生 '||get('' || V_RISK_ID || '', 'jyje')||'万以上交易!''';\n" +
            "\n" +
            "    V_SQL := ' FROM ( SELECT distinct a.yd_account_no,t.yd_customer_no as yd_customer_id ,''' || V_RISK_ID ||\n" +
            "    ''' as risk_id, a.yd_serial_id ,count(a.yd_serial_id) over(partition by yd_account_no)  as cnt from yd_risk_core_info a\n" +
            "    right join yd_accounts_all t on a.yd_account_no = t.yd_acct_no\n" +
            "    where a.yd_start_date BETWEEN to_char(sysdate -' ||get('' || V_RISK_ID || '', 'ts') || ',''yyyy-MM-dd'')\n" +
            "    and to_char(sysdate,''yyyy-MM-dd'') and a.yd_flag=''2'' and to_number(a.yd_amount)' || get('' || V_RISK_ID || '', 'jyje')||')';\n" +
            "\n" +
            "\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            "     --execute immediate V_SQL_2 || V_SQL  into V_CNT;\n" +
            "      dbms_output.put_line( V_SQL_1 || V_SQL);\n" +
            "      commit;\n" +
            "    --获取风险数据存入风险结果表中 ,sysdate,''0'',''0'',yd_serial_id\n" +
            "    open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_ACCOUNT_NO,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT,\n" +
            "               V_SERIAL_ID,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "\n" +
            "       -- select count(1) into V_CNT_EXITS from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_serial_id=V_SERIAL_ID;\n" +
            "       delete from yd_risk_trade_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_risk_point = V_SERIAL_ID;\n" +
            "       commit;\n" +
            "       -- IF V_CNT_EXITS =0 THEN\n" +
            "          insert into yd_risk_trade_info (yd_id,yd_risk_date,yd_account_no,yd_customer_id,yd_risk_id\n" +
            "          ,yd_risk_desc,yd_status,yd_created_date,yd_risk_point,Yd_Version_Ct,yd_serial_id,Yd_Deleted,yd_risk_cnt)\n" +
            "          values (V_ID,V_RISK_DATE,V_ACCOUNT_NO,V_CUSTOMER_NO,V_RISK_ID,V_RISK_DESC,'0',sysdate,V_ACCOUNT_NO,'0',V_SERIAL_ID,0,V_CNT);\n" +
            "        --ELSE\n" +
            "        --  update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_serial_id=V_SERIAL_ID and yd_status='0';\n" +
            "        --END IF;\n" +
            "      end loop;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";

    public static final String SP_RISK_2001_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2001_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description:开户人频繁开户、注销或变更账户信息\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL        VARCHAR2(3000);\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_SQL_2      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "  V_CNT        INTEGER;\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_CNT_1       INTEGER;\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2001_application';\n" +
            "  V_RISK_ID   := 'RISK_2001';\n" +
            "\n" +
            "  BEGIN\n" +
            "    V_SQL_1 := 'select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "               ''',''yyyy-MM-dd''),yd_customer_no,\n" +
            "               ''' || V_RISK_ID || ''',cnt,\n" +
            "               ''同一单位客户在' || get('' || V_RISK_ID || '', 'ts') ||\n" +
            "               '天内发生' || get('' || V_RISK_ID || '', 'rn') ||\n" +
            "               '次以上的开户、销户或变更信息操作！'' ';\n" +
            "    V_SQL_2 := 'select count(1)  ';\n" +
            "    V_SQL   := ' from (select t.yd_customer_no,count(1) as  cnt from yd_account_bills_all t\n" +
            "                left join yd_accounts_all t1 on  t1.yd_ref_bill_id = t.yd_id\n" +
            "                left outer join yd_account_public ac on t1.yd_id = ac.yd_account_id\n" +
            "                where t.yd_bill_date BETWEEN to_char(sysdate -' || get('' || V_RISK_ID || '', 'ts') ||',''yyyy-MM-dd'')\n" +
            "                and to_char(sysdate,''yyyy-MM-dd'')\n" +
            "                and ac.yd_acct_type = ''jiben''\n" +
            "                and t.yd_acct_no not in(\n" +
            "                select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t.yd_acct_no)\n" +
            "                and t.yd_bill_type in(''ACT_OPEN'',''ACCT_CHANGE'',''ACCT_REVOKE'',''ACCT_INIT'')\n" +
            "                and ac.yd_acct_big_type=''jiben''\n" +
            "                group by t.yd_customer_no) where 1=1 and cnt' || get('' || V_RISK_ID || '', 'rn');\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "        dbms_output.put_line('V_SQL_2--' || V_SQL_1 || V_SQL);\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            "      execute immediate V_SQL_2 || V_SQL\n" +
            "        into V_CNT;\n" +
            "      commit;\n" +
            "    --获取风险数据存入风险结果表中\n" +
            "    open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT_1,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "\n" +
            "       -- select count(1) into V_CNT_EXITS from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "        delete from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "         commit;\n" +
            "       --IF V_CNT_EXITS =0 THEN\n" +
            "          insert into YD_RISK_RECORD_INFO (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc, yd_status,  yd_created_date, yd_risk_point, Yd_Version_Ct,Yd_Deleted)\n" +
            "          values (V_ID,V_RISK_DATE,V_CUSTOMER_NO,V_RISK_ID,V_CNT_1,V_RISK_DESC, '0', sysdate, V_CUSTOMER_NO, '0',0);\n" +
            "        --ELSE\n" +
            "          --dbms_output.put_line( V_CUSTOMER_NO ||'---'|| V_RISK_ID);\n" +
            "          --update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_customer_id=V_CUSTOMER_NO and yd_status='0';\n" +
            "         commit;\n" +
            "       -- END IF;\n" +
            "      end loop;\n" +
            "\n" +
            "    end if;\n" +
            "  END;\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2001_STOCK = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2001_STOCK\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description: 统计开户人已经开户、注销或变更账户的次数\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2001_STOCK';\n" +
            "  V_RISK_ID   := 'RISK_2001';\n" +
            "  BEGIN\n" +
            "\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "    if V_STATUS = '1' then\n" +
            "      -- 删除既往数据\n" +
            "      delete from YD_RISK_MIDDLE_INFO where yd_risk_id = V_RISK_ID;\n" +
            "      commit;\n" +
            "      V_SQL_1 := 'insert into YD_RISK_MIDDLE_INFO\n" +
            "         (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc)\n" +
            "        select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' ||\n" +
            "                 I_DATE || ''',''yyyy-MM-dd''),yd_customer_no,\n" +
            "                   ''' || V_RISK_ID ||\n" +
            "                 ''',cnt,\n" +
            "                   ''同一单位客户在' ||\n" +
            "                 get('' || V_RISK_ID || '', 'ts') || '天内发生' ||\n" +
            "                 get('' || V_RISK_ID || '', 'rn') ||\n" +
            "                 '次以上的开户、销户或变更信息操作！''\n" +
            "                   from (select t.yd_customer_no,count(1) as  cnt from yd_account_bills_all t\n" +
            "                    left join yd_accounts_all t1 on  t1.yd_ref_bill_id = t.yd_id\n" +
            "                     left outer join yd_account_public ac\n" +
            "                on t1.yd_id = ac.yd_account_id\n" +
            "        where t.yd_bill_date BETWEEN to_char(sysdate -' ||\n" +
            "                 get('' || V_RISK_ID || '', 'ts') ||\n" +
            "                 ',''yyyy-MM-dd'')\n" +
            "        and to_char(sysdate,''yyyy-MM-dd'')\n" +
            "        and ac.yd_acct_big_type = ''jiben''\n" +
            "        and t.yd_acct_no not in(\n" +
            "                     select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t.yd_acct_no)\n" +
            "                     and t.yd_bill_type in(''ACT_OPEN'',''ACCT_CHANGE'',''ACCT_REVOKE'',''ACCT_INIT'')\n" +
            "                      group by t.yd_customer_no) where 1=1 ';\n" +
            "      dbms_output.put_line('V_SQL_1--' || V_SQL_1);\n" +
            "      execute immediate V_SQL_1;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2002_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2002_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-6-14\n" +
            "  description:单位同一注册地址被多次使用2.0\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL        VARCHAR2(3000);\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_SQL_2      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "  V_CNT        INTEGER;\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_CNT_1       INTEGER;\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  V_ADDRESS     VARCHAR2(500);\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2002_application';\n" +
            "  V_RISK_ID   := 'RISK_2002';\n" +
            "\n" +
            "  BEGIN\n" +
            "    V_SQL_1 := 'SELECT risk_record_info_seq.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "             ''',''yyyy-MM-dd''),yd_customer_no,risk_id,cnt ,yd_reg_address,\n" +
            "             ''多('||get('' || V_RISK_ID || '', 'rn')||')个单位客户预留同一注册地址(''||yd_reg_address||'')!''';\n" +
            "    V_SQL_2 := 'select count(1)  ';\n" +
            "    V_SQL   := ' FROM (SELECT c.yd_customer_no as yd_customer_no,''' || V_RISK_ID ||''' as risk_id,\n" +
            "                 a.yd_reg_address,count (distinct dc.yd_customer_no) over(partition by yd_reg_address)  as cnt\n" +
            "                FROM yd_customer_public a\n" +
            "                left outer join yd_customers_all c on a.yd_customer_id= c.yd_id\n" +
            "                 left outer join yd_accounts_all dc on c.yd_customer_no = dc.yd_customer_no\n" +
            "                 left outer join yd_account_public ac on dc.yd_id = ac.yd_account_id\n" +
            "                 where a.yd_reg_address is not null and ac.yd_acct_type=''jiben'' and 1=1 and yd_acct_no not in(\n" +
            "                 select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=yd_acct_no)) WHERE 1=1 and cnt'||get('' || V_RISK_ID || '', 'rn');\n" +
            "\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            "      execute immediate V_SQL_2 || V_SQL\n" +
            "        into V_CNT;\n" +
            "      commit;\n" +
            "\n" +
            "    --获取风险数据存入风险结果表中\n" +
            "    open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT_1,\n" +
            "               V_ADDRESS,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "\n" +
            "        --select count(1) into V_CNT_EXITS from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "        --IF V_CNT_EXITS =0 THEN\n" +
            "         delete from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "         commit;\n" +
            "          insert into YD_RISK_RECORD_INFO (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc,yd_status,yd_created_date,Yd_Risk_Point,Yd_Version_Ct,Yd_Deleted)\n" +
            "          values (V_ID,V_RISK_DATE,V_CUSTOMER_NO,V_RISK_ID,V_CNT_1,V_RISK_DESC,'0',sysdate,V_ADDRESS,'0',0);\n" +
            "        commit;\n" +
            "        --ELSE\n" +
            "         -- update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_customer_id=V_CUSTOMER_NO and yd_status='0';\n" +
            "       -- END IF;\n" +
            "      end loop;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "   dbms_output.put_line('V_SQL--' || V_SQL_1||V_SQL);\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2002_STOCK = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2002_STOCK\" (I_DATE       IN VARCHAR2)\n" +
            "/**\n" +
            "  author:yangcq\n" +
            "  date:2019-5-25\n" +
            "  description:统计单位同一注册地址已经被使用次数\n" +
            "\n" +
            "\n" +
            "  **/\n" +
            "AS\n" +
            "  V_SQL        VARCHAR2(30000);\n" +
            "  V_MODEL_NAME VARCHAR2(200);\n" +
            "  V_PROC_NAME  VARCHAR2(200);\n" +
            "  V_RISK_ID    VARCHAR2(30);\n" +
            "   V_STATUS     VARCHAR2(2);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2002_STOCK';\n" +
            "  V_RISK_ID   := 'RISK_2002';\n" +
            "--多个单位客户预留同一注册地址!\n" +
            "  BEGIN\n" +
            "    select yd_name,yd_status\n" +
            "      into V_MODEL_NAME,V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "    IF V_STATUS=1 THEN\n" +
            "        -- 删除既往数据\n" +
            "       delete from YD_RISK_MIDDLE_INFO where yd_risk_id = V_RISK_ID;\n" +
            "      commit;\n" +
            "    V_SQL := 'INSERT INTO YD_RISK_MIDDLE_INFO\n" +
            "     (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt,yd_risk_desc)\n" +
            "    SELECT risk_record_info_seq.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "             ''',''yyyy-MM-dd''),yd_customer_no,risk_id,cnt ,  yd_reg_address FROM (\n" +
            "    SELECT c.yd_customer_no as yd_customer_no,''' || V_RISK_ID ||''' as risk_id,\n" +
            "              a.yd_reg_address,count (distinct dc.yd_customer_no) over(partition by yd_reg_address)  as cnt\n" +
            "      FROM yd_customer_public a\n" +
            "      left outer join yd_customers_all c on a.yd_customer_id= c.yd_id\n" +
            "       left outer join yd_accounts_all dc on c.yd_customer_no = dc.yd_customer_no\n" +
            "       left outer join yd_account_public ac on dc.yd_id = ac.yd_account_id\n" +
            "       where a.yd_reg_address is not null and ac.yd_acct_type=''jiben'' and 1=1 and yd_acct_no not in(\n" +
            "                 select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=yd_acct_no)) WHERE 1=1 ';\n" +
            "    dbms_output.put_line('V_SQL4--' || V_SQL);\n" +
            "    EXECUTE IMMEDIATE V_SQL;\n" +
            "\n" +
            "    commit;\n" +
            "    END IF;\n" +
            "  END;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2005_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2005_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-06-17\n" +
            "  description:开户人频繁开户、注销或变更账户信息2.0\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL         VARCHAR2(3000);\n" +
            "  V_SQL_1       VARCHAR2(3000);\n" +
            "  V_SQL_2       VARCHAR2(3000);\n" +
            "  V_RISK_ID     VARCHAR2(200);\n" +
            "  V_MODEL_NAME  VARCHAR2(250);\n" +
            "  V_PROC_NAME   VARCHAR2(250);\n" +
            "  V_STATUS      VARCHAR2(2);\n" +
            "  V_CNT         INTEGER;\n" +
            "  V_CNT_EXITS   INTEGER;\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_CNT_1       INTEGER;\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  V_LEGAL_IDCARD_NO VARCHAR2(64);\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2005_application';\n" +
            "  V_RISK_ID   := 'RISK_2005';\n" +
            "\n" +
            "  BEGIN\n" +
            "    V_SQL_1 := 'select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "               ''',''yyyy-MM-dd''),yd_customer_no,risk_id,cnt,yd_legal_idcard_no,\n" +
            "             ''同一法人或负责人''||yd_legal_idcard_no||''已经开立了多(' ||\n" +
            "               get('' || V_RISK_ID || '', 'rn') || ')个账户！''   ';\n" +
            "    V_SQL_2 := 'select count(1)  ';\n" +
            "    V_SQL   := '  from ( select distinct ''' || V_RISK_ID ||  ''' as risk_id,a.yd_legal_idcard_no,b.yd_customer_no\n" +
            "     ,count(b.yd_customer_no)  over(partition by a.yd_legal_idcard_no) cnt\n" +
            "     from yd_customer_public a\n" +
            "     left outer join yd_customers_all b on a.yd_customer_id=b.yd_id\n" +
            "     left outer join yd_accounts_all c on b.yd_customer_no=c.yd_customer_no\n" +
            "     left outer join yd_account_public ac  on c.yd_id = ac.yd_account_id\n" +
            "      where 1 = 1\n" +
            "           and ac.yd_acct_type = ''jiben''\n" +
            "           and a.yd_legal_idcard_no is not null\n" +
            "      and c.yd_acct_no not in(\n" +
            "                 select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=c.yd_acct_no)  )\n" +
            "                 where 1=1 and cnt'||get('' || V_RISK_ID || '', 'rn');\n" +
            "\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            " dbms_output.put_line(  V_SQL_1 || V_SQL);\n" +
            "      execute immediate V_SQL_2 || V_SQL\n" +
            "        into V_CNT;\n" +
            "      commit;\n" +
            "      --获取风险数据存入风险结果表中\n" +
            "      open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT_1,\n" +
            "               V_LEGAL_IDCARD_NO,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "        --如果该风险已经发生，则不再重复发生\n" +
            "        --select count(1) into V_CNT_EXITS  from yd_risk_record_info tab where tab.yd_risk_id = V_RISK_ID and tab.yd_customer_id = V_CUSTOMER_NO;\n" +
            "        --IF V_CNT_EXITS = 0 THEN\n" +
            "          delete from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "         commit;\n" +
            "          insert into YD_RISK_RECORD_INFO(yd_id, yd_risk_date, yd_customer_id, yd_risk_id, yd_risk_cnt, yd_risk_desc, yd_status,  yd_created_date, yd_risk_point, Yd_Version_Ct,Yd_Deleted)\n" +
            "          values  (V_ID,  V_RISK_DATE, V_CUSTOMER_NO, V_RISK_ID, V_CNT_1, V_RISK_DESC, '0', sysdate, V_LEGAL_IDCARD_NO, '0',0);\n" +
            "           --  ELSE\n" +
            "         --  update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_customer_id=V_CUSTOMER_NO and yd_status='0';\n" +
            "\n" +
            "        --END IF;\n" +
            "      end loop;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2005_STOCK = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2005_STOCK\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:yangcq\n" +
            "  date:2019-5-25\n" +
            "  description:统计同一法定代表人或负责人已经开立多个账户，存入风险中间表中\n" +
            "  **/\n" +
            "AUTHID CURRENT_USER IS\n" +
            "  V_SQL        VARCHAR2(30000);\n" +
            "  V_PROC_NAME  VARCHAR2(100);\n" +
            "  V_MODEL_NAME VARCHAR2(200);\n" +
            "  V_RISK_ID    VARCHAR2(30);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2005_STOCK';\n" +
            "  V_RISK_ID   := 'RISK_2005';\n" +
            "  BEGIN\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "    IF V_STATUS = '1' THEN\n" +
            "      -- 删除既往数据\n" +
            "      delete from YD_RISK_MIDDLE_INFO where yd_risk_id = V_RISK_ID;\n" +
            "      commit;\n" +
            "      V_SQL := 'insert into YD_RISK_MIDDLE_INFO\n" +
            "     (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt,yd_risk_desc)\n" +
            "    select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "               ''',''yyyy-MM-dd''),yd_customer_no,risk_id,cnt,yd_legal_idcard_no  from (\n" +
            "    select distinct ''' || V_RISK_ID ||\n" +
            "               ''' as risk_id,a.yd_legal_idcard_no,b.yd_customer_no\n" +
            "     ,count(b.yd_customer_no)  over(partition by a.yd_legal_idcard_no) cnt\n" +
            "     from yd_customer_public a left outer join yd_customers_all b on a.yd_customer_id=b.yd_id left outer join\n" +
            "     yd_accounts_all c on b.yd_customer_no=c.yd_customer_no\n" +
            "     left outer join yd_account_public ac  on c.yd_id = ac.yd_account_id\n" +
            "      where 1 = 1\n" +
            "           and ac.yd_acct_type = ''jiben''\n" +
            "           and a.yd_legal_idcard_no is not null\n" +
            "           and c.yd_acct_no not in(\n" +
            "                 select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=c.yd_acct_no)  ) where 1=1 ';\n" +
            "      dbms_output.put_line('V_SQL--' || V_SQL);\n" +
            "      EXECUTE IMMEDIATE V_SQL;\n" +
            "      commit;\n" +
            "    END IF;\n" +
            "  END;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2006_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2006_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-06-17\n" +
            "  description:同一经办人员代理多个开立账户2.0\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL        VARCHAR2(3000);\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_SQL_2      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "  V_CNT        INTEGER;\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_CNT_1       INTEGER;\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  V_OPERATOR_IDCARD_NO VARCHAR2(64);\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2006_APPLICATION';\n" +
            "  V_RISK_ID   := 'RISK_2006';\n" +
            "\n" +
            "  BEGIN\n" +
            "    --查询详细信息头\n" +
            "    V_SQL_1 := 'select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "             ''',''yyyy-MM-dd''),yd_customer_no,risk_id,cnt ,yd_operator_idcard_no\n" +
            "             ''同一经办人(''||yd_operator_idcard_no||'')已经开立了多('||get('' || V_RISK_ID || '', 'rn')||')个账户！''';\n" +
            "  --查询信息数量头\n" +
            "   V_SQL_2 := 'select count(1)  ';\n" +
            "    V_SQL   := ' from (select\n" +
            "    c.yd_acct_no, b.yd_customer_no,''' || V_RISK_ID || ''' as risk_id,d.yd_operator_idcard_no\n" +
            "    ,count(distinct b.yd_customer_no)  over(partition by d.yd_operator_idcard_no) cnt\n" +
            "    from yd_customer_public a\n" +
            "    left outer join yd_customers_all b on a.yd_customer_id=b.yd_id\n" +
            "    left outer join yd_accounts_all c on b.yd_customer_no=c.yd_customer_no\n" +
            "    left outer join yd_account_public d on c.yd_id=d.yd_account_id\n" +
            "    where 1=1\n" +
            "    and  d.yd_operator_idcard_no is not null\n" +
            "    and d.yd_acct_type=''jiben''\n" +
            "    and c.yd_acct_no not in(\n" +
            "    select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=c.yd_acct_no) )\n" +
            "    where 1=1 and cnt '||get('' || V_RISK_ID || '', 'rn');\n" +
            "  --查询模型信息，用于判断是否启用\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            "        dbms_output.put_line('V_SQL--' ||  V_SQL_2 || V_SQL);\n" +
            "      execute immediate V_SQL_2 || V_SQL\n" +
            "        into V_CNT;\n" +
            "      commit;\n" +
            "    --获取风险数据存入风险结果表中\n" +
            "    open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT_1,\n" +
            "               V_OPERATOR_IDCARD_NO,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "\n" +
            "        --select count(1) into V_CNT_EXITS from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "        --IF V_CNT_EXITS =0 THEN\n" +
            "          delete from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "         commit;\n" +
            "          insert into YD_RISK_RECORD_INFO (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc,yd_status,Yd_Created_Date,yd_risk_point,Yd_Version_Ct,Yd_Deleted)\n" +
            "          values (V_ID,V_RISK_DATE,V_CUSTOMER_NO,V_RISK_ID,V_CNT_1,V_RISK_DESC,'0',sysdate,V_OPERATOR_IDCARD_NO,'0',0);\n" +
            "       -- ELSE\n" +
            "        --  update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_customer_id=V_CUSTOMER_NO and yd_status='0';\n" +
            "\n" +
            "       -- END IF;\n" +
            "      end loop;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String SP_RISK_2006_STOCK = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2006_STOCK\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:yangcq\n" +
            "  date:2019-5-25\n" +
            "  description:统计同一经办人员已经代理的开立账户\n" +
            "  address: yangcq\n" +
            "  **/\n" +
            "AUTHID CURRENT_USER IS\n" +
            "  V_SQL        VARCHAR2(30000);\n" +
            "  V_PROC_NAME  VARCHAR2(100);\n" +
            "  V_MODEL_NAME VARCHAR2(200);\n" +
            "  V_RISK_ID    VARCHAR2(30);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2006_STOCK';\n" +
            "  V_RISK_ID   := 'RISK_2006';\n" +
            "  BEGIN\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "    IF V_STATUS = '1' THEN\n" +
            "      -- 删除既往数据\n" +
            "       delete from YD_RISK_MIDDLE_INFO where yd_risk_id = V_RISK_ID;\n" +
            "      commit;\n" +
            "      V_SQL := 'insert into YD_RISK_MIDDLE_INFO\n" +
            "     (yd_id,yd_risk_date,yd_customer_id,yd_account_no,yd_risk_id,yd_risk_cnt,yd_risk_desc)\n" +
            "     select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "     ''',''yyyy-MM-dd''),yd_customer_no,'''',risk_id,cnt , yd_operator_idcard_no from (select\n" +
            "    c.yd_acct_no, b.yd_customer_no,''' || V_RISK_ID ||\n" +
            "               ''' as risk_id,d.yd_operator_idcard_no\n" +
            "    ,count(distinct b.yd_customer_no)  over(partition by d.yd_operator_idcard_no) cnt\n" +
            "    from yd_customer_public a left outer join yd_customers_all b on a.yd_customer_id=b.yd_id left outer join\n" +
            "      yd_accounts_all c on b.yd_customer_no=c.yd_customer_no left outer join yd_account_public d on c.yd_id=d.yd_account_id\n" +
            "       where 1=1\n" +
            "       and  d.yd_operator_idcard_no is not null\n" +
            "      and d.yd_acct_type=''jiben''\n" +
            "      and c.yd_acct_no not in(\n" +
            "      select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=c.yd_acct_no) ) where 1=1';\n" +
            "      dbms_output.put_line('V_SQL--' || V_SQL);\n" +
            "      EXECUTE IMMEDIATE V_SQL;\n" +
            "      commit;\n" +
            "    END IF;\n" +
            "  END;\n" +
            "END;\n" +
            "\n" +
            "\n";

    public static final String SP_RISK_2007_STOCK = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2007_STOCK\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description: 统计开户人已经开户、注销或变更账户的次数\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2007_STOCK';\n" +
            "  V_RISK_ID   := 'RISK_2007';\n" +
            "  BEGIN\n" +
            "\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "    if V_STATUS = '1' then\n" +
            "      -- 删除既往数据\n" +
            "      delete from YD_RISK_MIDDLE_INFO where yd_risk_id = V_RISK_ID;\n" +
            "      commit;\n" +
            "      V_SQL_1 := 'insert into YD_RISK_MIDDLE_INFO\n" +
            "         (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc)\n" +
            "        select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' ||\n" +
            "                 I_DATE || ''',''yyyy-MM-dd''),yd_customer_no,\n" +
            "                   ''' || V_RISK_ID ||\n" +
            "                 ''',cnt,\n" +
            "                   ''同一单位客户在' ||\n" +
            "                 get('' || V_RISK_ID || '', 'ts') || '天内发生' ||\n" +
            "                 get('' || V_RISK_ID || '', 'rn') ||\n" +
            "                 '次以上的开户、销户或变更信息操作！''\n" +
            "                   from (select t.yd_customer_no,count(1) as  cnt from yd_account_bills_all t\n" +
            "                    left join yd_accounts_all t1 on  t1.yd_ref_bill_id = t.yd_id\n" +
            "                     left outer join yd_account_public ac\n" +
            "                on t1.yd_id = ac.yd_account_id\n" +
            "        where t.yd_bill_date BETWEEN to_char(sysdate -' ||\n" +
            "                 get('' || V_RISK_ID || '', 'ts') ||\n" +
            "                 ',''yyyy-MM-dd'')\n" +
            "        and to_char(sysdate,''yyyy-MM-dd'')\n" +
            "        and t.yd_acct_no not in(\n" +
            "                     select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t.yd_acct_no)\n" +
            "                     and t.yd_bill_type in(''ACT_OPEN'',''ACCT_CHANGE'',''ACCT_REVOKE'',''ACCT_INIT'')\n" +
            "                      group by t.yd_customer_no) where 1=1 ';\n" +
            "      dbms_output.put_line('V_SQL_1--' || V_SQL_1);\n" +
            "      execute immediate V_SQL_1;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "END;\n";

    public static final String SP_RISK_2008_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2008_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:yangcq\n" +
            "  date:2019-6-13\n" +
            "  description:同一主体的法定代表人、财务负责人、财务经办人预留同一个联系方式\n" +
            "  **/\n" +
            "AUTHID CURRENT_USER IS\n" +
            "  V_SQL        VARCHAR2(30000);\n" +
            "  V_PROC_NAME  VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(200);\n" +
            "  V_RISK_ID    VARCHAR2(30);\n" +
            "  V_FIN_TEL    VARCHAR2(30); --财务负责人\n" +
            "  V_LEG_TEL    VARCHAR2(30); --法定代表人\n" +
            "  V_OPER_TEL   VARCHAR2(30); --财务经办人\n" +
            "  V_CUSTOMER_NO    VARCHAR2(64);\n" +
            "  V_STATUS         VARCHAR2(2);\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2008_APPLICATION';\n" +
            "  V_RISK_ID   := 'RISK_2008';\n" +
            "  declare\n" +
            "    cursor c_riskInfo is\n" +
            "      select distinct a.yd_finance_telephone,\n" +
            "                      a.yd_legal_telephone,\n" +
            "                      ac.yd_operator_telephone,\n" +
            "                      dc.yd_customer_no\n" +
            "        from yd_customer_public a\n" +
            "        left outer join yd_customers_all c on a.yd_customer_id = c.yd_id\n" +
            "        left outer join yd_accounts_all dc on c.yd_customer_no = dc.yd_customer_no\n" +
            "        left outer join yd_account_public ac on dc.yd_id = ac.yd_account_id\n" +
            "       where a.yd_finance_telephone is not null\n" +
            "         and a.yd_legal_telephone is not null\n" +
            "         and ac.yd_operator_telephone is not null\n" +
            "         and ac.yd_acct_type = 'jiben';\n" +
            "  BEGIN\n" +
            "    /*获取模型描述*/\n" +
            "    select yd_name,yd_status\n" +
            "      into V_MODEL_NAME,V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "\n" +
            "      IF V_STATUS='1' THEN\n" +
            "    open c_riskInfo;\n" +
            "    Loop\n" +
            "      fetch c_riskInfo\n" +
            "        into V_FIN_TEL, V_LEG_TEL, V_OPER_TEL, V_CUSTOMER_NO;\n" +
            "      exit when c_riskInfo%NOTFOUND;\n" +
            "      --将同一主体中法定代表人、财务负责人、财务经办人预留了同一个联系号码,插入风险结果表中\n" +
            "      if V_FIN_TEL = V_LEG_TEL and V_LEG_TEL = V_OPER_TEL then\n" +
            "      --select count(1) into V_CNT_EXITS from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "      --IF V_CNT_EXITS =0 THEN\n" +
            "        delete from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "         commit;\n" +
            "        insert into yd_risk_record_info(yd_id,yd_risk_date, yd_customer_id, yd_risk_id, yd_risk_desc, yd_status, yd_created_date, Yd_Version_Ct, yd_risk_point,Yd_Deleted)\n" +
            "        VALUES  (RISK_RECORD_INFO_SEQ.Nextval, TO_DATE('' || I_DATE || '', 'yyyy-MM-dd'),\n" +
            "           V_CUSTOMER_NO, '' || V_RISK_ID || '', '同一客户('''||V_CUSTOMER_NO||''')中法定代表人、财务负责人、财务经办人预留了同一个联系号码'||V_FIN_TEL ,'0',sysdate,'0',V_FIN_TEL,0);\n" +
            "       -- ELSE\n" +
            "      -- update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_customer_id=V_CUSTOMER_NO and yd_status='0' ;\n" +
            "      --END IF;\n" +
            "      end if;\n" +
            "    end loop;\n" +
            "    close c_riskInfo;\n" +
            "        commit;\n" +
            "    END IF;\n" +
            "  END;\n" +
            "Exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";

    public static final String SP_RISK_2009_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2009_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description:基本开户人频繁开户、注销或变更账户信息\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL        VARCHAR2(3000);\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_SQL_2      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "  V_CNT        INTEGER;\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "  V_ID          VARCHAR2(64);\n" +
            "  V_RISK_DATE   VARCHAR2(64);\n" +
            "  V_CUSTOMER_NO VARCHAR2(64);\n" +
            "  V_CNT_1       INTEGER;\n" +
            "  V_RISK_DESC   VARCHAR2(500);\n" +
            "  type risk_cousor is ref cursor;\n" +
            "  V_riskArray risk_cousor;\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2009_application';\n" +
            "  V_RISK_ID   := 'RISK_2009';\n" +
            "\n" +
            "  BEGIN\n" +
            "    V_SQL_1 := 'select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' || I_DATE ||\n" +
            "               ''',''yyyy-MM-dd''),yd_customer_no,\n" +
            "               ''' || V_RISK_ID || ''',cnt,\n" +
            "               ''同一单位客户在' || get('' || V_RISK_ID || '', 'ts') ||\n" +
            "               '天内发生' || get('' || V_RISK_ID || '', 'rn') ||\n" +
            "               '次以上的开户、销户或变更信息操作！'' ';\n" +
            "    V_SQL_2 := 'select count(1)  ';\n" +
            "    V_SQL   := ' from (select t.yd_customer_no,count(1) as  cnt from yd_account_bills_all t\n" +
            "                left join yd_accounts_all t1 on  t1.yd_ref_bill_id = t.yd_id\n" +
            "                left outer join yd_account_public ac on t1.yd_id = ac.yd_account_id\n" +
            "                where t.yd_bill_date BETWEEN to_char(sysdate -' || get('' || V_RISK_ID || '', 'ts') ||',''yyyy-MM-dd'')\n" +
            "                and to_char(sysdate,''yyyy-MM-dd'')\n" +
            "                and t.yd_acct_no not in(\n" +
            "                select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t.yd_acct_no)\n" +
            "                and t.yd_bill_type in(''ACT_OPEN'',''ACCT_CHANGE'',''ACCT_REVOKE'',''ACCT_INIT''\n" +
            "                group by t.yd_customer_no) where 1=1 and cnt' || get('' || V_RISK_ID || '', 'rn');\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "        dbms_output.put_line('V_SQL_2--' || V_SQL_1 || V_SQL);\n" +
            "\n" +
            "    if V_STATUS = '1' then\n" +
            "      execute immediate V_SQL_2 || V_SQL\n" +
            "        into V_CNT;\n" +
            "      commit;\n" +
            "    --获取风险数据存入风险结果表中\n" +
            "    open V_riskArray for V_SQL_1 || V_SQL;\n" +
            "      Loop\n" +
            "        fetch V_riskArray\n" +
            "          into V_ID,\n" +
            "               V_RISK_DATE,\n" +
            "               V_CUSTOMER_NO,\n" +
            "               V_RISK_ID,\n" +
            "               V_CNT_1,\n" +
            "               V_RISK_DESC;\n" +
            "        exit when V_riskArray%NOTFOUND;\n" +
            "\n" +
            "       -- select count(1) into V_CNT_EXITS from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "        delete from yd_risk_record_info tab where tab.yd_risk_id=V_RISK_ID and tab.yd_customer_id=V_CUSTOMER_NO;\n" +
            "         commit;\n" +
            "       --IF V_CNT_EXITS =0 THEN\n" +
            "          insert into YD_RISK_RECORD_INFO (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc, yd_status,  yd_created_date, yd_risk_point, Yd_Version_Ct,Yd_Deleted)\n" +
            "          values (V_ID,V_RISK_DATE,V_CUSTOMER_NO,V_RISK_ID,V_CNT_1,V_RISK_DESC, '0', sysdate, V_CUSTOMER_NO, '0',0);\n" +
            "        --ELSE\n" +
            "          --dbms_output.put_line( V_CUSTOMER_NO ||'---'|| V_RISK_ID);\n" +
            "          --update YD_RISK_RECORD_INFO set yd_risk_date = sysdate where yd_risk_id=V_RISK_ID and yd_customer_id=V_CUSTOMER_NO and yd_status='0';\n" +
            "         commit;\n" +
            "       -- END IF;\n" +
            "      end loop;\n" +
            "\n" +
            "    end if;\n" +
            "  END;\n" +
            "exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n";

    public static final String SP_RISK_2009_STOCK = "CREATE OR REPLACE PROCEDURE \"SP_RISK_2009_STOCK\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:杨春秋\n" +
            "  date:2019-5-20\n" +
            "  description: 统计开户人已经开户、注销或变更账户的次数\n" +
            "  **/\n" +
            " as\n" +
            "  V_SQL_1      VARCHAR2(3000);\n" +
            "  V_RISK_ID    VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(250);\n" +
            "  V_PROC_NAME  VARCHAR2(250);\n" +
            "  V_STATUS     VARCHAR2(2);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_2009_STOCK';\n" +
            "  V_RISK_ID   := 'RISK_2009';\n" +
            "  BEGIN\n" +
            "\n" +
            "    select yd_name, yd_status\n" +
            "      into V_MODEL_NAME, V_STATUS\n" +
            "      from yd_risk_models\n" +
            "     where yd_deleted = '0'\n" +
            "       and yd_model_id = '' || V_RISK_ID || '';\n" +
            "    if V_STATUS = '1' then\n" +
            "      -- 删除既往数据\n" +
            "      delete from YD_RISK_MIDDLE_INFO where yd_risk_id = V_RISK_ID;\n" +
            "      commit;\n" +
            "      V_SQL_1 := 'insert into YD_RISK_MIDDLE_INFO\n" +
            "         (yd_id,yd_risk_date,yd_customer_id,yd_risk_id,yd_risk_cnt ,yd_risk_desc)\n" +
            "        select RISK_RECORD_INFO_SEQ.Nextval,TO_DATE(''' ||\n" +
            "                 I_DATE || ''',''yyyy-MM-dd''),yd_customer_no,\n" +
            "                   ''' || V_RISK_ID ||\n" +
            "                 ''',cnt,\n" +
            "                   ''同一单位客户在' ||\n" +
            "                 get('' || V_RISK_ID || '', 'ts') || '天内发生' ||\n" +
            "                 get('' || V_RISK_ID || '', 'rn') ||\n" +
            "                 '次以上的开户、销户或变更信息操作！''\n" +
            "                   from (select t.yd_customer_no,count(1) as  cnt from yd_account_bills_all t\n" +
            "                    left join yd_accounts_all t1 on  t1.yd_ref_bill_id = t.yd_id\n" +
            "                     left outer join yd_account_public ac\n" +
            "                on t1.yd_id = ac.yd_account_id\n" +
            "        where t.yd_bill_date BETWEEN to_char(sysdate -' ||\n" +
            "                 get('' || V_RISK_ID || '', 'ts') ||\n" +
            "                 ',''yyyy-MM-dd'')\n" +
            "        and to_char(sysdate,''yyyy-MM-dd'')\n" +
            "        and t.yd_acct_no not in(\n" +
            "                     select rw.yd_account_id from yd_risk_white_list rw where rw.yd_account_id=t.yd_acct_no)\n" +
            "                     and t.yd_bill_type in(''ACT_OPEN'',''ACCT_CHANGE'',''ACCT_REVOKE'',''ACCT_INIT'')\n" +
            "                      group by t.yd_customer_no) where 1=1 ';\n" +
            "      dbms_output.put_line('V_SQL_1--' || V_SQL_1);\n" +
            "      execute immediate V_SQL_1;\n" +
            "      commit;\n" +
            "    end if;\n" +
            "  END;\n" +
            "END;\n";

    public static final String SP_RISK_DATA_APPLICATION = "CREATE OR REPLACE PROCEDURE \"SP_RISK_DATA_APPLICATION\" (I_DATE IN VARCHAR2)\n" +
            "/**\n" +
            "  author:yangcq\n" +
            "  date:2019-6-25\n" +
            "  description: 统计风险数据\n" +
            "  **/\n" +
            "as\n" +
            "  V_PROC_NAME  VARCHAR2(200);\n" +
            "  V_MODEL_NAME VARCHAR2(200);\n" +
            "  V_CNT_EXITS  INTEGER;\n" +
            "\n" +
            "  V_RISK_DATA  VARCHAR2(64);\n" +
            "  V_ACCOUNT_NO VARCHAR2(64);\n" +
            "  V_RISK_DESC  VARCHAR2(1000);\n" +
            "  V_RISK_ID    VARCHAR2(30);\n" +
            "  V_RISK_POINT VARCHAR2(200);\n" +
            "BEGIN\n" +
            "\n" +
            "  V_PROC_NAME := 'SP_RISK_DATA_APPLICATION';\n" +
            "  V_RISK_ID   := 'RISK_DATA';\n" +
            "  declare\n" +
            "    cursor c_riskData is\n" +
            "      select distinct t.yd_risk_date ,\n" +
            "                      a.yd_acct_no,\n" +
            "                      t.yd_risk_desc,\n" +
            "                      t.yd_risk_id,\n" +
            "                      t.yd_risk_point\n" +
            "        FROM yd_risk_record_info t\n" +
            "        left outer join yd_accounts_all a\n" +
            "          on t.yd_customer_id = a.yd_customer_no\n" +
            "       where 1 = 1 " +
            "union all\n" +
            "               select distinct t.yd_risk_date ,\n" +
            "                      a.yd_acct_no,\n" +
            "                      t.yd_risk_desc,\n" +
            "                      t.yd_risk_id,\n" +
            "                      t.yd_risk_point\n" +
            "        FROM yd_risk_trade_info t\n" +
            "        left outer join yd_accounts_all a\n" +
            "          on t.yd_customer_id = a.yd_customer_no\n" +
            "          where 1 = 1" +
            "union all\n" +
            "               select distinct t.yd_risk_date ,\n" +
            "                      a.yd_acct_no,\n" +
            "                      t.yd_risk_desc,\n" +
            "                      t.yd_risk_id,\n" +
            "                      t.yd_risk_point\n" +
            "        FROM yd_risk_check_info t\n" +
            "        left outer join yd_accounts_all a\n" +
            "          on t.yd_customer_id = a.yd_customer_no\n" +
            "       where 1 = 1;\n" +
            "  BEGIN\n" +
            "\n" +
            "    --将风险数据插入风险处理表中\n" +
            "    open c_riskData;\n" +
            "    Loop\n" +
            "      fetch c_riskData\n" +
            "        into V_RISK_DATA, V_ACCOUNT_NO, V_RISK_DESC, V_RISK_ID, V_RISK_POINT;\n" +
            "      exit when c_riskData%NOTFOUND;\n" +
            "      select count(1)\n" +
            "        into V_CNT_EXITS\n" +
            "        from yd_risk_handle_info\n" +
            "       where yd_account_no = V_ACCOUNT_NO and yd_risk_id=V_RISK_ID\n" +
            "         and yd_status = '0';\n" +
            "\n" +
            "      if V_CNT_EXITS = 0 then\n" +
            "        insert into yd_risk_handle_info (yd_id, yd_risk_date,yd_account_no, yd_risk_id,\n" +
            "        yd_risk_point, yd_risk_desc, yd_status, yd_created_date, Yd_Version_Ct, Yd_Deleted)\n" +
            "        VALUES\n" +
            "        (RISK_RECORD_INFO_SEQ.Nextval, V_RISK_DATA, V_ACCOUNT_NO, V_RISK_ID, V_RISK_POINT,\n" +
            "         V_RISK_DESC, '0', sysdate, 0, 0);\n" +
            "          commit;\n" +
            "      end if;\n" +
            "    end loop;\n" +
            "    close c_riskData;\n" +
            "  END;\n" +
            "Exception\n" +
            "  when others then\n" +
            "    rollback;\n" +
            "END;\n" +
            "\n" +
            "\n";


    public static final String GET = "CREATE OR REPLACE FUNCTION GET(modelid IN VARCHAR2,\n" +
            "                                         fieldid  IN VARCHAR2)\n" +
            " RETURN VARCHAR2 IS\n" +
            "  O_FIELD_SQL      VARCHAR2(4000); --查询SQL\n" +
            "BEGIN\n" +
            " select b.YD_CON_AND_VAL into O_FIELD_SQL from YD_RISK_RULE_FIELD a\n" +
            " left outer join YD_RISK_RULE_CONFIGURATION b on a.yd_id=b.yd_rule_id\n" +
            " where a.yd_deleted='0' and b.yd_deleted='0'\n" +
            " and yd_model_id=modelid and yd_field=fieldid;\n" +
            "  RETURN O_FIELD_SQL;\n" +
            "END;";


    public static final String sequence = "create sequence RISK_RECORD_INFO_SEQ\n" +
            "minvalue 1\n" +
            "maxvalue 9999999999999999\n" +
            "start with 1000000000604300\n" +
            "increment by 1\n" +
            "cache 20";
}
