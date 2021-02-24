package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
public class LinShiAmsOpenValidate extends AbstractAllPublicAccountOpenValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws Exception {
        super.validateCustomerInfoCommonOpen(dto);
        if (StringUtils.isBlank(dto.getEffectiveDate())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0064:有效日期不能为空!");
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(dto.getAcctCreateDate())){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0101:开户日期为空");
            }
            if(StringUtils.isBlank(dto.getEffectiveDate())){
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0102:有效日期为空");
            }
            long createDate = format.parse(dto.getAcctCreateDate()).getTime();
            long effectiveDate = format.parse(dto.getEffectiveDate()).getTime();
            if (format.parse(dto.getAcctCreateDate()).getTime() > System.currentTimeMillis()) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0014:开户日期不能大于当前日期");
            }
            if (createDate > effectiveDate) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0066:有效日期不能早于开户日期!");
            }
            if (createDate == effectiveDate) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0067:有效日期和开户日期不能是同一天!");
            }
            long days = (effectiveDate - createDate) / (1000 * 60 * 60 * 24);
            if (days - 730 > 0) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0068:有效日期不能大于开户日期2年以上!");
            }
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, e.getMessage());
        }
//			if (StringUtils.isBlank(dto.getRegAddress())) {
//				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0060:注册地址不能为空");
//			} else {
//				if (dto.getRegAddress().getBytes().length > 128) {
//					throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0061:地址超长(128个字符或64个汉字),请重新录入!");
//				}
//			}
			/*if (!dto.getFileType().equals("09")) {
				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0062:证明文件种类应该为主管部门批文!");
			}*/
        if (dto.getFileNo().getBytes().length > 128) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0063:证明文件种类1编号超长(32个字符或16个汉字),请重新录入!");
        }
        // 法定代表人或负责人身份证件种类、编号
        if (StringUtils.isBlank(dto.getLegalType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0064:法定代表人（单位负责人）不能为空!");
        }
        if (StringUtils.isNotEmpty(dto.getTaxRegNo()) && StringUtils.isNotEmpty(dto.getStateTaxRegNo())
                && StringUtils.isNotEmpty(dto.getNoTaxProve())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0065:无需办理税务登记证的文件或税务机关出具的证明与地税登记证号、国税登记证号不能同时存在!");
        }
        if (StringUtils.isNotEmpty(dto.getTaxRegNo()) && dto.getTaxRegNo().getBytes().length > 60) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0066:地税登记证号不能超过60个字符或30个汉字,请重新录入!");
        }
        // 邮政编码
        if (StringUtils.isNotBlank(dto.getZipcode()) && !RegexUtils.isZipCOde(dto.getZipcode())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0067:请录入正确的邮政编码!");
        }
        // 电话号码
        if (StringUtils.isNotBlank(dto.getTelephone()) && !RegexUtils.isPhoneNumber(dto.getTelephone())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0068:请录入正确的电话号码!");
        }


        //法人类型值判断
        String[] legalType = new String[] { "1", "2"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getLegalType())) {
                log.info("临时户法人类型当前值：" + dto.getLegalType());
                log.info("临时户法人类型值应为：\"1\", \"2\"");
                throw new SyncException("临时户法人类型值不正确");
            }
        }

        //上级法人类型值判断
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getParLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getParLegalType())) {
                log.info("临时户上级法人类型当前值：" + dto.getLegalType());
                log.info("临时户上级法人类型值应为：\"1\", \"2\"");
                throw new SyncException("临时户上级法人类型值不正确");
            }
        }
        //法人证件类型值判断
        String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getLegalIdcardType())) {
                log.info("临时户法人证件类型当前值：" + dto.getLegalIdcardType());
                log.info("临时户法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("临时户法人证件类型值不正确");
            }
        }

        //上级法人证件类型值判断
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getParLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getParLegalIdcardType())) {
                log.info("临时户上级法人证件类型当前值：" + dto.getParLegalIdcardType());
                log.info("临时户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("临时户上级法人证件类型值不正确");
            }
        }

        //证明文件种类1类型值判断
        String[] fileType = new String[] { "01", "09"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getFileType())){
            if (!ArrayUtils.contains(fileType, dto.getFileType())) {
                log.info("临时户证明文件种类1证件类型当前值：" + dto.getParLegalIdcardType());
                log.info("临时户证明文件种类1证件类型值应为：\"01\", \"09\"");
                throw new SyncException("临时户证明文件种类1证件类型值不正确");
            }
        }

        //临时户币种类型值判断
        String[] regCurrencyTypeAms = new String[] { "AUD","CAD","CNY","EUR","GBP","HKD","JPY","KRW","SGD","USD","XEU"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getRegCurrencyType())){
            if (!ArrayUtils.contains(regCurrencyTypeAms, dto.getRegCurrencyType())) {
                log.info("临时户币种类型当前值：" + dto.getRegCurrencyType());
                log.info("临时户币种类型值应为：\"AUD\",\"CAD\",\"CNY\",\"EUR\",\"GBP\",\"HKD\",\"JPY\",\"KRW\",\"SGD\",\"USD\",\"XEU\"");
                throw new SyncException("临时户币种类型值不正确");
            }
        }

        //基本户证明文件2种类类型值判断
        String[] isIdentifications = new String[] {"0", "1"};
        if(StringUtils.isNotBlank(dto.getIsIdentification())){
            if (!ArrayUtils.contains(isIdentifications, dto.getIsIdentification())) {
                log.info("临时户未标明注册资金当前值：" + dto.getIsIdentification());
                log.info("临时户未标明注册资金值应为：\"0\", \"1\"");
                throw new SyncException("临时户未标明注册资金值不正确");
            }
        }

        //临时户行业归属类型值判断
        String[] industryCode = new String[] {
                "A$$农、林、牧、渔业$$第一产业            $$1",
                "B$$采矿业$$第二产业            $$2",
                "C$$制造业$$第二产业            $$2",
                "D$$电力、煤气及水的生产和供应业$$第二产业            $$2",
                "E$$建筑业$$第二产业            $$2",
                "F$$交通运输、仓储及邮政业$$第一层次            $$3",
                "G$$信息传输、计算机服务和软件业$$第二层次            $$4",
                "H$$批发和零售业$$第二层次            $$4",
                "I$$住宿和餐饮业$$第二层次            $$4",
                "J$$金融业$$第二层次            $$4",
                "K$$房地产业$$第二层次            $$4",
                "L$$租赁和商务服务业$$第二层次            $$4",
                "M$$科学研究、技术服务和地质勘查业$$第三层次            $$5",
                "N$$水利、环境和公共设施管理业$$第三层次            $$5",
                "O$$居民服务和其他服务业$$第三层次            $$5",
                "P$$教育$$第三层次            $$5",
                "Q$$卫生、社会保障和社会福利业$$第三层次            $$5",
                "R$$文化、体育和娱乐业$$第三层次            $$5",
                "S$$公共管理和社会组织$$第四层次            $$6",
                "T$$国际组织（其他行业）$$第四层次            $$6",
                "U$$其他$$第一层次            $$3"
        };
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getIndustryCode())){
            if (!ArrayUtils.contains(industryCode, dto.getIndustryCode())) {
                throw new SyncException("临时户行业归属类型值不正确");
            }
        }
    }
}
