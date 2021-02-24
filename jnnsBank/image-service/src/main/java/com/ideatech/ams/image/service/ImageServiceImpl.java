package com.ideatech.ams.image.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.service.AccountImageService;
import com.ideatech.ams.account.service.AccountPublicLogService;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.image.dao.ImageAccountDao;
import com.ideatech.ams.image.dao.ImageDao;
import com.ideatech.ams.image.dto.*;
import com.ideatech.ams.image.entity.Image;
import com.ideatech.ams.image.entity.ImageAccount;
import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.IsUpload;

import com.ideatech.ams.image.utils.ImageUtils;
import com.ideatech.ams.image.utils.UploadUtil;
import com.ideatech.ams.image.utils.ZipUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("imageService")
@Transactional
@Slf4j
public class ImageServiceImpl implements ImageService{
    //影像本地存放地址
    @Value("${ams.image.path}")
    private String path;
    @Value("${ams.image.socketPort}")
    private String socketPort;

    @Value("${ams.image.username}")
    private String username;

    @Value("${ams.image.password}")
    private String password;

    @Value("${ams.image.serverName}")
    private String serverName;

    @Value("${ams.image.groupName}")
    private String groupName;

    @Value("${ams.image.coreGroupName}")
    private String coreGroupName;

    @Value("${ams.image.STARTCOLUMN}")
    private String STARTCOLUMN;

    @Value("${ams.image.modelCode}")
    private String modelCode;

    @Value("${ams.image.filePartName}")
    private String filePartName;

    @Value("${ams.image.HardWare}")
    private String HardWare;

    @Value("${ams.image.ip}")
    private String ip;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageAccountDao imageAccountDao;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Override
    public ImageInfo save(ImageInfo info) {
        Image image = new Image();
        ConverterService.convert(info,image);
        image = imageDao.save(image);
        info.setId(image.getId());
        
        return info;
    }

