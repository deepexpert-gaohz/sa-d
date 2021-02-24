package com.ideatech.ams.account.service;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dto.report.dto.ReportDataDto;
import com.ideatech.ams.account.dto.report.service.ReportDataService;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.common.enums.DepositorType;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class ReportDataServiceImpl implements ReportDataService {
    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Override
    public List<ReportDataDto> createAqmatReports(String currentDateStr) throws ParseException {
        List<ReportDataDto> reportDataList = new ArrayList();
        Date currentDate = null;
        String organFullId = SecurityUtils.getCurrentOrgFullId();

        if(StringUtils.isBlank(currentDateStr)) { //当天日期
            currentDate = new Date();
        } else {
            currentDate = org.apache.commons.lang.time.DateUtils.parseDate(currentDateStr, new String[]{"yyyy-MM"});
        }

        //账户性质赋值
        List<CompanyAcctType> jibenAcctBigType = new ArrayList<>();
        jibenAcctBigType.add(CompanyAcctType.jiben);
        List<CompanyAcctType> yibanAcctBigType = new ArrayList<>();
        yibanAcctBigType.add(CompanyAcctType.yiban);
        List<CompanyAcctType> specialAcctBigType = new ArrayList<>();
        specialAcctBigType.add(CompanyAcctType.yusuan);
        specialAcctBigType.add(CompanyAcctType.feiyusuan);
        specialAcctBigType.add(CompanyAcctType.teshu);
        List<CompanyAcctType> linshiAcctBigType = new ArrayList<>();
        linshiAcctBigType.add(CompanyAcctType.linshi);
        linshiAcctBigType.add(CompanyAcctType.feilinshi);

        //人行审核状态赋值
        List<CompanyAmsCheckStatus> amsCheckStatuses = new ArrayList<>();
        amsCheckStatuses.add(CompanyAmsCheckStatus.CheckPass);
        amsCheckStatuses.add(CompanyAmsCheckStatus.NoCheck);

        List<String> depositorTypes = null;

        for(int i = 0; i <= 3; i++) {
            depositorTypes = new ArrayList<>();
            if(i == 0) {
                depositorTypes.add("01");
            } else if(i == 1) {
                depositorTypes.add("02");
            } else if(i == 2) {
                depositorTypes.add("13");
                depositorTypes.add("14");
            } else if(i == 3) {
                depositorTypes.add("01");
                depositorTypes.add("02");
                depositorTypes.add("13");
                depositorTypes.add("14");
            }

            reportDataList.add(setReportDataDto(depositorTypes, organFullId, jibenAcctBigType, yibanAcctBigType, linshiAcctBigType,  specialAcctBigType, currentDate, amsCheckStatuses));
        }

        return reportDataList;
    }

    @Override
    public HSSFWorkbook exportXLS(String currentDateStr) throws ParseException {
        HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象

        HSSFFont font1 = wb.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 11);//字号
        font1.setBold(true);
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 11);//字号

        //文本格式
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));//设置格式为文本格式

        //表头样式
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font1);
        style1.setWrapText(true);//自动换行
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //表格内容样式
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font2);
        style2.setWrapText(true);//自动换行
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //左右居中

        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFont(font1);
        style3.setWrapText(true);//自动换行

        //表格内容样式
        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFont(font2);
        style4.setWrapText(true);//自动换行

        HSSFSheet sheet = wb.createSheet("企业银行账户数量监测分析表");//创建Excel工作表对象

        //设置列宽
        for (int i = 0; i <= 51; i++) {
            sheet.setColumnWidth(i, (short) (256 * 13));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        //设置表头
        HSSFRow row0 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        row0.setHeight((short) (30 * 20));//设置行高
        HSSFCell cell = row0.createCell(0);
        cell.setCellStyle(style1); //设置Excel指定单元格的样式
        cell.setCellValue("企业银行账户数量监测分析表"); //设置Excel指定单元格的值

        HSSFRow row1 = sheet.createRow((short) 1); //创建Excel工作表的第二行
        row1.setHeight((short) (30 * 20));//设置行高
        setCell(row1, 0, style3, "填制单位:");
        setCell(row1, 50, style3, "单位/户");

        HSSFRow row2 = sheet.createRow((short) 2); //创建Excel工作表的第三行
        row2.setHeight((short) (30 * 20));//设置行高
        setCell(row2, 0, style1, "存款人类别");
        setCell(row2, 1, style1, "基本存款账户");
        setCell(row2, 11, style1, "一般存款账户");
        setCell(row2, 21, style1, "专用存款账户");
        setCell(row2, 31, style1, "临时存款账户");
        setCell(row2, 41, style1, "合计");

        HSSFRow row3 = sheet.createRow((short) 3); //创建Excel工作表的第四行
        row3.setHeight((short) (30 * 20));//设置行高
        for(int i = 0; i < 10; i++) {
            int j = 1 + i * 5;  //0-1 1-6 2-11
            if(i % 2 == 0) {
                setCell(row3, j, style1, "本月账户开立情况");
            } else {
                setCell(row3, j, style1, "存量账户情况");
            }
        }

        HSSFRow row4 = sheet.createRow((short) 4); //创建Excel工作表的第五行
        row4.setHeight((short) (30 * 20));//设置行高
        for(int i = 0; i < 10; i++) {
            int j = 1 + i * 5;   //0-1 1-6 2-11
            if(i % 2 == 0) {
                setCell(row4, j, style1, "开户数");
                setCell(row4, ++j, style1, "上年开户数");
                setCell(row4, ++j, style1, "上月开户数");
            } else {
                setCell(row4, j, style1, "存量数");
                setCell(row4, ++j, style1, "上年存量数");
                setCell(row4, ++j, style1, "上月存量数");
            }
            setCell(row4, ++j, style1, "同比变化率");
            setCell(row4, ++j, style1, "环比变化率");
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$AY$1", "$A$2:$AC$2", "$AD$2:$AE$2", "$A$3:$A$5", "$B$3:$K$3", "$L$3:$U$3",
                "$V$3:$AE$3", "$AF$3:$AO$3", "$AP$3:$AY$3", "$B$4:$F$4", "$G$4:$K$4", "$L$4:$P$4", "$Q$4:$U$4", "$V$4:$Z$4",
                "$AA$4:$AE$4", "$AF$4:$AJ$4", "$AK$4:$AO$4", "$AP$4:$AT$4", "$AU$4:$AY$4", "$A$12:$N$12"};
        for (int i = 0; i < mergedRegionArr.length; i++) {
            CellRangeAddress cra = CellRangeAddress.valueOf(mergedRegionArr[i]);
            sheet.addMergedRegion(cra);
        }

        int j = 0;
        int i = 5;
        List<ReportDataDto> aqmatReports = createAqmatReports(currentDateStr);
        if(CollectionUtils.isNotEmpty(aqmatReports)) {
            for (ReportDataDto dto : aqmatReports) {
                HSSFRow row = sheet.createRow((short) i++); //创建Excel工作表的第五行
                j = 0;
                setCell(row, j++, style2, dto.getDepositorType());
                setCell(row, j++, style2, dto.getBaseAcct());
                setCell(row, j++, style2, dto.getBaseLYAcct());
                setCell(row, j++, style2, dto.getBaseLMAcct());
                setCell(row, j++, style2, dto.getBaseYToY());
                setCell(row, j++, style2, dto.getBaseMToM());
                setCell(row, j++, style2, dto.getBaseStorageCount());
                setCell(row, j++, style2, dto.getBaseLYStorageCount());
                setCell(row, j++, style2, dto.getBaseLMStorageCount());
                setCell(row, j++, style2, dto.getBaseStorageYToY());
                setCell(row, j++, style2, dto.getBaseStorageMToM());

                setCell(row, j++, style2, dto.getGeneralAcct());
                setCell(row, j++, style2, dto.getGeneralLYAcct());
                setCell(row, j++, style2, dto.getGeneralLMAcct());
                setCell(row, j++, style2, dto.getGeneralYToY());
                setCell(row, j++, style2, dto.getGeneralMToM());
                setCell(row, j++, style2, dto.getGeneralStorageCount());
                setCell(row, j++, style2, dto.getGeneralLYStorageCount());
                setCell(row, j++, style2, dto.getGeneralLMStorageCount());
                setCell(row, j++, style2, dto.getGeneralStorageYToY());
                setCell(row, j++, style2, dto.getGeneralStorageMToM());

                setCell(row, j++, style2, dto.getSpecialAcct());
                setCell(row, j++, style2, dto.getSpecialLYAcct());
                setCell(row, j++, style2, dto.getSpecialLMAcct());
                setCell(row, j++, style2, dto.getSpecialYToY());
                setCell(row, j++, style2, dto.getSpecialMToM());
                setCell(row, j++, style2, dto.getSpecialStorageCount());
                setCell(row, j++, style2, dto.getSpecialLYStorageCount());
                setCell(row, j++, style2, dto.getSpecialLMStorageCount());
                setCell(row, j++, style2, dto.getSpecialStorageYToY());
                setCell(row, j++, style2, dto.getSpecialStorageMToM());

                setCell(row, j++, style2, dto.getProvisionalAcct());
                setCell(row, j++, style2, dto.getProvisionalLYAcct());
                setCell(row, j++, style2, dto.getProvisionalLMAcct());
                setCell(row, j++, style2, dto.getProvisionalYToY());
                setCell(row, j++, style2, dto.getProvisionalMToM());
                setCell(row, j++, style2, dto.getProvisionalStorageCount());
                setCell(row, j++, style2, dto.getProvisionalLYStorageCount());
                setCell(row, j++, style2, dto.getProvisionalLMStorageCount());
                setCell(row, j++, style2, dto.getProvisionalStorageYToY());
                setCell(row, j++, style2, dto.getProvisionalStorageMToM());

                setCell(row, j++, style2, dto.getTotalAcct());
                setCell(row, j++, style2, dto.getTotalLYAcct());
                setCell(row, j++, style2, dto.getTotalLMAcct());
                setCell(row, j++, style2, dto.getTotalYToY());
                setCell(row, j++, style2, dto.getTotalMToM());
                setCell(row, j++, style2, dto.getTotalStorageCount());
                setCell(row, j++, style2, dto.getTotalLYStorageCount());
                setCell(row, j++, style2, dto.getTotalLMStorageCount());
                setCell(row, j++, style2, dto.getTotalStorageYToY());
                setCell(row, j++, style2, dto.getTotalStorageMToM());
            }
        }

        //设置表尾说明
        HSSFRow row11 = sheet.createRow((short) 11); //创建Excel工作表的第一行
        row11.setHeight((short) (30 * 30));//设置行高
        HSSFCell cell1 = row11.createCell(0);
        cell1.setCellStyle(style4); //设置Excel指定单元格的样式
        cell1.setCellValue("报送说明:" + "\r\n" +
                "1.银行应该按当月本统计表报送至所在地人民银行分支机构支付结算部门。数据报送按照属地原则执行， 由银行当地管辖行向所属人民银行分支机构报送，不纳入其上级管辖行重复报送。" + "\r\n" +
                "2.人民银行上海总部金融服务一部，各分行、营业管理部、省会(首府)城市中心支行、深圳市中心支行支付结算处应按月 汇总合并辖区内银行统计表，并发送至总行杨青业务网邮箱。"); //设置Excel指定单元格的值

        return wb;
    }

    private void setCell(HSSFRow row, int column, HSSFCellStyle style, Object value) {
        HSSFCell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(value == null ? "" : String.valueOf(value)));
    }

    private ReportDataDto setReportDataDto(List<String> depositorTypes, String organFullId, List<CompanyAcctType> jibenAcctBigType, List<CompanyAcctType> yibanAcctBigType, List<CompanyAcctType> linshiAcctBigType, List<CompanyAcctType> specialAcctBigType, Date currentDate, List<CompanyAmsCheckStatus> amsCheckStatuses) {
        ReportDataDto reportDataDto = new ReportDataDto();

        String depositorTypeName = DepositorType.getDisplayName(depositorTypes.get(0));
        if(StringUtils.indexOf(depositorTypeName, "个体工商户") != -1) {
            reportDataDto.setDepositorType("个体工商户");
        } else if(depositorTypes.size() == 4) {
            reportDataDto.setDepositorType("合计");
        } else {
            reportDataDto.setDepositorType(depositorTypeName);
        }

        //企业法人基本户本月开户账户
        long type_jibenOpenCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "");

        //企业法人基本户上一年开户账户
        long type_jibenOpenLastYearCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "");

        //企业法人基本户上月开户账户
        long type_jibenOpenLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "");

        //企业法人基本户去年的当月开户账户
        long type_jibenOpenLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "");

        reportDataDto.setBaseAcct(String.valueOf(type_jibenOpenCurrentMonthCount));
        reportDataDto.setBaseLMAcct(String.valueOf(type_jibenOpenLastMonthCount));
        reportDataDto.setBaseLYAcct(String.valueOf(type_jibenOpenLastYearCurrentMonthCount));
        reportDataDto.setBaseYToY(getRateString(type_jibenOpenCurrentMonthCount, type_jibenOpenLastYearCount));
        reportDataDto.setBaseMToM(getRateString(type_jibenOpenCurrentMonthCount, type_jibenOpenLastMonthCount));

        //企业法人基本户本月存量账户
        long type_jibenStockCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "stock");

        //企业法人基本户上一年存量账户
        long type_jibenStockLastYearCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "stock");

        //企业法人基本户上月存量账户
        long type_jibenStockLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "stock");

        //企业法人基本户上一年当月的存量账户
        long type_jibenStockLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, jibenAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "stock");

        reportDataDto.setBaseStorageCount(String.valueOf(type_jibenStockCurrentMonthCount));
        reportDataDto.setBaseLMStorageCount(String.valueOf(type_jibenStockLastMonthCount));
        reportDataDto.setBaseLYStorageCount(String.valueOf(type_jibenStockLastYearCurrentMonthCount));
        reportDataDto.setBaseStorageYToY(getRateString(type_jibenStockCurrentMonthCount, type_jibenStockLastYearCount));
        reportDataDto.setBaseStorageMToM(getRateString(type_jibenStockCurrentMonthCount, type_jibenStockLastMonthCount));


        //企业法人一般户本月开户账户
        long type_yibanOpenCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "");

        //企业法人一般户上一年开户账户
        long type_yibanOpenLastYearCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "");

        //企业法人一般户上月开户账户
        long type_yibanOpenLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "");

        //企业法人一般户上一年当月的开户账户
        long type_yibanOpenLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "");

        reportDataDto.setGeneralAcct(String.valueOf(type_yibanOpenCurrentMonthCount));
        reportDataDto.setGeneralLMAcct(String.valueOf(type_yibanOpenLastMonthCount));
        reportDataDto.setGeneralLYAcct(String.valueOf(type_yibanOpenLastYearCurrentMonthCount));
        reportDataDto.setGeneralYToY(getRateString(type_yibanOpenCurrentMonthCount, type_yibanOpenLastYearCount));
        reportDataDto.setGeneralMToM(getRateString(type_yibanOpenCurrentMonthCount, type_yibanOpenLastMonthCount));

        //企业法人一般户本月存量账户
        long type_yibanStockCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "stock");

        //企业法人一般户上一年存量账户
        long type_yibanStockLastYearCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "stock");

        //企业法人一般户上月存量账户
        long type_yibanStockLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "stock");

        //企业法人一般户上一年当月的存量账户
        long type_yibanStockLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, yibanAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "stock");

        reportDataDto.setGeneralStorageCount(String.valueOf(type_yibanStockCurrentMonthCount));
        reportDataDto.setGeneralLMStorageCount(String.valueOf(type_yibanStockLastMonthCount));
        reportDataDto.setGeneralLYStorageCount(String.valueOf(type_yibanStockLastYearCurrentMonthCount));
        reportDataDto.setGeneralStorageYToY(getRateString(type_yibanStockCurrentMonthCount, type_yibanStockLastYearCount));
        reportDataDto.setGeneralStorageMToM(getRateString(type_yibanStockCurrentMonthCount, type_yibanStockLastMonthCount));


        //企业法人专用户本月开户账户
        long type_specialOpenCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "");

        //企业法人专用户上一年开户账户
        long type_specialOpenLastYearCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear" ), "");

        //企业法人专用户上月开户账户
        long type_specialOpenLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "");

        //企业法人专用户上一年当月的开户账户
        long type_specialOpenLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth" ), "");

        reportDataDto.setSpecialAcct(String.valueOf(type_specialOpenCurrentMonthCount));
        reportDataDto.setSpecialLMAcct(String.valueOf(type_specialOpenLastMonthCount));
        reportDataDto.setSpecialLYAcct(String.valueOf(type_specialOpenLastYearCurrentMonthCount));
        reportDataDto.setSpecialYToY(getRateString(type_specialOpenCurrentMonthCount, type_specialOpenLastYearCount));
        reportDataDto.setSpecialMToM(getRateString(type_specialOpenCurrentMonthCount, type_specialOpenLastMonthCount));

        //企业法人专用户本月存量账户
        long type_specialStockCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "stock");

        //企业法人专用户上一年存量账户
        long type_specialStockLastYearCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "stock");

        //企业法人专用户上月存量账户
        long type_specialStockLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "stock");

        //企业法人专用户上一年当月的存量账户
        long type_specialStockLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, specialAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "stock");


        reportDataDto.setSpecialStorageCount(String.valueOf(type_specialStockCurrentMonthCount));
        reportDataDto.setSpecialLMStorageCount(String.valueOf(type_specialStockLastMonthCount));
        reportDataDto.setSpecialLYStorageCount(String.valueOf(type_specialStockLastYearCurrentMonthCount));
        reportDataDto.setSpecialStorageYToY(getRateString(type_specialStockCurrentMonthCount, type_specialStockLastYearCount));
        reportDataDto.setSpecialStorageMToM(getRateString(type_specialStockCurrentMonthCount, type_specialStockLastMonthCount));


        //企业法人临时户本月开户账户
        long type_linshiOpenCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "");

        //企业法人临时户上一年开户账户
        long type_linshiOpenLastYearCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "");

        //企业法人临时户上月开户账户
        long type_linshiOpenLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "");

        //企业法人临时户上一年当月的开户账户
        long type_linshiOpenLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "");

        reportDataDto.setProvisionalAcct(String.valueOf(type_linshiOpenCurrentMonthCount));
        reportDataDto.setProvisionalLMAcct(String.valueOf(type_linshiOpenLastMonthCount));
        reportDataDto.setProvisionalLYAcct(String.valueOf(type_linshiOpenLastYearCurrentMonthCount));
        reportDataDto.setProvisionalYToY(getRateString(type_linshiOpenCurrentMonthCount, type_linshiOpenLastYearCount));
        reportDataDto.setProvisionalMToM(getRateString(type_linshiOpenCurrentMonthCount, type_linshiOpenLastMonthCount));

        //企业法人临时户本月存量账户
        long type_linshiStockCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate,  "currnetMonth"), "stock");

        //企业法人临时户上一年存量账户
        long type_linshiStockLastYearCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYear"), "stock");

        //企业法人临时户上月存量账户
        long type_linshiStockLastMonthCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastMonth"), "stock");

        //企业法人临时户上一年当月的存量账户
        long type_linshiStockLastYearCurrentMonthCount = getAcctStockCount(getBillsList(
                organFullId, linshiAcctBigType, depositorTypes, CompanySyncStatus.tongBuChengGong,
                amsCheckStatuses, currentDate, "lastYearCurrnetMonth"), "stock");

        reportDataDto.setProvisionalStorageCount(String.valueOf(type_linshiStockCurrentMonthCount));
        reportDataDto.setProvisionalLMStorageCount(String.valueOf(type_linshiStockLastMonthCount));
        reportDataDto.setProvisionalLYStorageCount(String.valueOf(type_linshiStockLastYearCurrentMonthCount));
        reportDataDto.setProvisionalStorageYToY(getRateString(type_linshiStockCurrentMonthCount, type_linshiStockLastYearCount));
        reportDataDto.setProvisionalStorageMToM(getRateString(type_linshiStockCurrentMonthCount, type_linshiStockLastMonthCount));


        long totalAcct = type_jibenOpenCurrentMonthCount + type_yibanOpenCurrentMonthCount + type_specialOpenCurrentMonthCount + type_linshiOpenCurrentMonthCount;
        long totalLastYearAcct = type_jibenOpenLastYearCount + type_yibanOpenLastYearCount + type_specialOpenLastYearCount + +type_linshiOpenLastYearCount;
        long totalLastMonthAcct = type_jibenOpenLastMonthCount + type_yibanOpenLastMonthCount + type_specialOpenLastMonthCount + type_linshiOpenLastMonthCount;
        long totalLastYearCurrentMonthAcct = type_jibenOpenLastYearCurrentMonthCount + type_yibanOpenLastYearCurrentMonthCount + type_specialOpenLastYearCurrentMonthCount +
                type_linshiOpenLastYearCurrentMonthCount;
        reportDataDto.setTotalAcct(String.valueOf(totalAcct));
        reportDataDto.setTotalLMAcct(String.valueOf(totalLastMonthAcct));
        reportDataDto.setTotalLYAcct(String.valueOf(totalLastYearCurrentMonthAcct));
        reportDataDto.setTotalYToY(getRateString(totalAcct, totalLastYearAcct));
        reportDataDto.setTotalMToM(getRateString(totalAcct, totalLastMonthAcct));

        long totalStorageAcct = type_jibenStockCurrentMonthCount + type_yibanStockCurrentMonthCount + type_specialStockCurrentMonthCount + type_linshiStockCurrentMonthCount;
        long totalLastYearStorageAcct = type_jibenStockLastYearCount + type_yibanStockLastYearCount + type_specialStockLastYearCount + +type_linshiStockLastYearCount;
        long totalLastMonthStorageAcct = type_jibenStockLastMonthCount + type_yibanStockLastMonthCount + type_specialStockLastMonthCount + type_linshiStockLastMonthCount;
        long totalLastYearStorageCurrentMonthAcct = type_jibenStockLastYearCurrentMonthCount + type_yibanStockLastYearCurrentMonthCount + type_specialStockLastYearCurrentMonthCount +
                type_linshiStockLastYearCurrentMonthCount;

        reportDataDto.setTotalStorageCount(String.valueOf(totalStorageAcct));
        reportDataDto.setTotalLMStorageCount(String.valueOf(totalLastMonthStorageAcct));
        reportDataDto.setTotalLYStorageCount(String.valueOf(totalLastYearStorageCurrentMonthAcct));
        reportDataDto.setTotalStorageYToY(getRateString(totalStorageAcct, totalLastYearStorageAcct));
        reportDataDto.setTotalStorageMToM(getRateString(totalStorageAcct, totalLastMonthStorageAcct));

        return reportDataDto;
    }

    private List<AccountBillsAll> getBillsList(final String organFullId, final List<CompanyAcctType> acctBigTypes, final List<String> depositorTypes, final CompanySyncStatus pbcSyncStatus, final List<CompanyAmsCheckStatus> amsCheckStatuses, final Date currentDate, final String pbcSyncTimeType) {
        final String currentDateStr = DateUtils.DateToStr(currentDate, "yyyy-MM");
        //上个月
        final String lastMonthDateStr = DateUtils.DateToStr(DateUtil.subMonths(currentDate, 1), "yyyy-MM");
        //去年的今天
        final String lastYearDateStr = DateUtils.DateToStr(DateUtil.subMonths(currentDate, 12), "yyyy-MM");

        Specification<AccountBillsAll> specification = new Specification<AccountBillsAll>() {
            @Override
            public Predicate toPredicate(Root<AccountBillsAll> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                if(StringUtils.isNotBlank(organFullId)) {
                    expressions.add(cb.like(root.<String>get("organFullId"), organFullId + "%"));
                }
                if(pbcSyncStatus != null) {
                    expressions.add(cb.equal(root.<String>get("pbcSyncStatus"), pbcSyncStatus));
                }
                if(StringUtils.isNotBlank(pbcSyncTimeType)) {
                    if("currnetMonth".equals(pbcSyncTimeType)) {
                        expressions.add(cb.like(root.<String>get("pbcSyncTime"), currentDateStr + "%"));
                    } else if("lastMonth".equals(pbcSyncTimeType)) {
                        expressions.add(cb.like(root.<String>get("pbcSyncTime"), lastMonthDateStr + "%"));
                    } else if("lastYear".equals(pbcSyncTimeType)) {
                        expressions.add(cb.between(root.<String>get("pbcSyncTime"), "1970-01", lastYearDateStr));
                    } else if("lastYearCurrnetMonth".equals(pbcSyncTimeType)) {
                        expressions.add(cb.like(root.<String>get("pbcSyncTime"), lastYearDateStr + "%"));
                    }
                }

                if(acctBigTypes.size() > 0){
                    CriteriaBuilder.In<CompanyAcctType> in = cb.in(root.<CompanyAcctType>get("acctType"));
                    for (CompanyAcctType acctType : acctBigTypes) {
                        in.value(acctType);
                    }
                    expressions.add(cb.and(cb.and(in)));
                }

                if(depositorTypes.size() > 0){
                    CriteriaBuilder.In<String> in = cb.in(root.<String>get("depositorType"));
                    for (String depositorTypeStr : depositorTypes) {
                        in.value(depositorTypeStr);
                    }
                    expressions.add(cb.and(cb.and(in)));
                }

                if(amsCheckStatuses.size() > 0){
                    CriteriaBuilder.In<CompanyAmsCheckStatus> in = cb.in(root.<CompanyAmsCheckStatus>get("pbcCheckStatus"));
                    for (CompanyAmsCheckStatus pbcCheckStatus : amsCheckStatuses) {
                        in.value(pbcCheckStatus);
                    }
                    expressions.add(cb.and(cb.and(in)));
                }

                return predicate;
            }
        };

        List<AccountBillsAll> list = accountBillsAllDao.findAll(specification);
        return list;
    }

    /**
     * 占比格式化
     * @param acctCount1
     * @param acctCount2
     * @return
     */
    public static String getRateString(long acctCount1, long acctCount2){
        double data = acctCount2 == 0 ? 0.00 : (double) (acctCount1 - acctCount2) / acctCount2;

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(data * 100) + "%";
    }

    private long getAcctStockCount(List<AccountBillsAll> list, String type) {
        Boolean accountStausCheck = false;

        //比较器===流水信息去除重复账号
        Set<AccountBillsAll> treeSet = new TreeSet<>(new Comparator<AccountBillsAll>(){
            @Override
            public int compare(AccountBillsAll o1, AccountBillsAll o2) {
                int compareTo = o1.getAcctNo().compareTo(o2.getAcctNo());
                return compareTo;
            }
        });
        treeSet.addAll(list);

        int count = treeSet.size();

        for (Iterator iter = treeSet.iterator(); iter.hasNext(); ) {
            AccountBillsAll accountBillsAll = (AccountBillsAll) iter.next();
            AccountsAll accountsAll = accountsAllDao.findOne(accountBillsAll.getAccountId());

            if(accountsAll != null) {
                if("stock".equals(type)) {
                    accountStausCheck = (accountsAll.getAccountStatus() != AccountStatus.normal &&
                            accountsAll.getAccountStatus() != AccountStatus.suspend);
                } else {
                    accountStausCheck = (accountsAll.getAccountStatus() != AccountStatus.normal);
                }

                if(accountStausCheck) {
                    count--;
                }
            } else {
                count--;
            }
        }

        return count;
    }

}
