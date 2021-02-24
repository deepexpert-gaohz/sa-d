package com.ideatech.ams.risk.highRisk.service;

import com.ideatech.ams.account.dto.AccountPublicInfo;

import com.ideatech.ams.risk.highRisk.dto.HighRiskApiDto;
import com.ideatech.ams.risk.highRisk.dto.HighRiskDataDto;
import com.ideatech.ams.risk.highRisk.dto.HighRiskListDto;
import com.ideatech.ams.risk.highRisk.entity.HighRisk;
import com.ideatech.ams.risk.highRisk.entity.HighRiskApi;
import com.ideatech.ams.risk.highRisk.entity.HighRiskData;
import com.ideatech.ams.risk.highRisk.entity.HighRiskRule;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.List;

public interface HighRiskService {

    IExcelExport generateAnnualCompanyReport();

    void saveHighRisk(List<HighRisk> list);

    //更新黑名单表
    void updateHighRisk(List<HighRisk> list);

    List<HighRisk> findAllHighRisk();

    List<HighRiskRule> findAllHighRiskRule();

    List<HighRiskData> findAllHighRiskData();

    void saveCon(HighRiskRule highRiskRule, String str);

    //规则重置
    void resetData();

    //查询高风险列表信息
    HighRiskDataDto queryHighRiskData(HighRiskDataDto highRiskDataDto);

    //查询高风险详情信息
    HighRiskListDto queryHighRiskList(HighRiskListDto highRiskListDto);

    /**
     * @Description 查看外部数据接口
     * @author yangwz
     * @date 2019-10-29 11:16
     * @params * @param null
     * @return
     */

    HighRiskApiDto queryRiskApi(HighRiskApiDto highRiskApiDto);
    /**
     * 查看企业高风险详情
     * @author yangwz
     * @date 2019-10-11 12:25
     * @return
     */
    HighRiskListDto findhighRiskskList(String depositorNo);


    void disdisHighRisk(String id, String accountNo, String handleType, String choo, String customerNo);

    /**
     * @author yangwz
     * @date 2019-10-10 17:09
     * 查询所有的账号类型
     * @return
     */
    List<AccountPublicInfo> findAcctType();

    /**
     * @author yangwz
     * @date 2019-10-14 17:11
     * 查询外部接口
     * @return
     */
    List<HighRiskApi> findDataApi();

    void exportRiskList(HighRiskListDto highRiskListDto, String rootPath, String StrDt) throws Exception;


    //void exportRiskData(HighRiskDataDto highRiskDataDto,String rootPath);

    /**
     * @Description 新增接口信息
     * @author yangwz
     * @date 2019-10-29 14:21
     * @params * @param null
     * @return
     */
    boolean addRiskApi(HighRiskApi highRiskApi);

    /**
     * @Description 编辑修改接口信息
     * @author yangwz
     * @date 2019-10-29 14:32
     * @params * @param null
     * @return
     */

    ResultDto saveRiskApi(HighRiskApi highRiskApi);

    HighRiskApi findApiById(Long id);

    void delRiskApiById(Long id);

    /**
     * @Description 高风险跑批
     * @author yangwz
     * @date 2019-11-21 10:28
     * @params * @param null
     * @return
     */
    void syncHighRiskData(String riskId);


    HighRiskData findById(Long id);
}
