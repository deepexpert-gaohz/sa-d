package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.pbc.utils.NumberUtils;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.IDCardUtils;
import com.ideatech.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AbstractAllPublicAccountOpenValidate extends AbstractAllPublicAccountValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * 校验填写客户信息common开户校验(基本户、临时机构临时户、特殊单位专用户)
     *
     * @param dto
     * @return
     * @throws
     */
    protected void validateCustomerInfoCommonOpen(AllBillsPublicDTO dto) throws Exception {
        if (StringUtils.isEmpty(dto.getFileType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0001:证明文件种类1不能为空!");
        }
        if (StringUtils.isEmpty(dto.getFileNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0002:证明文件编号1不能为空!");
        }
        if ((StringUtils.isNotEmpty(dto.getFileType2()) || StringUtils.isNotEmpty(dto.getFileNo2()))
                && (StringUtils.isBlank(dto.getFileType2()) || StringUtils.isBlank(dto.getFileNo2()))) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0003:证明文件2证据类型与证件编号必须同时填写");
        }
        // 存款人名称
        if (StringUtils.isBlank(dto.getDepositorName())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0004:存款人名称不能为空");
        } else if (dto.getDepositorName().getBytes("GBK").length > 128) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0005:存款人名称长度过长");
        }
        // 法人类型
        if (StringUtils.isBlank(dto.getLegalType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-006:法人类型不能为空!");
        }
        // 法人姓名
        if (StringUtils.isBlank(dto.getLegalName())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-007:法人姓名不能为空!");
        }
        // 法人证件类型、编号
        if (StringUtils.isBlank(dto.getLegalIdcardNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-008:法人身份编号不能为空");
        } else if (dto.getLegalIdcardNo().getBytes().length > 18) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-009:法人身份编号长度不能大于18位");
        }
        if (StringUtils.isBlank(dto.getLegalIdcardType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0010:法人证件类型不能为空!");
        } else if (dto.getLegalIdcardType().equals("1")
                && StringUtils.isNotEmpty(IDCardUtils.IDCardValidate(dto.getLegalIdcardNo()))) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:请录入正确的法定代表人或负责人身份证件编号!");
        }
        // 国地税
        if (StringUtils.isNotEmpty(dto.getNoTaxProve()) && dto.getNoTaxProve().getBytes("GBK").length > 32) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0012:无需办理税务登记证的文件或税务机关出具的证明不能超过32个字符或16个汉字,请重新录入!");
        }
        // 当存款人类别为企业法人、非企业法人、个体工商户时 国地税获与无需办理税务登记证明必须有个必填
        if (StringUtils.isBlank(dto.getNoTaxProve())) {
            String[] arrays = new String[]{"01","02","13","14"};
//            String[] arrays = {"01:02:13:14"};
            if (ArrayUtils.contains(arrays, dto.getDepositorType())) {
                if (StringUtils.isBlank(dto.getStateTaxRegNo()) && StringUtils.isBlank(dto.getTaxRegNo())) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0013:请录入国税登记证号或地税登记证号,或填写无需办理税务登记证的文件或税务机关出具的证明!");
                }
            }
        }
        // 开户时间
        if (StringUtils.isBlank(dto.getAcctCreateDate())) {// 默认当前时间
            dto.setAcctCreateDate(DateUtils.getNowDateShort());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        //校验法人证件到期日
        if(StringUtils.isNotBlank(dto.getLegalIdcardDue())){
            try{
                Date date = format.parse(dto.getLegalIdcardDue());
            }catch (Exception e){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0016:法人证件有效日期格式错误,格式应为YYYY-MM-DD");
            }
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
        // 注册地地区代码
//    valiDateRegAreaCode(dto.getBasicAcctRegArea());
        try {
            valiDateRegAreaCode(dto.getRegAreaCode());
        } catch (SyncException e) {
            log.error("注册地地区代码校验", e);
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "注册地地区代码校验失败：" + e.getMessage());
        }
        // 行业归属
        if (StringUtils.isBlank(dto.getIndustryCode())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0017:行业归属不能为空");
        }
        // 工商注册地址（上报人行）
        if (dto.getAcctType() != null && (dto.getAcctType() == CompanyAcctType.jiben || dto.getAcctType() == CompanyAcctType.teshu || dto.getAcctType() == CompanyAcctType.linshi) && StringUtils.isBlank(dto.getRegFullAddress())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0018:工商注册地址不能为空");
        }

        // 注册资金
        if (StringUtils.isBlank(dto.getIsIdentification())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0106:未标明注册资金不能为空!");
        } else {
            //未选择未标明时需要校验必填
            if (!"1".equals(dto.getIsIdentification())) {
                if(dto.getRegisteredCapital() == null){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0107:注册资金不能为空");
                }
                if(dto.getRegisteredCapital() != null && !NumberUtils.isDigit(dto.getRegisteredCapital())){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0109:请录入正确的注册资金金额");
                }
                if (StringUtils.isBlank(String.valueOf(dto.getRegisteredCapital()))) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0107:注册资金不能为空");
                }
                if (StringUtils.isBlank(dto.getRegCurrencyType())) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0108:注册资金币种不能为空");
                }
            }
            //有值必须校验格式
            if (dto.getRegisteredCapital() != null && StringUtils.isNotBlank(String.valueOf(dto.getRegisteredCapital()))) {
                if (!com.ideatech.ams.pbc.utils.RegexUtils.isDecimal(String.valueOf(dto.getRegisteredCapital()))) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0109:请录入正确的注册资金金额");
                }
                if (StringUtils.isBlank(dto.getRegCurrencyType())) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0110:注册资金有值时，注册资金币种不能为空");
                }
            }

            if (StringUtils.isNotBlank(dto.getRegCurrencyType())) {
                if (StringUtils.isBlank(String.valueOf(dto.getRegisteredCapital()))) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0111:注册资金币种有值时，注册资金不能为空");
                } else {
                    if (!com.ideatech.ams.pbc.utils.RegexUtils.isDecimal(String.valueOf(dto.getRegisteredCapital()))) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0112请录入正确的注册资金金额");
                    }
                }
            }
        }
