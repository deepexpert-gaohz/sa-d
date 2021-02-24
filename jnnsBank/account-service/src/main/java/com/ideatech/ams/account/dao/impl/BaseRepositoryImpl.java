package com.ideatech.ams.account.dao.impl;

import org.apache.commons.collections.MapUtils;
import org.hibernate.engine.jdbc.SerializableClobProxy;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import com.ideatech.common.exception.EacException;

import oracle.sql.CLOB;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseRepositoryImpl {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	protected List getResultList(String sql, Map<String, Object> param) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		// 设置参数
		if (MapUtils.isNotEmpty(param)) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				nativeQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		nativeQuery.unwrap(org.hibernate.SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List mapList = nativeQuery.getResultList();
		return mapList;
	}

	protected Object getSingleResult(String sql, Map<String, Object> param) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		if (MapUtils.isNotEmpty(param)) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				nativeQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		nativeQuery.unwrap(org.hibernate.SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return nativeQuery.getSingleResult();
	}

	protected Page getPageResultList(String sql, Map<String, Object> param, Pageable pageRequest) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		// 设置参数
		if (MapUtils.isNotEmpty(param)) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				nativeQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		Assert.notNull(nativeQuery);

		Long total = count(sql, param);

		if (total == null) {
			return null;
		}
		// 设置分页参数
		nativeQuery.setFirstResult(pageRequest.getOffset());
		nativeQuery.setMaxResults(pageRequest.getPageSize());

		nativeQuery.unwrap(org.hibernate.SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List mapList = nativeQuery.getResultList();

		List<Map<String, Object>> content = total > pageRequest.getOffset() ? mapList : Collections.emptyList();

		for (Map<String, Object> map : content) {
			Iterator<Entry<String, Object>> sets=  map.entrySet().iterator();
			while (sets.hasNext()) {
				Map.Entry<String,Object> entry = (Map.Entry<String,Object>) sets
						.next();
				if(entry.getValue() instanceof Clob) {
					entry.setValue(clobToString((Clob)entry.getValue()));
				}
			}
		}
		
		return new PageImpl<>(content, pageRequest, total);
	}

	/**
	 * 批量删除操作
	 */
	public int excuteBatchDelete(String sql, Map<String, Object> param) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		/*
		 * if (map != null && map.size() > 0) { for (Map.Entry<String, List<Object>>
		 * entry : map.entrySet()) { nativeQuery.setParameter(entry.getKey(),
		 * entry.getValue()); } }
		 */
		if (MapUtils.isNotEmpty(param)) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				nativeQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return nativeQuery.executeUpdate();
	}

	protected Long count(String sql, Map<String, Object> param) {
		String countSql = "select count(*) from (" + sql + ") a";
		Query countQuery = entityManager.createNativeQuery(countSql);

		// 设置参数
		if (MapUtils.isNotEmpty(param)) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				countQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		Assert.notNull(countQuery);
		Long total = 0L;
		Object totalObj = countQuery.getSingleResult();
		try {
			if (totalObj != null) {
				if (totalObj instanceof BigDecimal) {
					BigDecimal bd = (BigDecimal) totalObj;
					total = bd.longValue();
				} else if (totalObj instanceof BigInteger) {
					BigInteger bd = (BigInteger) totalObj;
					total = bd.longValue();
				} else {
					total = Long.parseLong(totalObj.toString());
				}
			}
		} catch (Exception e) {
			LOG.error("不支持该数据库count返回类型！！");
		}
		return total;
	}

	
	private String clobConvertString(Clob clob) {
        String reString = ""; 
		SerializableClobProxy  proxy = (SerializableClobProxy)Proxy.getInvocationHandler(clob);
		Object obj = proxy.getWrappedClob();
		if(obj instanceof com.alibaba.druid.proxy.jdbc.ClobProxyImpl) {
	          com.alibaba.druid.proxy.jdbc.ClobProxyImpl impl = (com.alibaba.druid.proxy.jdbc.ClobProxyImpl)obj;
	          Clob realClob = impl.getRawClob();
	  		  oracle.sql.CLOB oClob = (CLOB)realClob;
	          java.io.Reader is;
		  		try {
		  			is = oClob.getCharacterStream();
		  	        BufferedReader br = new BufferedReader(is); 
		  	        String s = br.readLine(); 
		  	        StringBuffer sb = new StringBuffer(); 
		  	        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING 
		  	            sb.append(s); 
		  	            s = br.readLine(); 
		  	        } 
		  	        reString = sb.toString(); 
		  		} catch (SQLException e) {
		  			throw new EacException("Oracle 的 Clob转化出错");
		  		} catch (IOException e) {
		  			throw new EacException("Oracle 的 Clob转化出错");
		  		}
		}
        return reString;
	}

	public String clobToString(Clob clob) {
		if (clob == null) {
			return null;
		}
		try {
			Reader inStreamDoc = clob.getCharacterStream();
			char[] tempDoc = new char[(int) clob.length()];
			inStreamDoc.read(tempDoc);
			inStreamDoc.close();
			return new String(tempDoc);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new EacException("Clob转化出错");
		}
	}
}
