package com.ideatech.ams.mivs.dto.mad;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;


/**
 *
 * @author jzh
 * @date 2019/7/17.
 * 手机号码联网核查申请报文传输对象
 *
 *  * 证件号码 id改为identification
 */

@Data
public class MobilePhoneNumberApplyDto{

    private Long id;

    /**
     * MobilePhoneNumber
     * 手机号码
     * <MobNb>  [1..1] E164 (Max13NumericText) 禁止中文
     */
    @NotBlank(message="手机号码不能为空")
    @Pattern(regexp="[0-9]{1,13}",message="手机号码格式不正确")
    private String mobNb;

    /**
     * Name
     * 姓名
     * <Nm>  [1..1] Max140Text
     */
    @NotBlank(message="姓名不能为空")
    @Length(max = 140, message = "姓名长度超过140")
    private String nm;

    /**
     * IdentificationType
     * 证件类型
     * <IdTp>  [1..1] IdTypeCode (Max4Text) 禁止中文
     */
    @NotBlank(message="证件类型不能为空")
    @Length(max = 4, message = "证件类型超过35")
    private String idTp;

    /**
     * Identification
     * 证件号码
     * <Id>  [1..1] Max35Text  禁止中文
     */
    @NotBlank(message="证件号码不能为空")
    @Length(max = 35, message = "证件号码长度超过35")
    private String identification;

    /**
     * UniformSocialCreditCode
     * 统一社会信用代码
     * <UniSocCdtCd>  [1..1] Max18Text 企业客户的统一社会信用代码
     */
    @Length(max = 18, message = "统一社会信用代码长度超过18")
    private String uniSocCdtCd;

    /**
     * BusinessRegistrationNumber
     * 工商注册号
     * <BizRegNb>  [1..1] Max15Text 企业客户的工商注册号
     */
    @Length(max = 15, message = "工商注册号长度超过15")
    private String bizRegNb;

    /**
     * OperatorName
     * 操作员姓名
     * <OpNm>  [1..1] Max140Text 参与机构核查人员姓名
     */
    @NotBlank(message="操作员姓名不能为空")
    @Length(max = 140, message = "操作员姓名长度超过140")
    private String opNm;

}
