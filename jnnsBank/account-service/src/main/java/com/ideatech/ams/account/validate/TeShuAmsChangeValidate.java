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

@Slf4j
public class TeShuAmsChangeValidate extends AbstractAllPublicAccountChangeValidate{

    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws SyncException, Exception {
        //变更存款人名称
        if(StringUtils.isNotBlank(dto.getDepositorName())){
            if(dto.getDepositorName().getBytes("GBK").length > 128){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0114:存款人名称长度不能超过128字符或64汉字!");
            }
        }

        //变更法人姓名
        if(StringUtils.isNotBlank(dto.getLegalName())){
            if(dto.getLegalName().getBytes("GBK").length > 32){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0114:法人及主管单位的法定代表人或单位负责人姓名不能超过32个字符或16个汉字,请重新录入!");
            }
        }

        //变更法人姓名
        if(StringUtils.isNotBlank(dto.getParLegalName())){
            if(dto.getParLegalName().getBytes("GBK").length > 32){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0114:上级法人及主管单位的法定代表人或单位负责人姓名不能超过32个字符或16个汉字,请重新录入!");
            }
        }

        //邮政编码
        if(StringUtils.isNotBlank(dto.getZipcode())){
            if (!RegexUtils.isZipCOde(dto.getZipcode())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0026:邮政编码格式不正确");
            }
        }

        //无需办理税务登记证的文件或税务机关出具的证明校验
        if(StringUtils.isNotBlank(dto.getNoTaxProve())){
            if(dto.getNoTaxProve().getBytes("GBK").length > 22){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0115:无需办理税务登记证的文件或税务机关出具的证明长度不能超过22个字符或11个汉字！");
            }
        }

        //组织机构代码
        if(StringUtils.isNotBlank(dto.getOrgCode())){
            if(dto.getOrgCode().length() != 9){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0027:组织机构代码长度不正确，应为9位");
            }
        }

        //经营范围
        if(StringUtils.isNotBlank(dto.getBusinessScope())){
            if(dto.getBusinessScope().getBytes("GBk").length > 978){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0023:经营范围超长(978个字符或489个汉字),请重新录入！");
            }
        }

        //国税
        if(StringUtils.isNotBlank(dto.getStateTaxRegNo())){
            if(dto.getStateTaxRegNo().getBytes("GBK").length > 60){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0116:国税登记证号超长(60个字符或30个汉字),请重新录入!");
            }
        }

        //地税
        if(StringUtils.isNotBlank(dto.getTaxRegNo())){
            if(dto.getTaxRegNo().getBytes("GBK").length > 60){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0116:地税登记证号超长(60个字符或30个汉字),请重新录入!");
            }
        }

        //上级机构名称
        if(StringUtils.isNotBlank(dto.getParCorpName())){
            if(dto.getParCorpName().getBytes("GBK").length > 128){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0114:上级机构名称长度不能超过128字符或64汉字!");
            }
        }

        //证件编号合法性校验
        if(StringUtils.isNotBlank(dto.getLegalIdcardNo())){
            if(StringUtils.isNotBlank(dto.getLegalIdcardType()) && StringUtils.equals("1",dto.getLegalIdcardType())){
                //身份证进行校验
                if(StringUtils.isNotBlank(IDCardUtils.IDCardValidate(dto.getLegalIdcardNo()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:请录入正确的法定代表人或负责人身份证件编号!");
                }
            }
        }

        //上级法人证件编号
        if(StringUtils.isNotBlank(dto.getParLegalIdcardNo())){
            if(StringUtils.isNotBlank(dto.getParLegalIdcardType()) && StringUtils.equals("1",dto.getParLegalIdcardType())){
                //身份证进行校验
                if(StringUtils.isNotBlank(IDCardUtils.IDCardValidate(dto.getParLegalIdcardNo()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:请录入正确上级身份证件编号!");
                }
            }
        }

        //上级开户许可证
        if(StringUtils.isNotBlank(dto.getParAccountKey())){
            if (!dto.getParAccountKey().startsWith("J") && !dto.getParAccountKey().startsWith("j")) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0041:上级基本户开户许可证不正确,应以J开头!");
            }
            if (dto.getParAccountKey().length() != 14) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0042:上级基本户开户许可证长度不正确，应14位!");
            }
            if (!RegexUtils.isNumeric(dto.getParAccountKey().substring(1, 14))) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "上级基本户开户许可证(2-14位)必须是数字!");
            }
        }

        //上级组织机构代码
        if(StringUtils.isNotBlank(dto.getParOrgCode())){
            if(dto.getParOrgCode().length() != 9){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0027:上级组织机构代码长度不正确，应为9位");
            }
        }


        //法人类型值判断
        String[] legalType = new String[] { "1", "2"};
        if(StringUtils.isNotBlank(dto.getLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getLegalType())) {
                log.info("特殊户法人类型值当前值：" + dto.getLegalType());
                log.info("特殊户法人类型值应为：\"1\", \"2\"");
                throw new SyncException("特殊户法人类型值不正确");
            }
        }

        //上级法人类型值判断
        if(StringUtils.isNotBlank(dto.getParLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getParLegalType())) {
                log.info("特殊户上级法人类型值当前值：" + dto.getParLegalType());
                log.info("特殊户上级法人类型值不正确：\"1\", \"2\"");
                throw new SyncException("特殊户上级法人类型值不正确");
            }
        }
        //法人证件类型值判断
        String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(StringUtils.isNotBlank(dto.getLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getLegalIdcardType())) {
                log.info("特殊户法人证件类型当前值：" + dto.getLegalIdcardType());
                log.info("特殊户法人证件类型值应为确：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("特殊户法人证件类型值不正确");
            }
        }



        //上级法人证件类型值判断
        if(StringUtils.isNotBlank(dto.getParLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getParLegalIdcardType())) {
                log.info("特殊户上级法人证件类型值当前值：" + dto.getParLegalIdcardType());
                log.info("特殊户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("特殊户上级法人证件类型值不正确");
            }
        }

        //特殊户证明文件1种类类型值判断
        String[] fileType = new String[] { "12", "17"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getFileType())){
            if (!ArrayUtils.contains(fileType, dto.getFileType())) {
                log.info("特殊户证明文件1种类类型值当前值：" + dto.getFileType());
                log.info("特殊户证明文件1种类类型值应为：\"12\", \"17\"");
                throw new SyncException("特殊户证明文件1种类类型值不正确");
            }
        }


        //特殊户证明文件2种类类型值判断
        String[] fileType2 = new String[] {"13", "17"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getFileType2())){
            if (!ArrayUtils.contains(fileType2, dto.getFileType2())) {
                log.info("特殊户证明文件2种类类型值当前值：" + dto.getFileType2());
                log.info("特殊户证明文件2种类类型值应为：\"13\", \"17\"");
                throw new SyncException("特殊户证明文件2种类类型值不正确");
            }
        }
    }
}
