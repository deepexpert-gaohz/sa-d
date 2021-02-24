package com.ideatech.ams.risk.model.utils;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Component;

import java.sql.Types;


/**
 * author:liuz 2019-05-06
 * oracle方言类 用于解决navchar非hibernate方言问题
 */
public class MyOracleDialect  extends Oracle10gDialect {
    public MyOracleDialect() {
        super();
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
    }
}
