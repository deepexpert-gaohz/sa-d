package com.ideatech.ams.mivs.dao;

import com.ideatech.ams.mivs.entity.TaxPaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/30.
 */

@Repository
public interface TaxPaymentInformationDao extends JpaRepository<TaxPaymentInformation,Long> {
    List<TaxPaymentInformation> findAllByTaxInformationLogId(Long taxInformationLogId);
}
