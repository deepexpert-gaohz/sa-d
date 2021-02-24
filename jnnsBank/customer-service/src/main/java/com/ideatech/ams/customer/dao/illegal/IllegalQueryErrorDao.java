package com.ideatech.ams.customer.dao.illegal;

import com.ideatech.ams.customer.entity.illegal.IllegalQuery;
import com.ideatech.ams.customer.entity.illegal.IllegalQueryError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IllegalQueryErrorDao extends JpaRepository<IllegalQueryError, Long>, JpaSpecificationExecutor<IllegalQueryError> {

}
