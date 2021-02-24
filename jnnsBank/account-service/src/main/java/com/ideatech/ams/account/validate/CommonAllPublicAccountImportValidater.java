package com.ideatech.ams.account.validate;

import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


/**
 * 公共校验器
 *
 * @author vantoo
 */
@Slf4j
public class CommonAllPublicAccountImportValidater extends AbstractAllPublicAccountValidate {

    @Override
    protected void doValidate(AllBillsPublicDTO accountInfo) {
        if (accountInfo == null) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "t+1数据转对象为空");
        }
        if (StringUtils.isBlank(accountInfo.getAcctNo())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账号为空");
        }
        // 可能存在未知账户性质
        if (accountInfo.getAcctType() == null) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账户性质为空");
        }
        if (accountInfo.getBillType() == null) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "业务操作类型为空");
        }
        if (StringUtils.isBlank(accountInfo.getBankCode())) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "账号对应的机构不存在");
        }

        commonRevokeValidate(accountInfo);

    }

    private void commonRevokeValidate(AllBillsPublicDTO accountInfo) {
        if ((accountInfo.getAcctType() != CompanyAcctType.yiban && accountInfo.getAcctType() != CompanyAcctType.feiyusuan) && accountInfo.getBillType() == BillType.ACCT_REVOKE) {
            throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR, "核准类销户无法上报人行账管系统");
        }
    }
}
