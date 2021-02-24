package com.ideatech.ams.controller.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AccountImageInfo;
import com.ideatech.ams.account.service.AccountImageService;
import com.ideatech.ams.image.dto.ImageTypeInfo;
import com.ideatech.ams.image.service.ImageTypeService;
import com.ideatech.ams.image.utils.ZipUtils;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.entity.id.IdWorker;
import com.ideatech.common.enums.CompanyIfType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by houxianghua on 2018/11/8.
 */

@Slf4j
@RestController
@RequestMapping("/accountImage")
public class AccountImageController {

    @Autowired
    private AccountImageService accountImageService;
    @Autowired
    private ImageTypeService imageTypeService;

    //影像本地存放地址
    @Value("${ams.accounts.upload.path}")
    private String path;

    //影像本地存放地址zip
    @Value("${ams.accounts.upload.zipPath}")
    private String zipPath;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IdWorker idWorker;

    /**
     * 上传证件图片
     * @param file  图片
     * @param imageTypeId    图片类型ID
     * @param type  证件类型名称
     * @param name  证件名称
     * @param no    证件编号
     * @param date  有效截止日期
     * @param url
     * @param response
     */
    @RequestMapping(value = "/uploadImage")
    public void uploadImage(@RequestParam("file") MultipartFile[] file, Long acctId, Long acctBillsId,Long imageTypeId, String type, String name, String no, String date, String url,String tempId, HttpServletResponse response) {
        try {
            String paths = "";
            for (MultipartFile multipartFile : file) {
                long filePath = idWorker.nextId();
                String filename = multipartFile.getOriginalFilename();
                String suffixFilename = org.apache.commons.lang.StringUtils.substringAfterLast(filename,".");
                String path = accountImageService.uploadImage(multipartFile.getInputStream(),filePath+"."+suffixFilename);
                paths += path + ",";
            }
            if (paths.length() > 0) {
                paths = paths.substring(0, paths.length() - 1);
            }
            List<AccountImageInfo> aiiList = accountImageService.findAccountImageList(acctId, acctBillsId,tempId);//证件图片集合
            for(AccountImageInfo accountImageInfo :aiiList){
                if(accountImageInfo.getImageTypeId() != null && imageTypeId != null && accountImageInfo.getImageTypeId() >0 && accountImageInfo.getImageTypeId().equals(imageTypeId)){
                    String filePath = accountImageInfo.getFilePath();
                    if(StringUtils.isNotBlank(filePath)){
                        filePath = filePath+","+paths;
                    }else{
                        filePath = paths;
                    }
                    accountImageInfo.setFilePath(filePath);
                    accountImageInfo.setSyncStatus(CompanyIfType.No);
                    accountImageService.save(accountImageInfo);
                    return;
                }
            }
            AccountImageInfo aii = new AccountImageInfo();
            aii.setImageTypeId(imageTypeId);
            aii.setTempId(tempId);
            aii.setSyncStatus(CompanyIfType.No);
            aii.setAcctId(acctId ==0 ? null : acctId);
            aii.setAcctBillsId(acctBillsId == 0? null : acctBillsId);
            if(StringUtils.isNotBlank(date)){
                aii.setMaturityDate(date);
            }
            aii.setFilePath(paths);
            accountImageService.save(aii);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     *
     * @param fileName
     * @param response
     */
    @RequestMapping(value = "/downloadImage",method = RequestMethod.GET)
    public void downLoadImage(String fileName, HttpServletResponse response){
        if(StringUtils.isBlank(fileName)){
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(path+ File.separator+fileName);
            outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     *
     * @param accountImageInfo
     */
    @RequestMapping(value = "/editImage",method = RequestMethod.POST)
    public ResultDto editImageInfo(AccountImageInfo accountImageInfo){
        List<AccountImageInfo> aiiList = accountImageService.findAccountImageList(accountImageInfo.getAcctId(), accountImageInfo.getAcctBillsId(),accountImageInfo.getTempId());//证件图片集合
        AccountImageInfo accountImageByImageTypeId = accountImageService.findAccountImageByImageTypeId(aiiList, accountImageInfo.getImageTypeId());
        if(accountImageByImageTypeId !=null) {
            accountImageByImageTypeId.setFileName(accountImageInfo.getFileName());
            accountImageByImageTypeId.setFileNo(accountImageInfo.getFileNo());
            accountImageByImageTypeId.setMaturityDate(accountImageInfo.getMaturityDate());
            accountImageByImageTypeId.setSyncStatus(CompanyIfType.No);
            accountImageService.save(accountImageByImageTypeId);
            return ResultDtoFactory.toAck("");
        }else{
            AccountImageInfo accountImageInfoNew = new AccountImageInfo();
            accountImageInfoNew.setFileName(accountImageInfo.getFileName());
            accountImageInfoNew.setFileNo(accountImageInfo.getFileNo());
            accountImageInfoNew.setMaturityDate(accountImageInfo.getMaturityDate());
            accountImageInfoNew.setImageTypeId(accountImageInfo.getImageTypeId());
            accountImageInfoNew.setTempId(accountImageInfo.getTempId());
            accountImageInfoNew.setAcctId(accountImageInfo.getAcctId());
            accountImageInfoNew.setAcctBillsId(accountImageInfo.getAcctBillsId());
            accountImageInfoNew.setSyncStatus(CompanyIfType.No);
            accountImageService.save(accountImageInfoNew);
            return ResultDtoFactory.toAck("");
        }
    }


    /**
     * 图片删除
     * @param accountImageInfo
     */
    @RequestMapping(value = "/deleteImage",method = RequestMethod.POST)
    public ResultDto deleteImage(AccountImageInfo accountImageInfo){
        List<AccountImageInfo> aiiList = accountImageService.findAccountImageList(accountImageInfo.getAcctId(), accountImageInfo.getAcctBillsId(),accountImageInfo.getTempId());//证件图片集合
        AccountImageInfo accountImageByImageTypeId = accountImageService.findAccountImageByImageTypeId(aiiList, accountImageInfo.getImageTypeId());
        if(accountImageByImageTypeId !=null) {
            accountImageService.softDelete(accountImageByImageTypeId.getId());
        }
        return ResultDtoFactory.toAck("");
    }


    /**
     *图片压缩下载
     * @param accountImageInfo
     */
    @RequestMapping(value = "/downloadZip",method = RequestMethod.GET)
    public void downloadImageZip(AccountImageInfo accountImageInfo,HttpServletResponse response){
        List<AccountImageInfo> aiiList = accountImageService.findAccountImageList(accountImageInfo.getAcctId(), accountImageInfo.getAcctBillsId(),accountImageInfo.getTempId());//证件图片集合
        AccountImageInfo accountImageByImageTypeId = accountImageService.findAccountImageByImageTypeId(aiiList, accountImageInfo.getImageTypeId());
        if(accountImageByImageTypeId !=null) {
            ImageTypeInfo imageTypeInfo = imageTypeService.getById(accountImageByImageTypeId.getImageTypeId());
            String fileName = accountImageByImageTypeId.getFileName();
            if(imageTypeInfo !=null){
                fileName = imageTypeInfo.getImageName();
            }
            String zipUrl = accountImageService.downloadZip(fileName);
            String filePath = accountImageByImageTypeId.getFilePath();
            String[] filePaths = StringUtils.split(filePath, ",");
            File[] files = new File[filePaths.length];
            for(int index=0;index < filePaths.length;index++){
                files[index] = new File(path+ File.separator+filePaths[index]);
            }
            ZipUtils.compressFilesZip(files, zipUrl); // 压缩文件
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(zipUrl);
                outputStream = response.getOutputStream();
                response.setContentType("application/x-download");
//                response.addHeader("Content-Disposition", "attachment;filename=" + fileName+".zip");
                response.addHeader("Content-Disposition", " attachment;filename=" + new String(fileName.getBytes(),"iso-8859-1") + ".zip");
                IOUtils.copy(inputStream, outputStream);
                outputStream.flush();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    /**
     * 获取证件图片数据
     * @param acctId    账号id
     * @param acctBillsId   流水号
     * @param info  集合类型查询参数实体
     * @return
     */
    @RequestMapping(value = "/getAccountImageList")
    public ResultDto getAccountImageList(Long acctId, Long acctBillsId, ImageTypeInfo info, String tempId) {
        List<AccountImageInfo> aiiList = accountImageService.findAccountImageList(acctId, acctBillsId,tempId);//证件图片集合
        List<ImageTypeInfo> all = imageTypeService.getImageType(info);//证件图片类型集合
        JSONObject out = new JSONObject();
        out.put("acctId", acctId);
        out.put("acctBillsId", acctBillsId);
        JSONArray arr = new JSONArray();
        for (ImageTypeInfo ii : all) {
            JSONObject json = new JSONObject();
            json.put("id", ii.getId());
            json.put("type", ii.getImageName());
            json.put("name", "");
            json.put("no", "");
            json.put("date", "");
            json.put("url", "");
            json.put("acctId","");
            json.put("acctBillsId","");
            json.put("tempId","");
            json.put("list", new JSONArray());
            for (AccountImageInfo aii : aiiList) {
                if (aii.getImageTypeId() != null && aii.getImageTypeId() >0 && aii.getImageTypeId().equals(ii.getId())) {
                    json.put("name", aii.getFileName());
                    json.put("no", aii.getFileNo());
                    json.put("date", aii.getMaturityDate());
                    if(aii.getAcctId() != null && aii.getAcctBillsId() != null){
                        out.put("acctId", aii.getAcctId());
                        out.put("acctBillsId", aii.getAcctBillsId());
                    }
                    json.put("acctId",aii.getAcctId());
                    json.put("acctBillsId",aii.getAcctBillsId());
                    json.put("tempId",aii.getTempId());
                    json.put("url", "");
                    JSONArray pathJsonArr = new JSONArray();
                    if (aii.getFilePath() != null) {
                        String[] pathArr = aii.getFilePath().split(",");
                        for (String path : pathArr) {
                            JSONObject j = new JSONObject();
                            j.put("imgUrl", path);
                            pathJsonArr.add(j);
                        }
                    }
                    json.put("list", pathJsonArr);
                }
            }
            arr.add(json);
        }
        out.put("data",arr);
        return ResultDtoFactory.toAckData(out);
    }

    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    public ResultDto getConfig(){
        List<ConfigDto> list = configService.findByKey("imageCollect");
        return ResultDtoFactory.toAckData(configService.list());
    }
}
