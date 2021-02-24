package com.ideatech.ams.image.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountsAllInfo;
import com.ideatech.ams.account.dto.bill.AccountBillsAllInfo;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.AccountsAllService;
import com.ideatech.ams.account.service.SyncHistoryService;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.customer.dto.CustomerPublicLogInfo;
import com.ideatech.ams.customer.service.CustomerPublicLogService;
import com.ideatech.ams.image.dao.ImageAllDao;
import com.ideatech.ams.image.dao.ImageConfigTerraceDao;
import com.ideatech.ams.image.dto.*;
import com.ideatech.ams.image.entity.ImageAll;
import com.ideatech.ams.image.entity.ImageConfigTerrace;
import com.ideatech.ams.image.utils.ImageUtils;
import com.ideatech.ams.image.utils.UploadUtil;
import com.ideatech.ams.image.utils.ZipUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.ams.system.dict.dto.OptionDto;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.ws.api.service.ImageAllSyncService;
import com.ideatech.common.converter.ConverterService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ImageAllServiceImpl implements ImageAllService{
    //影像本地存放地址
    @Value("${ams.image.path}")
    private String filePath;
    @Value("${ams.image.interfaceSyncUse:false}")
    private Boolean interfaceSyncUse;
    @Autowired
    private ImageAllDao imageAllDao;

    @Autowired
    private ImageTypeService imageTypeService;

    @Autowired
    private ImageConfigTerraceDao imageConfigTerraceDao;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private CustomerPublicLogService customerPublicLogService;

    @Autowired
    private AccountsAllService accountsAllService;

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ImageAllSyncService imageAllSyncService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private SyncHistoryService syncHistoryService;
    @PersistenceContext
    private EntityManager em; //注入EntityManager


    @Override
    public ImageAllInfo save(ImageAllInfo info) {
        setAccountIdAndCusId(info);
        ImageAll all = new ImageAll();
        ConverterService.convert(info,all);
        all = imageAllDao.save(all);
        info.setId(all.getId());
        return info;
    }

    @Override
    public ImageAllInfo createImage(String base64) {
        ImageAllInfo info = new ImageAllInfo();
        String filename = new Date().getTime() + ".jpg";
        String filePath = createFilePath()+"/"+filename;
        try {
            File file = new File(createFilePath());
            if (!file.exists()) {
                log.info("开始创建影像文件夹："+createFilePath());
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
    public String uploadImage(InputStream is,String filename) {
        String suffixFilename = org.apache.commons.lang.StringUtils.substringAfterLast(filename,".");
        String url = createFilePath()+"/"+filename;
        OutputStream os = new ByteArrayOutputStream();
        File file = new File(createFilePath());
        if (!file.exists()) {
            log.info("开始创建影像文件夹："+createFilePath());
            file.mkdirs();
        }
        byte[] byteArray = new byte[0];
        try {
            byteArray = IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("传输文件出现异常", e);
        }
        //压缩影像
        ImageUtils.compressImage(byteArray, os, suffixFilename);
        File finalFile = new File(url);
        try {
            FileUtils.writeByteArrayToFile(finalFile, ((ByteArrayOutputStream) os).toByteArray());
        }catch (Exception e){
            log.error("影像保存异常", e);
        }finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        //info.setFileName(filename);
        //info.setImgPath(url);
        return url;
    }

    @Override
    public ImageAllInfo findImageById(Long imageId) {
        ImageAllInfo info =null;
        ImageAll imageAll = imageAllDao.findOne(imageId);
        if(imageAll!=null){
            info = new ImageAllInfo();
            ConverterService.convert(imageAll,info);
        }
        return info;
    }

    @Override
    public ImageAllInfo editImageInfo(ImageAllInfo info) {
        ImageAll imageAll = imageAllDao.findOne(info.getId());
        if(StringUtils.isNotBlank(info.getDocCode())){
            imageAll.setDocCode(info.getDocCode());
        }
        if(StringUtils.isNotBlank(info.getFileNmeCN())){
            imageAll.setFileNmeCN(info.getFileNmeCN());
        }
        if(StringUtils.isNotBlank(info.getBatchNumber())){
            imageAll.setBatchNumber(info.getBatchNumber());
        }
        if(StringUtils.isNotBlank(info.getFileNo())){
            imageAll.setFileNo(info.getFileNo());
        }
        if(StringUtils.isNotBlank(info.getExpireDate())){
            imageAll.setExpireDate(info.getExpireDate());
        }
        imageAllDao.save(imageAll);
        return info;
    }

    @Override
    public void deleteById(Long imageId) {
        ImageAll imageAll = imageAllDao.findOne(imageId);
        if(imageAll==null){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像不存在！");
        }
        if(imageAll.getSyncStatus()==CompanyIfType.Yes){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像平台不可删除！");
        }
        File file = new File(imageAll.getImgPath());
        if (file.exists()) {
            file.delete();
        }else{
            log.info("影像文件不存在！");
        }
        imageAllDao.delete(imageId);
    }

    @Override
    public List<ImageAllInfo> queryByBillsId(Long billsId,String docCode) {
        List<ImageAll> list =null ;
        if(billsId!=null){
            if(StringUtils.isNotBlank(docCode)){
                list = imageAllDao.findByBillsIdAndDocCodeOrderByCreatedDateDesc(billsId,docCode);
            }else{
                list = imageAllDao.findByBillsIdOrderByCreatedDateDesc(billsId);
            }
        }
        return query(list);
    }

    @Override
    public List<ImageAllInfo> queryByAcctId(Long acctId, String docCode) {
        List<ImageAll> list =null;
        if(acctId!=null){
            if(StringUtils.isNotBlank(docCode)){
                list = imageAllDao.findByAcctIdAndDocCode(acctId,docCode);
            }else{
                list = imageAllDao.findByAcctId(acctId);
            }
        }
        return query(list);
    }

    @Override
    public List<ImageAllInfo> queryByCustomerId(Long customerId, String docCode) {
        List<ImageAll> list =null;
        if(customerId!=null){
            if(StringUtils.isNotBlank(docCode)){
                list = imageAllDao.findByCustomerIdAndDocCode(customerId,docCode);
            }else{
                list = imageAllDao.findByCustomerId(customerId);
            }
        }
        return query(list);
    }

    @Override
    public int uploadToImage(Long billsId) {
        //记录上传失败的数量

        if(billsId==null){
            log.info("流水id不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"流水id不能为空！");
        }
        List<ImageAll> images = imageAllDao.findByBillsIdAndSyncStatus(billsId,CompanyIfType.No);

        return uploadToImage(images);
    }

    @Override
    public int uploadToImage(Long billsId, String docCode) {
        if(billsId==null){
            log.info("流水id不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"流水id不能为空！");
        }
        if(StringUtils.isBlank(docCode)){
            log.info("影像类别code不能为空！");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像类别code不能为空！");
        }
        List<ImageAll> images = imageAllDao.findByBillsIdAndAndDocCodeAndSyncStatus(billsId,docCode,CompanyIfType.No);
        return uploadToImage(images);
    }
    private int uploadToImage(List<ImageAll> images){
        int res = 0;
        if(interfaceSyncUse){
            log.info("使用现场接口上传到影像平台");
            return interfaceSync(images);
        }
        log.info("开始上传影像至影像平台！");
        ImageConfig config = new ImageConfig();
        ImageConfigTerrace ict = imageConfigTerraceDao.findOne(1001L);
        ConverterService.convert(ict,config);
        UploadUtil util = new UploadUtil();
        //登录影像平台
        log.info("登录影像平台");
        util.login(config);

        for (ImageAll image : images) {
            String result = util.upload(config, image.getImgPath(),image.getFileFormat());// 上传
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
                image.setSyncStatus(CompanyIfType.Yes);
                image.setImgPath("");
                imageAllDao.save(image);
            }
        }
        util.logout(config);
        log.info("登出影像平台");
        log.info("影像上传至影像平台结束！");
        return res;
    }
    private int interfaceSync(List<ImageAll> images){
        int num=0;
        for (ImageAll i:images) {
            try {
                ImageAllInfo info = new ImageAllInfo();
                ConverterService.convert(i,info);
                String batchNumber = imageAllSyncService.syncToImage(info);
                log.info("影像上传到影像平台返回批次号：{}",batchNumber);
                if(StringUtils.isNotBlank(batchNumber)){
                    i.setBatchNumber(batchNumber);
                    i.setSyncStatus(CompanyIfType.Yes);
                    i.setImgPath("");
                    imageAllDao.save(i);
                }else{
                    num++;
                }
            }catch (Exception e){
                num++;
                log.error("上报失败：{}",e);
            }
        }
        return num;
    }
    @Override
    public List<ImageAllInfo> findByBillsId(Long billsId, String docCode) {
        List<ImageAllInfo> data = new ArrayList<>();
        List<ImageAll> list = imageAllDao.findByBillsIdAndDocCodeOrderByExpireDateDesc(billsId,docCode);
        for (ImageAll image:list) {
            ImageAllInfo info = new ImageAllInfo();
            ConverterService.convert(image,info);
            data.add(info);
        }
        return data;
    }

    @Override
    public void downloadImageZip(Long acctBillsId, Long imageTypeId, HttpServletResponse response) {
        String zipUrl = createFilePath()+"/"+System.currentTimeMillis() + ".zip";
        File fileZip = new File(zipUrl);
        InputStream is = null;
        OutputStream zos = null;
        ImageTypeInfo typeInfo = imageTypeService.getById(imageTypeId);
        List<ImageAll> listData = imageAllDao.findByBillsIdAndDocCodeOrderByExpireDateDesc(acctBillsId,typeInfo.getValue());
        List<File> tempFile = new ArrayList<>();
        for (int i=0;i<listData.size();i++) {
            File file = new File(listData.get(i).getImgPath());
            tempFile.add(file);
        }
        File[] files = new File[tempFile.size()];
        if(tempFile.size()<=0){
            log.info("没有查到影像");
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有查到影像");
        }else{
            for (int i = 0 ; i < tempFile.size() ; i++){
                files[i] = tempFile.get(i);
            }
        }
        try {
            zos = response.getOutputStream();
            ZipUtils.compressFilesZip(files, zipUrl); // 压缩文件
            is = new FileInputStream(fileZip);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_dd_ss");
            String format = sdf.format(new Date());
            String downFileName = format + ".zip";
            response.setContentType("application/x-download");
            response.setHeader("content-disposition", "attachment;fileName=" + new String(downFileName.getBytes(),"iso-8859-1"));
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
    public void downloadImageZip2(String fileName, String[] filePaths, HttpServletResponse response) {
        String zipUrl = createFilePath() + "/" + System.currentTimeMillis() + ".zip";
        File[] files = new File[filePaths.length];
        for (int index = 0; index < filePaths.length; index++) {
            files[index] = new File(filePaths[index]);
        }
        ZipUtils.compressFilesZip(files, zipUrl); // 压缩文件
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(zipUrl);
            outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".zip");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    public void setCustomerIdAndAcctId(Long acctBillsId, Long customerId, Long acctId) {
       List<ImageAll> list = imageAllDao.findByBillsId(acctBillsId);
        for (ImageAll im:list) {
            if(customerId!=null){
                im.setCustomerId(customerId);
            }
            if(acctId!=null){
                im.setAcctId(acctId);
            }
            imageAllDao.save(im);
        }
    }

    @Override
    public void setCustomerIdAndAcctIdByImageTempNo(String imageTempNo, Long acctBillsId, Long customerId, Long acctId) {
            if(StringUtils.isNotBlank(imageTempNo)){
                String[] strings = imageTempNo.split(",");
                for (String str:strings) {
                    List<ImageAll> data = imageAllDao.findByTempNo(str);
                    for (ImageAll all :data) {
                        if(acctBillsId!=null){
                            all.setBillsId(acctBillsId);
                        }
                        if(customerId!=null){
                            all.setCustomerId(customerId);
                        }
                        if(acctId!=null){
                            all.setAcctId(acctId);
                        }
                        imageAllDao.save(all);
                    }
                }

            }
    }

    @Override
    public String saveImageFromOut(ImageAllInfo info,String flag) {
        ImageAll all = new ImageAll();
        if("1".equals(flag)){
            if(info.getBillsId()==null){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"流水编号不能为空！");
            }
        }
        if(StringUtils.isBlank(info.getBatchNumber())){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像批次号不能为空！");
        }
        if(StringUtils.isBlank(info.getDocCode())){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"影像类型code不能为空！");
        }
        if(info.getBillsId()!=null){
            setAccountIdAndCusId(info);
        }
        ConverterService.convert(info,all);
        all.setSyncStatus(CompanyIfType.Yes);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String tempNo = formatter.format(currentTime)+String.valueOf(currentTime.getTime());
        all.setTempNo(tempNo);
        imageAllDao.save(all);
        return tempNo;
    }


    /**
     * 根据客户id获取影像信息
     *
     * @param customerNo 客户号
     */
    @Override
    public List<ImageAllInfo> getImageForCustomer(String customerNo) {
        //根据客户id获取账户id信息
        List<AccountsAllInfo> aaiList = accountsAllService.findByCustomerNo(customerNo);
        List<Long> acctIdList = new ArrayList<>();//账户id集合
        for (AccountsAllInfo aai : aaiList) {
            acctIdList.add(aai.getId());
        }
        return this.getImageForAcctIds(acctIdList);
    }

    /**
     * 根据CustomerId获取客户最新影像信息
     * @param customerId
     */
    @Override
    public List<ImageAllInfo> getImageByCustomerId(Long customerId) {

        //根据客户id获取对公客户日志ids信息
        List<CustomerPublicLogInfo>  cplList = customerPublicLogService.getByCustomerId(customerId);
        List<Long> idsList = new ArrayList<>();//customerPublicLogid集合
        for (CustomerPublicLogInfo c : cplList) {
            idsList.add(c.getId());
        }
        List<AccountBillsAllInfo> abaiList = accountBillsAllService.findByCustomerLogIdIn(idsList);

        List<Long> idsList2 = new ArrayList<>();//customerPublicLogid集合
        for (AccountBillsAllInfo a : abaiList) {
            idsList2.add(a.getId());
        }
        return this.getImageForBillIds(idsList2);
    }

    @Override
    public boolean isHaveBills(String customerNo) {
        List<AccountsAllInfo> aaiList = accountsAllService.findByCustomerNo(customerNo);
        if(CollectionUtils.isEmpty(aaiList) || aaiList.size()<=0){
            return false;
        }else{
            List<Long> acctIdList = new ArrayList<>();//账户id集合
            for (AccountsAllInfo aai : aaiList) {
                acctIdList.add(aai.getId());
            }
            List<AccountBillsAllInfo> abaiList = accountBillsAllService.findByAccountIdIn(acctIdList);
            if(CollectionUtils.isNotEmpty(aaiList) && abaiList.size()>0){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据客户id判断是否有流水
     * @param customerId
     * @return true 有流水；false 没有流水
     */
    @Override
    public boolean isHaveBills2(Long customerId) {
        //根据客户id获取对公客户日志ids信息
        List<CustomerPublicLogInfo>  cplList = customerPublicLogService.getByCustomerId(customerId);
        List<Long> idsList = new ArrayList<>();//账户id集合

        if(CollectionUtils.isEmpty(cplList) || cplList.size()<=0){
            return false;//对公客户日志为空无法查到关联流水。
        }else{
            for (CustomerPublicLogInfo c : cplList) {
                idsList.add(c.getId());
            }
            List<AccountBillsAllInfo> abaiList = accountBillsAllService.findByCustomerLogIdIn(idsList);
            if(abaiList.size()>0){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据账户id集合获取影像信息
     *
     * @param acctId 账户id
     */
    @Override
    public List<ImageAllInfo> getImageForAcctId(Long acctId) {
        List<Long> acctIdList = new ArrayList<>();
        acctIdList.add(acctId);
        return this.getImageForAcctIds(acctIdList);
    }

    /**
     * 根据流水id获取影像信息
     *
     * @param billId 流水id
     */
    @Override
    public List<ImageAllInfo> getImageForBillId(Long billId) {
        List<ImageAll> imageAllList = imageAllDao.findByBillsId(billId);
        //去远程服务器中获取影像信息
        List<ImageAllInfo> list = this.query(imageAllList);
        //设置docName
        List<OptionDto> optionDtoList = dictionaryService.findOptionsByDictionaryName("imageOption");//获取影像类型字典数据
        for (ImageAllInfo iai : list) {
            for (OptionDto optionDto : optionDtoList) {
                if (optionDto.getValue().equals(iai.getDocCode())) {
                    iai.setDocName(optionDto.getName());
                    break;
                }
            }
        }
        return list;
    }

    @Override
    public JSONArray formatImageJson(List<ImageAllInfo> imageAllList) {
        JSONArray arr = new JSONArray();
        Map<String, JSONArray> map = new HashMap();
        for (ImageAllInfo iai : imageAllList) {
            if (iai.getDocName() != null) {
                JSONObject j = new JSONObject();
                j.put("imgUrl", iai.getImgPath());
                if (map.containsKey(iai.getDocName())) {
                    map.get(iai.getDocName()).add(j);
                } else {
                    JSONArray pathJsonArr = new JSONArray();
                    pathJsonArr.add(j);
                    map.put(iai.getDocName(), pathJsonArr);
                }
            }
        }
        int id = 1;
        for (String docName : map.keySet()) {
            JSONObject json = new JSONObject();
            json.put("type", docName);
            json.put("list", map.get(docName));
            json.put("url", "");
//            json.put("acctBillsId", 1069930295833600L);
//            json.put("acctId", 1069930296152064L);
//            json.put("tempId", "155005029746734447");
            json.put("id", id);
            for (ImageAllInfo iai : imageAllList) {
                if (iai.getDocName().equals(docName)) {
                    json.put("name", iai.getFileNmeCN());
                    json.put("no", iai.getFileNo());
                    json.put("date", iai.getExpireDate());
                    break;
                }
            }
            arr.add(json);
            id++;
        }
        return arr;
    }

    @Override
    public ResultDto sync(Long billId) {
        AllBillsPublicDTO dto = allBillsPublicService.findOne(billId);
        if(dto.getPbcSyncStatus()!=CompanySyncStatus.tongBuChengGong){
            return ResultDtoFactory.toNack("人行账管需上报成功才能上报影像，当前人行上报状态："+dto.getPbcSyncStatus().getValue());
        }
        if(dto.getImgaeSyncStatus()==CompanySyncStatus.tongBuChengGong){
            return ResultDtoFactory.toNack("人行影像已经上报成功，无需再次上报");
        }
        List<ImageAllSyncDto> data = new ArrayList<>();
        List<ImageAll> list = imageAllDao.findByBillsId(billId);
        log.info("需要上报的影像数量:{}",list.size());
        List<ConfigDto> pbcLogin = configService.findByKey("syncImageEnabled");
        if (CollectionUtils.isNotEmpty(pbcLogin)) {
            boolean syncImageEnabled = Boolean.valueOf(pbcLogin.get(0).getConfigValue());
            if(syncImageEnabled){
                log.info("登录挡板开启，默认上报成功");
                accountBillsAllService.updateImageSyncStatus(billId,CompanySyncStatus.tongBuChengGong);
                log.info("调用上报接口结束，更新本地流水状态");
                syncHistoryService.write(dto,EAccountType.IMS,CompanySyncStatus.tongBuChengGong,"");
                return ResultDtoFactory.toAck();
            }
        }else{
            log.info("登录挡板开启，默认上报成功");
            syncHistoryService.write(dto,EAccountType.IMS,CompanySyncStatus.tongBuChengGong,"");
            return ResultDtoFactory.toAck();
        }
        if(CollectionUtils.isNotEmpty(list) && list.size()>0){
            for (ImageAll imageAll:list) {
                ImageAllSyncDto imageAllSyncDto = new ImageAllSyncDto();
                ConverterService.convert(imageAll,imageAllSyncDto);
                File file  = new File(imageAll.getImgPath());
                imageAllSyncDto.setFile(file);
                data.add(imageAllSyncDto);
            }
        }

        try {
            log.info("开始调用上报接口");
            imageAllSyncService.sync(data);
            log.info("调用上报接口结束，更新本地流水状态");
            accountBillsAllService.updateImageSyncStatus(billId,CompanySyncStatus.tongBuChengGong);
            syncHistoryService.write(dto,EAccountType.IMS,CompanySyncStatus.tongBuChengGong,"");
            log.info("影像上报完成");
            return ResultDtoFactory.toAck();
        }catch (Exception e){
            log.error("流水{}的影像数据上报异常，原因：{}",billId,e.getMessage());
            String msg = "影像数据上报异常，原因:"+e.getMessage();
            accountBillsAllService.updateImageSyncStatus(billId,CompanySyncStatus.tongBuShiBai);
            syncHistoryService.write(dto,EAccountType.IMS,CompanySyncStatus.tongBuShiBai,msg);
            return ResultDtoFactory.toNack(msg);
        }

    }

    @Override
    public ResultDto update(Long[] imgIds,Long imageTypeId) {

        ImageTypeInfo info = imageTypeService.getById(imageTypeId);
        if(info!=null){
            int success = 0;
            int fail = 0;
            String failNumMsg="";
            for (Long id:imgIds) {
                ImageAll imageAll = imageAllDao.findOne(id);
                if(imageAll!=null){
                    imageAll.setDocCode(info.getValue());
                    imageAllDao.save(imageAll);
                    success++;
                }else{
                    failNumMsg = failNumMsg+"【" + String.valueOf(id)+"】";
                    fail++;
                }
            }
            String msg = "转移成功"+success+"条，失败"+fail+"条,具体ID："+failNumMsg;
            return ResultDtoFactory.toAck(msg);
        }else{
            log.info("找不到影像配置");
            return ResultDtoFactory.toNack("找不到影像配置");
        }
    }

    /**
     * 根据账户id集合获取影像信息
     *
     * @param acctIdList 账户id集合
     */
    private List<ImageAllInfo> getImageForAcctIds(List<Long> acctIdList) {
        //根据账户id获取该账户所有流水id
        List<AccountBillsAllInfo> abaiList = accountBillsAllService.findByAccountIdIn(acctIdList);
        List<Long> billIdList = new ArrayList<>();//流水id集合
        for (AccountBillsAllInfo abai : abaiList) {
            billIdList.add(abai.getId());
        }
        return this.getImageForBillIds(billIdList);
    }

    /**
     * 根据流水id集合获取影像信息
     *
     * @param billIdList 流水id集合
     */
    private List<ImageAllInfo> getImageForBillIds(List<Long> billIdList) {
        List<ImageAll> imageAllList = this.getNewImageAllsByBills(billIdList);
        //去远程服务器中获取影像信息
        List<ImageAllInfo> list = this.query(imageAllList);
        //设置docName
        List<OptionDto> optionDtoList = dictionaryService.findOptionsByDictionaryName("imageOption");//获取影像类型字典数据
        for (ImageAllInfo iai : list) {
            for (OptionDto optionDto : optionDtoList) {
                if (optionDto.getValue().equals(iai.getDocCode())) {
                    iai.setDocName(optionDto.getName());
                    break;
                }
            }
        }
        return list;
    }

    /**
     * 根据指定流水集合查询出所有类型的最新影像
     * <p>
     * 由于一种影像可能有多张影像信息，故以下逻辑处理时，
     * 先查询出每个影像类型的最新数据，
     * 再根据这条数据查询出他所在的流水，
     * 再查询出该条流水上传的该影像类型的影像数据（一条或多条）
     *
     * @param billIdList
     * @return
     */
    private List<ImageAll> getNewImageAllsByBills(List<Long> billIdList) {
        String sql = "   SELECT " +
                "          max( YD_CREATED_DATE ) as YD_CREATED_DATE, " +
                "          YD_DOC_CODE " +
                "        FROM " +
                "          YD_IMAGE_ALL " +
                "        WHERE " +
                "          YD_DOC_CODE IS NOT NULL " +
                "          AND YD_BILLS_ID in (?1) " +
                "        GROUP BY " +
                "          YD_DOC_CODE ";

        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter(1, billIdList);
        List rows = query.getResultList();

        List<ImageAll> imageAllList = new ArrayList<>();
        for (Object obj : rows) {
            Map map = (Map) obj;
            List<ImageAll> imageList1 = imageAllDao.findByCreatedDateAndDocCode((Date) map.get("YD_CREATED_DATE"), (String) map.get("YD_DOC_CODE"));
            for (ImageAll imageAll : imageList1) {
                List<ImageAll> imageAllList2 = imageAllDao.findByBillsIdAndDocCodeOrderByExpireDateDesc(imageAll.getBillsId(), imageAll.getDocCode());
                imageAllList.addAll(imageAllList2);
            }
        }
        return imageAllList;
    }

    private List<ImageAllInfo> query(List<ImageAll> list){
        List<ImageAllInfo> data = new ArrayList<>();
        if(list!=null && list.size()>0){
            for (ImageAll image:list) {
                ImageAllInfo resInfo = new ImageAllInfo();
                ConverterService.convert(image,resInfo);
                if(image.getSyncStatus()==CompanyIfType.No){
                    //log.info("查询本地库,影像名称："+image.getFileName());
                    resInfo.setImgPath(image.getImgPath());
                    data.add(resInfo);
                }else if (image.getSyncStatus()==CompanyIfType.Yes){
                    log.info("查询影像平台,影像名称："+image.getFileName()+"批次号："+image.getBatchNumber());
                    if(interfaceSyncUse){
                        log.info("使用现场接口查询影像平台数据");
                        try {
                            String url = imageAllSyncService.selectToImage(image.getBatchNumber());
                            log.info("影响平台查询回来的URL：{}",url);
                            resInfo.setImgPath(url);
                            data.add(resInfo);
                        }catch (Exception e){
                            log.error("查询异常：{}",e);
                            continue;
                        }
                    }else{
                        ImageConfig config = new ImageConfig();
                        ImageConfigTerrace ict = imageConfigTerraceDao.findOne(1001L);
                        ConverterService.convert(ict,config);
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
                        List<XmlFileBean> imageBeans = null;
                        try {
                            imageBeans = xmlRoot.getBatchBean().getDocument_Objects().getBatchFileBean().getFiles()
                                    .getFileBean();
                        } catch (Exception e) {
                            log.info("没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                        }

                        if(imageBeans.size()<=0 || imageBeans==null){
                            log.info("没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"没有找到批次号【"+image.getBatchNumber()+"】在影像平台的影像！");
                        }else{
                            log.info("开始提取影像url！");
                            for (XmlFileBean xmlFileBean : imageBeans) {
                                if(filterImageFormat(xmlFileBean.getFILE_FORMAT())){
                                    resInfo.setImgPath(xmlFileBean.getURL());
                                    data.add(resInfo);
                                }
                            }
                            log.info("url提取结束！");
                        }
                        log.info("登出影像平台");
                        util.logout(config);
                    }

                }else{
                    log.info("查询异常");
                }
            }

        }
        return data;
    }
    private void setAccountIdAndCusId(ImageAllInfo info){
        //设置客户id和账户id
        AllBillsPublicDTO dto =  allBillsPublicService.findOne(info.getBillsId());
        if(dto!=null){
            info.setAcctId(dto.getAcctId());
            if(dto.getCustomerLogId()!=null){
                CustomerPublicLogInfo log = customerPublicLogService.getOne(dto.getCustomerLogId());
                if(log!=null){
                    info.setCustomerId(log.getCustomerId());
                }
            }
        }
    }
    private String createFilePath() {
        String path = filePath +"/"+DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
    private boolean filterImageFormat(String format){
        if("png".equalsIgnoreCase(format) || "JPEG".equalsIgnoreCase(format) || "GIF".equalsIgnoreCase(format) || "jpg".equalsIgnoreCase(format)){
            return true;
        }else{
            return false;
        }
    }
}
