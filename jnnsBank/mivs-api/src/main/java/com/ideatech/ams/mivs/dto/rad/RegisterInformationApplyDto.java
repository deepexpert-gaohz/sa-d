package com.ideatech.ams.mivs.dto.rad;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author jzh
 * @date 2019/7/19.
 * 登记信息联网核查申请报文输出对象
 */

@Data
public class RegisterInformationApplyDto {

    private Long id;

    /************************ 公共 **********************/

    /**
     * 代理人姓名
     */
    @Length(max = 140, message = "代理人姓名长度超过140")
    private String agtNm;

    /**
     * 代理人身份证件号码
     */
    @Length(max = 35, message = "代理人身份证件号码长度超过35")
    private String agtId;

    /**
     * 操作员姓名
     */
    @NotBlank(message="操作员姓名不能为空")
    @Length(max = 140, message = "操作员姓名长度超过140")
    private String opNm;


    /************************--Enterprise <Ent>市场主体类型：企业**********************/
    /**
     * 企业名称
     */
    @Length(max = 100, message = "企业名称长度超过100")
    private String entNm;

    /**
     * 统一社会信用代码
     */
    @Length(max = 18, message = "统一社会信用代码长度超过18")
    private String uniSocCdtCd;

    /**
     * 法定代表人或单位负责人姓名
     */
    @Length(max = 200, message = "法定代表人或单位负责人姓名长度超过200")
    private String nmOfLglPrsn;

    /**
     * 法定代表人或单位负责人身份证件号
     */
    @Length(max = 35, message = "法定代表人或单位负责人身份证件号长度超过35")
    private String idOfLglPrsn;


    /************************--SelfEmployedPeople <SlfEplydPpl>市场主体类型：个体工商户**********************/

    /**
     * 字号名称
     */
    @Length(max = 256, message = "字号名称长度超过256")
    private String traNm;

    /**
     * 统一社会信用代码
     */
    //private String uniSocCdtCd;

    /**
     * 经营者姓名
     */
    @Length(max = 200, message = "经营者姓名长度超过200")
    private String nm;

    /**
     * 经营者证件号
     */
    @Length(max = 35, message = "经营者证件号长度超过35")
    private String managerId;


}