//
//        if (StringUtils.isBlank(dto.getIsIdentification())) {
//            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0106:未标明注册资金不能为空!");
//        }
//        // 注册资金
//        if (StringUtils.isBlank(dto.getIsIdentification())) {
//            if (StringUtils.isBlank(String.valueOf(dto.getRegisteredCapital()))
//                    && (!dto.getIsIdentification().equals("1"))) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0019:请录入注册资金!");
//            } else if (!RegexUtils.isDecimal(String.valueOf(dto.getRegisteredCapital()))) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0020:请录入正确的注册资金金额!");
//            }
//            if (StringUtils.isBlank(dto.getRegCurrencyType())) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0021:请选择注册资金币种或未标明注册资金!");
//            }
//        }
        // 经营范围
        if (StringUtils.isBlank(dto.getBusinessScope()) && dto.getFileType().equals("01")) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0022:经营范围不能为空");
        } else {
            if (StringUtils.isNotBlank(dto.getBusinessScope())) {
                if (dto.getBusinessScope().length() > 489) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0023:经营范围超长(978个字符或489个汉字),请重新录入！");
                }
            }
            if (StringUtils.isNotBlank(dto.getBusinessScopeEccs())) {
                if (dto.getBusinessScopeEccs().length() > 200) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0024:上报信用机构经营（业务）范围字段超长！");
                }
            }
        }
        // 联系电话
        if (StringUtils.isBlank(dto.getTelephone())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0025:联系电话不能为空");
        }
        // 邮政编码
        if (StringUtils.isBlank(dto.getZipcode())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0026:邮政编码不能为空");
        } else if (!RegexUtils.isZipCOde(dto.getZipcode())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0026:邮政编码格式不正确");
        }
        // 组织机构代码
        if (StringUtils.isNotEmpty(dto.getOrgCode())) {
            if (dto.getOrgCode().getBytes().length != 9) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0027:组织机构代码长度不正确，应为9位");
            }
        }
        /* 上级信息 */
        if (StringUtils.isNotEmpty(dto.getParLegalName())
                && dto.getParLegalName().getBytes().length > 32) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0028:上级法人及主管单位的法定代表人或单位负责人姓名不能超过32个字符或16个汉字,请重新录入");
        }
        if (StringUtils.isNotEmpty(dto.getParLegalName())
                || StringUtils.isNotEmpty(dto.getParCorpName())) {
            if (StringUtils.isBlank(dto.getParLegalType())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0029:您录入了上级法人及主管单位的法定代表人或单位负责人姓名,请再选择法定代表人或单位负责人及相关信息");
            }
        }
        if (StringUtils.isNotEmpty(dto.getParLegalIdcardNo())
                && dto.getParLegalIdcardNo().getBytes().length > 18) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0038:上级法人证件编号长度不能大于18位");
        }
        if (StringUtils.isNotEmpty(dto.getParLegalName())
                || (StringUtils.isNotEmpty(dto.getParLegalIdcardType()) && (!dto.getParLegalIdcardType()
                .equals(" , "))) || StringUtils.isNotEmpty(dto.getParLegalIdcardNo())) {
            if (StringUtils.isBlank(dto.getParLegalName())
                    || StringUtils.isBlank(dto.getParLegalIdcardType())
                    || StringUtils.isBlank(dto.getParLegalIdcardNo())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,
                        "AMSERROR-0030:上级法人及主管单位的法定代表人或负责人姓名、身份证明文件种类及其编号三个数据项,必须全部为空、或全部不为空!");
            }
        }
        if(StringUtils.isNotEmpty(dto.getParAccountKey())){
            if(!dto.getParAccountKey().substring(0, 1).equalsIgnoreCase("J")){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0031:请录入正确的上级法人或主管单位信息基本存款账户开户许可证核准号!");
            }
            if(dto.getParAccountKey().getBytes().length != 14){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0031:请录入正确的上级法人或主管单位信息基本存款账户开户许可证核准号!");
            }
        }
