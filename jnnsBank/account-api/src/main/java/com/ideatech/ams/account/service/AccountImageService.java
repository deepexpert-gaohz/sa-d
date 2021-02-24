package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.common.enums.CompanyIfType;

import java.io.InputStream;
import java.util.List;

/**
 * Created by houxianghua on 2018/11/8.
 */
public interface AccountImageService {
    /**
     * 上传证件图片
     * @param is
     * @return 文件存储路径
     */
    String uploadImage(InputStream is,String fileName);

    String downloadZip(String fileName);

    AccountImageInfo save(AccountImageInfo info);

    List<AccountImageInfo> findAccountImageList(Long acctId, Long acctBillsId,String tempId);

    AccountImageInfo findAccountImageByImageTypeId(List<AccountImageInfo> list, Long imageTypeId);

    void updateAccountImage(Long acctId,Long acctBillsId,String tempId);
//    List<AccountImageInfo> findAccountImageList(Long acctId, Long acctBillsId, String fileType);

    /**
     * 获取图片对象
     * @param batchNo
     * @return
     */
    AccountImageInfo findAccountImageInfoByBatchNo(Long batchNo);

    void softDelete(Long id);

    List<AccountImageInfo> findTop10BySyncStatus(CompanyIfType companyIfType);

    List<AccountImageInfo> findByAcctIdIn(List<Long> acctIds);

    List<AccountImageInfo> getNewImagesByBillForLocal(List<Long> billIdList);

    List<AccountImageInfo> findByBillId(Long billId);

    List<AccountImageInfo> findAll();

}
