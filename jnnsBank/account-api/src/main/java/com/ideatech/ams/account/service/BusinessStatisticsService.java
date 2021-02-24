package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AllBillsPublicSearchDTO;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.vo.AccountStatisticsInfoVo;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BusinessStatisticsService {

    List<Map<String, Object>> query(String createddatestart, String createddateend);

    /**
     * 开变销统计异步请求
     * @param nodeId
     * @param organId
     * @param createddatestart
     * @param createddateend
     * @return
     */
    TreeTable query(Long nodeId, Long organId, String createddatestart, String createddateend,String acctType);


    TreeTable query(Long nodeId, Long organId, AccountStatisticsInfoVo accountStatisticsInfoVo);

    /**
     * 账户开变销统计Excel导出
     */
    IExcelExport exportKbxXLS(Long organId, String createddatestart, String createddateend,String acctType);

    /**
     * 账户信息统计Excel导出
     *
     * @param pid
     * @param organId
     * @param accountStatisticsInfoVo
     * @return
     */
    IExcelExport exportXLS(Long pid, Long organId, AccountStatisticsInfoVo accountStatisticsInfoVo);

    /**
     * 获取账户开户报送统计查询列表中，存款人类别多选框中的数据
     */
    JSONArray getDepositorTypeJson();

    /**
     * 获取账户开户报送统计查询列表中，账户性质多选框中的数据
     */
    JSONArray getAcctTypeJson();

    /**
     * 定义“人行上报数据统计”按钮的默认勾选数据
     */
    void setDepositorTypeAcctTypeIsPbc(JSONArray depositorTypeJson, JSONArray acctTypeJson);

    /**
     * 开户成功的数据统计（需要上报的数据）
     *
     * @param depositorTypeList 存款人类别（多选数据）
     * @param acctTypeList      账户性质（多选数据）
     * @param beginDate         开始时间
     * @param endDate           结束时间
     */
    JSONArray openStatisticsList(String[] depositorTypeList, String[] acctTypeList, String beginDate, String endDate);

    /**
     * 开户成功的数据统计详情（需要上报的数据）
     *
     * @param acctNo              账号
     * @param kernelOrgCode       网点机构号
     * @param depositorName       存款人名称
     * @param openAccountSiteType 本异地标识
     * @param createdBy           申请人
     * @param depositorType       账户性质
     * @param acctType            存款人类别
     * @param beginDate           上报开始时间
     * @param endDate             上报结束时间
     * @param beginDateApply      申请开始时间
     * @param endDateApply        申请结束时间
     */
    TableResultResponse<AllBillsPublicSearchDTO> openStatisticsDetailList(String acctNo, String kernelOrgCode, String depositorName, String openAccountSiteType, String createdBy,
                                                                          String depositorType, String acctType, String beginDate, String endDate,
                                                                          Date beginDateApply, Date endDateApply, Pageable pageable);

    /**
     * 开户成功的数据统计导出excel（需要上报的数据）
     *
     * @param depositorTypeList 存款人类别（多选数据）
     * @param acctTypeList      账户性质（多选数据）
     * @param beginDate         开始时间
     * @param endDate           结束时间
     */
    HSSFWorkbook exportExcel(String[] depositorTypeList, String[] acctTypeList, String beginDate, String endDate);
}
