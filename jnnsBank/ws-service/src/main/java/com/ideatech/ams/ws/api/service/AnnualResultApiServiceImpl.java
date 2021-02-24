package com.ideatech.ams.ws.api.service;

import com.ideatech.ams.annual.dto.AnnualResultDto;
import com.ideatech.ams.annual.enums.ResultStatusEnum;
import com.ideatech.ams.annual.service.AnnualResultService;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 年检结果对外接口
 * @Author wanghongjie
 * @Date 2018/11/20
 **/
@Service
@Transactional
@Slf4j
public class AnnualResultApiServiceImpl implements  AnnualResultApiService{
    @Autowired
    private AnnualResultService annualResultService;
    @Autowired
    private ConfigService configService;
    /**
     * 年检比对上报完成之后调用核心系统
     * @param acctNo
     */
    @Override
    public void submitPbcFinished(String acctNo) {
    }

    @Override
    public AnnualResultDto annualResultSearch(String acctNo) {
        return annualResultService.annualResultSearch(acctNo);
    }

    @Override
    public ResultDto updateAnnualResult(String acctNo) {
        ResultDto resultDto = new ResultDto();
        AnnualResultDto dto = annualResultService.annualResultSearch(acctNo);
        if(dto!=null){
            if(dto.getResult()==ResultStatusEnum.PASS){
                resultDto.setCode("2");
                resultDto.setMessage("年检已通过，无需再次年检");
            }else{
                try {
                    List<ConfigDto> cdList = configService.findByKey("annualAgainEnabled");
                    String config = "false";
                    if (cdList.size() > 0) {
                        config = cdList.get(0).getConfigValue();
                    }
                    if ("true".equals(config)) {
                        annualResultService.annualAgain(dto.getId());
                    }else {
                        ResultDto<AnnualResultDto> annualResultDtoResultDto = annualResultService.submitAnnualAccount(dto.getId());
                        if (annualResultDtoResultDto.getData() == null) {
                            return ResultDtoFactory.toApiError("2", annualResultDtoResultDto.getMessage());
                        } else {
                            if (annualResultDtoResultDto.getData().getResult() == ResultStatusEnum.PASS) {
                                return ResultDtoFactory.toApiSuccess(annualResultDtoResultDto.getData().getResult());
                            } else {
                                return ResultDtoFactory.toApiError("2", annualResultDtoResultDto.getData().getPbcSubmitErrorMsg());
                            }
                        }
                    }
                    return ResultDtoFactory.toApiSuccess(annualResultService.findById(dto.getId()));
                }catch (Exception e){
                    log.error("维护年检结果异常");
                    resultDto.setCode("2");
                    resultDto.setMessage("维护年检结果异常");
                    return resultDto;
                }
            }

        }else{
            resultDto.setCode("2");
            resultDto.setMessage("没有找到需要维护的账号");
        }
        return resultDto;
    }
}
