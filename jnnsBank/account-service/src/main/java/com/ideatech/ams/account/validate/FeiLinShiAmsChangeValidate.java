package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.IDCardUtils;
import com.ideatech.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

@Slf4j
public class FeiLinShiAmsChangeValidate extends AbstractAllPublicAccountChangeValidate{

    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws SyncException, Exception {

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

        String[] cancelHeZhunType = {"企业法人", "非法人企业", "有字号的个体工商户", "无字号的个体工商户", "01", "02", "13", "14"};
        if(!Arrays.asList(cancelHeZhunType).contains(dto.getDepositorType())) {
            if (StringUtils.isBlank(dto.getAcctCreateReason())) { // 申请开户原因(非临时核准类的必填，非临时备案类的非必填)
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0084:请选择申请开户原因!");
            }
        }
        //内设部门名称
        if(StringUtils.isNotBlank(dto.getNontmpProjectName())){
            //变更项目部名称(64个字符或32个汉字)
            if(dto.getNontmpProjectName().getBytes("GBK").length > 64){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0113：变更内设部门名称长度不能超过64个字符或32个汉字!");
            }
        }
        //变更项目部地址
        if(StringUtils.isNotBlank(dto.getNontmpAddress())){
            //变更项目部地址(128个字符或64个汉字)
            if(dto.getNontmpAddress().getBytes("GBK").length > 128){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0113：变更内设部门地址长度不能超过128个字符或64个汉字!");
            }
        }
        //项目部证件类型
        if(StringUtils.isNotBlank(dto.getNontmpLegalIdcardType())){
            //证件类型值判断
            String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getNontmpLegalIdcardType())){
                if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getNontmpLegalIdcardType())) {
                    log.info("非临时负责人证件类型当前值：" + dto.getNontmpLegalIdcardType());
                    log.info("非临时负责人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"非临时负责人身份证件类型值不正确");
                }
            }
        }

        //证件编号，变更证件类型为1的时候检验身份证的合法性
        if(StringUtils.isNotBlank(dto.getNontmpLegalIdcardNo())){
            if(StringUtils.isNotBlank(dto.getNontmpLegalIdcardType()) && StringUtils.equals("1",dto.getNontmpLegalIdcardType())){
                if(StringUtils.isNotBlank(IDCardUtils.IDCardValidate(dto.getLegalIdcardNo()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:请录入正确的身份证件编号!");
                }
            }
        }

        // 邮政编码
        if (StringUtils.isNotBlank(dto.getNontmpZipcode())) {
            if (!RegexUtils.isZipCOde(dto.getNontmpZipcode())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0026:邮政编码格式不正确");
            }
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

    }
}
