package com.ideatech.ams.apply.cryptography;

import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import lombok.Data;

/**
 * 预约单创建传参 除了标记'选填',其它都是必传参数
 */
@Data
public class CryptoAddApplyAcctVo {
    /**
     * 预约客户企业名称
     */
    private String name;

    /**
     * 预约业务操作类型
     */
    private BillType billType;

    /**
     * 账户性质
     */
    private CompanyAcctType type;

    /**
     * 开户行银行名称
     */
    private String bank;

    /**
     * 开户行网点名称
     */
    private String branch;

    /**
     * 客户预约办理时间
     */
    private String applyDate;

    /**
     * 预约客户姓名
     */
    private String operator;

    /**
     * 预约客户手机号
     */
    private String phone;

    /**
     * 预约开户行网点银行12位人行联行号
     */
    private String organId;

    /**
     * 基本户开户核准号(选填)
     */
    private String accountKey;

    /**
     * 账号(选填)
     */
    private String acctNo;

    /**
     * 销户理由(选填)
     * 1-转户 2-撤并 3-解散 4-宣告破产 5-关闭 6-被吊销营业执照或执业许可证 7-其它
     */
    private String cancelReason;

}
