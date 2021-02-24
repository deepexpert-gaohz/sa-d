package com.ideatech.ams.controller.image;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.image.dto.ImageAllInfo;
import com.ideatech.ams.image.dto.ImageTypeInfo;
import com.ideatech.ams.image.service.ImageAllService;
import com.ideatech.ams.image.service.ImageTypeService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.CompanyIfType;
import com.ideatech.common.util.EscapeUnescape;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/imageAll")
public class ImageAllController {
    @Autowired
    private ImageAllService imageAllService;
    @Autowired
    private ImageTypeService imageTypeService;


    /**
     * 导入影像
     * @param file
     */
    @PostMapping(value = "/uploadImage")
    public void uploadImage(@RequestParam("file") MultipartFile[] file,Long acctId, Long acctBillsId,Long imageTypeId, String type, String name, String no, String date, String url,String tempId){
        ImageAllInfo info = new ImageAllInfo();
        ImageTypeInfo typeInfo = imageTypeService.getById(imageTypeId);
        info.setBillsId(acctBillsId);
        info.setDocCode(typeInfo.getValue());
        try {
            for (MultipartFile multipartFile : file) {
                String filename = multipartFile.getOriginalFilename();
                String suffixFilename = org.apache.commons.lang.StringUtils.substringAfterLast(filename,".");
                filename=new Date().getTime()+"."+suffixFilename;
                log.info("图片影像名称：{}",filename);
                String path = imageAllService.uploadImage(multipartFile.getInputStream(),filename);
                info.setImgPath(path);
                info.setFileName(filename);
                info.setFileFormat(suffixFilename);
                imageAllService.save(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *高拍仪上传
     */
    @PostMapping(value = "/uploadByHardWare")
    public ResultDto<ImageAllInfo> uploadByHardWare(HttpServletRequest request,Long acctBillsId,Long imageTypeId){
        String base64 = request.getParameter("base64");
        ImageAllInfo info = new ImageAllInfo();
        ImageTypeInfo typeInfo = imageTypeService.getById(imageTypeId);
        info.setBillsId(acctBillsId);
        info.setDocCode(typeInfo.getValue());
        ImageAllInfo res = imageAllService.createImage(base64);
        info.setImgPath(res.getImgPath());
        info.setFileName(res.getFileName());
        info.setFileFormat("jpg");
        info = imageAllService.save(info);
        return ResultDtoFactory.toAckData(info);
    }

    /**
     * 编辑影像信息
     */
    @RequestMapping(value = "/editImage",method = RequestMethod.POST)
    public ResultDto setImageType(String maturityDate,String fileName,String fileNo,Long acctBillsId,Long imageTypeId,String docCode){
        ImageAllInfo info = new ImageAllInfo();
        if(StringUtils.isBlank(maturityDate)){
            info.setExpireDate("");
        }else{
            info.setExpireDate(maturityDate);
        }
        if(StringUtils.isBlank(fileName)){
            info.setFileNmeCN("");
        }else{
            info.setFileNmeCN(fileName);
        }
        if(StringUtils.isBlank(fileNo)){
            info.setFileNo("");
        }else{
            info.setFileNo(fileNo);
        }
        if(StringUtils.isNotBlank(docCode)){
            info.setDocCode(docCode);
        }
        ImageTypeInfo typeInfo = imageTypeService.getById(imageTypeId);
        List<ImageAllInfo> list = imageAllService.findByBillsId(acctBillsId,typeInfo.getValue());
        for (ImageAllInfo imageInfo:list) {
            info.setId(imageInfo.getId());
            imageAllService.editImageInfo(info);
        }
        return ResultDtoFactory.toAck();
    }

    /**
     * 删除影像
     */
    @RequestMapping(value = "/deleteImage",method = RequestMethod.POST)
    public ResultDto deleteImage(Long acctBillsId,Long imageTypeId){
        ImageTypeInfo typeInfo = imageTypeService.getById(imageTypeId);
        List<ImageAllInfo> list = imageAllService.findByBillsId(acctBillsId,typeInfo.getValue());
        for (ImageAllInfo imageInfo:list) {
            imageAllService.deleteById(imageInfo.getId());
        }
        return ResultDtoFactory.toAck();
    }

    /**
     * 图片下载
     */
    @RequestMapping(value = "/downloadImage",method = RequestMethod.GET)
    public void downLoadImage(String fileName, HttpServletResponse response){
        if(org.apache.commons.lang3.StringUtils.isBlank(fileName)){
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
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
     *图片压缩下载
     */
    @RequestMapping(value = "/downloadZip",method = RequestMethod.GET)
    public void downloadImageZip(Long acctBillsId,Long imageTypeId,HttpServletResponse response){
        imageAllService.downloadImageZip(acctBillsId,imageTypeId,response);
    }

    /**
     * 图片压缩下载
     *
     * @param fileName    压缩文件名称
     * @param filePath 影像存放路径
     * @param response
     */
    @RequestMapping(value = "/downloadZip2")
    public void downloadImageZip2(String fileName, String[] filePath, HttpServletResponse response) {
        for (int i = 0; i < filePath.length; i++) {//文件路径解码
            log.info(EscapeUnescape.unescape(filePath[i]));
            filePath[i] = EscapeUnescape.unescape(filePath[i]);
        }
        imageAllService.downloadImageZip2(fileName, filePath, response);
    }

    /**
     * 查询影像
     * billsId(流水id) 必填
     * docCode(影像种类code) 选填
     *
     * @return
     */
    @RequestMapping(value = "/queryByBillsId")
    public ResultDto  query(Long acctBillsId,ImageTypeInfo info){
        List<ImageTypeInfo> all = imageTypeService.getImageType(info);//证件图片类型集合

        JSONObject out = new JSONObject();
        out.put("acctBillsId", acctBillsId);
        out.put("acctId", "");
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
            json.put("docCode",ii.getValue());
            //json.put("list", new JSONArray());
            List<ImageAllInfo> aiiList = imageAllService.queryByBillsId(acctBillsId,ii.getValue());
            JSONArray pathJsonArr = new JSONArray();
            for (ImageAllInfo aii : aiiList) {
                if(StringUtils.isNotBlank(aii.getDocCode())&&StringUtils.equals(aii.getDocCode(),ii.getValue())){
                    json.put("name", aii.getFileNmeCN());
                    json.put("no", aii.getFileNo());
                    json.put("date", aii.getExpireDate());
                    json.put("acctBillsId", aii.getBillsId());
                    json.put("url", "");
                    if(aii.getSyncStatus()==CompanyIfType.No){
                        json.put("type", ii.getImageName()+"(未上报)");
                    }else{
                        json.put("type", ii.getImageName()+"(已上报)");
                    }

                    JSONObject j = new JSONObject();
                    j.put("imgUrl", aii.getImgPath());
                    j.put("id", aii.getId());
                    pathJsonArr.add(j);
                }
            }
            json.put("list", pathJsonArr);
            arr.add(json);
        }
        out.put("data",arr);
        /*try {
            List<ImageAllInfo> list = imageAllService.queryByBillsId(billsId,docCode);
            return ResultDtoFactory.toAckData(list);
        }catch (Exception e){
            ResultDto dto = new ResultDto();
            dto.setMessage(e.getMessage());
            return dto;
        }*/
        return ResultDtoFactory.toAckData(out);
    }

    /**
     * 上传到影像平台
     * refBillId(流水id) 必填
     *
     */
    @RequestMapping(value = "/uploadToImage",method = RequestMethod.POST)
    public ResultDto  uploadToImage(Long acctBillsId,Long imageTypeId){

        int res =0;
        try {
            if(imageTypeId!=null){
                ImageTypeInfo typeInfo = imageTypeService.getById(imageTypeId);
                res = imageAllService.uploadToImage(acctBillsId,typeInfo.getValue());
            }else {
                res = imageAllService.uploadToImage(acctBillsId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDtoFactory.toNack(e.getMessage());
        }

        return ResultDtoFactory.toAckData(res);
    }


    /**
     * 根据客户号获取最新的营业执照和法人身份证的影像信息
     * 1、根据customerNo拿到accountsAll帐户表的id集合（跟accountsAll的customerNo字段关联）
     * 2、帐户表的id集合拿到accountBillsAll流水表的id集合（跟accountBillsAll的accountId字段关联）
     * 3、根据流水表的id集合拿到YD_IMAGE_ALL影像表的数据集合（跟YD_IMAGE_ALL的YD_BILLS_ID字段关联）
     * @param customerNo 客户号
     */
    @RequestMapping(value = "/getImageForCustomer", method = RequestMethod.POST)
    public ResultDto getImageForCustomer(String customerNo) {
        List<ImageAllInfo> imageAllList = imageAllService.getImageForCustomer(customerNo);
        //筛选影像类型
        List<ImageAllInfo> list = new ArrayList<>();
        List<String> customerImageTypeList = Arrays.asList("33", "2", "3");//单位负责人身份证件，营业执照正本，营业执照副本
        for (String type : customerImageTypeList) {
            for (ImageAllInfo iai : imageAllList) {
                if (type.equals(iai.getDocCode())) {
                    list.add(iai);
                }
            }
        }
        JSONArray arr = imageAllService.formatImageJson(list);
        return ResultDtoFactory.toAckData(arr);
    }

    /**
     * RSN-4422
     * 方式二：CustomerId
     * 1、根据CustomerId拿到CustomerPublicLog对公客户日志表的id（跟CustomerPublicLog的CustomerId字段关联）
     * 2、公客户日志表的id集合拿到accountBillsAll流水表的id集合（跟accountBillsAll的customerPublicLogId字段关联）
     * 3、根据流水表的id集合拿到YD_IMAGE_ALL影像表的数据集合（跟YD_IMAGE_ALL的YD_BILLS_ID字段关联）
     *
     * @param customerId 客户id
     */
    @RequestMapping(value = "/getImageForCustomerId", method = RequestMethod.POST)
    public ResultDto getImageForCustomerId(Long customerId) {
        List<ImageAllInfo> imageAllList = imageAllService.getImageByCustomerId(customerId);
        //筛选影像类型
        List<ImageAllInfo> list = new ArrayList<>();
        List<String> customerImageTypeList = Arrays.asList("33", "2", "3");//单位负责人身份证件，营业执照正本，营业执照副本
        for (String type : customerImageTypeList) {
            for (ImageAllInfo iai : imageAllList) {
                if (type.equals(iai.getDocCode())) {
                    list.add(iai);
                }
            }
        }
        JSONArray arr = imageAllService.formatImageJson(list);
        return ResultDtoFactory.toAckData(arr);
    }

    @RequestMapping(value = "/isHaveBills", method = RequestMethod.GET)
    public ResultDto isHaveBills(String customerNo){
        boolean result = imageAllService.isHaveBills(customerNo);
        return ResultDtoFactory.toAckData(result);
    }

    /**
     * 根据customerId判断是否有流水。
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/isHaveBills2", method = RequestMethod.GET)
    public ResultDto isHaveBillsByCustomerId(Long customerId){
        boolean result = imageAllService.isHaveBills2(customerId);
        return ResultDtoFactory.toAckData(result);
    }

    /**
     * 根据账户id获取最新的影像信息
     * @param accountId 账户id
     */
    @RequestMapping(value = "/getImageForAccount", method = RequestMethod.POST)
    public ResultDto getImageForAccount(Long accountId) {
        List<ImageAllInfo> imageAllList = imageAllService.getImageForAcctId(accountId);
        JSONArray arr = imageAllService.formatImageJson(imageAllList);
        return ResultDtoFactory.toAckData(arr);
    }

    /**
     * 根据账户id获取最新的影像信息
     * @param billId 流水id
     */
    @RequestMapping(value = "/getImageForBill", method = RequestMethod.POST)
    public ResultDto getImageForBill(Long billId) {
        List<ImageAllInfo> imageAllList = imageAllService.getImageForBillId(billId);
        JSONArray arr = imageAllService.formatImageJson(imageAllList);
        return ResultDtoFactory.toAckData(arr);
    }
    /**
     * 影像上报
     * @param acctBillsId 流水id
     */
    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    public ResultDto sync(Long acctBillsId) {
        return imageAllService.sync(acctBillsId);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete")
    public ResultDto delete(@RequestParam(value = "ids[]") Long[] ids){
        for (Long id:ids) {
            imageAllService.deleteById(id);
        }
        return ResultDtoFactory.toAck();
    }
    @RequestMapping(value = "/beatchEdit")
    public ResultDto beatchEdit(@RequestParam(value = "imgIds[]") Long[] imgIds,Long imageTypeId){
        return imageAllService.update(imgIds,imageTypeId);
    }
}
