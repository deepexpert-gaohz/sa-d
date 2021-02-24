package com.ideatech.ams.controller.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dto.AllBillsPublicSearchDTO;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.service.BusinessStatisticsService;
import com.ideatech.ams.account.vo.AccountStatisticsInfoVo;
import com.ideatech.common.dto.ResultDto;
import com.ideatech.common.dto.ResultDtoFactory;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.ExportExcel;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "/businessStatistics")
public class BusinessStatisticsController {

    @Autowired
    private BusinessStatisticsService businessStatisticsService;

    /**
     * 账户开变销统计异步查询
     */
    @GetMapping("/list")
    public TreeTable menuList(Long pid, String createddatestart, String createddateend,String acctType){
        Long organId = null;
        if (pid == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }

        return businessStatisticsService.query(pid, organId, createddatestart, createddateend,acctType);
    }

    /**
     * 账户开变销统计excel导出
     */
    @GetMapping("/export")
    public void exportKbxXLS(String createddatestart, String createddateend,String acctType, HttpServletResponse response) throws IOException {
        Long organId = SecurityUtils.getCurrentUser().getOrgId();
        IExcelExport iExcelExport = businessStatisticsService.exportKbxXLS(organId, createddatestart, createddateend,acctType);

        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode("账户开变销统计", "UTF-8") + ".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(), "yyyy-MM-dd", iExcelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 账户信息统计异步查询
     */
    @GetMapping("/info/list")
    public TreeTable menuInfoList(Long pid, AccountStatisticsInfoVo accountStatisticsInfoVo){
        Long organId = null;
        if (pid == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }

        return businessStatisticsService.query(pid, organId, accountStatisticsInfoVo);
    }

    /**
     * 账户信息统计excel导出
     */
    @GetMapping("/info/export")
    public void exportXLS(Long pid, AccountStatisticsInfoVo accountStatisticsInfoVo, HttpServletResponse response) throws IOException {
        Long organId = null;
        if (pid == null) {
            organId = SecurityUtils.getCurrentUser().getOrgId();
        }
        IExcelExport iExcelExport = businessStatisticsService.exportXLS(pid, organId, accountStatisticsInfoVo);

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode("账户信息统计", "UTF-8")+".xls");
        response.setContentType("application/octet-stream");
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        ExportExcel.export(response.getOutputStream(),"yyyy-MM-dd",iExcelExport);
        toClient.flush();
        response.getOutputStream().close();
    }

    /**
     * 获取账户开户报送统计查询列表中，存款人类别和账户性质多选框中的数据，并定义“人行上报数据统计”按钮的默认勾选数据
     */
    @RequestMapping("/open/getAcctTypeDepositorType")
    public JSONObject getAcctTypeDepositorType() {
        JSONArray depositorTypeJson =  businessStatisticsService.getDepositorTypeJson();//获取账户开户报送统计查询列表中，存款人类别多选框中的数据
        JSONArray acctTypeJson =  businessStatisticsService.getAcctTypeJson();//获取账户开户报送统计查询列表中，账户性质多选框中的数据
        businessStatisticsService.setDepositorTypeAcctTypeIsPbc(depositorTypeJson, acctTypeJson);//定义“人行上报数据统计”按钮的默认勾选数据
        JSONObject json = new JSONObject();
        json.put("depositorTypeList", depositorTypeJson);
        json.put("acctTypeList", acctTypeJson);
        return json;
    }

    /**
     * 账户开户报送 统计数据（开户成功的数据统计）（需要上报的数据）
     *
     * @param depositorTypeList 存款人类别（多选数据）
     * @param acctTypeList      账户性质（多选数据）
     * @param beginDate         开始时间
     * @param endDate           结束时间
     */
    @RequestMapping("/open/list")
    public JSONArray openStatisticsList(@RequestParam(value = "depositorTypeList[]") String[] depositorTypeList,
                                    @RequestParam(value = "acctTypeList[]") String[] acctTypeList,
                                    String beginDate, String endDate) {
        return businessStatisticsService.openStatisticsList(depositorTypeList, acctTypeList, beginDate, endDate);
    }

    /**
     * 账户开户报送 统计详情数据（开户成功的数据统计）（需要上报的数据）
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
    @RequestMapping("/open/detail/list")
    public TableResultResponse<AllBillsPublicSearchDTO> openStatisticsDetailList(String acctNo, String kernelOrgCode, String depositorName, String openAccountSiteType, String createdBy,
                                                                                 String depositorType, String acctType, String beginDate, String endDate,
                                                                                 String beginDateApply, String endDateApply, Pageable pageable) {
        Date beginDateApplyDate = null;
        Date endDateApplyDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (StringUtils.isNotBlank(beginDateApply)) {
                beginDateApplyDate = sdf.parse(beginDateApply);
            }
            if (StringUtils.isNotBlank(endDateApply)) {
                endDateApplyDate = sdf.parse(endDateApply);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return businessStatisticsService.openStatisticsDetailList(acctNo, kernelOrgCode, depositorName, openAccountSiteType, createdBy, depositorType, acctType, beginDate, endDate, beginDateApplyDate, endDateApplyDate, pageable);
    }

    /**
     * 账户开户报送统计导出excel（需要上报的数据）
     *
     * @param depositorTypeList 存款人类别（多选数据）
     * @param acctTypeList      账户性质（多选数据）
     * @param beginDate         开始时间
     * @param endDate           结束时间
     */
    @RequestMapping("/open/exportXLS")
    public void openStatisticsExportXLS(@RequestParam(value = "depositorTypeList") String[] depositorTypeList,
                                        @RequestParam(value = "acctTypeList") String[] acctTypeList,
                                        String beginDate, String endDate,
                                        HttpServletResponse response) {
        HSSFWorkbook wb = businessStatisticsService.exportExcel(depositorTypeList, acctTypeList, beginDate, endDate);
        response.setHeader("Content-Disposition", "attachment;filename=openAccount.xls");//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        try {
            OutputStream fileOut = response.getOutputStream();
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
