package com.ideatech.ams.customer.dao.newcompany;

import com.ideatech.ams.customer.entity.newcompany.FreshCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface FreshCompanyDao extends JpaRepository<FreshCompany, Long>, JpaSpecificationExecutor<FreshCompany> {

    List<FreshCompany> findByName(String name);

    void deleteAllByOpenDateLessThan(String openDate);
}
