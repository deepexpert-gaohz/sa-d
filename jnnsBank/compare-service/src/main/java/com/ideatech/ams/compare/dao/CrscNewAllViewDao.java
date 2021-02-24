package com.ideatech.ams.compare.dao;

import com.ideatech.ams.compare.view.CrscNewAllView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CrscNewAllViewDao extends JpaRepository<CrscNewAllView,Long> , JpaSpecificationExecutor<CrscNewAllView> {

}
