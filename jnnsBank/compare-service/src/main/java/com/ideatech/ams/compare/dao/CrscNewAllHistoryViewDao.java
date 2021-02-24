package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.view.CrscNewAllHistoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CrscNewAllHistoryViewDao extends JpaRepository<CrscNewAllHistoryView,Long>, JpaSpecificationExecutor<CrscNewAllHistoryView> {

}
