package com.ideatech.ams.account.dao.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideatech.ams.account.entity.CorePublicAccountError;

/**
 * @author wanghongjie
 *
 * @version 2018-06-21 19:47
 */
@Repository
public interface CorePublicAccountErrorDao extends JpaRepository<CorePublicAccountError, Long>, JpaSpecificationExecutor<CorePublicAccountError> {
}
