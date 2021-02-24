package com.ideatech.ams.kyc.dto;


import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * IDP返回工商数据
 */

@Data
public class SaicIdpInfo extends BaseMaintainableDto {

    /**
     * ID
     */
    private Long id;

    /**
     * 地址
     */
    private String address;

    /**
     * 最后年检年度 YYYY
     */
    private String ancheyear;
    /**
     * 最后年检年日期
     */
    private String ancheyeardate;

    /**
     * 营业期限终止日期
     */
    private String enddate;

    /**
     * 法人
     */
    private String legalperson;

    /**
     * 法人类型可能的值为“法定代表人”，“经营者”，“负责人”
     */
    private String legalpersontype;

    /**
     * 核准日期
     */
    private String licensedate;

    /**
     * 名称
     */
    private String name;

    /**
     * 开业日期
     */
    private String opendate;

    /**
     * 省
     */
    private String province;

    /**
     * 注册资金
     */
    private String registfund;

    /**
     * 注册资金币种
     */
    private String registfundcurrency;

    /**
     * 注册号
     */
    private String registno;

    /**
     * 登记机关
     */
    private String registorgan;

    /**
     * 全国企业信用信息公示系统代码
     */
    private String saiccode;

    /**
     * 经营范围
     */
    private String scope;

    /**
     * 营业期限起始日期
     */
    private String startdate;

    /**
     * 经营状态
     */
    private String state;

    /**
     * 类型
     */
    private String type;

    /**
     * 数据更新时间
     */
    private Date updatetime;

    /**
     * 失败JSON
     */
    private String failjsonstr;

    /**
     * 内部工商号
     */
    private String idpno;

    /**
     * 消息公告
     */
    private String notice;

    /**
     * 注册地地区
     */
    private String regarea;

    /**
     * 注册地地区代码
     */
    private String regareacode;

    /**
     * 注册地城市
     */
    private String regcity;

    /**
     * 注册地省份
     */
    private String regprovince;

    /**
     * 注销或吊销日期
     */
    private String revokedate;

    /**
     * 工商注册号
     */
    private String unitycreditcode;

    private String idpId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 股东
     */
    private List<StockHolderDto> stockholders;

    /**
     * 主要成员
     */
    private List<EmployeeDto> employees;

    /**
     * 分支机构
     */
    private List<BranchDto> branchs;

    /**
     * 变更记录
     */
    private List<ChangeRecordDto> changes;

    /**
     * 经营异常信息
     */
    private List<ChangeMessDto> changemess;

    /**
     * 年报信息
     */
    private List<ReportDto> reports;

    /**
     * 企业对外投资情况
     */
    private List<EntInvDto> entInvList;

    /**
     * 法定代表人对外投资信息
     */
    private List<FrInvDto> frInvList;

    /**
     * 法定代表人其他公司任职信息
     */
    private List<FrPositionDto> frPositionList;

    /**
     * 失信被执行人信息
     */
    private List<PunishBreakDto> punishBreakList;

    /**
     * 行政处罚历史信息
     */
    private List<CaseInfoDto> caseInfoList;

    /**
     * 股权冻结历史信息
     */
    private List<SharesFrostDto> sharesFrostList;

    /**
     * 股权出质历史信息
     */
    private List<SharesImpawnDto> sharesImpawnList;

    /**
     * 动产抵押信息
     */
    private List<MorDetailDto> morDetailList;

    /**
     * 动产抵押物信息
     */
    private List<MorGuaInfoDto> morGuaInfoList;

    /**
     * 清算信息
     */
    private List<LiquidationDto> liquidationList;


    /**
     * 严重违法失信企业名单
     */
    private List<IllegalDto> illegals;

    /**
     * 欠贷信息
     */
    private List<AliDebtDto> aliDebtList;

    /**
     * 董事
     */
    private List<DirectorDto> directorList;

    /**
     * 监事
     */
    private List<SuperviseDto> superviseList;

    /**
     * 高管
     */
    private List<ManagementDto> managementList;

    /**
     * 受益人
     */
    private List<BeneficiaryDto> beneficiaryList;

    /**
     * 股权结构
     */
    private List<EquityShareDto> equityShareList;

    /**
     * 基本户履历
     */
    private List<BaseAccountDto> baseAccountList;
}
