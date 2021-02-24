package com.ideatech.common.entity.spec;

import com.ideatech.common.util.BeanValueUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.Path;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * <pre>
 *
 * <pre>
 * @author jojo 2014-8-12 下午2:58:10
 *
 */
public abstract class AbstractEventConditionBuilder<T, C> extends AbstractConditionBuilder<T> {

	/**
	 * 查询条件
	 */
	private C condition;
	
	/**
	 * @param condition 查询条件
	 */
	public AbstractEventConditionBuilder(C condition){
		this.condition = condition;
	}

	/**
	 * 向查询中添加包含(like)条件
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取，并且指出要向哪个字段添加包含(like)条件。(同一个字段名称)
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addLikeCondition(QueryWraper<T> queryWraper, String field){
		addLikeCondition(queryWraper, field, field);
	}
	
	/**
	 * 向查询中添加包含(like)条件
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取
	 * @param column 指出要向哪个字段添加包含(like)条件
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addLikeCondition(QueryWraper<T> queryWraper, String field, String column){
		addLikeConditionToColumn(queryWraper, column, (String) 
				BeanValueUtils.getValue(getCondition(), field));
	}
	
	
	/**
	 * 向查询中添加包含(like)条件,%放在值后面
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取，并且指出要向哪个字段添加包含(like)条件。(同一个字段名称)
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addStartsWidthCondition(QueryWraper<T> queryWraper, String field){
		addStartsWidthCondition(queryWraper, field, field);
	}
	
	/**
	 * 向查询中添加包含(like)条件,%放在值后面
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取
	 * @param column 指出要向哪个字段添加包含(like)条件
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addStartsWidthCondition(QueryWraper<T> queryWraper, String field, String column){
		addStartsWidthConditionToColumn(queryWraper, column, (String) 
				BeanValueUtils.getValue(getCondition(), field));
	}
		
	/**
	 * 向查询中添加等于(=)条件
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取，并且指出要向哪个字段添加条件。(同一个字段名称)
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addEqualsCondition(QueryWraper<T> queryWraper, String field){
		addEqualsCondition(queryWraper, field, field);
	}
	
	/**
	 * 向查询中添加等于(=)条件
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取
	 * @param column 指出要向哪个字段添加条件
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addEqualsCondition(QueryWraper<T> queryWraper, String field, String column){
		addEqualsConditionToColumn(queryWraper, column, 
				BeanValueUtils.getValue(getCondition(), field));
	}
	/**
	 * 向查询中添加等于(=)条件
	 * 若ondition对象中的field的值=value参数，则查询出column=value的数据
	 * 若ondition对象中的field的值!=value参数，则查询出column!=value的数据
	 *
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取
	 * @param value 数据库中该字段的值
	 * @param column 指出要向哪个字段添加条件
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	protected void addEqualsIsOrNotCondition(QueryWraper<T> queryWraper, String column, Object value) {
		if (value.equals(BeanValueUtils.getValue(getCondition(), column))) {
			addEqualsConditionToColumn(queryWraper, column,
					BeanValueUtils.getValue(getCondition(), column));
		} else {
			addNotEqualsConditionToColumn(queryWraper, column,
					BeanValueUtils.getValue(getCondition(), column));
		}
	}
	
	/**
	 * 向查询中添加不等于(!=)条件
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取，并且指出要向哪个字段添加条件。(同一个字段名称)
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addNotEqualsCondition(QueryWraper<T> queryWraper, String field){
		addNotEqualsCondition(queryWraper, field, field);
	}
	
	/**
	 * 向查询中添加等于(=)条件
	 * 
	 * @param queryWraper
	 * @param field 指出查询条件的值从condition对象的哪个字段里取
	 * @param column 指出要向哪个字段添加条件
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected void addNotEqualsCondition(QueryWraper<T> queryWraper, String field, String column){
		addNotEqualsConditionToColumn(queryWraper, column, BeanValueUtils.getValue(getCondition(), field));
	}
	
	/**
	 * <pre>
	 * 向查询中添加in条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @author jojo 2014-8-12 下午3:26:50
	 */
	protected void addInCondition(QueryWraper<T> queryWraper, String field) {
		addInCondition(queryWraper, field, field);
	}
	/**
	 * <pre>
	 * 向查询中添加in条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 * @author jojo 2014-8-12 下午3:27:46
	 */
	protected void addInCondition(QueryWraper<T> queryWraper, String field, String column) {
		addInConditionToColumn(queryWraper, column, 
				BeanValueUtils.getValue(getCondition(), field));
	}

	/**
	 * <pre>
	 * 向查询中添加in条件或者为null
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 *
	 */
	protected void addInOrNullCondition(QueryWraper<T> queryWraper, String field, String column) {
		addInAndNullConditionToColumn(queryWraper, column,
				BeanValueUtils.getValue(getCondition(), field));
	}
	/**
	 * <pre>
	 * 向查询中添加in条件包括null
	 * 在条件集合中直接插入null，会根据集合中是否含有null进行删选
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 *
	 */
	protected void addInIncludeNullCondition(QueryWraper<T> queryWraper, String field, String column) {
		addInIncludeNullConditionToColumn(queryWraper, column,
				BeanValueUtils.getValue(getCondition(), field));
	}

