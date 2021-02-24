package com.ideatech.ams.image.dto;

import com.ideatech.ams.image.enums.BusinessTypeEnum;
import lombok.Data;

/**
 * 双录短信发送对象
 * @author jzh
 * @date 2019-12-03.
 */

@Data
public class NoticeMessageDto {

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 客户名称（企业名称）
     */
    private String depositorName;

    /**
     * 法人名称
     */
    private String legalName;

    /**
     * 开户性质
     */
    private String acctType;

    /**
     * 远程双录访问链接
     */
    private String url;

    /**
     * 网点电话
     */
    private String telephone;

    /**
     * 法人手机号
     */
    private String legalTelephone;

    /**
     * 法人证件号
     */
    private String legalIdcardNo;

    /**
     * 是否指定，是否随机坐席
     */
    private Boolean randomFlag;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 短信内容
     */
    private String message;
}
