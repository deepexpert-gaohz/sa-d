package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.exception.SyncException;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.IDCardUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class YuSuanAmsChangeValidate extends AbstractAllPublicAccountChangeValidate{
    @Override
    protected void doValidate(AllBillsPublicDTO dto) throws SyncException, Exception {

        //内设部门证件编号校验
        if(StringUtils.isNotBlank(dto.getFundManagerIdcardNo())){
            if(StringUtils.isNotBlank(dto.getInsideLeadIdcardType()) && StringUtils.equals("1",dto.getInsideLeadIdcardType())){
                //身份证进行校验
                if(StringUtils.isNotBlank(IDCardUtils.IDCardValidate(dto.getInsideLeadIdcardNo()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:请录入正确内设部门负责人身份编号!");
                }
            }
        }


        //资金管理人证件编号校验
        if(StringUtils.isNotBlank(dto.getFundManagerIdcardNo())){
            if(StringUtils.isNotBlank(dto.getFundManagerIdcardType()) && StringUtils.equals("1",dto.getFundManagerIdcardType())){
                //身份证进行校验
                if(StringUtils.isNotBlank(IDCardUtils.IDCardValidate(dto.getFundManagerIdcardNo()))){
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:请录入正确的资金管理人身份证编号!");
                }
            }
        }

        if(StringUtils.isBlank(dto.getAccountNameFrom())){
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0011:账户名称构成方式不能为空！");
        }

        //预算户账户名称构成方式类型值不正确
        String[] accountNameFrom = new String[]{ "0","1","2"};
        if(StringUtils.isNotBlank(dto.getAccountNameFrom())){
            if (!ArrayUtils.contains(accountNameFrom, dto.getAccountNameFrom())) {
                log.info("预算户账户名称构成方式类型当前值：" + dto.getAccountNameFrom());
                log.info("预算户账户名称构成方式类型值应为：\"0\",\"1\",\"2\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户账户名称构成方式类型值不正确");
            }
        }

        //预算户资金性质类型值不正确
        String[] capitalProperty = new String[]{ "01","02","03","04","05","06","07","08","09","10","11","12","13","14","16"};
        if(StringUtils.isNotBlank(dto.getCapitalProperty())){
            if (!ArrayUtils.contains(capitalProperty, dto.getCapitalProperty())) {
                log.info("预算户资金性质类型当前值：" + dto.getCapitalProperty());
                log.info("预算户资金性质类型值应为：\"01\",\"02\",\"03\",\"04\",\"05\",\"06\",\"07\",\"08\",\"09\",\"10\",\"11\",\"12\",\"13\",\"14\",\"16\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户资金性质类型值不正确");
            }
        }

        //资金管理人身份证件类型值不正确
        String[] moneyManagerCtype = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        if(StringUtils.isNotBlank(dto.getFundManagerIdcardType())){
            if (!ArrayUtils.contains(moneyManagerCtype, dto.getFundManagerIdcardType())) {
                log.info("资金管理人身份证件类型当前值：" + dto.getFundManagerIdcardType());
                log.info("资金管理人身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"资金管理人身份证件类型值不正确");
            }
        }

        if(StringUtils.isNotBlank(dto.getInsideLeadIdcardType())){
            if (!ArrayUtils.contains(moneyManagerCtype, dto.getInsideLeadIdcardType())) {
                log.info("内设部门身份证件类型当前值：" + dto.getInsideLeadIdcardType());
                log.info("内设部门身份证件类型值应为：\"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"内设部门身份证件类型值不正确");
            }
        }

        if(StringUtils.isNotBlank(dto.getAcctFileType2())){
            //证明文件1类型判断
            String[] accountFileType2 = new String[] { "08"};
            if (!ArrayUtils.contains(accountFileType2, dto.getAcctFileType2())) {
                log.info("预算户证明文件2类型值当前值：" + dto.getAcctFileType2());
                log.info("预算户证明文件2类型值应为：\"08\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户开户证明文件2类型值不正确");
            }
        }
        // 证明文件1类型判断
        if (StringUtils.isNotBlank(dto.getAcctFileType())) {
            //证明文件1类型判断
            String[] accountFileType = new String[] { "09", "10","11"};
            if (!ArrayUtils.contains(accountFileType, dto.getAcctFileType())) {
                log.info("预算户证明文件1类型值当前值：" + dto.getAcctFileType());
                log.info("预算户证明文件1类型值应为：\"09\", \"10\",\"11\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"预算户开户证明文件1类型值不正确");
            }
        }
    }
}
