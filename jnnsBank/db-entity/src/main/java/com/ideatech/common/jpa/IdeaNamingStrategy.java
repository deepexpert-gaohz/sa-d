/**
 *
 */
package com.ideatech.common.jpa;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.io.Serializable;
import java.util.Locale;

/**
 * 数据库表的命名策略。<br>
 * 当前的实现是在表名和字段名前加'yd_'，然后所有表名和字段名全都大写
 *
 * @author fantao
 * @see PhysicalNamingStrategy
 */
@SuppressWarnings({"serial", "deprecation"})
public class IdeaNamingStrategy implements PhysicalNamingStrategy, Serializable {

    public static final String PREFIX = "yd_";

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return this.apply(name, jdbcEnvironment);
    }


    private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        } else {
            String nameText = name.getText();
            if (!nameText.startsWith(PREFIX)) {
                nameText = PREFIX + name.getText();
            }
            StringBuilder builder = new StringBuilder(nameText.replace('.', '_'));

            for (int i = 1; i < builder.length() - 1; i++) {
    			if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
    					builder.charAt(i + 1))) {
    				builder.insert(i++, '_');
    			}
    		}

            return this.getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
        }
    }

    protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        if (this.isCaseInsensitive(jdbcEnvironment)) {
            name = name.toLowerCase(Locale.ROOT);
        }

        return new Identifier(name, quoted);
    }

    protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
        return true;
    }

    private boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
    }

}
