package com.ideatech.ams.risk.report.service;

import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.bill.AccountBillsAllDao;
import com.ideatech.ams.account.dto.report.dto.AcctNumChangeRepDto;
import com.ideatech.ams.account.dto.report.dto.ReportAccountDto;
import com.ideatech.ams.account.dto.report.dto.ReportDataDto;
import com.ideatech.ams.account.entity.AccountsAll;
import com.ideatech.ams.account.entity.bill.AccountBillsAll;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.CompanyAcctType;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.risk.Constant.OracleSQLConstant;
import com.ideatech.common.enums.DepositorTypeSzsm;
import com.ideatech.common.util.DateUtil;
import com.ideatech.common.util.DateUtils;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@Slf4j
public class RiskReportDataServiceImpl implements riskReportDataService {
    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private AccountBillsAllDao accountBillsAllDao;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Autowired
    EntityManager em;

    @Override
    public List<ReportDataDto> createAqmatReports(String currentDateStr) throws ParseException {
        List<ReportDataDto> reportDataList = new ArrayList();
        Date currentDate = null;
        String organFullId = SecurityUtils.getCurrentOrgFullId();

        if (StringUtils.isBlank(currentDateStr)) { //当天日期
            currentDate = new Date();
            currentDateStr = DateUtils.DateToStr(currentDate, "yyyy-MM");
        } else {
            currentDate = org.apache.commons.lang.time.DateUtils.parseDate(currentDateStr, new String[]{"yyyy-MM"});
        }
        //上个月
        String lastMonthDate = DateUtils.DateToStr(DateUtil.subMonths(currentDate, 1), "yyyy-MM");
        //去年的今天
        String lastYearDate = DateUtils.DateToStr(DateUtil.subMonths(currentDate, 12), "yyyy-MM");

        //账户性质赋值
        List<CompanyAcctType> acctBigType = new ArrayList<>();
        acctBigType.add(CompanyAcctType.jiben);
        acctBigType.add(CompanyAcctType.yiban);
        acctBigType.add(CompanyAcctType.yanzi);
        acctBigType.add(CompanyAcctType.feiyusuan);
        acctBigType.add(CompanyAcctType.linshi);
        acctBigType.add(CompanyAcctType.feilinshi);
    /*     specialAcctBigType.add(CompanyAcctType.teshu);
        List<CompanyAcctType> linshiAcctBigType = new ArrayList<>();
        linshiAcctBigType.add(CompanyAcctType.linshi);
        linshiAcctBigType.add(CompanyAcctType.feilinshi);*/

        //操作类型赋值
        List<BillType> billTypeOpen = new ArrayList<>();
        billTypeOpen.add(BillType.ACCT_OPEN);
        List<BillType> billTypes = new ArrayList<>();
        billTypes.add(BillType.ACCT_OPEN);
        billTypes.add(BillType.ACCT_CHANGE);
        billTypes.add(BillType.ACCT_REVOKE);

        //人行审核状态赋值
        List<CompanySyncStatus> amsCheckStatuses = new ArrayList<>();
        amsCheckStatuses.add(CompanySyncStatus.buTongBu);
        //amsCheckStatuses.add(CompanyAmsCheckStatus.NoCheck);

        DepositorTypeSzsm[]  depositorType= DepositorTypeSzsm .values ();
        List<String> depositorTypes = null;
        List<String> allDepositorTypes = new ArrayList<> (  );
        for (int i = 0; i< depositorType.length ; i++) {
            depositorTypes = new ArrayList<> (  );
            DepositorTypeSzsm depositorTypeSzsm= depositorType[i];
            String displayName = depositorTypeSzsm.getDisplayName ();
            depositorTypes.add ( displayName );
            allDepositorTypes.add ( displayName );
            reportDataList.add(setReportDataDto(depositorTypes, organFullId, acctBigType, currentDateStr, billTypeOpen, billTypes, amsCheckStatuses, lastMonthDate, lastYearDate,0));
        }
        //合计
        reportDataList.add(setReportDataDto(allDepositorTypes, organFullId, acctBigType, currentDateStr, billTypeOpen, billTypes, amsCheckStatuses, lastMonthDate, lastYearDate,allDepositorTypes.size ()));
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
        for (int i = 0; i <= 31; i++) {
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
        setCell(row1, 29, style3, "单位/户");

        HSSFRow row2 = sheet.createRow((short) 2); //创建Excel工作表的第三行
        row2.setHeight((short) (30 * 20));//设置行高
        setCell(row2, 0, style1, "存款人类别");
        setCell(row2, 1, style1, "基本存款账户");
        setCell(row2, 7, style1, "一般存款账户");
        setCell(row2, 13, style1, "专用存款账户");
        setCell(row2, 19, style1, "临时存款账户");
        setCell(row2, 25, style1, "合计");

        HSSFRow row3 = sheet.createRow((short) 3); //创建Excel工作表的第四行
        row3.setHeight((short) (30 * 20));//设置行高
        for (int i = 0; i < 10; i++) {
            int j = i + 1 + i * 2;
            if (i % 2 == 0) {
                setCell(row3, j, style1, "本月账户开立情况");
            } else {
                setCell(row3, j, style1, "存量账户情况");
            }
        }

        HSSFRow row4 = sheet.createRow((short) 4); //创建Excel工作表的第五行
        row4.setHeight((short) (30 * 20));//设置行高
        for (int i = 0; i < 10; i++) {
            int j = i + 1 + i * 2;
            if (j % 2 == 0) {
                setCell(row4, j, style1, "存量数");
            } else {
                setCell(row4, j, style1, "开户数");
            }
            setCell(row4, j + 1, style1, "同比变化率");
            setCell(row4, j + 2, style1, "环比变化率");
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$AE$1", "$A$2:$AC$2", "$AD$2:$AE$2", "$A$3:$A$5", "$B$3:$G$3", "$H$3:$M$3",
                "$N$3:$S$3", "$T$3:$Y$3", "$Z$3:$AE$3", "$B$4:$D$4", "$E$4:$G$4", "$H$4:$J$4", "$K$4:$M$4", "$N$4:$P$4",
                "$Q$4:$S$4", "$T$4:$V$4", "$W$4:$Y$4", "$Z$4:$AB$4", "$AC$4:$AE$4", "$A$12:$N$12"};
        for (int i = 0; i < mergedRegionArr.length; i++) {
            CellRangeAddress cra = CellRangeAddress.valueOf(mergedRegionArr[i]);
            sheet.addMergedRegion(cra);
        }

        int j = 0;
        int i = 5;
        List<ReportDataDto> aqmatReports = createAqmatReports(currentDateStr);
        if (CollectionUtils.isNotEmpty(aqmatReports)) {
            for (ReportDataDto dto : aqmatReports) {
                HSSFRow row = sheet.createRow((short) i++); //创建Excel工作表的第五行
                j = 0;
                setCell(row, j++, style2, dto.getDepositorType());
                setCell(row, j++, style2, dto.getBaseAcct());
                setCell(row, j++, style2, dto.getBaseYToY());
                setCell(row, j++, style2, dto.getBaseMToM());
                setCell(row, j++, style2, dto.getBaseStorageCount());
                setCell(row, j++, style2, dto.getBaseStorageYToY());
                setCell(row, j++, style2, dto.getBaseStorageMToM());

                setCell(row, j++, style2, dto.getGeneralAcct());
                setCell(row, j++, style2, dto.getGeneralYToY());
                setCell(row, j++, style2, dto.getGeneralMToM());
                setCell(row, j++, style2, dto.getGeneralStorageCount());
                setCell(row, j++, style2, dto.getGeneralStorageYToY());
                setCell(row, j++, style2, dto.getGeneralStorageMToM());

                setCell(row, j++, style2, dto.getSpecialAcct());
                setCell(row, j++, style2, dto.getSpecialYToY());
                setCell(row, j++, style2, dto.getSpecialMToM());
                setCell(row, j++, style2, dto.getSpecialStorageCount());
                setCell(row, j++, style2, dto.getSpecialStorageYToY());
                setCell(row, j++, style2, dto.getSpecialStorageMToM());

                setCell(row, j++, style2, dto.getProvisionalAcct());
                setCell(row, j++, style2, dto.getProvisionalYToY());
                setCell(row, j++, style2, dto.getProvisionalMToM());
                setCell(row, j++, style2, dto.getProvisionalStorageCount());
                setCell(row, j++, style2, dto.getProvisionalStorageYToY());
                setCell(row, j++, style2, dto.getProvisionalStorageMToM());

                setCell(row, j++, style2, dto.getTotalAcct());
                setCell(row, j++, style2, dto.getTotalYToY());
                setCell(row, j++, style2, dto.getTotalMToM());
                setCell(row, j++, style2, dto.getTotalStorageCount());
                setCell(row, j++, style2, dto.getTotalStorageYToY());
                setCell(row, j++, style2, dto.getTotalStorageMToM());
            }
        }

        //设置表尾说明
        HSSFRow row11 = sheet.createRow((short) 11); //创建Excel工作表的第一行
        row11.setHeight((short) (30 * 30));//设置行高
        HSSFCell cell1 = row11.createCell(0);
        cell1.setCellStyle(style4); //设置Excel指定单元格的样式
   /*     cell1.setCellValue("报送说明:" + "\r\n" +
                "1.银行应该按当月本统计表报送至所在地人民银行分支机构支付结算部门。数据报送按照属地原则执行， 由银行当地管辖行向所属人民银行分支机构报送，不纳入其上级管辖行重复报送。" + "\r\n" +
                "2.人民银行上海总部金融服务一部，各分行、营业管理部、省会(首府)城市中心支行、深圳市中心支行支付结算处应按月 汇总合并辖区内银行统计表，并发送至总行杨青业务网邮箱。"); //设置Excel指定单元格的值
*/
        return wb;
    }

    @Override
    public HSSFWorkbook exportAcctChangeXLS(String currentDateStr) throws ParseException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet名称
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //获取表数据，根据自己实际情况获取
        List<AcctNumChangeRepDto> acctNumChange = this.createAcctNumChange(currentDateStr);
        //新增数据行，并且设置单元格数据
        int rowNum = 2;
        //设置表头
        //表头样式
        HSSFFont font1 = workbook.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 11);//字号
        font1.setBold(true);
        HSSFCellStyle style1 = workbook.createCellStyle();
        style1.setFont(font1);
        style1.setWrapText(true);//自动换行
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFRow row0 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        row0.setHeight((short) (30 * 20));//设置行高
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellStyle(style1); //设置Excel指定单元格的样式
        cell0.setCellValue("账户数量变动表"); //设置Excel指定单元格的值

