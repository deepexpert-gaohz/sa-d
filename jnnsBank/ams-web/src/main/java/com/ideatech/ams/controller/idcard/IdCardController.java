package com.ideatech.ams.controller.idcard;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.customer.dto.CustomerTuneSearchHistoryDto;
import com.ideatech.ams.customer.enums.CustomerTuneSearchEntranceType;
import com.ideatech.ams.customer.enums.CustomerTuneSearchType;
import com.ideatech.ams.customer.service.CustomerTuneSearchHistoryService;
import com.ideatech.ams.kyc.dto.idcard.IdCardLocalDto;
import com.ideatech.ams.kyc.dto.idcard.IdCheckLogDto;
import com.ideatech.ams.kyc.dto.idcard.ResultDto;
import com.ideatech.ams.kyc.service.idcard.IdCardComperService;
import com.ideatech.ams.kyc.service.idcard.IdCardLocalService;
import com.ideatech.ams.system.pbc.dto.PbcAccountDto;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.ams.system.pbc.service.PbcAccountService;
import com.ideatech.ams.vo.IdCardRowVo;
import com.ideatech.common.constant.ResultCode;
import com.ideatech.common.excel.util.ImportExcel;
import com.ideatech.common.util.Base64Utils;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/idCard")
public class IdCardController {

    @Autowired
    private IdCardLocalService idCardLocalService;

    @Autowired
    private IdCardComperService idCardComperService;

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 联网核查
     * @param info
     * @param request
     * @return
     */
    @RequestMapping(value = "/sumit", method = RequestMethod.POST)
    public IdCheckLogDto sumit(IdCardLocalDto info, HttpServletRequest request){
        //info.setIdCardLocalImageByte(info.getString01());
        info.setString01("");
        idCardLocalService.save(info);
        IdCheckLogDto idCheckLogInfo = idCardComperService.comperIdCard(info.getIdCardNo(), info.getIdCardName());
        IdCardLocalDto info2 = idCardLocalService.queryByIdCardNoAndIdCardName(info.getIdCardNo(), info.getIdCardName());
        //ResultDto dto = getImage(request, info2.getIdCardLocalImageByte(), info2.getIdCardPbcImageByte());
        //idCheckLogInfo.setString01(dto.getUrlLocal());
        //idCheckLogInfo.setString02(dto.getUrlPbc());
        return idCheckLogInfo;
    }
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void importdata(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        com.ideatech.common.dto.ResultDto dto = new com.ideatech.common.dto.ResultDto();
        try {
            ImportExcel importExcel = new ImportExcel(file, 0, 0);
            List<IdCardRowVo> list = importExcel.getDataList(IdCardRowVo.class);
            for (IdCardRowVo vo:list) {
                idCardComperService.save(vo.getIdCardNo(),vo.getIdCardName());
            }
            dto.setCode(ResultCode.ACK);
            dto.setMessage("上传成功");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }catch (Exception e){
            log.error("上传失败", e);
            dto.setCode(ResultCode.NACK);
            dto.setMessage("上传失败");
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(JSON.toJSONString(dto));
        }
    }
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public com.ideatech.common.dto.ResultDto start(){
        com.ideatech.common.dto.ResultDto dto = new com.ideatech.common.dto.ResultDto();
        try {
            idCardComperService.start();
            dto.setCode(ResultCode.ACK);
            dto.setMessage("批量核查执行结束");
        }catch (Exception e){
            dto.setCode(ResultCode.NACK);
            dto.setMessage("批量核查执行异常");
        }
        return dto;
    }
    public ResultDto getImage(HttpServletRequest request,String local,String pbc){
        ResultDto dto = new ResultDto();
        String path = request.getSession().getServletContext().getRealPath("images");
        String filenamelocal = "local.jpg";
        String filenamePbc = "Pbc.jpg";
        String urllocal = path+File.separator+filenamelocal;
        String urlpbc = path+File.separator+filenamePbc;
        try {
            File file = new File(path);
            if (!file.exists()) {
                log.info("文件夹不存在，开始创建！");
                file.mkdirs();
            }
            if(StringUtils.isNotBlank(local)){
                File imageFile = new File(urllocal);

                if (!imageFile.exists()) {
                    imageFile.createNewFile();
                }
                Base64Utils.decoderBase64File(local,urllocal);//身份证头像数据
                dto.setUrlLocal("../../images/"+filenamelocal);
            }
            if(StringUtils.isNotBlank(pbc)){
                File imageFilepbc = new File(urlpbc);
                if(!imageFilepbc.exists()){
                    imageFilepbc.createNewFile();
                }
                Base64Utils.decoderBase64File(pbc,urlpbc);
                dto.setUrlPbc("../../images/"+filenamePbc);
            }
            return dto;
        }catch (Exception e) {
            e.printStackTrace();
            log.error("身份头像数据错误："+e.getMessage());
        }
        return dto;
    }

}
