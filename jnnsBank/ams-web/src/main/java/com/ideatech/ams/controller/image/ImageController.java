package com.ideatech.ams.controller.image;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.image.dto.ImageInfo;
import com.ideatech.ams.image.enums.IsUpload;
import com.ideatech.ams.image.service.ImageService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


/**
 * 影像控制器
 */
@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;
    /**
     *高拍仪上传
     * @param refBillid
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadImage/{refBillid}",method = RequestMethod.POST)
    public ResultDto<ImageInfo> uploadByHardWare(@PathVariable("refBillid") Long refBillid, HttpServletRequest request){
        ImageInfo info =null;
        String base64 = request.getParameter("base64");
        try {
            //AccountBillsAllInfo bills = accountBillsAllService.getOne(refBillid);
            info = imageService.createImage(base64);
            info.setDocCode("0");
            info.setRefBillId(refBillid);
            info.setIsUpload(IsUpload.FALSE);
            //info.setAcctId(bills.getAccountId());
            info = imageService.save(info);
            info.setImgPath("/image/view/"+info.getId());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResultDtoFactory.toAckData(info);
    }
    /**
     * 本地导入影像
     * @param request
     * @param refBillid
     * @return
     */
    @RequestMapping(value = "/upload/{refBillid}",method = RequestMethod.POST)
    public void importImage(HttpServletRequest request, @RequestParam("file") MultipartFile[] file, @PathVariable("refBillid") Long refBillid,HttpServletResponse response) {
        List<ImageInfo> list = new ArrayList<>();
        try {
            ImageInfo info = null;
            //AccountBillsAllInfo bills = accountBillsAllService.getOne(refBillid);
            //info = imageService.uploadImage(request);
            for (MultipartFile multipartFile : file) {
                info = imageService.uploadImage(multipartFile.getInputStream());
                info.setDocCode("0");
                info.setRefBillId(refBillid);
                info.setIsUpload(IsUpload.FALSE);
                //info.setAcctId(bills.getAccountId());
                info = imageService.save(info);
                info.setImgPath("/image/view/"+info.getId());
                list.add(info);
            }
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(info));
        }catch (Exception e){
            log.error(e.getMessage());
        }

        //return ResultDtoFactory.toAckData(list);
    }
    /**
     * 影像查看
     * @param response
     * @param imageid
     */
    @RequestMapping(value = "/view/{imageid}",method = RequestMethod.GET)
    public void view(HttpServletResponse response,@PathVariable("imageid") Long imageid){
        ImageInfo info =imageService.findImageById(imageid);
        try {
            OutputStream os = response.getOutputStream();
            File file = new File(info.getImgPath());
            response.setContentType("image/jpeg");
            os.write(org.apache.commons.io.FileUtils.readFileToByteArray(file));
            os.flush();
        }catch (Exception e){
            log.error("影像读取异常："+e.getMessage());
        }
    }
    /**
     * 设置影像类型(保存影像信息也可用该接口)
     * @param info
     * @param imageid
     * @return
     */
    @RequestMapping(value = "/setImageType/{imageid}",method = RequestMethod.POST)
    public ResultDto setImageType(ImageInfo info,@PathVariable("imageid") Long imageid){
        info.setId(imageid);
        imageService.setType(info);
        return ResultDtoFactory.toAck();
    }
    /**
     * 删除影像
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResultDto deleteImage(@PathVariable("id")Long id){
        imageService.delete(id);
        return ResultDtoFactory.toAck();
    }
    /**
     * 查询影像
     * refBillId(流水id) 必填
     * docCode(影像种类code) 选填
     * @param info
     * @return
     */
    @RequestMapping(value = "/query",method = RequestMethod.GET)
    public ResultDto  query(ImageInfo info){
        try {
            List<ImageInfo> list = imageService.query(info);
            return ResultDtoFactory.toAckData(list);
        }catch (Exception e){
            ResultDto dto = new ResultDto();
            dto.setMessage(e.getMessage());
            return dto;
        }

    }
    /**
     * 上传到影像平台
     * refBillId(流水id) 必填
     *
     */
    @RequestMapping(value = "/uploadToImage/{billId}",method = RequestMethod.GET)
    public ResultDto  uploadToImage(@PathVariable("billId")Long id){
        ImageInfo info = new ImageInfo();
        info.setRefBillId(id);
        int res = imageService.uploadToImage(info);
        return ResultDtoFactory.toAckData(res);
    }
    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public ResultDto getById(@PathVariable("id")Long id){
            ImageInfo info = imageService.findImageById(id);
        return ResultDtoFactory.toAckData(info);
    }
    @RequestMapping(value = "/downLoadImage/{id}",method = RequestMethod.GET)
    public void downLoadImage(@PathVariable("id")Long id,HttpServletResponse response){
        ImageInfo info = imageService.findImageById(id);
        if (info.getIsUpload()==IsUpload.TRUE){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"已上传影像平台不可下载");
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(info.getImgPath());
            outputStream = response.getOutputStream();
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + info.getFileName());
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    @RequestMapping(value = "/downLoadBatchImage/{refBillId}",method = RequestMethod.GET)
    public void downLoadBatchImage(HttpServletResponse response,@PathVariable("refBillId")Long refBillId){
        imageService.downLoadBatchImage(response,refBillId);
    }

}
