package com.ideatech.ams.account.util;

import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.OpenAccountSiteType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.jpa.IdeaNamingStrategy;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vantoo
 * @date 10:24 2018/5/29
 */
@Slf4j
public class Map2DomainUtils {

    private static Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    public static Object converter(Map<String, String> formMap, Class<?> clazz) throws Exception {
        Map<String, Object> target = new HashMap<String, Object>(16);
        Object object = clazz.newInstance();
        if (MapUtils.isEmpty(formMap)) {
            return object;
        }
        target.putAll(formMap);
        // 该对象所有字段
        Map<String, Class> refMap = ReflectUtils.getDeclaredFieldAndType(clazz);
        // 循环需要保存的map
        for (Map.Entry<String, Object> entry : target.entrySet()) {
            // 数据库中的字段名
            String key = entry.getKey();
            key = key.replaceAll("_", "");
            for (Map.Entry<String, Class> refEntry : refMap.entrySet()) {
                // map中的字段名，不包含yd_前缀
                String field = refEntry.getKey();

                Class<?> fieldClass = refEntry.getValue();
                String prefix = IdeaNamingStrategy.PREFIX.replace("_", "");
                if (StringUtils.equalsIgnoreCase(key, prefix + field) || StringUtils.equalsIgnoreCase(key, IdeaNamingStrategy.PREFIX + field) || StringUtils.equalsIgnoreCase(key, field)) {

                    if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
                        String value = entry.getValue().toString();
                        if ("null".equals(value) || (fieldClass.isAssignableFrom(BillStatus.class) && isContainChinese(value))) {
                            continue;
                        }
                        Object obj = null;
                        if (fieldClass.isAssignableFrom(AccountStatus.class)) {
                            obj = AccountStatus.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(SyncCheckStatus.class)) {
                            obj = SyncCheckStatus.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(CompanySyncOperateType.class)) {
                            obj = CompanySyncOperateType.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(BillType.class)) {
                            obj = BillType.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(CompanySyncStatus.class)) {
                            obj = CompanySyncStatus.valueOf(value);
                        } /*else if (fieldClass.isAssignableFrom(CompanyVerifyStatus.class)) {
                            obj = CompanyVerifyStatus.valueOf(value);
                        } */ else if (fieldClass.isAssignableFrom(CompanyAmsCheckStatus.class)) {
                            obj = CompanyAmsCheckStatus.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(BillStatus.class)) {
                            obj = BillStatus.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(CompanyIfType.class)) {
                            obj = CompanyIfType.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(AccountClass.class)) {
                            obj = AccountClass.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(CompanyAcctType.class)) {
                            obj = CompanyAcctType.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(CompanySyncOperateType.class)) {
                            obj = CompanySyncOperateType.valueOf(value);
                        }else if(fieldClass.isAssignableFrom(OpenAccountSiteType.class)){
                            obj = OpenAccountSiteType.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(CustomerType.class)) {
                            obj = CustomerType.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(BillFromSource.class)) {
                            obj = BillFromSource.valueOf(value);
                        } else if (fieldClass.isAssignableFrom(BigDecimal.class)) {
                            try {
                                obj = new BigDecimal(value);
                            } catch (Exception e) {
                                obj = new BigDecimal("0");
                            }
                        } else if (fieldClass.isAssignableFrom(Long.class)) {
                            obj = Long.parseLong(value);
                        } else if (fieldClass.isAssignableFrom(Set.class)) {
                            // set类型跳过
                            continue;
                        } else if (fieldClass.isAssignableFrom(String.class)) {
                            // 下面做处理
                            obj = value;
                        } else {
                            log.info("类型为" + fieldClass + "的值" + value + "转换失败");
                        }
                        if (obj != null) {
                            BeanValueUtils.setValue(object, field, obj);
                        }
                    } else {
                        if (fieldClass.isAssignableFrom(String.class)) {
                            BeanValueUtils.setValue(object, field, "");
                        }
                    }

                }
            }
        }
        return object;
    }

    public static boolean isContainChinese(String str) {
        Matcher m = CHINESE_PATTERN.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
