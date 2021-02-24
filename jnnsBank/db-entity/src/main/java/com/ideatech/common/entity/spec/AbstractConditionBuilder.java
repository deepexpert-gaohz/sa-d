package com.ideatech.common.entity.spec;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *
 * <pre>
 * @author jojo 2014-8-12 下午2:57:22
 *
 */
public abstract class AbstractConditionBuilder<T> {
	
	/**
     * 添加in条件
     *
     * @param queryWraper
     * @param values
     */
    protected void addInConditionToColumn(QueryWraper<T> queryWraper, String column, Object values) {
		if (needAddCondition(values)) {
			Path<?> fieldPath = getPath(queryWraper.getRoot(), column);
			Predicate predicate = null;
			if (values instanceof Collection) {
				predicate = fieldPath.in((Collection<?>) values);
			} else {
				predicate = fieldPath.in(values);
			}
			queryWraper.addPredicate(predicate);
		}
	}

    /**
     * 添加in条件
     *
     * @param queryWraper
     * @param values
     */
    protected void addInAndNullConditionToColumn(QueryWraper<T> queryWraper, String column, Object values) {
        if (needAddCondition(values)) {
        	Path<?> fieldPath = getPath(queryWraper.getRoot(), column);
			CriteriaBuilder cb = queryWraper.getCb();
			Predicate conjunction = cb.conjunction();
			List<Expression<Boolean>> expressions = conjunction.getExpressions();
			Predicate pre = fieldPath.in(values);
			Predicate aNull = fieldPath.isNull();
			expressions.add(cb.or(pre,aNull));
			queryWraper.addPredicate(conjunction);
        }
    }

	/**
	 * 添加in条件包括null
	 *
	 * @param queryWraper
	 * @param values
	 */
	protected void addInIncludeNullConditionToColumn(QueryWraper<T> queryWraper, String column, Object values) {
		if (needAddCondition(values)) {
			if(values.getClass() == ArrayList.class ){//判断是ArrayList对象
				List list = com.ideatech.common.util.CollectionUtils.removeNull((List) values);
				if(list.size() == ((List) values).size()){//没有null
					addInConditionToColumn(queryWraper,column,values);
				}else{
					addInAndNullConditionToColumn(queryWraper,column,list);
				}
			}else{
				addInConditionToColumn(queryWraper,column,values);
			}
		}
	}

	/**
     * 添加between条件查询
     * @param queryWraper
     * @param experssion
     * @param minValue  范围下限
     * @param maxValue  范围上限
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addBetweenConditionToColumn(QueryWraper<T> queryWraper, String column, Comparable minValue, Comparable maxValue) {
    	if (minValue != null || maxValue != null) {
    		Path<? extends Comparable> fieldPath = getPath(queryWraper.getRoot(), column);
    		if(minValue != null && maxValue != null){
    			queryWraper.addPredicate(queryWraper.getCb().between(fieldPath, minValue, maxValue));
    		}else if(minValue != null){
    			queryWraper.addPredicate(queryWraper.getCb().greaterThanOrEqualTo(fieldPath, minValue));
    		}else if(maxValue != null){
    			queryWraper.addPredicate(queryWraper.getCb().lessThanOrEqualTo(fieldPath, maxValue));
    		}
    	}
    }

    /**
     * 添加between条件查询
	 * 例如：（a=1 or b=2 or c=1）
     * @param queryWraper
     * @param columnArr 字段名
     * @param minValueArr  范围下限
     * @param maxValueArr  范围上限
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addBetweenConditionToColumn(QueryWraper<T> queryWraper, String[] columnArr, Comparable[] minValueArr, Comparable[] maxValueArr) {
		boolean b = false;
		CriteriaBuilder cb = queryWraper.getCb();
		Predicate conjunction = cb.conjunction();
		List<Expression<Boolean>> expressions = conjunction.getExpressions();

		Predicate[] predicateArr = new Predicate[columnArr.length];
		for (int i = 0; i < columnArr.length; i++) {
			String column = columnArr[i];
			Comparable minValue = minValueArr[i];
			Comparable maxValue = maxValueArr[i];
			if (minValue != null || maxValue != null) {
				Path<? extends Comparable> fieldPath = getPath(queryWraper.getRoot(), column);
				if (minValue != null && maxValue != null) {
					b = true;
					predicateArr[i] = queryWraper.getCb().between(fieldPath, minValue, maxValue);
				} else if (minValue != null) {
					b = true;
					predicateArr[i] = queryWraper.getCb().greaterThanOrEqualTo(fieldPath, minValue);
				} else if (maxValue != null) {
					b = true;
					predicateArr[i] = queryWraper.getCb().lessThanOrEqualTo(fieldPath, maxValue);
				}
			}
		}
		if (b) {
			expressions.add(cb.or(predicateArr));
			queryWraper.addPredicate(conjunction);
		}
	}

    /**
     * 添加大于条件查询
     * @param queryWraper
     * @param experssion
     * @param minValue
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addGreaterThanConditionToColumn(QueryWraper<T> queryWraper, String column,  Comparable minValue) {
    	if (minValue != null) {
    		Path<? extends Comparable> fieldPath = getPath(queryWraper.getRoot(), column);
    		queryWraper.addPredicate(queryWraper.getCb().greaterThan(fieldPath, minValue));
    	}
    }
	
	/**
     * 添加大于等于条件查询
     * @param queryWraper
     * @param experssion
     * @param minValue
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addGreaterThanOrEqualConditionToColumn(QueryWraper<T> queryWraper, String column,  Comparable minValue) {
    	if (minValue != null) {
    		Path<? extends Comparable> fieldPath = getPath(queryWraper.getRoot(), column);
    		queryWraper.addPredicate(queryWraper.getCb().greaterThanOrEqualTo(fieldPath, minValue));
    	}
    }
	
	/**
     * 添加小于条件查询
     * @param queryWraper
     * @param experssion
     * @param maxValue
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLessThanConditionToColumn(QueryWraper<T> queryWraper, String column,  Comparable maxValue) {
    	if (maxValue != null) {
    		Path<? extends Comparable> fieldPath = getPath(queryWraper.getRoot(), column);
    		queryWraper.addPredicate(queryWraper.getCb().lessThan(fieldPath, maxValue));
    	}
    }

	/**
     * 添加小于等于条件查询
     * @param queryWraper
     * @param experssion
     * @param maxValue
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void addLessThanOrEqualConditionToColumn(QueryWraper<T> queryWraper, String column,  Comparable maxValue) {
    	if (maxValue != null) {
    		Path<? extends Comparable> fieldPath = getPath(queryWraper.getRoot(), column);
    		queryWraper.addPredicate(queryWraper.getCb().lessThanOrEqualTo(fieldPath, maxValue));
    	}
    }
	
	/**
	 * <pre>
	 * 添加like条件
	 * <pre>
	 * @param queryWraper
	 * @param column
	 * @param value
	 * @author jojo 2014-8-12 下午3:13:44
	 */
	@SuppressWarnings("unchecked")
	protected void addLikeConditionToColumn(QueryWraper<T> queryWraper, String column, String value) {
		if (StringUtils.isNotBlank(value)) {
			Path<String> fieldPath = getPath(queryWraper.getRoot(), column);
			queryWraper.addPredicate(queryWraper.getCb().like(fieldPath, "%" + value + "%"));
		}
	}
	
