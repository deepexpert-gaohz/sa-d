package com.ideatech.ams.mivs.dto.rrd;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/7/22.
 *
 * 企业照面信息部分。根据企业/个体户核查类型，此部分或个体户照面信息部分在每个分片报文中均出现
 */

@Data
public class BasicInformationOfEnterpriseDto {

    private Long id;
    private Long registerInformationLogId;

    /**
     * 企业名称
     * [1..1]	Max100Text
     */
    private String entNm;

    /**
     * 统一社会信用代码
     * [1..1]	Max18Text
     * 企业客户的统一社会信用代码
     * 禁止中文
     */
    private String uniSocCdtCd;

    /**
     * 市场主体类型
     * [0..1]	Max128Text
     */
    private String coTp;

    /**
     * 住所
     * [0..1]	Max512Text
     * 常驻代表机构-驻在场所，营业单位-地址
     */
    private String dom;

    /**
     * 注册资本(金)
     * [0..1]	ActiveCurrencyAndAmount
     * 表示货币符号和金额，其中金额的整数部分最多16位数字，小数部分固定2位数字。 注：不带正负（即+-）号。
     * 例如：<Amt Ccy="CNY">
     * 2784245.00</Amt>
     * 例如一元只能为1.00，不能为1或者1.0，金额第一位非零数字前禁止补零（例如一元只能为1.00，不能为01.00或者前补更多0）。
     * 注：货币符号采用《ISO-4217：Codes for the representation of currencies and funds》标准，人民币的货币符号为“CNY”，本标准中一般为CNY。
     */
    private String regCptl;

    /**
     * 成立日期
     * [0..1]	ISODate
     */
    private String dtEst;

    /**
     * 经营期限自
     * [0..1]	ISODate
     */
    private String opPrdFrom;

    /**
     *  经营期限至
     * [0..1]	ISODate
     */
    private String opPrdTo;

    /**
     * 登记状态
     * [0..1]	Max128Text
     */
    private String regSts;

    /**
     * 法定代表人或单位负责人姓名
     * [0..1]	Max200Text
     */
    private String nmOfLglPrsn;

    /**
     * 登记机关
     * [0..1]	Max128Text
     */
    private String regAuth;

    /**
     * 经营范围
     * [0..1]	Max3000Text
     */
    private String bizScp;

    /**
     * 核准日期
     * [0..1]	ISODate
     *
     */
    private String dtAppr;
}
