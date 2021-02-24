package com.ideatech.ams.system.proof.dto;

import com.ideatech.ams.system.proof.enums.CompanyAcctType;
import com.ideatech.ams.system.proof.enums.ProofType;
import com.ideatech.common.dto.BaseMaintainableDto;
import com.ideatech.common.enums.CompanyIfType;
import lombok.Data;

import java.util.List;


@Data
public class ProofReportDto extends BaseMaintainableDto {
    private Long id;
    /**
     * 账号
     */
    private String acctNo;
    /**
     * 账户名称
     */
    private String acctName;
    /**
     * 账户性质
     */
    private CompanyAcctType acctType;

    private String acctTypeStr;
    /**
     * 企业名称
     */
    private String entname;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 开户行
     */
    private String openBankName;
    /**
     * 尽调机构
     */
    private String proofBankName;
    /**
     * 尽调人
     */
    private String username;
    /**
     * 最后一次尽调时间
     */
    private String dateTime;
    /**
     * 是否尽调
     */

    private CompanyIfType kycFlag;

    private String kycFlagStr;
    /**
     * 类型
     */

    private ProofType type;

    private String typeStr;

    private String typeDetil;
    /**
     * 尽调机构fullid
     */
    private String organFullId;
    /**
     * 价格
     */
    private String price;

    private String beginDate;
    private String endDate;

    /**
     * 校验结果
     */
    private String result;

    private String accountKey;

    private String regAreaCode;


    private List<ProofType> typeList;

}
