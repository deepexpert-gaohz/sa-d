package com.ideatech.ams.kyc.dto.idcard;

import com.ideatech.ams.kyc.enums.IdCardType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class IdCardLocalDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 身份证号码
     */

    private String idCardNo;
    /**
     * 姓名
     */
    private String idCardName;
    /**
     *性别 1-男  0-女
     */
    private String idCardSex;
    /**
     * 民族
     */
    private String idCardNation;
    /**
     * 出生日期
     */
    private String idCardBirthday;
    /**
     * 身份证上地址
     */
    private String idCardAddress;
    /**
     * 身份证签发机关
     */
    private String idCardOrgan;
    /**
     * 有效期
     */
    private String idCardValidity;
    /**
     * 本地身份证头像base64字符串
     */

    private String idCardLocalImageByte;
    /**
     * 人行身份证头像base64字符串
     */

    private String idCardPbcImageByte;

    /**
     * 身份证的代次（1/2代）
     */

    private IdCardType idCardType;
    /**
     * 核查人员类型
     */
    private String idCardedType;
    /**
     * 完整机构ID
     */
    private String organFullId;
    /**
     * 备用字段1
     */
    private String string01;
    /**
     * 备用字段2
     */
    private String string02;
    /**
     * 备用字段3
     */
    private String string03;
    /**
     * 备用字段4
     */
    private String string04;
    /**
     * 备用字段5
     */
    private String string05;

}
