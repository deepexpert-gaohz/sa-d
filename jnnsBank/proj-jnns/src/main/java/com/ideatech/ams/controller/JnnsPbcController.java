package com.ideatech.ams.controller;

import com.ideatech.ams.account.service.pbc.PbcAmsService;
import com.ideatech.ams.dto.JnnsPbc;
import com.ideatech.ams.dto.JnnsPbcResponse;
import com.ideatech.ams.dto.gmsp.GMSP;
import com.ideatech.ams.pbc.dto.AmsCheckResultInfo;
import com.ideatech.ams.system.dict.service.DictionaryService;
import com.ideatech.ams.ws.enums.ResultCode;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/jnnsCheckPbc")
@RestController
@Slf4j
public class JnnsPbcController {

    @Autowired
    private PbcAmsService  pbcAmsService;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 查询人行接口
     */
    @PostMapping("/pbc")
    public ResultDto getCorrect(@RequestBody JnnsPbc billsPublics, HttpServletResponse response) throws Exception {
        ResultDto resultDto=new ResultDto();
          if (StringUtils.isEmpty(billsPublics.getAccountKey())){
              log.info("基本户许可证号不可为空");
              return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "基本户许可证号不可为空");
          }
         if (StringUtils.isEmpty(billsPublics.getRegAreaCode())){
            log.info("注册地区代码不可为空");
             return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "注册地区代码不可为空");
          }
         if (StringUtils.isEmpty(billsPublics.getOrganCode())){
            log.info("行内机构号不可为空");
            return ResultDtoFactory.toApiError(ResultCode.PARAM_IS_BLANK.code(), "行内机构号不可为空");

          }
       try{
           AmsCheckResultInfo amsCheckResultInfo = pbcAmsService.checkPbcByAccountKeyAndRegAreaCode(billsPublics.getOrganCode(), billsPublics.getAccountKey(), billsPublics.getRegAreaCode());
        JnnsPbcResponse   jnnsPbcResponse=new JnnsPbcResponse();
        if (StringUtils.isNotEmpty(amsCheckResultInfo.getAmsAccountInfo().getDepositorName())){
               jnnsPbcResponse.setDepositorName(amsCheckResultInfo.getAmsAccountInfo().getDepositorName());
               jnnsPbcResponse.setFileNo(amsCheckResultInfo.getAmsAccountInfo().getFileNo());
               jnnsPbcResponse.setFileType(amsCheckResultInfo.getAmsAccountInfo().getFileType());
               jnnsPbcResponse.setLegalIdcardNo(amsCheckResultInfo.getAmsAccountInfo().getLegalIdcardNo());
               jnnsPbcResponse.setLegalIdcardType(amsCheckResultInfo.getAmsAccountInfo().getLegalIdcardType());
               jnnsPbcResponse.setLegalName(amsCheckResultInfo.getAmsAccountInfo().getLegalName());
            /**
             * 负责人身份证件种类
             */
            if (StringUtils.isNotEmpty(amsCheckResultInfo.getAmsAccountInfo().getLegalIdcardType())) {
                String insideLeadIdcardType = dictionaryService.transalte("core2dataLegalIdCardType", amsCheckResultInfo.getAmsAccountInfo().getLegalIdcardType());
                if (org.apache.commons.lang.StringUtils.isNotBlank(insideLeadIdcardType)) {
                    jnnsPbcResponse.setLegalIdcardType(insideLeadIdcardType);
                }
            }

            /**
             *法人身份
             */
            if (StringUtils.isNotEmpty(amsCheckResultInfo.getAmsAccountInfo().getLegalType())) {
                String insideLeagalype = dictionaryService.transalte("core2pbc-legalType", amsCheckResultInfo.getAmsAccountInfo().getLegalType());
                if (org.apache.commons.lang.StringUtils.isNotBlank(insideLeagalype)) {
                    jnnsPbcResponse.setLegalType(insideLeagalype);
                }
            }

            resultDto.setCode("1");
               resultDto.setMessage("查询人行成功");
               resultDto.setData(jnnsPbcResponse);
           }else {

               resultDto.setCode("2");
               resultDto.setMessage("查询人行失败");
               resultDto.setData(jnnsPbcResponse);
           }
    }catch (Exception e) {
           log.error("查询人行连接失败", e);
           resultDto.setCode("111");
           resultDto.setMessage(e.getMessage());
           resultDto.setData(null);
       }
        return resultDto;
    }
}
