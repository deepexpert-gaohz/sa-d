package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class YiBanAmsChangeValidate extends AbstractAllPublicAccountChangeValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO dto) {
        // 当证明文件种类为：“借款合同”时,证明文件编号为必输项,反之可以为空
        if (StringUtils.isNotBlank(dto.getAcctFileType()) && dto.getAcctFileType().equals("06") && StringUtils.isEmpty(dto.getAcctFileNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0040:文件种类为“借款合同”时,文件编号为必输项");
        }
        if (StringUtils.isBlank(dto.getAccountKey())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0080:基本存款账户开户许可证核准号不能为空!");
        }
        if (StringUtils.isNotBlank(dto.getAccountKey()) && !dto.getAccountKey().startsWith("J") && !dto.getAccountKey().startsWith("j")) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0041:请录入正确的基本存款账户开户许可证核准号!");
        }
        if (StringUtils.isNotBlank(dto.getAccountKey()) && dto.getAccountKey().length() < 14) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "AMSERROR-0042:请录入正确的基本存款账户开户许可证核准号!");
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
