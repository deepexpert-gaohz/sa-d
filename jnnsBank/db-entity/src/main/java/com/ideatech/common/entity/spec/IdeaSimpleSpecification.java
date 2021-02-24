/**
 * 
 */
package com.ideatech.common.entity.spec;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre></pre>
 *
 * @author jojo 2014-2-17 下午2:29:16
 *
 */
public abstract class IdeaSimpleSpecification<T, C> extends AbstractEventConditionBuilder<T, C>
		implements Specification<T> {

	protected static final Logger LOG = LoggerFactory.getLogger(IdeaSimpleSpecification.class);

	/**
	 * @param condition
	 */
	public IdeaSimpleSpecification(C condition) {
		super(condition);
	}

	/**
	 * 
	 * 构建查询条件，子类必须实现addCondition方法来编写查询的逻辑。
	 * 
	 * 子类可以通过addFetch方法控制查询的关联和抓取行为。
	 * 
	 */
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		if (Long.class != query.getResultType()) {
			addFetch(root);
		}

		List<Predicate> predicates = new ArrayList<Predicate>();

		QueryWraper<T> queryWraper = new QueryWraper<T>(root, query, cb, predicates);

		addCondition(queryWraper);

		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	/**
	 * 获取organFullId的路径，默认是直接读取organFullId属相，当目标与organ有关联时，可能通过关联对象获取，这是，
	 * 子类需要覆盖此方法并制定获取organFullId的路径
	 * 
	 * @param queryWraper
	 * @return
	 */
	protected Path<String> getDomainOrganFullIdPath(QueryWraper<T> queryWraper) {
		Path<String> fieldPath = queryWraper.getRoot().get("organFullId");
		return fieldPath;
	}

	/**
	 * <pre>
	 * 子类可以通过覆盖此方法实现关联抓取，避免n+1查询
	 * 
	 * <pre>
	 * 
	 * @param root
	 * @author jojo 2014-7-22 上午9:49:26
	 */
	protected void addFetch(Root<T> root) {

	}

	protected abstract void addCondition(QueryWraper<T> queryWraper);

}
