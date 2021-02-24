package com.ideatech.ams.image.dao;

import com.ideatech.ams.image.entity.Image;
import com.ideatech.ams.image.enums.IsUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface ImageDao extends JpaRepository<Image,Long>, JpaSpecificationExecutor<Image> {
    List<Image> findByRefBillId(Long refBillId);
    List<Image> findByRefBillIdAndDocCode(Long refBillId,String docCode);
    List<Image> findByRefBillIdAndIsUpload(Long refBillId,IsUpload isUpload);
    List<Image> findByAcctIdIn(List<Long> acctIds);
    List<Image> findByCreatedDateAndDocCode(Date createdDate, String docCode);
}