	/**
	 * <pre>
	 * 向查询中添加between条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @author jojo 2014-8-12 下午3:26:50
	 */
	protected void addBetweenCondition(QueryWraper<T> queryWraper, String field) {
		addBetweenCondition(queryWraper, field, field+"To", field);
	}
	/**
	 * <pre>
	 * 向查询中添加between条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 * @author jojo 2014-8-12 下午3:27:46
	 */
	@SuppressWarnings("rawtypes")
	protected void addBetweenCondition(QueryWraper<T> queryWraper, String startField, String endField, String column) {
		Comparable start = null;
		Comparable end = null;
		if (StringUtils.isNotBlank((String) BeanValueUtils.getValue(getCondition(), startField))) {
			start = (Comparable) BeanValueUtils.getValue(getCondition(), startField);
		}
		if (StringUtils.isNotBlank((String) BeanValueUtils.getValue(getCondition(), endField))) {
			end = (Comparable) BeanValueUtils.getValue(getCondition(), endField);
		}
		addBetweenConditionToColumn(queryWraper, column, start, end);
	}
	/**
	 * 向查询中添加between条件
	 * 例如：（a=1 or b=2 or c=1）
	 */
	@SuppressWarnings("rawtypes")
	protected void addBetweenOrCondition(QueryWraper<T> queryWraper, String[]... strArr) {
		String[] columnArr = new String[strArr.length];
		Comparable[] startArr = new Comparable[strArr.length];
		Comparable[] endArr = new Comparable[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			String[] p = strArr[i];//原参数String startField, String endField, String column
			Comparable start = null;
			Comparable end = null;
			if (StringUtils.isNotBlank((String) BeanValueUtils.getValue(getCondition(), p[0]))) {
				start = (Comparable) BeanValueUtils.getValue(getCondition(), p[0]);
			}
			if (StringUtils.isNotBlank((String) BeanValueUtils.getValue(getCondition(), p[1]))) {
				end = (Comparable) BeanValueUtils.getValue(getCondition(), p[1]);
			}
			columnArr[i] = p[2];
			startArr[i] = start;
			endArr[i] = end;
		}
		addBetweenConditionToColumn(queryWraper, columnArr, startArr, endArr);
	}
	/**
	 * 向查询中添加between条件(Date类型)
	 */
	@SuppressWarnings("rawtypes")
	protected void addBetweenConditionDate(QueryWraper<T> queryWraper, String startField, String endField, String column) {
		Comparable start = null;
		Comparable end = null;
		if (((Date) BeanValueUtils.getValue(getCondition(), startField))!=null) {
			start = (Comparable) BeanValueUtils.getValue(getCondition(), startField);
		}
		if (((Date) BeanValueUtils.getValue(getCondition(), endField))!=null) {
			end = (Comparable) BeanValueUtils.getValue(getCondition(), endField);
		}
		addBetweenConditionToColumn(queryWraper, column, start, end);
	}
	
	/**
	 * <pre>
	 * 向查询中添加大于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @author jojo 2014-8-12 下午3:26:50
	 */
	protected void addGreaterThanCondition(QueryWraper<T> queryWraper, String field) {
		addGreaterThanCondition(queryWraper, field, field);
	}
	/**
	 * <pre>
	 * 向查询中添加大于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 * @author jojo 2014-8-12 下午3:27:46
	 */
	@SuppressWarnings("rawtypes")
	protected void addGreaterThanCondition(QueryWraper<T> queryWraper, String field, String column) {
		addGreaterThanConditionToColumn(queryWraper, column, 
				(Comparable)BeanValueUtils.getValue(getCondition(), field));
	}
	
	/**
	 * <pre>
	 * 向查询中添加大于等于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @author jojo 2014-8-12 下午3:26:50
	 */
	protected void addGreaterThanOrEqualCondition(QueryWraper<T> queryWraper, String field) {
		addGreaterThanOrEqualCondition(queryWraper, field, field);
	}
	
	/**
	 * <pre>
	 * 向查询中添加大于等于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 * @author jojo 2014-8-12 下午3:27:46
	 */
	@SuppressWarnings("rawtypes")
	protected void addGreaterThanOrEqualCondition(QueryWraper<T> queryWraper, String field, String column) {
		addGreaterThanOrEqualConditionToColumn(queryWraper, column, 
				(Comparable)BeanValueUtils.getValue(getCondition(), field));
	}
	
	/**
	 * <pre>
	 * 向查询中添加小于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @author jojo 2014-8-12 下午3:26:50
	 */
	protected void addLessThanCondition(QueryWraper<T> queryWraper, String field) {
		addLessThanCondition(queryWraper, field, field);
	}
	/**
	 * <pre>
	 * 向查询中添加小于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 * @author jojo 2014-8-12 下午3:27:46
	 */
	@SuppressWarnings("rawtypes")
	protected void addLessThanCondition(QueryWraper<T> queryWraper, String field, String column) {
		addLessThanConditionToColumn(queryWraper, column, 
				(Comparable)BeanValueUtils.getValue(getCondition(), field));
	}

	/**
	 * <pre>
	 * 向查询中添加小于等于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @author jojo 2014-8-12 下午3:26:50
	 */
	protected void addLessThanOrEqualCondition(QueryWraper<T> queryWraper, String field) {
		addLessThanOrEqualCondition(queryWraper, field, field);
	}
	
	/**
	 * <pre>
	 * 向查询中添加小于等于条件
	 * <pre>
	 * @param queryWraper
	 * @param field
	 * @param column
	 * @author jojo 2014-8-12 下午3:27:46
	 */
	@SuppressWarnings("rawtypes")
	protected void addLessThanOrEqualCondition(QueryWraper<T> queryWraper, String field, String column) {
		addLessThanOrEqualConditionToColumn(queryWraper, column, 
				(Comparable)BeanValueUtils.getValue(getCondition(), field));
	}

	/**
	 * @return the condition
	 */
	public C getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(C condition) {
		this.condition = condition;
	}
	
}
