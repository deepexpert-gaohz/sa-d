package com.ideatech.ams.account.service.core;

import com.ideatech.ams.account.enums.AccountClass;
import com.ideatech.ams.account.enums.AcctBigType;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.*;
import com.ideatech.ams.customer.enums.CustomerType;
import com.ideatech.ams.pbc.enums.AccountStatus;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.BeanValueUtils;
import com.ideatech.common.util.ReflectUtils;
import com.ideatech.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author vantoo
 *
 */
@Component
@Slf4j
public class Map2DomainServiceImpl implements Map2DomainService {

	private static final Logger LOG = LoggerFactory.getLogger(Map2DomainServiceImpl.class);

	private static final String PREFIX = "yd_";

	@Override
	public Object converter(Map<String, String> formMap, Class<?> clazz) throws Exception {
		Map<String, Object> target = new CaseInsensitiveMap(16);
		Object object = clazz.newInstance();
		if (MapUtils.isEmpty(formMap)) {
			return object;
		}
		target.putAll(formMap);
		// 该对象所有字段
		Map<String, Class> refMap = ReflectUtils.getDeclaredFieldAndType(clazz);
		// 循环需要保存的map

		// 0917性能优化修改开始

		for (Entry<String, Class> stringClassEntry : refMap.entrySet()) {
			String field = stringClassEntry.getKey();
			String dataKey = StringUtil.changeColumnToDataInCache(PREFIX+field);
			if (!target.containsKey(dataKey)) {
				dataKey = StringUtil.changeColumnToDataInCache(field);
			}
			Object dataValue = target.get(dataKey);
			Class fieldClass = stringClassEntry.getValue();
			if (dataValue != null) {
				String value = dataValue.toString();
				if("null".equals(value) || "".equalsIgnoreCase(value) || (fieldClass.isAssignableFrom(BillStatus.class) && isContainChinese(value))){//
					continue;
				}
				Object obj = null;
				if (fieldClass.isAssignableFrom(String.class)) {
					// 下面做处理
					//九江银行要求去掉时间戳后面的毫秒
					if(field.equals("createdDate")){
						if(value.indexOf(".") != -1) {
							obj = value.substring(0, value.indexOf("."));
						} else {
							obj = value;
						}
					}else{
						obj = value;
					}
				} else if (fieldClass.isAssignableFrom(AccountStatus.class)) {
					obj = AccountStatus.str2enum(value);
				} else if (fieldClass.isAssignableFrom(com.ideatech.ams.account.enums.AccountStatus.class)) {
					obj = com.ideatech.ams.account.enums.AccountStatus.str2enum(value);
				} else if (fieldClass.isAssignableFrom(SyncCheckStatus.class)) {
					obj = SyncCheckStatus.str2enum(value);
				} /*else if (fieldClass.isAssignableFrom(CompanyOperateType.class)) {
							obj = CompanyOperateType.valueOf(value);
						} */else if (fieldClass.isAssignableFrom(CompanySyncStatus.class)) {
					obj = CompanySyncStatus.str2enum(value);
				} /*else if (fieldClass.isAssignableFrom(CompanyVerifyStatus.class)) {
							obj = CompanyVerifyStatus.valueOf(value);
						} */else if (fieldClass.isAssignableFrom(CompanyAmsCheckStatus.class)) {
					obj = CompanyAmsCheckStatus.str2enum(value);
				} else if (fieldClass.isAssignableFrom(BillStatus.class)) {
					obj = BillStatus.str2enum(value);
				} else if (fieldClass.isAssignableFrom(CompanyIfType.class)) {
					obj = CompanyIfType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(AccountClass.class)) {
					obj = AccountClass.str2enum(value);
				} else if (fieldClass.isAssignableFrom(CompanyAcctType.class)) {
					obj = CompanyAcctType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(CompanySyncOperateType.class)) {
					obj = CompanySyncOperateType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(CustomerType.class)) {
					obj = CustomerType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(AcctBigType.class)) {
					obj = AcctBigType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(BillType.class)) {
					obj = BillType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(com.ideatech.common.enums.BillType.class)) {
					obj = com.ideatech.common.enums.BillType.str2enum(value);
				} else if (fieldClass.isAssignableFrom(BillFromSource.class)) {
					obj = BillFromSource.str2enum(value);
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
				} else {
					log.info("类型为" + fieldClass + "的值" + value + "转换失败");
				}
				if (obj != null)
					BeanValueUtils.setValue(object, field, obj);
			} else if( fieldClass.isAssignableFrom(String.class) ){
				BeanValueUtils.setValue(object, field, "");
			}

		}
		// 0917性能优化修改结束

//		for (Entry<String, String> entry : target.entrySet()) {
//			// 数据库中的字段名
//			String key = entry.getKey();
//			for (Entry<String, Class> refEntry : refMap.entrySet()) {
//				// map中的字段名，不包含yd_前缀
//				String field = refEntry.getKey();
//
//				Class<?> fieldClass = refEntry.getValue();
//				if (StringUtils.equalsIgnoreCase(key, StringUtil.changeColumnToDataInCache(PREFIX + field)) || StringUtils.equalsIgnoreCase(key, StringUtil.changeColumnToDataInCache(field))) {
//
//					if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
//						String value = entry.getValue().toString();
//						if("null".equals(value) || (fieldClass.isAssignableFrom(BillStatus.class) && isContainChinese(value))){//
//							continue;
//						}
//						Object obj = null;
//						if (fieldClass.isAssignableFrom(AccountStatus.class)) {
//							obj = AccountStatus.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(com.ideatech.ams.account.enums.AccountStatus.class)) {
//							obj = com.ideatech.ams.account.enums.AccountStatus.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(SyncCheckStatus.class)) {
//							obj = SyncCheckStatus.str2enum(value);
//						} /*else if (fieldClass.isAssignableFrom(CompanyOperateType.class)) {
//							obj = CompanyOperateType.valueOf(value);
//						} */else if (fieldClass.isAssignableFrom(CompanySyncStatus.class)) {
//							obj = CompanySyncStatus.str2enum(value);
//						} /*else if (fieldClass.isAssignableFrom(CompanyVerifyStatus.class)) {
//							obj = CompanyVerifyStatus.valueOf(value);
//						} */else if (fieldClass.isAssignableFrom(CompanyAmsCheckStatus.class)) {
//							obj = CompanyAmsCheckStatus.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(BillStatus.class)) {
//							obj = BillStatus.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(CompanyIfType.class)) {
//							obj = CompanyIfType.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(AccountClass.class)) {
//							obj = AccountClass.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(CompanyAcctType.class)) {
//							obj = CompanyAcctType.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(CompanySyncOperateType.class)) {
//							obj = CompanySyncOperateType.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(CustomerType.class)) {
//							obj = CustomerType.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(AcctBigType.class)) {
//							obj = AcctBigType.str2enum(value);
//						} else if (fieldClass.isAssignableFrom(BigDecimal.class)) {
//							try {
//								obj = new BigDecimal(value);
//							} catch (Exception e) {
//								obj = new BigDecimal("0");
//							}
//						} else if (fieldClass.isAssignableFrom(Long.class)) {
//							obj = Long.parseLong(value);
//						} else if (fieldClass.isAssignableFrom(Set.class)) {
//							// set类型跳过
//							continue;
//						} else if (fieldClass.isAssignableFrom(String.class)) {
//							// 下面做处理
//							//九江银行要求去掉时间戳后面的毫秒
//							if(field.equals("createdDate")){
//								if(value.indexOf(".") != -1) {
//									obj = value.substring(0, value.indexOf("."));
//								} else {
//									obj = value;
//								}
//							}else{
//								obj = value;
//							}
//						} else {
//							log.info("类型为" + fieldClass + "的值" + value + "转换失败");
//						}
//						if (obj != null)
//							BeanValueUtils.setValue(object, field, obj);
//					} else {
//						if (fieldClass.isAssignableFrom(String.class)) {
//							BeanValueUtils.setValue(object, field, "");
//						}
//					}
//
//				}
//			}
//		}
		return object;
	}
	
	 public static boolean isContainChinese(String str) {
	        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	        Matcher m = p.matcher(str);
	        if (m.find()) {
	            return true;
	        }
	        return false;
	    }

}
