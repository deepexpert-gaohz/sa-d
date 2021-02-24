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

import java.text.SimpleDateFormat;

@Slf4j
public class YuSuanAmsOpenValidate extends AbstractAllPublicAccountOpenValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO dto) {
        // 开户时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (format.parse(dto.getAcctCreateDate()).getTime() > System.currentTimeMillis()) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0014:开户日期不能大于当前日期");
            }
        } catch (Exception e) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0016:日期格式错误");
        }
//		if (StringUtils.isEmpty(dto.getCapitalProperty())) {
//			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0064:资金性质不能为空!");
//		}

        if (StringUtils.isBlank(dto.getAccountKey())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0041:基本户开户许可证不能为空!");
        } else {
            if (!dto.getAccountKey().startsWith("J") && !dto.getAccountKey().startsWith("j")) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0041:基本户开户许可证不正确,应以J开头!");
            }
            if (dto.getAccountKey().length() != 14) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0042:基本户开户许可证长度不正确，应14位!");
            }
            if (!com.ideatech.ams.pbc.utils.RegexUtils.isNumeric(dto.getAccountKey().substring(1, 14))) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "基本户开户许可证(2-14位)必须是数字!");
            }
        }
        if (StringUtils.isBlank(dto.getBasicAcctRegArea())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0081:基本存款账户开户地地区代码不能为空!");
        } else {
            if (dto.getBasicAcctRegArea().trim().length() != 6) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0043:请录入正确的基本存款账户开户地地区代码!");
            }
        }

        if (StringUtils.isNotBlank(dto.getAcctFileType()) && (!"10".equals(dto.getAcctFileType()))) {
            if (StringUtils.isBlank(dto.getAcctFileNo())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0045:开户证明文件1编号不能为空");
            }
        }
        // 证明文件2种类与编号
        if (StringUtils.isBlank(dto.getAcctFileType2()) || StringUtils.isBlank(dto.getAcctFileNo2())) {
            if (StringUtils.isNotBlank(dto.getAcctFileNo2())
                    || StringUtils.isNotBlank(dto.getAcctFileType2())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "开户证明文件2编号与开户证明文件2类型必须全部为空、或全部不为空!");
            }
        }
        if(StringUtils.isBlank(dto.getAcctFileType())){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0047:开户证明文件1类型不能为空！");
        }
        if (dto.getAcctFileType().equals("11") || dto.getAcctFileType().equals("09")) {
            if (StringUtils.isBlank(dto.getAcctFileNo())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0047:开户证明文件1编号不能为空!");
            }
        }

        if (com.ideatech.common.utils.StringUtils.isBlank(dto.getCapitalProperty())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "资金性质不能为空");
        }

        String returnString = "";
        // 账户构成方式
        if (com.ideatech.common.utils.StringUtils.isBlank(dto.getAccountNameFrom())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账户构成方式不能为空");
        } else if (dto.getAccountNameFrom().equals("1")) { // 加内设部门
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideAddress())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门地址不能为空");
            }
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideDeptName())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门名称不能为空");
            }
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideLeadIdcardType())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门负责人身份证件类型不能为空");
            }
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideLeadName())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门负责人姓名不能为空");
            }
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideLeadIdcardNo())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门负责人身份编号不能为空");
            }
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideTelephone())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门联系电话不能为空");
            }
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getInsideZipcode())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "内设部门邮政编码不能为空");
            }
        }

        if (dto.getAccountNameFrom().equals("1")) {
            if (RegexUtils.length(dto.getInsideDeptName()) < 0 || RegexUtils.length(dto.getInsideDeptName()) > 70) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0048:内设部门名称超长(70个字符或35个汉字),请重新录入!");
            }
            if (dto.getInsideLeadIdcardType().equals("1")) {
                try {
                    returnString = IDCardUtils.IDCardValidate(dto.getInsideLeadIdcardNo());
                    if (StringUtils.isNotBlank(returnString)) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0049" + returnString);
                    }
                } catch (Exception e) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0050:身份证验证出错！");
                }
            }