//        if (StringUtils.isNotEmpty(dto.getParAccountKey())
//                && (!dto.getParAccountKey().substring(0, 1).equalsIgnoreCase("J"))
//                && dto.getParAccountKey().getBytes().length != 14) {
//            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0031:请录入正确的上级法人或主管单位信息基本存款账户开户许可证核准号!");
//        }
        if (StringUtils.isNotEmpty(dto.getParAccountKey())) {
            if (StringUtils.isBlank(dto.getParLegalName())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0032:上级开户许可证不为空时上级法定代表人或单位负责人姓名不能为空!");
            }
            if (StringUtils.isBlank(dto.getParCorpName())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0033:上级开户许可证不为空时上级主管单位名称不能为空!");
            }
        }
        if (StringUtils.isNotEmpty(dto.getParOrgCode())) {
            if (StringUtils.isBlank(dto.getParLegalName())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0035:上级组织机构代码不为空时上级法定代表人或单位负责人姓名不能为空!");
            }
            if (StringUtils.isBlank(dto.getParCorpName())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0036:上级组织机构代码不为空时上级主管单位名称不能为空!");
            }
            if (StringUtils.isBlank(dto.getParAccountKey())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0037:上级组织机构代码不为空请录入上级主管单位基本存款账户开户许可证核准号!");
            }
        }
    }

    /**
     * @param regAreaCode 注册地地区代码
     * @return
     * @throws SyncException 校验注册地地区代码
     * @throws
     */
    protected void valiDateRegAreaCode(String regAreaCode) throws SyncException {
        if (StringUtils.isEmpty(regAreaCode)) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0039:注册地地区代码不能为空");
        } else if (regAreaCode.getBytes().length != 6) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0040:注册地地区代码长度不正确，应6位");
        } else if (!RegexUtils.isNumberOrLetter(regAreaCode)) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-041:注册地地区代码应为6位数字");
        }
    }
}