	/**
	 * <pre>
	 * 添加like条件
	 * <pre>
	 * @param queryWraper
	 * @param column
	 * @param value
	 * @author jojo 2014-8-12 下午3:13:44
	 */
	@SuppressWarnings("unchecked")
	protected void addStartsWidthConditionToColumn(QueryWraper<T> queryWraper, String column, String value) {
		if (StringUtils.isNotBlank(value)) {
			Path<String> fieldPath = getPath(queryWraper.getRoot(), column);
			queryWraper.addPredicate(queryWraper.getCb().like(fieldPath,  value + "%"));
		}
	}


	/**
	 *  添加等于条件
	 * @param queryWraper
	 * @param column 指出要向哪个字段添加条件
	 * @param value 指定字段的值
	 */
	protected void addEqualsConditionToColumn(QueryWraper<T> queryWraper, String column, Object value) {
		if(needAddCondition(value)) {
			Path<?> fieldPath = getPath(queryWraper.getRoot(), column);
			queryWraper.addPredicate(queryWraper.getCb().equal(fieldPath, value));
		}
	}
	
	/**
	 *  添加不等于条件
	 * @param queryWraper
	 * @param column 指出要向哪个字段添加条件
	 * @param value 指定字段的值
	 */
	protected void addNotEqualsConditionToColumn(QueryWraper<T> queryWraper, String column, Object value) {
		if(needAddCondition(value)) {
			Path<?> fieldPath = getPath(queryWraper.getRoot(), column);
			queryWraper.addPredicate(queryWraper.getCb().notEqual(fieldPath, value));
		}
	}
	
	/**
	 * <pre>
	 *
	 * <pre>
	 * @param root
	 * @param property
	 * @return
	 * @author jojo 2014-8-12 下午3:06:58
	 */
	@SuppressWarnings("rawtypes")
	protected Path getPath(Root root, String property){
		String[] names = StringUtils.split(property, ".");
		Path path = root.get(names[0]);
		for (int i = 1; i < names.length; i++) {
			path = path.get(names[i]);
		}
		return path;
	}

	/**
	 * <pre>
	 * 判断是否需要添加where条件
	 * <pre>
	 * @param value
	 * @return
	 * @author jojo 2014-8-12 下午3:07:00
	 */
	@SuppressWarnings("rawtypes")
	protected boolean needAddCondition(Object value) {
		boolean addCondition = false;
		if (value != null) {
			if(value instanceof String) {
				if(StringUtils.isNotBlank(value.toString())) {
					addCondition = true;
				}
			}else if(value.getClass().isArray()) {
				if(ArrayUtils.isNotEmpty((Object[]) value)) {
					addCondition = true;
				}
			}else if(value instanceof Collection) {
				if(CollectionUtils.isNotEmpty((Collection) value)) {
					addCondition = true;
				}
			}else {
				addCondition = true;
			}
		}
		return addCondition;
	}

}
