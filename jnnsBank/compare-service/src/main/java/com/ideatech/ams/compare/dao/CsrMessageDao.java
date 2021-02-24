package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.entity.CsrMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jzh
 * @date 2019/6/28.
 */
public interface CsrMessageDao extends JpaRepository<CsrMessage,Long>, JpaSpecificationExecutor<CsrMessage> {
}
