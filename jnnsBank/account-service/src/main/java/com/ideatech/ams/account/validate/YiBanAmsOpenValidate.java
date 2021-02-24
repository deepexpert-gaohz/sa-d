package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.pbc.utils.RegexUtils;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;

@Slf4j
public class YiBanAmsOpenValidate extends AbstractAllPublicAccountOpenValidate {

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
        // 证明文件1种类
//		if (StringUtils.isBlank(dto.getAcctFileType())) {
//			throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"证明文件1类型不能为空");
//		}
        // 证明文件1编号
//        if (dto.getAcctFileType().equals("06")) {
//            if (StringUtils.isBlank(dto.getAcctFileNo())) {
//                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "开户证明文件种类编号不能为空");
//            }
//        }
        // 当证明文件种类为：“借款合同”时,证明文件编号为必输项,反之可以为空
        if (StringUtils.isBlank(dto.getAcctFileType())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0103:开户证明文件种类不能为空！");
        }
        // 当证明文件种类为：“借款合同”时,证明文件编号为必输项,反之可以为空
        if (StringUtils.isNotBlank(dto.getAcctFileType()) && dto.getAcctFileType().equals("06")
                && StringUtils.isEmpty(dto.getAcctFileNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0040:文件种类为“借款合同”时,开户证明文件编号为必输项");
        }
        if (StringUtils.isBlank(dto.getAccountKey())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0080:一般存款账户开户许可证核准号不能为空!");
        }
        if (!dto.getAccountKey().startsWith("J") && !dto.getAccountKey().startsWith("j")) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0041:开户许可证不正确,应以J开头!");
        }
        if (dto.getAccountKey().length() != 14) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0042:开户许可证长度不正确，应14位!");
        }
        if (!RegexUtils.isNumeric(StringUtils.substring(dto.getAccountKey(), 1, 14))) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "开户许可证(2-14位)必须是数字!");
        }
        if (StringUtils.isBlank(dto.getBasicAcctRegArea())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0081:一般存款账户开户地地区代码不能为空!");
        } else {
            if (dto.getBasicAcctRegArea().trim().length() != 6) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0043:请录入正确的基本存款账户开户地地区代码!");
            }
        }

        // 证明文件1编号
        if ("06".equals(dto.getAcctFileType())) {
            if (com.ideatech.common.utils.StringUtils.isBlank(dto.getAcctFileNo())) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "文件种类为“借款合同”时,开户证明文件编号为必输项");
            }
            try {
                if (dto.getAcctFileNo().getBytes("GBK").length > 32) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "证明文件编号超长(32个字符或16个汉字),请重新录入!");
                }
            } catch (Exception e) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"证明文件编号超长(32个字符或16个汉字),请重新录入!");
                }
        }
        // 证明文件2编号
        if ("07".equals(dto.getAcctFileType())) {
            try {
                if (StringUtils.isNotBlank(dto.getAcctFileNo()) && dto.getAcctFileNo().getBytes("GBK").length > 30) {
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "证明文件编号超长(30个字符或15个汉字),请重新录入!");
                }
            } catch (Exception e) {
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR,"证明文件编号超长(30个字符或15个汉字),请重新录入!");
            }
        }

        //判断开户证明文件种类下拉框值校验
        String[] accountFileType = new String[] { "06", "07"};
        if(com.ideatech.common.utils.StringUtils.isNotBlank(dto.getAcctFileType())){
            if (!ArrayUtils.contains(accountFileType, dto.getAcctFileType())) {
                log.info("开户证明文件编号类型值当前值：" + dto.getAcctFileType());
                log.info("开户证明文件编号类型值应为：\"06\", \"07\"");
                throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "开户证明文件编号类型值不正确");
            }
        }
    }
}
