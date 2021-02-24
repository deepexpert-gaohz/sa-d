package com.ideatech.ams.image.dao;

import com.ideatech.ams.image.entity.ImageType;
import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.CompanyAcctType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;


public interface ImageTypeDao extends JpaRepository<ImageType,Long>, JpaSpecificationExecutor<ImageType> {
    List<ImageType> findByValueAndAcctTypeAndOperateTypeAndDepositorTypeCode(String value,CompanyAcctType acctType,BillType operateType,String depositorTypeCode);
    List<ImageType> findByAcctTypeAndOperateTypeAndDepositorTypeCode(CompanyAcctType acctType,BillType operateType,String depositorTypeCode);
    List<ImageType> findByAcctTypeAndOperateType(CompanyAcctType acctType,BillType operateType);
}
