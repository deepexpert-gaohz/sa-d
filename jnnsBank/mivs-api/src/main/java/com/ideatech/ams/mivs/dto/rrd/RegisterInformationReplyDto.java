package com.ideatech.ams.mivs.dto.rrd;

import com.ideatech.ams.mivs.dto.*;
import lombok.Data;

import java.util.List;

/**
 * @author jzh
 * @date 2019/7/19.
 * 登记信息联网核查应答报文输出对象
 */

@Data
public class RegisterInformationReplyDto implements ReplyMsgDto {

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


    /***********************当“登记信息核查结果”为“MCHD”时填写start*******************************/
    /**
     * {Or
     * ------BasicInformationOfEnterprise
     * <BasInfOfEnt>	[1..1]
     * 企业照面信息部分。根据企业/个体户核查类型，此部分或个体户照面信息部分在每个分片报文中均出现
     */
    private BasicInformationOfEnterpriseDto basicInformationOfEnterpriseDto;

    /**
     * Or}
     * ------BasicInformationOfSelfEmployedPeople
     * <BasInfOfSlfEplydPpl>	[1..1]
     * 个体户照面信息部分。根据企业/个体户核查类型，此部分或企业照面信息部分在每个分片报文中均出现
     */
    private BasicInformationOfSelfEmployedPeopleDto basicInformationOfSelfEmployedPeopleDto;



    /**
     * ------CompanyShareholdersAndFundingInformation
     * <CoShrhdrFndInfo>	[0..n]
     * 企业股东及出资信息部分，属于企业登记信息核查内容。
     */
    private List<CompanyShareholdersAndFundingInformationDto> companyShareholdersAndFundingInformationDtoList;

    /**
     * ------DirectorSupervisorSeniorManagerInformation
     * <DirSupSrMgrInfo>	[0..n]
     * 董事监事及高管信息，属于企业登记信息核查内容。
     */
    private List<DirectorSupervisorSeniorManagerInformationDto> directorSupervisorSeniorManagerInformationDtoList;

    /**
     * ------ChangeInformation
     * <ChngInfo>	[0..n]
     * 历史变更信息，属于企业/个体户登记信息核查内容。
     */
    private List<ChangeInformationDto> changeInformationDtoList;


    /***
     * ------AbnormalBusinessInformation
     * <AbnmlBizInfo>	[0..n]
     * 异常经营信息，属于企业登记信息核查内容。
     */
    private List<AbnormalBusinessInformationDto> abnormalBusinessInformationDtoList;

    /**
     * ------IllegalAndDiscreditInformation
     * <IllDscrtInfo>	[0..n]
     * 严重违法失信信息，属于企业登记信息核查内容。
     */
    private List<IllegalAndDiscreditInformationDto> illegalAndDiscreditInformationDtoList;


    /**
     * ------LicenseNullify
     * <LicNull>	[0..n]
     * 营业执照作废声明，属于企业登记信息核查内容。
     */
    private List<LicenseNullifyDto> licenseNullifyDtoList;

    /***********************当“登记信息核查结果”为“MCHD”时填写end*********************************/



    /**********************************************成功 end***********************************************/



}
