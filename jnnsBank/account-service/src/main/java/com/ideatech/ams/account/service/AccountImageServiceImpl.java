package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountImageDao;
import com.ideatech.ams.account.domain.AccountImageDo;
import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.ams.account.entity.AccountImage;
import com.ideatech.ams.account.entity.AccountPublic;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.BeanCopierUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.util.ImageUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by houxianghua on 2018/11/8.
 */
@Service
@Transactional
@Slf4j
public class AccountImageServiceImpl implements AccountImageService {

    //影像本地存放地址
    @Value("${ams.accounts.upload.path}")
    private String path;

    //影像本地存放地址zip
    @Value("${ams.accounts.upload.zipPath}")
    private String zipPath;

    @Autowired
    private AccountImageDao accountImageDao;

    @PersistenceContext
    private EntityManager em; //注入EntityManager

    @Override
    public String uploadImage(InputStream is,String fileName) {
//        String filename = new Date().getTime() + ".jpg";
        String url = path + "/" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            log.info("开始创建账户证件文件夹：" + path);
            file.mkdirs();
        }
        File finalFile = new File(url);
        try {
            FileUtils.copyInputStreamToFile(is,finalFile);
        } catch (Exception e) {
            log.error("影像保存异常", e);
        }
        return fileName;
    }

    @Override
    public String downloadZip(String fileName) {
        String zipUrl = zipPath+File.separator+fileName+"-"+System.currentTimeMillis() + ".zip";
        com.ideatech.common.util.FileUtils.mkdirs(zipPath);
        return zipUrl;
    }


    @Override
    public AccountImageInfo save(AccountImageInfo info) {
        AccountImage accountImage = null;
        if(info.getId() != null &&info.getId()>0){
            accountImage = accountImageDao.findOne(info.getId());
        }
        if(accountImage == null){
            accountImage = new AccountImage();
        }
        BeanCopierUtils.copyProperties(info, accountImage);
        accountImage = accountImageDao.save(accountImage);
        info.setId(accountImage.getId());
        return info;
    }

    @Override
    public List<AccountImageInfo> findAccountImageList(Long acctId, Long acctBillsId,String tempId) {
        List<AccountImage> aiList = new ArrayList<AccountImage>();
//        if(acctId != null && acctId >0){
//            aiList = accountImageDao.findByAcctId(acctId);
//        }
        if(acctBillsId !=null && acctBillsId >0 && aiList.size()==0){
            aiList = accountImageDao.findByAcctBillsId(acctBillsId);
        }

        if(StringUtils.isNotBlank(tempId)  && aiList.size()==0){
            aiList = accountImageDao.findByTempId(tempId);
        }
        if(aiList.size()==0){
            return new ArrayList<>();
        }
        List<AccountImageInfo> aiiList = new ArrayList<>();
        for (AccountImage ai:aiList) {
            AccountImageInfo aii = new AccountImageInfo();
            ConverterService.convert(ai, aii);
            aiiList.add(aii);
        }
        return aiiList;
    }


    @Override
    public AccountImageInfo findAccountImageByImageTypeId(List<AccountImageInfo> list, Long imageTypeId){
        if(imageTypeId != null && imageTypeId >0){
           for(AccountImageInfo accountImageInfo : list) {
              if(accountImageInfo.getImageTypeId() != null &&accountImageInfo.getImageTypeId().equals(imageTypeId)){
                  return accountImageInfo;
              }
           }
        }
        return null;
    }

    @Override
    public void updateAccountImage(Long acctId, Long acctBillsId, String tempId) {
        List<AccountImageInfo> byTempId = findAccountImageList(acctId,acctBillsId,tempId);
        for(AccountImageInfo accountImage : byTempId){
            if(accountImage.getAcctId() ==null || accountImage.getAcctBillsId() ==null){
                accountImage.setAcctId(acctId);
                accountImage.setAcctBillsId(acctBillsId);
                save(accountImage);
            }
        }
    }


    @Override
    public AccountImageInfo findAccountImageInfoByBatchNo(Long batchNo){
        AccountImage accountImage = accountImageDao.findByBatchNo(batchNo);
        AccountImageInfo accountImageInfo = new AccountImageInfo();
        log.info("批次号[{}]的对象为",batchNo,accountImage);
        if(accountImage !=null){
            BeanUtils.copyProperties(accountImage,accountImageInfo);
            String filePath = accountImage.getFilePath();
            if(StringUtils.isNotBlank(filePath)){
                String[] filePaths = StringUtils.split(filePath, ",");
                log.info("批次号[{}]的图片数量为{}",batchNo,filePaths.length);
                if(filePaths.length>0){
                    List<String> imageStrs = new ArrayList<>();
                    for(String fileStr:filePaths){
                        log.info("批次号[{}]的获取图片-{}",batchNo,fileStr);
                        if(com.ideatech.common.util.FileUtils.exists(path+File.separator+fileStr)){
                            try{
                                imageStrs.add(com.ideatech.common.util.FileUtils.encodeFileToBase64Binary(new File(path + File.separator + fileStr)));
                            }catch (Exception e){
                                log.error("图片转base64报错",e);
                            }
                        }
                    }
                    accountImageInfo.setFileDetail(imageStrs);
                }
            }
        }
        return accountImageInfo;
    }

    @Override
    public void softDelete(Long id) {
        AccountImage one = accountImageDao.findOne(id);
        accountImageDao.delete(one);
    }

    @Override
    public List<AccountImageInfo> findTop10BySyncStatus(CompanyIfType companyIfType) {
        List<AccountImage> top10BySyncStatus = accountImageDao.findTop10BySyncStatus(companyIfType);
        return ConverterService.convertToList(top10BySyncStatus,AccountImageInfo.class);
    }

    @Override
    public List<AccountImageInfo> findByAcctIdIn(List<Long> acctIds) {
        return ConverterService.convertToList(accountImageDao.findByAcctIdIn(acctIds), AccountImageInfo.class);
    }

    /**
     * 根据指定流水集合查询出所有类型的最新影像（影像采集方式：本地上传）
     * <p>
     * 由于一种影像可能有多张影像信息，故以下逻辑处理时，
     * 先查询出每个影像类型的最新数据，
     * 再根据这条数据查询出他所在的流水，
     * 再查询出该条流水上传的该影像类型的影像数据（一条或多条）
     *
     * @param billIdList 流水id集合
     * @return
     */
    @Override
    public List<AccountImageInfo> getNewImagesByBillForLocal(List<Long> billIdList) {
//        SELECT
//        ai.*
//                FROM
//        YD_ACCOUNTS_IMAGE ai,
//        YD_IMAGE_TYPE it ,
//        (
//                SELECT
//        max( ai.yd_created_date ) AS yd_created_date,
//        it.yd_value
//                FROM
//        YD_ACCOUNTS_IMAGE ai,
//        YD_IMAGE_TYPE it
//        WHERE
//        ai.yd_image_type_id = it.yd_id
//        -- AND ai.yd_acct_bills_id in ()
//        GROUP BY
//        it.yd_value
//	) aa
//                WHERE
//        ai.yd_image_type_id = it.yd_id AND aa.yd_created_date = ai.yd_created_date AND aa.yd_value = it.yd_value

        String sql = "SELECT " +
                "        max( ai.yd_created_date ) AS YD_CREATED_DATE, " +
                "        it.YD_VALUE AS YD_VALUE, " +
                "        it.YD_IMAGE_NAME AS YD_IMAGE_NAME " +
                "     FROM " +
                "        YD_ACCOUNTS_IMAGE ai, " +
                "        YD_IMAGE_TYPE it " +
                "     WHERE " +
                "        ai.yd_image_type_id = it.yd_id " +
                "        AND ai.yd_acct_bills_id in ?1 " +
                "     GROUP BY " +
                "        it.YD_VALUE,it.YD_IMAGE_NAME";
        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter(1, billIdList);
        List rows = query.getResultList();
        String sql2 = "SELECT " +
                " new com.ideatech.ams.account.domain.AccountImageDo(ai,it) " +
                "FROM " +
                " AccountImage ai, " +
                " ImageType it " +
                "WHERE " +
                " ai.imageTypeId = it.id AND ai.createdDate = ?1 AND it.value = ?2";
        Query query2 = em.createQuery(sql2);
        List<AccountImageInfo> list = new ArrayList<>();
        for (Object obj : rows) {
            Map map = (Map) obj;
            query2.setParameter(1, map.get("YD_CREATED_DATE"));
            query2.setParameter(2, map.get("YD_VALUE"));
            List<AccountImageDo> listDo = query2.getResultList();
            for (AccountImageDo aid : listDo) {
                AccountImageInfo aii = new AccountImageInfo();
                ConverterService.convert(aid.getAccountImage(), aii);
                aii.setDocCode(aid.getImageType().getValue());
                aii.setDocName((String) (map.get("YD_IMAGE_NAME")));
                list.add(aii);
            }
        }
        return list;
    }

    @Override
    public List<AccountImageInfo> findByBillId(Long billId) {
        List<AccountImage> aiList = accountImageDao.findByAcctBillsId(billId);
        return ConverterService.convertToList(aiList, AccountImageInfo.class);
    }
    @Override
    public List<AccountImageInfo> findAll() {
        return ConverterService.convertToList(accountImageDao.findAll(),AccountImageInfo.class);
    }

}
