package com.ideatech.ams.mivs.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author jzh
 * @date 2019/7/31.
 * 登记信息联网核查申请应答报文
 *
 * 统一社会信用代码uniSocCdtCd 复用
 * 经营者证件号id 改为 managerId
 */

@Entity
@Data
@Table(name = "mivs_register_info_log")
public class RegisterInformationLog extends ExtendEntity{

    /************************ 公共 **********************/

    /**
     * 代理人姓名
     */
    private String agtNm;

    /**
     * 代理人身份证件号码
     */
    private String agtId;

    /**
     * 操作员姓名
     */
    private String opNm;


    /************************--Enterprise <Ent>市场主体类型：企业**********************/
    /**
     * 企业名称
     */
    private String entNm;

    /**
     * 统一社会信用代码
     */
    private String uniSocCdtCd;

    /**
     * 法定代表人或单位负责人姓名
     */
    private String nmOfLglPrsn;

    /**
     * 法定代表人或单位负责人身份证件号
     */
    private String idOfLglPrsn;


    /************************--SelfEmployedPeople <SlfEplydPpl>市场主体类型：个体工商户**********************/

    /**
     * 字号名称
     */
    private String traNm;

    /**
     * 统一社会信用代码
     */
    //private String uniSocCdtCd;

    /**
     * 经营者姓名
     */
    private String nm;

    /**
     * 经营者证件号
     */
    private String managerId;

    /**********************************************成功 start***********************************************/

    /**
     * ----Result
     * 登记信息核查结果
     * <Rslt>	[1..1]	RegistrationVerificationResultCode(Max4Text)
     *
     * MCHD:要素核查匹配正确
     * WCON:要素核查条件中统一社会信用代码存在，企业名称/字号名称不一致
     * WLPN: 要素核查条件中统一社会信用代码存在，企业名称/字号名称一致，法定代表人/单位负责人姓名/经营者姓名不一致
     * WLPI: 要素核查条件中统一社会信用代码存在，企业名称/字号名称一致，法定代表人或单位负责人姓名/经营者姓名一致，法定代表人或单位负责人身份证件号码/经营者证件号不一致
     * NTFD:统一社会信用代码匹配失败
     */
    private String rslt;

    /**
     * ----DataResourceDate
     *      数据源日期
     *      <DataResrcDt>	[0..1]	ISODate
     */
    private String dataResrcDt;


    /**********************************************成功 end***********************************************/
}
