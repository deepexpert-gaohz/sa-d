package com.ideatech.common.jpa;

import org.hibernate.dialect.DB2Dialect;

/**
 * 自定义数据库方言，修改建表时CLOB长度默认255的问题
 *
 * @author fantao
 * @date 2019-06-05 15:00
 */
public class IdeaDB2Dialect extends DB2Dialect {

    public IdeaDB2Dialect() {
        this.registerColumnType(2004, "blob");
        this.registerColumnType(2005, "clob");
    }
}
