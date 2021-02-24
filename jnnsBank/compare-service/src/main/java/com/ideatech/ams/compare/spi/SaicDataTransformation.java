package com.ideatech.ams.compare.spi;

import com.ideatech.ams.compare.dto.data.CompareDataDto;
import com.ideatech.ams.compare.service.DataTransformation;
import com.ideatech.ams.kyc.dto.SaicInfoDto;
import com.ideatech.common.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component("saicDataTransformation")
public class SaicDataTransformation implements DataTransformation<SaicInfoDto> {

    @Override
    public void dataTransformation(CompareDataDto compareDataDto, SaicInfoDto saicInfoDto) {
        compareDataDto.setBusinessScope(saicInfoDto.getScope());
        compareDataDto.setCancelDate(saicInfoDto.getEnddate());
        compareDataDto.setRegNo(StringUtils.isNotBlank(saicInfoDto.getUnitycreditcode()) ? saicInfoDto.getUnitycreditcode() : saicInfoDto.getRegistno());
        compareDataDto.setFileNo(StringUtils.isNotBlank(saicInfoDto.getUnitycreditcode()) ? saicInfoDto.getUnitycreditcode() : saicInfoDto.getRegistno());
        compareDataDto.setLegalName(saicInfoDto.getLegalperson());
        compareDataDto.setRegAddress(saicInfoDto.getAddress());
        compareDataDto.setRegisteredCapital(saicInfoDto.getRegistfund());
        compareDataDto.setDepositorName(saicInfoDto.getName());

        try {
            //转换注册资金
            if (saicInfoDto.getRegistfund()!=null && saicInfoDto.getRegistfund().contains("万元人民币")){
                String registfund = saicInfoDto.getRegistfund().replace("万元人民币","");
//                Integer num = (int)(Double.parseDouble(registfund)*10000);
                BigDecimal num = new BigDecimal(registfund);//工商注册资金
                BigDecimal wan = new BigDecimal("10000");//10000
                BigDecimal num1 = num.multiply(wan);//num * 10000
                compareDataDto.setRegisteredCapital(num1.stripTrailingZeros().toPlainString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (saicInfoDto.getEnddate()!=null && saicInfoDto.getEnddate().length()!=0){
                compareDataDto.setEnddate(DateUtils.strToStrAsFormat(saicInfoDto.getEnddate(),"yyyy-MM-dd"));//营业执照到期日 客户异动比对使用
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            if (saicInfoDto.getOpendate()!=null && saicInfoDto.getOpendate().length()!=0){
                compareDataDto.setOpendate(DateUtils.strToStrAsFormat(saicInfoDto.getOpendate(),"yyyy-MM-dd"));//成立日期 客户异动比对使用
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
