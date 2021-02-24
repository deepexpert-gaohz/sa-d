package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.ams.system.config.dto.ConfigDto;
import com.ideatech.ams.system.config.service.ConfigService;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
public class JiBenAmsOpenValidate extends AbstractAllPublicAccountOpenValidate {

    @Autowired
    private ConfigService configService;

    @Value("${ams.company.pbc.eccs:true}")
    private boolean eccsSyncEnabled;

    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws Exception {
        super.validateCustomerInfoCommonOpen(dto);

        //基本特殊有存款人类别
        if (StringUtils.isEmpty(dto.getDepositorType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0100:存款人类别不能为空!");
        }
        //判断开户字段下拉框值合法性
        //存款人类别（基本户）校验
        String[] depositorType = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "20" };
        if (StringUtils.isNotBlank(dto.getDepositorType())) {
            if (!ArrayUtils.contains(depositorType, dto.getDepositorType())) {
                log.info("基本户存款人类别（基本户）类型当前值：" + dto.getDepositorType());
                log.info("基本户存款人类别（基本户）类型值应为：\"01\", \"02\", \"03\", \"04\", \"05\", \"06\", \"07\", \"08\", \"09\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\", \"17\", \"20\"");
                throw new SyncException("基本户存款人类别（基本户）类型值不正确");
            }
        }

        //法人类型值判断
        String[] legalType = new String[] { "1", "2"};
        if (StringUtils.isNotBlank(dto.getLegalType())) {
            if (!ArrayUtils.contains(legalType, dto.getLegalType())) {
                log.info("基本户法人类型当前值：" + dto.getLegalType());
                log.info("基本户法人类型值应为：\"1\", \"2\"");
                throw new SyncException("基本户法人类型值不正确");
            }
        }

        //上级法人类型值判断
        if(StringUtils.isNotBlank(dto.getParLegalType())){
            if (!ArrayUtils.contains(legalType, dto.getParLegalType())) {
                log.info("基本户上级法人类型当前值：" + dto.getParLegalType());
                log.info("基本户上级法人类型值应为：\"1\", \"2\"");
                throw new SyncException("基本户上级法人类型值不正确");
            }
        }
        //法人证件类型值判断
        String[] legalIdcardTypeAms = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if (StringUtils.isNotBlank(dto.getLegalIdcardType())) {
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getLegalIdcardType())) {
                log.info("基本户法人证件类型当前值：" + dto.getLegalIdcardType());
                log.info("基本户法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("基本户法人证件类型值不正确");
            }
        }

