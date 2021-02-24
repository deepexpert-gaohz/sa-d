package com.ideatech.ams.risk.initializer;

import com.ideatech.ams.risk.tradeRisk.entity.Risk2009;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2010;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2011;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2012;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2013;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2014;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2015;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2016;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2017;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2019;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2020;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2021;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2022;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2023;
import com.ideatech.ams.risk.tradeRisk.entity.Risk2024;
import com.ideatech.common.entity.DealBase;
import com.ideatech.common.entity.util.Comment;
import com.ideatech.common.initializer.AbstractDataInitializer;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class RiskComInitiazer extends AbstractDataInitializer {
    @Autowired
    EntityManager em;

    @Override
    protected void doInit() throws Exception {
        List<Class<?>> clasList = new ArrayList<>();
        clasList.add(new Risk2009().getClass());
        clasList.add(new Risk2010().getClass());
        clasList.add(new Risk2011().getClass());
        clasList.add(new Risk2012().getClass());
        clasList.add(new Risk2013().getClass());
        clasList.add(new Risk2014().getClass());
        clasList.add(new Risk2015().getClass());
        clasList.add(new Risk2016().getClass());
        clasList.add(new Risk2017().getClass());
        clasList.add(new Risk2019().getClass());
        clasList.add(new Risk2020().getClass());
        clasList.add(new Risk2021().getClass());
        clasList.add(new Risk2022().getClass());
        clasList.add(new Risk2023().getClass());
        clasList.add(new Risk2024().getClass());
        //父类字段
        DealBase dealBase = new DealBase();
        Field[] dealBaseFields = dealBase.getClass().getDeclaredFields();
        for (Class<?> cla : clasList) {
            exComment(cla.getDeclaredFields(), dealBaseFields, cla);
        }
    }

    public void exComment(Field[] fields, Field[] dealBaseFields, Class<?> riskClass) {
        createComment(fields, riskClass);
        createComment(dealBaseFields, riskClass);
    }

    public void createComment(Field[] fields, Class<?> riskClass) {
        Table table = riskClass.getAnnotation(Table.class);
        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                // 选出包含注解@Comment和@Column的类
                if (field.isAnnotationPresent(Comment.class) && field.isAnnotationPresent(Column.class)) {
                    Comment comment = field.getAnnotation(Comment.class);
                    Column column = field.getAnnotation(Column.class);
                    String sql = "comment on column yd_" + table.name() + ".yd_" + column.name() + " is '" + comment.value() + "'";
                    Query nativeQuery = em.createNativeQuery(sql);
                    nativeQuery.executeUpdate();
                }
            }
        }
    }

    @Override
    protected boolean isNeedInit() {
        return true;
    }

    @Override
    public Integer getIndex() {
        return -200;
    }


}

