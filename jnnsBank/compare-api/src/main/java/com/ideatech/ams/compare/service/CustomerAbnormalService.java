package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.CompareResultDto;
import com.ideatech.ams.compare.dto.CompareResultSaicCheckDto;
import com.ideatech.ams.compare.dto.CustomerAbnormalDto;
import com.ideatech.ams.compare.dto.CustomerAbnormalSearchDto;
import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author jzh
 * @date 2019/6/13.
 */
public interface CustomerAbnormalService {

    /**
     * 多条件分页查询
     * @param customerAbnormalSearchDto
     * @param pageable
     * @return
     */
    TableResultResponse<CustomerAbnormalDto> page(CustomerAbnormalSearchDto customerAbnormalSearchDto, Pageable pageable);

    /**
     * 被动查询接口：通过企业名称（必填）、是否经营异常、是否严重违法、是否营业到期、工商异常状态（状态参数）、工商登记是否异动；
     * @param name 客户名称
     * @return
     */
    CustomerAbnormalDto findOneByCustomerName(String name);

    TableResultResponse<CustomerAbnormalDto> pageHistory(CustomerAbnormalSearchDto customerAbnormalSearchDto, Pageable pageable);

    /**
     * 主动推送接口：在每次异动结果生成的时候，推送增量异动结果；增量异动包括同一个企业中新增的风险点；
     * @param taskId
     * @return
     */
    List<CustomerAbnormalDto> list(Long taskId);

    /**
     * 根据搜索结果导出Excel
     * @param customerAbnormalSearchDto
     * @return
     */
    IExcelExport export(CustomerAbnormalSearchDto customerAbnormalSearchDto);

    /**
     * 获取基本信息sheet
     */
    HSSFWorkbook getBaseInfoWorkbook(SaicIdpInfo saicInfo, HSSFWorkbook wb);

    /**
     * 获取严重违法sheet
     */
    HSSFWorkbook getIllegalsWorkbook(SaicIdpInfo saicInfo, HSSFWorkbook wb);

    /**
     * 获取经营异常sheet
     */
    HSSFWorkbook getChangeMessWorkbook(SaicIdpInfo saicInfo, HSSFWorkbook wb);

    /**
     * 营业到期sheet
     */
    HSSFWorkbook getBusinessExpiresWorkbook(SaicIdpInfo saicInfo, CompareResultSaicCheckDto checkDto,HSSFWorkbook wb);

    /**
     * 工商状态sheet
     */
    HSSFWorkbook getSaicStateWorkbook(CompareResultSaicCheckDto checkDto,HSSFWorkbook wb);

    /**
     * 工商登记信息异动sheet
     */
    HSSFWorkbook getChangedWorkbook(CompareResultSaicCheckDto checkDto,CompareResultDto compareResultDto, HSSFWorkbook wb);

    JSONArray getChangedList(CompareResultDto compareResultDto);

    JSONObject getIndexAbnormalCounts(String organFullId);

    /**
     * 异动处理
     * @param ids          要处理的id集合
     * @param processState 处理后的处理状态
     */
    void process(String processState, Long[] ids);

    /**
     * 根据id获取客户异动结果
     */
    CustomerAbnormalDto findOneById(Long id);

    /**
     * 根据id修改是否发送短信
     */
    void chageMessage(String flag,Long id);
}