        //上级法人证件类型值判断
        if(StringUtils.isNotBlank(dto.getParLegalIdcardType())){
            if (!ArrayUtils.contains(legalIdcardTypeAms, dto.getParLegalIdcardType())) {
                log.info("基本户上级法人证件类型当前值：" + dto.getLegalIdcardType());
                log.info("基本户上级法人证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new SyncException("基本户上级法人证件类型值不正确");
            }
        }

        //基本户币种类型值判断
        String[] regCurrencyTypeAms = new String[] { "AUD","CAD","CNY","EUR","GBP","HKD","JPY","KRW","SGD","USD","XEU"};
        if(StringUtils.isNotBlank(dto.getRegCurrencyType())){
            if (!ArrayUtils.contains(regCurrencyTypeAms, dto.getRegCurrencyType())) {
                log.info("基本户币种类型当前值：" + dto.getRegCurrencyType());
                log.info("基本户币种类型值应为：\"AUD\",\"CAD\",\"CNY\",\"EUR\",\"GBP\",\"HKD\",\"JPY\",\"KRW\",\"SGD\",\"USD\",\"XEU\"");
                throw new SyncException("基本户币种类型值不正确");
            }
        }

        //基本户证明文件1种类类型值判断
        String[] fileType = new String[] { "01", "02", "03", "04"};
        if(StringUtils.isNotBlank(dto.getFileType())){
            if (!ArrayUtils.contains(fileType, dto.getFileType())) {
                log.info("基本户证明文件1种类当前值：" + dto.getFileType());
                log.info("基本户证明文件1种类类型值应为：\"01\", \"02\", \"03\", \"04\"");
                throw new SyncException("基本户证明文件1种类类型值不正确");
            }
        }


        //基本户证明文件2种类类型值判断
        String[] fileType2 = new String[] {"02", "03", "04"};
        if(StringUtils.isNotBlank(dto.getFileType2())){
            if (!ArrayUtils.contains(fileType2, dto.getFileType2())) {
                log.info("基本户证明文件2种类当前值：" + dto.getFileType2());
                log.info("基本户证明文件2种类类型值应为：\"02\", \"03\", \"04\"");
                throw new SyncException("基本户证明文件2种类类型值不正确");
            }
        }

        //基本户行业归属类型值判断
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
        if(StringUtils.isNotBlank(dto.getIndustryCode())){
            if (!ArrayUtils.contains(industryCode, dto.getIndustryCode())) {
                log.info("基本户行业归属类型当前值：" + dto.getIndustryCode());
                throw new SyncException("基本户行业归属类型值不正确");
            }
        }

//        //基本户证明文件2种类类型值判断
//        String[] isIdentifications = new String[] {"0", "1"};
//        if(StringUtils.isNotBlank(dto.getIsIdentification())){
//            if (!ArrayUtils.contains(isIdentifications, dto.getIsIdentification())) {
//                log.info("基本户未标明注册资金当前值：" + dto.getIsIdentification());
//                log.info("基本户未标明注册资金值应为：\"0\", \"1\"");
//                throw new SyncException("基本户未标明注册资金值不正确");
//            }
//        }

//        if (StringUtils.isBlank(dto.getDepositorType())) {
//            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "存款人类型不能为空");
//        }
//        // 当存款人类别为企业法人、非企业法人、个体工商户时 国地税获与无需办理税务登记证明必须有个必填
//        if (StringUtils.isBlank(dto.getNoTaxProve())) {
//            String[] arrays = { "01", "02", "13", "14" };
//            if (ArrayUtils.contains(arrays, dto.getDepositorType())) {
//                if (StringUtils.isBlank(dto.getStateTaxRegNo()) && StringUtils.isBlank(dto.getTaxRegNo())) {
//                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请录入国税登记证号或地税登记证号,或填写无需办理税务登记证的文件或税务机关出具的证明!");
//                }
//            }
//        }

        // 注册资金
//        if (StringUtils.isBlank(dto.getIsIdentification())) {
//            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "未标明注册资金不能为空");
//        } else {
//            //未选择未标明时需要校验必填
//            if (!"1".equals(dto.getIsIdentification())) {
//                if (dto.getRegisteredCapital() == null || StringUtils.isBlank(String.valueOf(dto.getRegisteredCapital()))) {
//                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "注册资金不能为空");
//                }
//                if (StringUtils.isBlank(dto.getRegCurrencyType())) {
//                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "注册资金币种不能为空");
//                }
//            }
//            //有值必须校验格式
//            if (dto.getRegisteredCapital() != null && StringUtils.isNotBlank(String.valueOf(dto.getRegisteredCapital()))) {
//                if (!com.ideatech.ams.pbc.utils.RegexUtils.isDecimal(String.valueOf(dto.getRegisteredCapital()))) {
//                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请录入正确的注册资金金额");
//                }
//                if (StringUtils.isBlank(dto.getRegCurrencyType())) {
//                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "注册资金有值时，注册资金币种不能为空");
//                }
//            }
//
//            if (StringUtils.isNotBlank(dto.getRegCurrencyType())) {
//                if (StringUtils.isBlank(String.valueOf(dto.getRegisteredCapital()))) {
//                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "注册资金币种有值时，注册资金不能为空");
//                } else {
//                    if (!com.ideatech.ams.pbc.utils.RegexUtils.isDecimal(String.valueOf(dto.getRegisteredCapital()))) {
//                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "请录入正确的注册资金金额");
//                    }
//                }
//            }
//        }

        // 存款类型为个体户时
        if ("14".equals(dto.getDepositorType())) {
            if (dto.getDepositorName().indexOf("个体户") < 0) {
                dto.setDepositorName("个体户" + dto.getDepositorName());
            }
            String legalName;
            if(dto.getLegalName().contains("个体户")){
                legalName = dto.getLegalName();
            }else{
                legalName = "个体户" + dto.getLegalName();
            }
            // 存款人类型
            if (!dto.getDepositorName().equals(legalName)) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0042:当存款人类别为'无字号个体工商户'时,法定代表人或单位负责人姓名与存款人名称要求一致!");
            }
            if (dto.getDepositorName().equals("个体户")) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0043:当存款人类别为'无字号个体工商户'时,存款人名称不能为‘个体户’!");
            }
        }
        // 法定代表人或负责人身份证件种类、编号
        if (StringUtils.isBlank(dto.getLegalType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0045:法定代表人（单位负责人）不能为空!");
        }
        if (StringUtils.isEmpty(dto.getRegFullAddress())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0048:(上报人行)工商注册地址(与营业执照一致)不能为空");
        }
        if (StringUtils.isNotEmpty(dto.getTaxRegNo()) && StringUtils.isNotEmpty(dto.getStateTaxRegNo())
                && StringUtils.isNotEmpty(dto.getNoTaxProve())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0049:无需办理税务登记证的文件或税务机关出具的证明与地税登记证号、国税登记证号不能同时存在!");
        }
        if (StringUtils.isBlank(dto.getTelephone())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0050:请录入正确的电话号码!");
        }
        //取消电话号码的验证  只做判空的校验
//        else {
//            if (!RegexUtils.isPhoneNumber(dto.getTelephone())) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0051:请录入正确的电话号码!");
//            }
//        }

//		if (StringUtils.isEmpty(dto.getFinanceName())) {
//			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0055:财务主管姓名不能为空!");
//		}
        /*if (StringUtils.isEmpty(dto.getFinanceIdcardNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0056:财务身份证号码不能为空!");
        }*/

//        if (StringUtils.isEmpty(dto.getCorpScale())) {
//            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0059:企业规模不能为空!");
//        }

        //信用机构上报启用时校验
        if(true == eccsSyncEnabled) {
            log.info("------------代码证上报为true，开始校验信用代码证上报字段");
            if (StringUtils.isBlank(dto.getOrgType())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0046:组织机构类别细分不能为空");
            }
            if (StringUtils.isEmpty(dto.getRegAddress())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0047:(上报信用机构)注册地址（详细地址）不能为空");
            }
            if (StringUtils.isEmpty(dto.getRegType())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0052:工商注册类型不能为空!");
            }
            if (StringUtils.isEmpty(dto.getRegNo())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0053:工商注册编号不能为空!");
            }
            //登记注册号类型为01 工商注册号   07 统一社会代码证   进去判断长度
            if(StringUtils.equals(dto.getRegType(),"01") || StringUtils.equals(dto.getRegType(),"07")){
                if (StringUtils.isNotEmpty(dto.getRegNo())
                        && (dto.getRegNo().length() != 15 && dto.getRegNo().length() != 18)) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0054:请录入正确的工商注册编号!");
                }
            }
//            if (StringUtils.isEmpty(dto.getFinanceTelephone())) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0057:财务主管电话不能为空!");
//            }
//            if (StringUtils.isEmpty(dto.getEconomyIndustryName())) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0058:经济行业分类不能为空!");
//            }
            if (StringUtils.isBlank(dto.getRegOffice())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0060:登记部门不能为空!");
            }
            if (StringUtils.isBlank(dto.getRegProvince())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0061:注册地址中“省份”Code不能为空");
            }
            if (StringUtils.isBlank(dto.getRegCity())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0062:注册地址中“城市”Code不能为空");
            }
            if (StringUtils.isBlank(dto.getRegArea())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0063:注册地址中“地区”Code不能为空");
            }
            if ("01".equals(dto.getDepositorType()) || "02".equals(dto.getDepositorType())
                    || "14".equals(dto.getDepositorType())) {
                if (!(dto.getRegType().equals("01") || dto.getRegType().equals("07"))) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0044:存款人类别为企业法人、非企业法人、有无字号的个体工商户时,工商注册类型为工商注册号或统一社会信用代码");
                }
            }
        }

    }
}