        String[] headers = {"机构", "总账户数量", "新增账户数量"};
        HSSFRow row = sheet.createRow((short)1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellStyle(style1);
            cell.setCellValue(text);
        }
        //合并单元格
        String[] mergedRegionArr = {"$A$1:$C$1"};
        for (int i = 0; i < mergedRegionArr.length; i++) {
            CellRangeAddress cra = CellRangeAddress.valueOf(mergedRegionArr[i]);
            sheet.addMergedRegion(cra);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //在表中存放查询到的数据放入对应的列
        for (AcctNumChangeRepDto changeRepDto : acctNumChange) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(changeRepDto.getOrg());
            row1.createCell(1).setCellValue(changeRepDto.getTotalData());
            row1.createCell(2).setCellValue(changeRepDto.getUpDateData());
            rowNum++;
        }
        //设置自动列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 13 / 10);
        }
        return workbook;
    }

    @Override
    public HSSFWorkbook exportfinancialLiquXLS(String year,String season) throws ParseException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet名称
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //获取表数据，根据自己实际情况获取
        List<ReportAccountDto> financialLiquReport = this.createFinancialLiquReport(null);
        //新增数据行，并且设置单元格数据
        int rowNum = 3;
        //文本格式
        HSSFCellStyle textStyle = workbook.createCellStyle();
        HSSFDataFormat format = workbook.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));//设置格式为文本格式
        for (int i = 0; i <= 31; i++) {
            sheet.setColumnWidth(i, (short) (256 * 13));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }
        //设置表头
        //表头样式
        HSSFFont font1 = workbook.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 11);//字号
        font1.setBold(true);

        HSSFCellStyle style1 = workbook.createCellStyle();
        style1.setFont(font1);
        style1.setWrapText(true);//自动换行
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFRow row0 = sheet.createRow((short) 0); //创建Excel工作表的第一行
        row0.setHeight((short) (30 * 20));//设置行高2019年
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellStyle(style1); //设置Excel指定单元格的样式
        cell0.setCellValue("账户资金流动情况统计表"); //设置Excel指定单元格的值

        String[] headers = {"填制单位:", year+"年"+season+"季度", "单位:万元"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow((short)1);
        //在excel表中添加表头
        setCell(row, 0, style1, headers[0]);
        setCell(row, 2, style1, headers[1]);
        setCell(row, 3, style1, headers[2]);


        HSSFRow row2 = sheet.createRow((short)2);
        String[] titles = {"企业类型", "期初余额", "流入","流出","期末余额"};
        for (int i = 0; i < titles.length; i++) {
            HSSFCell cell2 = row2.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(titles[i]);
            cell2.setCellValue(text);
        }

        //合并单元格
        String[] mergedRegionArr = {"$A$1:$E$1","$A$2:$B$2","$D$2:$E$2"};
        for (int i = 0; i < mergedRegionArr.length; i++) {
            CellRangeAddress cra = CellRangeAddress.valueOf(mergedRegionArr[i]);
            sheet.addMergedRegion(cra);
        }
        //在表中存放查询到的数据放入对应的列
        for (ReportAccountDto reportAccountDto  : financialLiquReport) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(reportAccountDto.getDepositorType());
            row1.createCell(1).setCellValue(reportAccountDto.getBeginPeriodData());
            row1.createCell(2).setCellValue(reportAccountDto.getInflowData());
            row1.createCell(3).setCellValue(reportAccountDto.getOutflowData());
            row1.createCell(4).setCellValue(reportAccountDto.getEndPeriodData());
            rowNum++;
        }
        //设置自动列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 13 / 10);
        }
        return workbook;
    }

    /**
     * 账户数量变动报表
     *
     * @param currentDateStr
     * @return
     * @author liuz 20191101
     */
    @Override
    public List<AcctNumChangeRepDto> createAcctNumChange(String currentDateStr) {
        String currentOrgFullId = SecurityUtils.getCurrentOrgFullId();
        List<AcctNumChangeRepDto> acctNumChangeRepDtos = new ArrayList<AcctNumChangeRepDto>();
        String totalSql = "SELECT\n" +
                "\tyd_bank_Name,\n" +
                "\tyd_organ_Full_Id,\n" +
                "\tcount( 1 ) \n" +
                "FROM\n" +
                "\tyd_accounts_all \n" +
                "WHERE 1=1 \n" +
                // "\tyd_acct_Type = 'jiben' \n" +
                //MysqlSQLConstant.getAccountChangeReportRule;
                "\tAND "+OracleSQLConstant.getAccountChangeReportRule+" <= '" + currentDateStr + "' \n" +
                "AND yd_organ_full_id like \'%"+currentOrgFullId+"%\'"+
                "GROUP BY\n" +
                "\tyd_bank_Name,\n" +
                "\tyd_organ_Full_Id";
        String upSql = "SELECT\n" +
                "\tyd_bank_Name,\n" +
                "\tyd_organ_Full_Id,\n" +
                "\tcount( 1 ) \n" +
                "FROM\n" +
                "\tyd_accounts_all \n" +
                "WHERE 1=1 \n" +
                //"\tyd_acct_Type = 'jiben' \n" +
                "\tAND "+OracleSQLConstant.getAccountChangeReportRule+" = '" + currentDateStr + "' \n" +
                "AND yd_organ_full_id like \'%"+currentOrgFullId+"%\'"+
                "GROUP BY\n" +
                "\t yd_bank_Name,\n" +
                "\t yd_organ_Full_Id";
        Query totalQuery = em.createNativeQuery(totalSql);
        Query upQuery = em.createNativeQuery(upSql);
        List<Object[]> totalList = totalQuery.getResultList();
        List<Object[]> upList = upQuery.getResultList();
        //把新增数据放在map便于查找,根据organ_Full_Id
        Map<String, Object[]> ma = new HashMap<String, Object[]>();
        for (Object[] obj : upList) {
            if (obj[1] != null) {
                //organfullid为key
                ma.put(obj[1].toString(), obj);
            }
        }
        for (int i = 0; i < totalList.size(); i++) {
            AcctNumChangeRepDto acctNumChangeRepDto = new AcctNumChangeRepDto();
            String fullId = totalList.get(i)[1] == null ? "" : totalList.get(i)[1].toString();
            String count = totalList.get(i)[2] == null ? "0" : totalList.get(i)[2].toString();
            acctNumChangeRepDto.setOrg(totalList.get(i)[0] == null ? "" : totalList.get(i)[0].toString());
            acctNumChangeRepDto.setTotalData(count);
            Object[] objects =ma.get(fullId);
            if(objects!=null){
                acctNumChangeRepDto.setUpDateData(objects[2] == null ? "0" : objects[2].toString());
            }else{
                acctNumChangeRepDto.setUpDateData("0");
            }
            acctNumChangeRepDtos.add(acctNumChangeRepDto);
        }
        return acctNumChangeRepDtos;
    }


    private void setCell(HSSFRow row, int column, HSSFCellStyle style, Object value) {
        HSSFCell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(value == null ? "" : String.valueOf(value)));
    }

    private ReportDataDto setReportDataDto(List<String> depositorTypes, String organFullId,  List<CompanyAcctType> acctBigType, String currentDateStr, List<BillType> billTypeOpen, List<BillType> billTypes, List<CompanySyncStatus> amsCheckStatuses, String lastMonthDate, String lastYearDate,int count) {
        ReportDataDto reportDataDto = new ReportDataDto();
        if (depositorTypes.size() == count) {
            reportDataDto.setDepositorType("合计");
        } else {
            String depositorTypeName = depositorTypes.get ( 0 );
            reportDataDto.setDepositorType(depositorTypeName);
        }
        //企业法人基本户本月开户账户
        long type_jibenOpenCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn (
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "");

        //企业法人基本户上一年开户账户
        long type_jibenOpenLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "");

        //企业法人基本户上月开户账户
        long type_jibenOpenLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "");

        reportDataDto.setBaseAcct(String.valueOf(type_jibenOpenCurrentMonthCount));
        reportDataDto.setBaseYToY(getRateString(type_jibenOpenCurrentMonthCount, type_jibenOpenLastYearCount));
        reportDataDto.setBaseMToM(getRateString(type_jibenOpenCurrentMonthCount, type_jibenOpenLastMonthCount));

        //企业法人基本户本月存量账户
        long type_jibenStockCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "stock");

        //企业法人基本户上一年存量账户
        long type_jibenStockLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "stock");

        //企业法人基本户上月存量账户
        long type_jibenStockLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "stock");

        reportDataDto.setBaseStorageCount(String.valueOf(type_jibenStockCurrentMonthCount));
        reportDataDto.setBaseStorageYToY(getRateString(type_jibenStockCurrentMonthCount, type_jibenStockLastYearCount));
        reportDataDto.setBaseStorageMToM(getRateString(type_jibenStockCurrentMonthCount, type_jibenStockLastMonthCount));


        //企业法人一般户本月开户账户
        long type_yibanOpenCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "");

        //企业法人一般户上一年开户账户
        long type_yibanOpenLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "");

        //企业法人一般户上月开户账户
        long type_yibanOpenLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "");

        reportDataDto.setGeneralAcct(String.valueOf(type_yibanOpenCurrentMonthCount));
        reportDataDto.setGeneralYToY(getRateString(type_yibanOpenCurrentMonthCount, type_yibanOpenLastYearCount));
        reportDataDto.setGeneralMToM(getRateString(type_yibanOpenCurrentMonthCount, type_yibanOpenLastMonthCount));

        //企业法人一般户本月存量账户
        long type_yibanStockCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "stock");

        //企业法人一般户上一年存量账户
        long type_yibanStockLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "stock");

        //企业法人一般户上月存量账户
        long type_yibanStockLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "stock");

        reportDataDto.setGeneralStorageCount(String.valueOf(type_yibanStockCurrentMonthCount));
        reportDataDto.setGeneralStorageYToY(getRateString(type_yibanStockCurrentMonthCount, type_yibanStockLastYearCount));
        reportDataDto.setGeneralStorageMToM(getRateString(type_yibanStockCurrentMonthCount, type_yibanStockLastMonthCount));


        //企业法人专用户本月开户账户
        long type_specialOpenCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "");

        //企业法人专用户上一年开户账户
        long type_specialOpenLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "");

        //企业法人专用户上月开户账户
        long type_specialOpenLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "");

        reportDataDto.setSpecialAcct(String.valueOf(type_specialOpenCurrentMonthCount));
        reportDataDto.setSpecialYToY(getRateString(type_specialOpenCurrentMonthCount, type_specialOpenLastYearCount));
        reportDataDto.setSpecialMToM(getRateString(type_specialOpenCurrentMonthCount, type_specialOpenLastMonthCount));

        //企业法人专用户本月存量账户
        long type_specialStockCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "stock");

        //企业法人专用户上一年存量账户
        long type_specialStockLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "stock");

        //企业法人专用户上月存量账户
        long type_specialStockLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "stock");

        reportDataDto.setSpecialStorageCount(String.valueOf(type_specialStockCurrentMonthCount));
        reportDataDto.setSpecialStorageYToY(getRateString(type_specialStockCurrentMonthCount, type_specialStockLastYearCount));
        reportDataDto.setSpecialStorageMToM(getRateString(type_specialStockCurrentMonthCount, type_specialStockLastMonthCount));


        //企业法人临时户本月开户账户
        long type_linshiOpenCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "");

        //企业法人临时户上一年开户账户
        long type_linshiOpenLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "");

        //企业法人临时户上月开户账户
        long type_linshiOpenLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "");

        reportDataDto.setProvisionalAcct(String.valueOf(type_linshiOpenCurrentMonthCount));
        reportDataDto.setProvisionalYToY(getRateString(type_linshiOpenCurrentMonthCount, type_linshiOpenLastYearCount));
        reportDataDto.setProvisionalMToM(getRateString(type_linshiOpenCurrentMonthCount, type_linshiOpenLastMonthCount));

        //企业法人临时户本月存量账户
        long type_linshiStockCurrentMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), currentDateStr, "stock");

        //企业法人临时户上一年存量账户
        long type_linshiStockLastYearCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastYearDate, "stock");

        //企业法人临时户上月存量账户
        long type_linshiStockLastMonthCount = getAcctStockCount(accountBillsAllDao.findByOrganFullIdStartingWithAndAcctTypeInAndDepositorTypeInAndPbcSyncStatusAndPbcCheckStatusInAndBillTypeIn(
                organFullId, acctBigType, depositorTypes, CompanySyncStatus.buTongBu,
                CompanyAmsCheckStatus.NoCheck,billTypes), lastMonthDate, "stock");

        reportDataDto.setProvisionalStorageCount(String.valueOf(type_linshiStockCurrentMonthCount));
        reportDataDto.setProvisionalStorageYToY(getRateString(type_linshiStockCurrentMonthCount, type_linshiStockLastYearCount));
        reportDataDto.setProvisionalStorageMToM(getRateString(type_linshiStockCurrentMonthCount, type_linshiStockLastMonthCount));


        long totalAcct = type_jibenOpenCurrentMonthCount + type_yibanOpenCurrentMonthCount + type_specialOpenCurrentMonthCount + type_linshiOpenCurrentMonthCount;
        long totalLastYearAcct = type_jibenOpenLastYearCount + type_yibanOpenLastYearCount + type_specialOpenLastYearCount + +type_linshiOpenLastYearCount;
        long totalLastMonthAcct = type_jibenOpenLastMonthCount + type_yibanOpenLastMonthCount + type_specialOpenLastMonthCount + type_linshiOpenLastMonthCount;
        reportDataDto.setTotalAcct(String.valueOf(totalAcct));
        reportDataDto.setTotalYToY(getRateString(totalAcct, totalLastYearAcct));
        reportDataDto.setTotalMToM(getRateString(totalAcct, totalLastMonthAcct));

        long totalStorageAcct = type_jibenStockCurrentMonthCount + type_yibanStockCurrentMonthCount + type_specialStockCurrentMonthCount + type_linshiStockCurrentMonthCount;
        long totalLastYearStorageAcct = type_jibenStockLastYearCount + type_yibanStockLastYearCount + type_specialStockLastYearCount + +type_linshiStockLastYearCount;
        long totalLastMonthStorageAcct = type_jibenStockLastMonthCount + type_yibanStockLastMonthCount + type_specialStockLastMonthCount + type_linshiStockLastMonthCount;

        reportDataDto.setTotalStorageCount(String.valueOf(totalStorageAcct));
        reportDataDto.setTotalStorageYToY(getRateString(totalStorageAcct, totalLastYearStorageAcct));
        reportDataDto.setTotalStorageMToM(getRateString(totalStorageAcct, totalLastMonthStorageAcct));

        return reportDataDto;
    }

    /**
     * 占比格式化
     *
     * @param acctCount1
     * @param acctCount2
     * @return
     */
    public static String getRateString(long acctCount1, long acctCount2) {
        double data = acctCount2 == 0 ? 0.00 : (double) (acctCount1 - acctCount2) / acctCount2;

        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(data * 100) + "%";
    }

    private long getAcctStockCount(List<AccountBillsAll> list, String dateStr, String type) {
        Boolean accountStausCheck = false;
        //比较器===流水信息去除重复账号
        Set<AccountBillsAll> treeSet = new TreeSet<>(new Comparator<AccountBillsAll>() {
            @Override
            public int compare(AccountBillsAll o1, AccountBillsAll o2) {
                int compareTo = o1.getAcctNo().compareTo(o2.getAcctNo());
                return compareTo;
            }
        });
        treeSet.addAll(list);
        int count = treeSet.size();
        //根据账户状态上报时间统计
        for (Iterator iter = treeSet.iterator(); iter.hasNext(); ) {
            AccountBillsAll accountBillsAll = (AccountBillsAll) iter.next();
            AccountsAll accountsAll = accountsAllDao.findOne(accountBillsAll.getAccountId());

            if (accountsAll != null) {
                if (StringUtils.isBlank(accountBillsAll.getBillDate ())) {
                    accountBillsAll.setPbcSyncTime(accountsAll.getAcctCreateDate());
                }
                if ("stock".equals(type)) {
                    accountStausCheck = (accountsAll.getAccountStatus() != AccountStatus.normal &&
                            accountsAll.getAccountStatus() != AccountStatus.suspend);
                } else {
                    accountStausCheck = (accountsAll.getAccountStatus() != AccountStatus.normal);
                }
                if ("stock".equals(type)) {  //存量数据统计这个月之前的所有账户数据
                    if(StringUtils.isNotEmpty ( accountBillsAll.getBillDate() )){
                        if (accountStausCheck || dateStr.compareTo(StringUtils.substringBeforeLast(accountBillsAll.getBillDate(), "-")) < 0) {
                            count--;
                        }
                    }

                } else {
                    if(StringUtils.isNotEmpty ( accountBillsAll.getBillDate() )) {
                        if (accountStausCheck || !dateStr.equals ( StringUtils.substringBeforeLast ( accountBillsAll.getBillDate (), "-" ) )) {
                            count--;
                        }
                    }
                }
            } else {
                count--;
            }
        }
        return count;
    }

    /**
     * 账户资金流动性监测表
     * @param currentDateStr
     * @return
     */
    @Override
    public List<ReportAccountDto> createFinancialLiquReport(String currentDateStr) {
        SecurityUtils.UserInfo user = SecurityUtils.getCurrentUser();
        String flowSql = "select t2.YD_DEPOSITOR_TYPE,\n" +
                "sum(case when t1.yd_cr_dr_maint_ind = 'C' then t1.yd_tran_amt else 0 end ) as inflow,\n" +
                "sum(case when t1.yd_cr_dr_maint_ind = 'D' then t1.yd_tran_amt else 0 end ) as outflow   \n" +
                "from yd_intf_ams_tran_hist t1\n" +
                "right join yd_customers_all t2 on t1.yd_client_no = t2.yd_customer_no\n" +
                "where t1.yd_TRAN_DATE>=\n" +
                " concat(date_format(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
                "interval QUARTER(CURDATE())*3-3 month),'%Y%m'),'01') and t1.yd_tran_date<=DATE_FORMAT(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
                "interval QUARTER(CURDATE())*3-1 month),'%Y%m%d')\n" +
                " and t2.YD_ORGAN_FULL_ID like '%"+user.getOrgFullId ()+"%' "+
                "group by t2.YD_DEPOSITOR_TYPE";
        String poriedSql = "select tab1.YD_DEPOSITOR_TYPE , tab2.begPoried,tab1.endPoried from (\n" +
                "select t2.YD_DEPOSITOR_TYPE,sum(t1.yd_actual_bal) as endPoried \n" +
                "from yd_intf_ams_tran_hist t1\n" +
                "left join yd_customers_all t2 on t1.yd_client_no = t2.YD_CUSTOMER_NO\n" +
                "where t1.yd_TRAN_DATE= \n" +
                "concat(date_format(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
                "interval QUARTER(CURDATE())*3-3 month),'%Y%m'),'01') and t2.YD_ORGAN_FULL_ID like '%"+user.getOrgFullId ()+"%' group by t2.YD_DEPOSITOR_TYPE \n" +
                ")tab1,\n" +
                "(select t2.YD_DEPOSITOR_TYPE,sum(t1.yd_actual_bal) as begPoried from yd_intf_ams_tran_hist t1\n" +
                "left join yd_customers_all t2 on t1.yd_client_no = t2.YD_CUSTOMER_NO\n" +
                "where t1.yd_TRAN_DATE= DATE_FORMAT(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM CURDATE()),1) + \n" +
                "interval QUARTER(CURDATE())*3-1 month),'%Y%m%d') and t2.YD_ORGAN_FULL_ID like '%"+user.getOrgFullId ()+"%' group by t2.YD_DEPOSITOR_TYPE) tab2\n" +
                "where tab1.YD_DEPOSITOR_TYPE = tab2.YD_DEPOSITOR_TYPE";
        Query flowQuery = em.createNativeQuery(flowSql);
        Query poriedQuery = em.createNativeQuery(poriedSql);
        List<Object[]> flowList = flowQuery.getResultList();
        List<Object[]> poriedList = poriedQuery.getResultList();
        List<ReportAccountDto> list = new ArrayList<>();
        //遍历所有查询信息，
        if(null!=flowList){
            for(int i = 0;i<flowList.size (); i++){
                ReportAccountDto dtos= new ReportAccountDto();
                String depositorType = flowList.get(i)[0] == null ? "" : flowList.get(i)[0].toString();//存款人类别
                BigDecimal tenThousand = new BigDecimal("10000");
                BigDecimal bigDecimalInflow = new BigDecimal(flowList.get(i)[1] == null ? "0" : flowList.get(i)[1].toString());
                String inflowData = String.valueOf(bigDecimalInflow.divide(tenThousand,0,BigDecimal.ROUND_CEILING).toPlainString());//流入
                BigDecimal bigDecimalOuntflow = new BigDecimal(flowList.get(i)[2] == null ? "0" : flowList.get(i)[2].toString());
                String outflowData = String.valueOf(bigDecimalOuntflow.divide(tenThousand,0,BigDecimal.ROUND_CEILING).toPlainString());//流出
                dtos.setDepositorType (depositorType);
                dtos.setInflowData ( inflowData );
                dtos.setOutflowData ( outflowData );
                if(null!=poriedList){
                    for(int j=0; j<poriedList.size ();j++){
                        String depositorTypePu = poriedList.get(i)[0] == null ? "" : poriedList.get(i)[0].toString();//存款人类别
                        if(depositorTypePu.equalsIgnoreCase ( depositorType )){
                            BigDecimal bigDecimalBegin = new BigDecimal(poriedList.get(i)[1] == null ? "0" : poriedList.get(i)[1].toString());
                            String BeginPeriodData = String.valueOf(bigDecimalBegin.divide(tenThousand,0,BigDecimal.ROUND_CEILING).toPlainString());
                            BigDecimal bigDecimalEnd = new BigDecimal(poriedList.get(i)[2] == null ? "0" : poriedList.get(i)[2].toString());
                            String endPeriodData = String.valueOf(bigDecimalEnd.divide(tenThousand,0,BigDecimal.ROUND_CEILING).toPlainString());
                            dtos.setBeginPeriodData ( BeginPeriodData );
                            dtos.setEndPeriodData ( endPeriodData );
                        }
                    }
                }
                list.add ( dtos );
            }
        }
        return list;
    }
}