    @Override
    public ImageInfo createImage(String base64) {
        ImageInfo info = new ImageInfo();
        String filename = new Date().getTime() + ".jpg";
        String filePath = createTempFilePath()+"/"+filename;
        try {
            File file = new File(createTempFilePath());
            if (!file.exists()) {
                log.info("开始创建影像文件夹："+createTempFilePath());
                file.mkdirs();
            }
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                log.info("开始创建影像文件："+filePath);
                imageFile.createNewFile();
            }
            Base64Utils.decoderBase64File(base64, filePath);
            info.setFileName(filename);
            info.setImgPath(filePath);
        }catch (Exception e){
            log.error("影像数据异常："+e.getMessage());
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像数据异常："+e.getMessage());
        }
        return info;
    }

    @Override
    public ImageInfo uploadImage(InputStream is) {
        ImageInfo info = new ImageInfo();
        String filename = new Date().getTime() + ".jpg";
        String url = createTempFilePath()+"/"+filename;
        OutputStream os = new ByteArrayOutputStream();
        File file = new File(createTempFilePath());
        if (!file.exists()) {
            log.info("开始创建影像文件夹："+createTempFilePath());
            file.mkdirs();
        }
        byte[] byteArray = new byte[0];
        try {
            byteArray = IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("传输文件出现异常", e);
        }
        //压缩影像
        ImageUtils.compressImage(byteArray, os, "jpg");
        File finalFile = new File(url);
        try {
            FileUtils.writeByteArrayToFile(finalFile, ((ByteArrayOutputStream) os).toByteArray());
        }catch (Exception e){
            log.error("影像保存异常", e);
        }
        info.setFileName(filename);
        info.setImgPath(url);
        return info;
    }

    @Override
    public ImageInfo findImageById(Long id) {
        ImageInfo info = new ImageInfo();
        Image image = imageDao.findOne(id);
        ConverterService.convert(image,info);
        return info;
    }

    @Override
    public void setType(ImageInfo info) {
        Image image = imageDao.findOne(info.getId());
        if(image!=null){
            image.setDocCode(info.getDocCode());
            if(StringUtils.isNotBlank(info.getFileNmeCN())){
                image.setFileNmeCN(info.getFileNmeCN());
            }
            if(StringUtils.isNotBlank(info.getNumber())){
                image.setNumber(info.getNumber());
            }
            if(StringUtils.isNotBlank(info.getExpireDateStr())){
                image.setExpireDateStr(info.getExpireDateStr());
            }
            imageDao.save(image);
            //如果是开户，将所有影像数据存入到ImageAccount表，其他则要更新ImageAccount表
            /*if(info.getOperateType()!=BillType.ACCT_OPEN){
                List<ImageAccount> list = imageAccountDao.findByDocCodeAndAcctId(info.getDocCode(),image.getAcctId());
                if (list.size()>0){
                    for (ImageAccount imageAccount:list) {
                        imageAccountDao.delete(imageAccount);
                    }
                }
            }
            ImageAccount account = new ImageAccount();
            ConverterService.convert(image,account);
            ImageAccount ia = imageAccountDao.findOne(image.getId());
            if(ia!=null){
                account.setVersionCt(ia.getVersionCt());
                imageAccountDao.save(account);
            }*/


        }else{
            log.info("影像不存在！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像不存在！");
        }
    }

    @Override
    public void delete(Long id) {
        imageDao.delete(id);
    }

    @Override
    public List<ImageInfo> query(ImageInfo info) {
        List<ImageInfo> list = new ArrayList<>();
        List<Image> images = new ArrayList<>();
        if(info.getRefBillId()!=null){
            if(info.getDocCode()!=null){
                //分类查询
                images = imageDao.findByRefBillIdAndDocCode(info.getRefBillId(),info.getDocCode());
            }else{
                //查询该流水所有影像
                images = imageDao.findByRefBillId(info.getRefBillId());
            }
        }else{
            log.info("流水id不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"流水id不能为空！");
        }
        if (images != null && images.size() > 0) {
            list = this.getImagePathForImageList(images);
        } else {
            log.info("没有查到影像！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "没有查到影像！");
        }
        return list;
    }

    @Override
    public int uploadToImage(ImageInfo info) {
        //记录上传失败的数量
        int res = 0;
        if(info.getRefBillId()==null){
            log.info("流水id不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"流水id不能为空！");
        }
        List<Image> images = imageDao.findByRefBillIdAndIsUpload(info.getRefBillId(),IsUpload.FALSE);
        log.info("开始上传影像至影像平台！");
        ImageConfig config = getImageConfig();
        UploadUtil util = new UploadUtil();
        //登录影像平台
        log.info("登录影像平台");
        util.login(config);

        for (Image image : images) {
            String result = util.upload(config, image.getImgPath());// 上传
            if (result.contains("FAIL")) {
                log.info("上传失败，本地影像路径："+image.getImgPath());
                res++;
            }else{
                log.info("上传成功，本地影像路径："+image.getImgPath());
                File file = new File(image.getImgPath());
                if(!file.exists()){
                    log.info("影像不存在，影像路径："+image.getImgPath());
                }else{
                    file.delete();
                    log.info("本地影像删除成功，影像路径："+image.getImgPath());
                }
                String batchNumber = util.deal(result);
                image.setBatchNumber(batchNumber);
                image.setIsUpload(IsUpload.TRUE);
                image.setImgPath("");
                imageDao.save(image);
            }
        }
        util.logout(config);
        log.info("登出影像平台");
        log.info("影像上传至影像平台结束！");
        return res;
    }

    @Override
    public void downLoadBatchImage(HttpServletResponse response, Long refBillId) {
        String zipUrl = createTempFilePath()+"/"+System.currentTimeMillis() + ".zip";
        File fileZip = new File(zipUrl);
        InputStream is = null;
        OutputStream zos = null;
        List<Image> list = imageDao.findByRefBillId(refBillId);
        List<File> tempFile = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            if(list.get(i).getIsUpload()==IsUpload.TRUE){
                log.info("已上传影像平台，不可下载");
                continue;
            }
            File file = new File(list.get(i).getImgPath());
            tempFile.add(file);

        }
        File[] files = new File[tempFile.size()];
        if(tempFile.size()<=0){
            log.info("没有查到影像或者影像已上传影像平台");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有查到影像或者影像已上传影像平台");
        }else{
            for (int i = 0 ; i < tempFile.size() ; i++){
                files[i] = tempFile.get(i);
            }
        }
        try {
            zos = response.getOutputStream();
            ZipUtils.compressFilesZip(files, zipUrl); // 压缩文件
            is = new FileInputStream(fileZip);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHddss");
            String format = sdf.format(new Date());
            String downFileName = format + ".zip";
            response.setContentType("application/x-download");
            response.setHeader("content-disposition", "attachment;fileName=" + downFileName);
            IOUtils.copy(is, zos);
            zos.flush();
        }catch (Exception e){
            log.error("批量影像下载异常："+e.getMessage());
        }finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(is);
        }

    }

    @Override
    public List<ImageInfo> queryByAccount(ImageInfo info) {
        List<ImageAccount> ImageAccounts = new ArrayList<>();
        List<ImageInfo> list = new ArrayList<>();
        if(info.getAcctId()!=null){
            if(info.getDocCode()!=null){
                //账户分类查询
                ImageAccounts = imageAccountDao.findByDocCodeAndAcctId(info.getDocCode(),info.getAcctId());
            }else{
                //账户查询所有
                ImageAccounts = imageAccountDao.findByAcctId(info.getAcctId());
            }
        }else{
            log.info("账户ID不能为空");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"查询影像异常！");
        }
        if(ImageAccounts!=null || ImageAccounts.size()>0){
            for (ImageAccount imageAccount:ImageAccounts) {
                ImageInfo resInfo = new ImageInfo();
                ConverterService.convert(imageAccount,resInfo);
                if(imageAccount.getIsUpload()==IsUpload.FALSE){
                    log.info("查询本地库,影像名称："+imageAccount.getFileName());
                    resInfo.setImgPath("/image/view/"+imageAccount.getId());
                    list.add(resInfo);
                }else if(imageAccount.getIsUpload()==IsUpload.TRUE){
                    log.info("查询影像平台,影像名称："+imageAccount.getFileName()+"批次号："+imageAccount.getBatchNumber());
                    ImageConfig config = getImageConfig();
                    UploadUtil util = new UploadUtil();
                    //登录影像平台
                    log.info("登录影像平台");
                    util.login(config);
                    //查询影像平台返回xml报文
                    log.info("开始查询影像");
                    String xmlString = util.query(config,imageAccount.getBatchNumber());
                    log.info("查询影像结束，返回报文："+xmlString);
                    //开始解析报文
                    log.info("开始解析报文");
                    XmlRoot xmlRoot = XmlUtils.dealXml(xmlString);
                    log.info("报文解析结束");
                    List<XmlFileBean> beans = null;
                    try {
                        beans = xmlRoot.getBatchBean().getDocument_Objects().getBatchFileBean().getFiles()
                                .getFileBean();
                    } catch (Exception e) {
                        log.info("没有找到批次号【"+imageAccount.getBatchNumber()+"】在影像平台的影像！");
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有找到批次号【"+imageAccount.getBatchNumber()+"】在影像平台的影像！");
                    }
                    if(beans.size()<=0 || beans==null){
                        log.info("没有找到批次号【"+imageAccount.getBatchNumber()+"】在影像平台的影像！");
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有找到批次号【"+imageAccount.getBatchNumber()+"】在影像平台的影像！");
                    }else{
                        log.info("开始提取影像url");
                        for (XmlFileBean xmlFileBean : beans) {
                            if(filterImageFormat(xmlFileBean.getFILE_FORMAT())){
                                resInfo.setImgPath(xmlFileBean.getURL());
                                list.add(resInfo);
                            }
                        }
                        log.info("URL提取结束");
                    }
                    log.info("登出影像平台");
                    util.logout(config);
                }else{
                    log.info("查询异常");
                }
            }
        }else{
            log.info("没有查到影像！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有查到影像！");
        }
        return list;
    }


    /**
     * 获取影像平台影像信息
     *
     * @param imageList 本地数据库影像集合
     * @return
     */
    private List<ImageInfo> getImagePathForImageList(List<Image> imageList) {
        List<ImageInfo> list = new ArrayList<>();
        for (Image image:imageList) {
            ImageInfo resInfo = new ImageInfo();
            ConverterService.convert(image,resInfo);
            if(image.getIsUpload()==IsUpload.FALSE){
                log.info("查询本地库,影像名称："+image.getFileName());
                resInfo.setImgPath("/image/view/"+image.getId());
                list.add(resInfo);
            }else if (image.getIsUpload()==IsUpload.TRUE){
                log.info("查询影像平台,影像名称："+image.getFileName()+"批次号："+image.getBatchNumber());
                ImageConfig config = getImageConfig();
                UploadUtil util = new UploadUtil();
                //登录影像平台
                log.info("登录影像平台");
                util.login(config);
                //查询影像平台返回xml报文
                log.info("开始查询影像");
                String xmlString = util.query(config,image.getBatchNumber());
                log.info("查询影像结束，返回报文："+xmlString);
                //开始解析报文
                log.info("开始解析报文");
                XmlRoot xmlRoot = XmlUtils.dealXml(xmlString);
                log.info("报文解析结束");
                List<XmlFileBean> beans = null;
                try {
                    beans = xmlRoot.getBatchBean().getDocument_Objects().getBatchFileBean().getFiles()
                            .getFileBean();
                } catch (Exception e) {
                    log.info("没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                }

                if(beans.size()<=0 || beans==null){
                    log.info("没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                }else{
                    log.info("开始提取影像url");
                    for (XmlFileBean xmlFileBean : beans) {
                        if(filterImageFormat(xmlFileBean.getFILE_FORMAT())){
                            resInfo.setImgPath(xmlFileBean.getURL());
                            list.add(resInfo);
                        }
                    }
                    log.info("url提取结束");
                }
                log.info("登出影像平台");
                util.logout(config);
            }else{
                log.info("查询异常");
            }
        }
        return list;
    }

    private String createTempFilePath() {
        return path +"/"+DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
    }
    private boolean filterImageFormat(String format){
        if("png".equalsIgnoreCase(format) || "JPEG".equalsIgnoreCase(format) || "GIF".equalsIgnoreCase(format) || "jpg".equalsIgnoreCase(format)){
            return true;
        }else{
            return false;
        }
    }
    private ImageConfig getImageConfig(){
        ImageConfig config = new ImageConfig();
        config.setFilePartName(filePartName);
        config.setGroupName(groupName);
        config.setIp(ip);
        config.setModelCode(modelCode);
        config.setPassword(password);
        config.setServerName(serverName);
        config.setSocketPort(socketPort);
        config.setSTARTCOLUMN(STARTCOLUMN);
        return config;
    }
}
