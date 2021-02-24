package com.ideatech.ams.image.dao;

import com.ideatech.ams.image.entity.ImageAll;
import com.ideatech.common.enums.CompanyIfType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ImageAllDao extends JpaRepository<ImageAll,Long>, JpaSpecificationExecutor<ImageAll> {
    List<ImageAll> findByBillsId(Long billsId);
    List<ImageAll> findByAcctId(Long acctId);
    List<ImageAll> findByCustomerId(Long customerId);


    List<ImageAll> findByBillsIdOrderByExpireDateDesc(Long billsId);
    List<ImageAll> findByBillsIdAndDocCodeOrderByExpireDateDesc(Long billsId,String docCode);

    List<ImageAll> findByBillsIdOrderByCreatedDateDesc(Long billsId);
    List<ImageAll> findByBillsIdAndDocCodeOrderByCreatedDateDesc(Long billsId,String docCode);



    List<ImageAll> findByAcctIdAndDocCode(Long acctId,String docCode);
    List<ImageAll> findByCustomerIdAndDocCode(Long customerId,String docCode);

    List<ImageAll> findByBillsIdAndSyncStatus(Long billsId,CompanyIfType syncStatus);
    List<ImageAll> findByBillsIdAndAndDocCodeAndSyncStatus(Long billsId,String docCode,CompanyIfType syncStatus);

    List<ImageAll> findByTempNo(String tempNo);

    List<ImageAll> findByCreatedDateAndDocCode(Date yd_created_date, String yd_doc_code);

    List<ImageAll> findByImgPath(String imgPath);
}
