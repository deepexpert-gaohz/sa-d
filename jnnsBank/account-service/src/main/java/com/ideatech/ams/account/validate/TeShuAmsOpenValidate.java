package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class TeShuAmsOpenValidate extends AbstractAllPublicAccountOpenValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws Exception {
        super.validateCustomerInfoCommonOpen(dto);

        if (StringUtils.isBlank(dto.getDepositorType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0100:存款人类别不能为空!");
        }
        //特殊户校验无需登记部门
//            if (StringUtils.isBlank(dto.getRegOffice())) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0069:登记部门不能为空!");
//            }
        // 法定代表人或负责人身份证件种类、编号
        if (StringUtils.isBlank(dto.getLegalType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0070:法定代表人（单位负责人）不能为空!");
        }
        if (StringUtils.isBlank(dto.getTelephone())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0071:请录入正确的电话号码!");
        } else {
            if (!RegexUtils.isPhoneNumber(dto.getTelephone())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0072:请录入正确的电话号码!");
            }
        }

        if (StringUtils.isEmpty(dto.getRegFullAddress())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0048:(上报人行)工商注册地址(与营业执照一致)不能为空");
        }

        // 存款人类别
        if (com.ideatech.common.utils.StringUtils.isNotBlank(dto.getDepositorType())) {
            String[] arrays = {"50", "51", "52"};
            if (!ArrayUtils.contains(arrays, dto.getDepositorType())) {
                log.info("特殊户存款人类别当前值：" + dto.getDepositorType());
                log.info("特殊户存款人类别值应为：\"50\", \"51\", \"52\"");
                throw new SyncException("特殊户存款人类别不正确！");
            }
        }

        //法人类型值判断
        String[] legalType = new String[] { "1", "2"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getLegalType())) {
                log.info("特殊户法人类型值当前值：" + dto.getLegalType());
                log.info("特殊户法人类型值应为：\"1\", \"2\"");
                throw new SyncException("特殊户法人类型值不正确");
            }
        }

        //上级法人类型值判断
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getParLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getParLegalType())) {
                log.info("特殊户上级法人类型值当前值：" + dto.getParLegalType());
                log.info("特殊户上级法人类型值不正确：\"1\", \"2\"");
                throw new SyncException("特殊户上级法人类型值不正确");
            }
        }
        //法人证件类型值判断
        String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getLegalIdcardType())) {
                log.info("特殊户法人证件类型当前值：" + dto.getLegalIdcardType());
                log.info("特殊户法人证件类型值应为确：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("特殊户法人证件类型值不正确");
            }
        }


        //上级法人证件类型值判断
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getParLegalIdcardType())){
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

        //基本户证明文件2种类类型值判断
        String[] isIdentifications = new String[] {"0", "1"};
        if(StringUtils.isNotBlank(dto.getIsIdentification())){
            if (!ArrayUtils.contains(isIdentifications, dto.getIsIdentification())) {
                log.info("特殊户未标明注册资金当前值：" + dto.getIsIdentification());
                log.info("特殊户未标明注册资金值应为：\"0\", \"1\"");
                throw new SyncException("特殊户未标明注册资金值不正确");
            }
        }
        //特殊户行业归属类型值判断
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
                throw new SyncException("特殊户行业归属类型值不正确");
            }
        }

        //临时户币种类型值判断
        String[] regCurrencyTypeAms = new String[] { "AUD","CAD","CNY","EUR","GBP","HKD","JPY","KRW","SGD","USD","XEU"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getRegCurrencyType())){
            if (!ArrayUtils.contains(regCurrencyTypeAms, dto.getRegCurrencyType())) {
                log.info("特殊户币种类型值当前值：" + dto.getRegCurrencyType());
                log.info("特殊户币种类型值应为：\"AUD\",\"CAD\",\"CNY\",\"EUR\",\"GBP\",\"HKD\",\"JPY\",\"KRW\",\"SGD\",\"USD\",\"XEU\"");
                throw new SyncException("特殊户币种类型值不正确");
            }
        }
    }
}