//			if (StringUtils.isBlank(dto.getInsideLeadName())) {
//				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0070:内设部门负责人姓名不能为空!");
//			}
//			if (StringUtils.isBlank(dto.getInsideTelephone())) {
//				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0052:内设部门联系电话不能为空!");
//			}
//			if (StringUtils.isBlank(dto.getInsideZipcode())) {
//				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0053:内设部门邮编不能为空!");
//			}
//			if (StringUtils.isBlank(dto.getInsideAddress())) {
//				throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"AMSERROR-0054:内设部门地址不能为空!");
//			}
        } else if (dto.getAccountNameFrom().equals("2")) {
            if (RegexUtils.length(dto.getFundManager()) < 0 || RegexUtils.length(dto.getFundManager()) > 70) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0051:资金管理人姓名超长(70个字符或35个汉字),请重新录入!");
            }
            if (dto.getFundManagerIdcardType().equals("1")) {
                try {
                    returnString = IDCardUtils.IDCardValidate(dto.getFundManagerIdcardNo());
                    if (StringUtils.isNotBlank(returnString)) {
                        throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0052" + returnString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0050:身份证验证出错！");
                }
            }
        }
        // 取现标识
        if (StringUtils.isNotBlank(dto.getCapitalProperty())) {
            if (dto.getCapitalProperty().equals("14") || dto.getCapitalProperty().equals("04")
                    || dto.getCapitalProperty().equals("11") || dto.getCapitalProperty().equals("13")
                    || dto.getCapitalProperty().equals("10")) {
                dto.setEnchashmentType("1");
            } else {
                dto.setEnchashmentType("0");
            }
        }


        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctFileType2())){
            //证明文件1类型判断
            String[] accountFileType2 = new String[] { "08"};
            if (!ArrayUtils.contains(accountFileType2, dto.getAcctFileType2())) {
                log.info("预算户证明文件2类型值当前值：" + dto.getAcctFileType2());
                log.info("预算户证明文件2类型值应为：\"08\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户开户证明文件2类型值不正确");
            }
        }
        // 证明文件1类型判断
        if (com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctFileType())) {
            //证明文件1类型判断
            String[] accountFileType = new String[] { "09", "10","11"};
            if (!ArrayUtils.contains(accountFileType, dto.getAcctFileType())) {
                log.info("预算户证明文件1类型值当前值：" + dto.getAcctFileType());
                log.info("预算户证明文件1类型值应为：\"09\", \"10\",\"11\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户开户证明文件1类型值不正确");
            }
        }
        // 证明文件2种类与编号
        if (com.ideatech.common.utils.StringUtils.isBlank(dto.getAcctFileType2()) || com.ideatech.common.utils.StringUtils.isBlank(dto.getAcctFileNo2())) {
            if (com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctFileNo2()) || com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctFileType2())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"开户证明文件2编号与开户证明文件2类型必须全部为空、或全部不为空!");
            }
        }

        //预算户账户名称构成方式类型值不正确
        String[] accountNameFrom = new String[]{ "0","1","2"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAccountNameFrom())){
            if (!ArrayUtils.contains(accountNameFrom, dto.getAccountNameFrom())) {
                log.info("预算户账户名称构成方式类型当前值：" + dto.getAccountNameFrom());
                log.info("预算户账户名称构成方式类型值应为：\"0\",\"1\",\"2\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户账户名称构成方式类型值不正确");
            }
        }

        //预算户资金性质类型值不正确
        String[] capitalProperty = new String[]{ "01","02","03","04","05","06","07","08","09","10","11","12","13","14","16"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getCapitalProperty())){
            if (!ArrayUtils.contains(capitalProperty, dto.getCapitalProperty())) {
                log.info("预算户资金性质类型当前值：" + dto.getCapitalProperty());
                log.info("预算户资金性质类型值应为：\"01\",\"02\",\"03\",\"04\",\"05\",\"06\",\"07\",\"08\",\"09\",\"10\",\"11\",\"12\",\"13\",\"14\",\"16\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户资金性质类型值不正确");
            }
        }

        //资金管理人身份证件类型值不正确
        String[] moneyManagerCtype = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getFundManagerIdcardType())){
            if (!ArrayUtils.contains(moneyManagerCtype, dto.getFundManagerIdcardType())) {
                log.info("资金管理人身份证件类型当前值：" + dto.getFundManagerIdcardType());
                log.info("资金管理人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"资金管理人身份证件类型值不正确");
            }
        }

        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getInsideLeadIdcardType())){
            if (!ArrayUtils.contains(moneyManagerCtype, dto.getInsideLeadIdcardType())) {
                log.info("内设部门身份证件类型当前值：" + dto.getInsideLeadIdcardType());
                log.info("内设部门身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"内设部门身份证件类型值不正确");
            }
        }
    }
}
