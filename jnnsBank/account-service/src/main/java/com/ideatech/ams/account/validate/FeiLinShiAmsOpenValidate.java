package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
public class FeiLinShiAmsOpenValidate extends AbstractAllPublicAccountOpenValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO dto) {
        if (StringUtils.isBlank(dto.getAccountKey())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0080:基本户开户许可证不能为空!");
        }
        if (!dto.getAccountKey().startsWith("J") && !dto.getAccountKey().startsWith("j")) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0041:基本户开户许可证不正确,应以J开头!");
        }
        if (dto.getAccountKey().length() != 14) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0042:基本户开户许可证长度不正确，应14位!");
        }
        if (!com.ideatech.ams.pbc.utils.RegexUtils.isNumeric(dto.getAccountKey().substring(1, 14))) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "基本户开户许可证(2-14位)必须是数字!");
        }
        if (StringUtils.isBlank(dto.getBasicAcctRegArea())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0081:基本存款账户开户地地区代码不能为空!");
        } else {
            if (dto.getBasicAcctRegArea().trim().length() != 6) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0043:请录入正确的基本存款账户开户地地区代码!");
            }
        }
        if (StringUtils.isBlank(dto.getEffectiveDate())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0082:有效日期不能为空!");
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            if(StringUtils.isBlank(dto.getAcctCreateDate())){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0101:开户日期为空");
            }
            if(StringUtils.isBlank(dto.getEffectiveDate())){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0102:有效日期为空");
            }
            try {
                if (format.parse(dto.getAcctCreateDate()).getTime() > System.currentTimeMillis()) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0014:开户日期不能大于当前日期");
                }
            } catch (ParseException e) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0016:开户日期格式错误,格式应为YYYY-MM-DD");
            }
            // 工商有效日期
            if (StringUtils.isNotEmpty(dto.getEffectiveDate())) {
                try {
                    if (format.parse(dto.getEffectiveDate()).getTime() < format.parse(dto.getAcctCreateDate()).getTime()) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0015:有效日期不能早于开户日期");
                    }
                } catch (ParseException e) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0016:有效日期格式错误,格式应为YYYY-MM-DD");
                }
            }
            long createDate = format.parse(dto.getAcctCreateDate()).getTime();
            long effectiveDate = format.parse(dto.getEffectiveDate()).getTime();
//            if (format.parse(dto.getAcctCreateDate()).getTime() > System.currentTimeMillis()) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0014:开户日期不能大于当前日期");
//            }
            if (createDate > effectiveDate) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0057:有效日期不能早于开户日期!");
            }
            if (createDate == effectiveDate) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0058:有效日期和开户日期不能是同一天!");
            }
            long days = (effectiveDate - createDate) / (1000 * 60 * 60 * 24);
            if (days - 730 > 0) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0059:有效日期不能大于开户日期2年以上!");
            }
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, e.getMessage());
        }
        if (StringUtils.isBlank(dto.getAcctCreateReason())) { // 申请开户原因
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0084:请选择申请开户原因!");
        }
        if (StringUtils.isBlank(dto.getAcctFileType())) { // 证明文件种类
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0085:开户证明文件种类不能为空!");
        }
        if (StringUtils.isBlank(dto.getAcctFileNo())) { // 证明文件编号
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0086:开户证明文件种类编号不能为空!");
        }

        //非临时户证明文件1种类类型值判断
        String[] fileType = new String[] { "14", "15", "16"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctFileType())){
            if (!ArrayUtils.contains(fileType, dto.getAcctFileType())) {
                log.info("非临时户证明文件1种类类型当前值：" + dto.getAcctFileType());
                log.info("非临时户证明文件1种类类型值应为：\"14\", \"15\", \"16\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"非临时户证明文件1种类类型值不正确");
            }
        }

        //非临时户申请开户原因类型值判断
        String[] acctCreateReason = new String[] { "1", "2"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctCreateReason())){
            if (!ArrayUtils.contains(acctCreateReason, dto.getAcctCreateReason())) {
                log.info("非临时户申请开户原因类型当前值：" + dto.getAcctCreateReason());
                log.info("非临时户申请开户原因类型值应为：\"1\", \"2\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"非临时户申请开户原因类型值不正确");
            }
        }

        //法人证件类型值判断
        String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getNontmpLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getNontmpLegalIdcardType())) {
                log.info("非临时负责人证件类型当前值：" + dto.getNontmpLegalIdcardType());
                log.info("非临时负责人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"非临时负责人身份证件类型值不正确");
            }
        }
    }
}
