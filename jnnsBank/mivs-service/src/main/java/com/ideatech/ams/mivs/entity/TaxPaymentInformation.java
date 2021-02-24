package com.ideatech.ams.mivs.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/29.
 * 纳税信息 属于：企业纳税信息联网核查申请应答记录
 */

@Entity
@Data
@Table(name = "mivs_tax_pay_info")
public class TaxPaymentInformation extends BaseMaintainablePo {

    private Long taxInformationLogId;

    /**
     * TaxAuthorityCode
     * 税务机关代码
     * <TxAuthCd>  [1..1] TxAuthCd (Max11Text)
     */
    private String txAuthCd;

    /**
     * TaxAuthorityName
     * 税务机关名称
     * <TxAuthNm>  [1..1] Max300Text
     */
    private String txAuthNm;

    /**
     * TaxpayerStatus
     * 纳税人状态
     * <TxpyrSts>  [1..1] TaxpayerStatusCode (Max4Text)
     */
    private String txpyrSts;
}
