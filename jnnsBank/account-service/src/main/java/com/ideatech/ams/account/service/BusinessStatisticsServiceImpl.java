package com.ideatech.ams.account.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.account.dao.AccountsAllDao;
import com.ideatech.ams.account.dao.spec.AccountsAllStatisticsInfoSpec;
import com.ideatech.ams.account.dto.AllBillsPublicSearchDTO;
import com.ideatech.ams.account.dto.bill.AllBillsPublicDTO;
import com.ideatech.ams.account.dto.poi.StatisticsInfoPoi;
import com.ideatech.ams.account.dto.poi.StatisticsPoi;
import com.ideatech.ams.account.enums.AccountStatus;
import com.ideatech.ams.account.enums.bill.BillType;
import com.ideatech.ams.account.enums.bill.CompanyAmsCheckStatus;
import com.ideatech.ams.account.enums.bill.CompanySyncStatus;
import com.ideatech.ams.account.service.bill.AccountBillsAllService;
import com.ideatech.ams.account.service.bill.AllBillsPublicService;
import com.ideatech.ams.account.service.poi.StatisticsInfoRecordExport;
import com.ideatech.ams.account.service.poi.StatisticsRecordExport;
import com.ideatech.ams.account.vo.AccountStatisticsInfoVo;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import com.ideatech.common.dto.TreeTable;
import com.ideatech.common.excel.util.PoiExcelUtils;
import com.ideatech.common.excel.util.service.IExcelExport;
import com.ideatech.common.msg.TableResultResponse;
import com.ideatech.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
@Transactional
public class BusinessStatisticsServiceImpl implements BusinessStatisticsService {

    @Autowired
    private AccountBillsAllService accountBillsAllService;

    @Autowired
    private AllBillsPublicService allBillsPublicService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AccountsAllDao accountsAllDao;

    @Override
    public List<Map<String, Object>> query(String createddatestart, String createddateend) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        long count_acctOpen = 0;
        long count_acctChange = 0;
        long count_acctRevoke = 0;

        List<OrganizationDto> organizationList = organizationService.listDescendant(SecurityUtils.getCurrentUser().getOrgId());

        if(organizationList != null && organizationList.size() > 0) {
            for(OrganizationDto organization : organizationList) {
                map = new HashMap<>();

                map.put("id", organization.getId());
                map.put("parentId", organization.getParentId());
                map.put("name", organization.getName());

                if(StringUtils.isNotBlank(createddatestart) && StringUtils.isNotBlank(createddateend)) {
                    count_acctOpen = accountBillsAllService.countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(BillType.ACCT_OPEN, organization.getFullId(), createddatestart, createddateend);
                    count_acctChange = accountBillsAllService.countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(BillType.ACCT_CHANGE, organization.getFullId(), createddatestart, createddateend);
                    count_acctRevoke = accountBillsAllService.countByBillTypeAndOrganFullIdStartsWithAndBillDateBetween(BillType.ACCT_REVOKE, organization.getFullId(), createddatestart, createddateend);
                } else {
                    count_acctOpen = accountBillsAllService.countByBillTypeAndOrganFullIdStartsWith(BillType.ACCT_OPEN, organization.getFullId());
                    count_acctChange = accountBillsAllService.countByBillTypeAndOrganFullIdStartsWith(BillType.ACCT_CHANGE, organization.getFullId());
                    count_acctRevoke = accountBillsAllService.countByBillTypeAndOrganFullIdStartsWith(BillType.ACCT_REVOKE, organization.getFullId());
                }
                map.put("acctOpen", count_acctOpen);
                map.put("acctChange", count_acctChange);
                map.put("acctRevoke", count_acctRevoke);

                list.add(map);
            }
        }

        log.info(JSONObject.toJSON(list).toString());

        return list;
    }

    @Override
    public TreeTable query(Long pid, Long organId, String createddatestart, String createddateend,String acctType) {
        TreeTable result = new TreeTable();
        List<OrganizationDto> organChilds = null;

        if (pid == null) {
            OrganizationDto parent = organizationService.findById(organId);

            Map<String, Object> row = buildRow(parent, createddatestart, createddateend,acctType);

            List<Map<String, Object>> rows = new ArrayList<>();
            organChilds = organizationService.searchChild(organId, "");

            for (OrganizationDto child : organChilds) {
                rows.add(buildRow(child, createddatestart, createddateend,acctType));
            }
            row.put("rows", rows);

            result.getRows().add(row);
        } else {
            result.setPid(pid.toString());
            organChilds = organizationService.searchChild(pid, "");

            for (OrganizationDto childOrgan : organChilds) {
                result.getRows().add(buildRow(childOrgan, createddatestart, createddateend,acctType));
            }
        }

        return result;
    }

    @Override
    public TreeTable query(Long pid, Long organId, AccountStatisticsInfoVo accountStatisticsInfoVo) {
        TreeTable result = new TreeTable();
        List<OrganizationDto> organChilds = null;

        if (pid == null) {
            OrganizationDto parent = organizationService.findById(organId);

            Map<String, Object> row = buildRow(parent, accountStatisticsInfoVo);

            List<Map<String, Object>> rows = new ArrayList<>();
            organChilds = organizationService.searchChild(organId, "");

            for (OrganizationDto child : organChilds) {
                rows.add(buildRow(child, accountStatisticsInfoVo));
            }
            row.put("rows", rows);

            result.getRows().add(row);
        } else {
            result.setPid(pid.toString());
            organChilds = organizationService.searchChild(pid, "");

            for (OrganizationDto childOrgan : organChilds) {
                result.getRows().add(buildRow(childOrgan, accountStatisticsInfoVo));
            }
        }

        return result;
    }

    @Override
    public IExcelExport exportKbxXLS(Long organId, String createddatestart, String createddateend,String acctType) {
        IExcelExport companyStatisticsExport = new StatisticsRecordExport();
        List<StatisticsPoi> statisticsPoiList = new ArrayList<>();
        loopQuery2(null, organId, createddatestart, createddateend, statisticsPoiList,acctType);
        companyStatisticsExport.setPoiList(statisticsPoiList);
        return companyStatisticsExport;
    }


    @Override
    public IExcelExport exportXLS(Long pid, Long organId, AccountStatisticsInfoVo accountStatisticsInfoVo) {
        IExcelExport companyStatisticsExport = new StatisticsInfoRecordExport();
        List<StatisticsInfoPoi> statisticsPoiList = new ArrayList<>();

        loopQuery(pid,organId,accountStatisticsInfoVo,statisticsPoiList);

        companyStatisticsExport.setPoiList(statisticsPoiList);
        return companyStatisticsExport;
    }

    /**
     * 获取账户开户报送统计查询列表中，存款人类别多选框中的数据
     */
    @Override
    public JSONArray getDepositorTypeJson() {
        String depositorTypeStr = "[" +
                "    { value: '01', text: '企业法人', checked: true },\n" +
                "    { value: '02', text: '非法人企业', checked: true },\n" +
                "    { value: '03', text: '机关', checked: true },\n" +
                "    { value: '04', text: '实行预算管理的事业单位', checked: true },\n" +
                "    { value: '05', text: '非预算管理的事业单位', checked: true },\n" +
                "    { value: '06', text: '团级(含)以上军队及分散执勤的支(分)队', checked: true },\n" +
                "    { value: '07', text: '团级(含)以上武警部队及分散执勤的支(分)队', checked: true },\n" +
                "    { value: '08', text: '社会团体', checked: true },\n" +
                "    { value: '09', text: '宗教组织', checked: true },\n" +
                "    { value: '10', text: '民办非企业组织', checked: true },\n" +
                "    { value: '11', text: '外地常设机构', checked: true },\n" +
                "    { value: '12', text: '外国驻华机构', checked: true },\n" +
                "    { value: '13', text: '有字号的个体工商户', checked: true },\n" +
                "    { value: '14', text: '无字号的个体工商户', checked: true },\n" +
                "    { value: '15', text: '居民委员会、村民委员会、社区委员会', checked: true },\n" +
                "    { value: '16', text: '单位设立的独立核算的附属机构', checked: true },\n" +
                "    { value: '17', text: '其他组织', checked: true },\n" +
                "    { value: '20', text: '境外机构', checked: true },\n" +
                "    { value: '50', text: 'QFII', checked: true },\n" +
                "    { value: '51', text: '境外贸易机构', checked: true },\n" +
                "    { value: '52', text: '跨境清算', checked: true }\n" +
                "  ]";
        return JSONArray.parseArray(depositorTypeStr);
    }

    /**
     * 获取账户开户报送统计查询列表中，账户性质多选框中的数据
     */
    @Override
    public JSONArray getAcctTypeJson() {
        String acctTypeStr = "[" +
                "    { value: 'jiben', text: '基本存款账户', checked: true },\n" +
                "    { value: 'yiban', text: '一般存款账户', checked: true },\n" +
                "    { value: 'yusuan', text: '预算单位专用存款账户', checked: true },\n" +
                "    { value: 'feiyusuan', text: '非预算单位专用存款账户', checked: true },\n" +
                "    { value: 'teshu', text: '特殊单位专用存款账户', checked: true },\n" +
                "    { value: 'linshi', text: '临时机构临时存款账户', checked: true },\n" +
                "    { value: 'feilinshi', text: '非临时机构临时存款账户', checked: true }\n" +
                "  ]";
        return JSONArray.parseArray(acctTypeStr);
    }

    /**
     * 定义“人行上报数据统计”按钮的默认勾选数据
     */
    @Override
    public void setDepositorTypeAcctTypeIsPbc(JSONArray depositorTypeJson, JSONArray acctTypeJson) {
        String[] depositorTypePbc = new String[]{"01", "02", "13", "14"};
        String[] acctTypePbc = new String[]{"jiben", "yiban", "yusuan", "feiyusuan", "teshu", "linshi", "feilinshi"};
        for (Object aDepositorTypeJson : depositorTypeJson) {
            JSONObject obj = (JSONObject) aDepositorTypeJson;
            if (Arrays.asList(depositorTypePbc).contains((String) obj.get("value"))) {
                obj.put("isPbc", true);
            }
        }
        for (Object anAcctTypeJson : acctTypeJson) {
            JSONObject obj = (JSONObject) anAcctTypeJson;
            if (Arrays.asList(acctTypePbc).contains((String) obj.get("value"))) {
                obj.put("isPbc", true);
            }
        }
    }

    /**
     * 开户成功的数据统计（需要上报的数据）
     *
     * @param depositorTypeList 存款人类别（多选数据）
     * @param acctTypeList      账户性质（多选数据）
     * @param beginDate         开始时间
     * @param endDate           结束时间
     */
    @Override
    public JSONArray openStatisticsList(String[] depositorTypeList, String[] acctTypeList, String beginDate, String endDate) {
        if (StringUtils.isNotBlank(beginDate) && beginDate.length() == "yyyy-MM-dd".length()) {
            beginDate = beginDate + " 00:00:00";
        }
        if (StringUtils.isNotBlank(endDate) && endDate.length() == "yyyy-MM-dd".length()) {
            endDate = endDate + " 00:00:00";
        }
        String organFullId = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgFullId();
        JSONArray arr = new JSONArray();
        for (String acctType : acctTypeList) {
            long count = 0;
            for (String depositorType : depositorTypeList) {
                JSONObject obj = new JSONObject();
                obj.put("depositorType", depositorType);
                obj.put("acctType", acctType);
                try {
                    long num = accountBillsAllService.countOpenSuccess(depositorType, acctType, organFullId, beginDate, endDate);
                    obj.put("value", num);
                    count += num;
                } catch (Exception e) {
                    log.info("统计depositorType为{}，acctType为{}出现错误", depositorType, acctType, e);
                }
                arr.add(obj);
            }
            //添加合计数据
            JSONObject obj = new JSONObject();
            obj.put("depositorType", "count");
            obj.put("acctType", acctType);
            obj.put("value", count);
            arr.add(obj);
        }
        return arr;
    }

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
    @Override
    public TableResultResponse<AllBillsPublicSearchDTO> openStatisticsDetailList(String acctNo, String kernelOrgCode, String depositorName, String openAccountSiteType, String createdBy,
                                                                                 String depositorType, String acctType, String beginDate, String endDate,
                                                                                 Date beginDateApply, Date endDateApply, Pageable pageable) {
        String organFullId = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getOrgFullId();
        return allBillsPublicService.openStatisticsDetailList(acctNo, kernelOrgCode, depositorName, openAccountSiteType, createdBy, depositorType, acctType, organFullId, beginDate, endDate, beginDateApply, endDateApply, pageable);
    }

    /**
     * 开户成功的数据统计导出excel（需要上报的数据）
     *
     * @param depositorTypeList 存款人类别（多选数据）
     * @param acctTypeList      账户性质（多选数据）
     * @param beginDate         开始时间
     * @param endDate           结束时间
     */
    @Override
    public HSSFWorkbook exportExcel(String[] depositorTypeList, String[] acctTypeList, String beginDate, String endDate) {
        JSONArray openAccountJsonArr = this.openStatisticsList(depositorTypeList, acctTypeList, beginDate, endDate);//开户成功统计结果

        boolean isZhuanYongAccount = isZhuanYongAccount(acctTypeList);
        boolean isLinShiAccount = isLinShiAccount(acctTypeList);
        boolean isIndividualBusiness = isIndividualBusiness(depositorTypeList);
        boolean rowIs2 = isZhuanYongAccount || isLinShiAccount;//如果数据中有专用存款账户或有临时存款账户，表头需要2行，第三行为数据开始行
        boolean colIs2 = isIndividualBusiness;//如果数据中有个体工商户，纵列需要2列描述，第3列为数据开始列

        int individualBusinessStart = 0;//合并单元格开始行（个体工商户）
        int individualBusinessEnd = 0;//合并单元格结束行（个体工商户）

        int jibenYibanNum = 0;//基本户和一般户个数
        int zhuanYongNum = 0;//专用存款账户个数
        int linShiNum = 0;//临时存款账户个数

//        //对depositorTypeList进行排序，方便后续合并单元格
        if (isIndividualBusiness) {
            List<String> tempList = new ArrayList<>();
            int index = -1;
            for (int i = 0; i < depositorTypeList.length; i++) {
                if (isIndividualBusiness(depositorTypeList[i])) {
                    if (index == -1) {
                        index = i;
                        individualBusinessStart = i;
                        tempList.add(depositorTypeList[i]);
                    } else {
                        tempList.add(index + 1, depositorTypeList[i]);
                        index++;
                    }
                } else {
                    tempList.add(depositorTypeList[i]);
                }
            }
            individualBusinessEnd = index;
            depositorTypeList = tempList.toArray(new String[tempList.size()]);
        }
        //对acctTypeList进行排序，方便后续合并单元格
        if (isZhuanYongAccount || isLinShiAccount) {//基本户和一般户
            List<String> tempList = new ArrayList<>();
            int index = -1;
            for (int i = 0; i < acctTypeList.length; i++) {
                if (!isZhuanYongAccount(acctTypeList[i]) && !isLinShiAccount(acctTypeList[i])) {
                    if (index == -1) {
                        index = i;
                        tempList.add(acctTypeList[i]);
                    } else {
                        tempList.add(index + 1, acctTypeList[i]);
                        index++;
                    }
                    jibenYibanNum++;
                } else {
                    tempList.add(acctTypeList[i]);
                }
            }
            acctTypeList = tempList.toArray(new String[tempList.size()]);
        }
        if (isZhuanYongAccount) {
            List<String> tempList = new ArrayList<>();
            int index = -1;
            for (int i = 0; i < acctTypeList.length; i++) {
                if (isZhuanYongAccount(acctTypeList[i])) {
                    if (index == -1) {
                        index = i;
                        tempList.add(acctTypeList[i]);
                    } else {
                        tempList.add(index + 1, acctTypeList[i]);
                        index++;
                    }
                    zhuanYongNum++;
                } else {
                    tempList.add(acctTypeList[i]);
                }
            }
            acctTypeList = tempList.toArray(new String[tempList.size()]);
        }
        if (isLinShiAccount) {
            List<String> tempList = new ArrayList<>();
            int index = -1;
            for (int i = 0; i < acctTypeList.length; i++) {
                if (isLinShiAccount(acctTypeList[i])) {
                    if (index == -1) {
                        index = i;
                        tempList.add(acctTypeList[i]);
                    } else {
                        tempList.add(index + 1, acctTypeList[i]);
                        index++;
                    }
                    linShiNum++;
                } else {
                    tempList.add(acctTypeList[i]);
                }
            }
            acctTypeList = tempList.toArray(new String[tempList.size()]);
        }

        int jibenYibanStart = 1;//合并单元格开始列（基本户和一般户）
        int zhuanYongAccountStart = jibenYibanStart + jibenYibanNum;//合并单元格开始列（专用存款账户）
        int linShiAccountStart = zhuanYongAccountStart + zhuanYongNum;//合并单元格开始列（临时存款账户）

        HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象

        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);//字号

        //文本格式
        HSSFCellStyle textStyle = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));//设置格式为文本格式
        //标题样式
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setFont(font);
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左右靠左
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style1.setWrapText(true);//自动换行
        //表格内容样式
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFont(font);
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);//左右靠左
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中
        style2.setWrapText(true);//自动换行
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFSheet sheet = wb.createSheet("开户成功数据");//创建Excel工作表对象
        //设置列宽
        List<Double> columnWidthList = new ArrayList<>();
        if (colIs2) {
            columnWidthList.add(15d);
            columnWidthList.add(30d);
        } else {
            columnWidthList.add(45d);
        }
        for (int i = 0; i < jibenYibanNum; i++) {
            columnWidthList.add(15d);
        }
        for (int i = 0; i < zhuanYongNum + linShiNum; i++) {
            columnWidthList.add(23d);
        }
        for (int i = 0; i < columnWidthList.size(); i++) {
            sheet.setColumnWidth(i, (short) (256 * columnWidthList.get(i)));//设置列宽
            sheet.setDefaultColumnStyle(i, textStyle);//设置格式为文本格式
        }

        int rowIndex = 0;//开始行
        //设置表头
        JSONArray depositorTypeJson = this.getDepositorTypeJson();
        JSONArray acctTypeJson = this.getAcctTypeJson();

        HSSFRow rowTh1 = sheet.createRow((short) rowIndex);//第一行数据
        this.setCell(rowTh1, 0, style1, "存款人类别");
        for (int i = 0; i < acctTypeList.length; i++) {
            int col = i + 1 + (colIs2 ? 1 : 0);//账户类型表头数据所在的列num
            if (rowIs2 && isZhuanYongAccount(acctTypeList[i])) {
                this.setCell(rowTh1, col, style1, "专用存款账户");
            } else if (rowIs2 && isLinShiAccount(acctTypeList[i])) {
                this.setCell(rowTh1, col, style1, "临时存款账户");
            } else {
                this.setCell(rowTh1, col, style1, getDepositorTypeAcctTypeName(acctTypeJson, acctTypeList[i]));
            }
        }
        //如果数据中有专用存款账户或有临时存款账户，行数+1
        if (rowIs2) {
            HSSFRow rowTh2 = sheet.createRow((short) ++rowIndex);//第一行数据
            for (int i = 0; i < acctTypeList.length; i++) {
                int col = i + 1 + (colIs2 ? 1 : 0);//账户类型表头数据所在的列num
                if (isZhuanYongAccount(acctTypeList[i]) || isLinShiAccount(acctTypeList[i])) {
                    this.setCell(rowTh2, col, style1, getDepositorTypeAcctTypeName(acctTypeJson, acctTypeList[i]));
                } else {
                    this.setCell(rowTh2, col, style1, "");
                }
            }
        }

        //添加数据行
        for (int i = 0; i < depositorTypeList.length; i++) {
            HSSFRow row = sheet.createRow((short) ++rowIndex);//数据行
            //如果数据中有个体工商户，列数+1
            if (colIs2) {
                if (isIndividualBusiness(depositorTypeList[i])) {
                    this.setCell(row, 0, style1, "个体工商户");//第1列
                    this.setCell(row, 1, style1, getDepositorTypeAcctTypeName(depositorTypeJson, depositorTypeList[i]));//第2列
                } else {
                    this.setCell(row, 0, style1, getDepositorTypeAcctTypeName(depositorTypeJson, depositorTypeList[i]));//第1列
                    this.setCell(row, 1, style1, "");//第2列
                }
            } else {
                this.setCell(row, 0, style1, getDepositorTypeAcctTypeName(depositorTypeJson, depositorTypeList[i]));//第1列
            }
            for (int j = 0; j < acctTypeList.length; j++) {
                int col = j + 1 + (colIs2 ? 1 : 0);
                for (Object obj : openAccountJsonArr) {
                    JSONObject jsonObject = (JSONObject) obj;
                    if (depositorTypeList[i].equals((String) jsonObject.get("depositorType"))
                            && acctTypeList[j].equals((String) jsonObject.get("acctType"))) {
                        this.setCell(row, col, style2, (Long) jsonObject.get("value"));
                    }
                }
            }
        }
        //添加合计行
        HSSFRow countRow = sheet.createRow((short) ++rowIndex);
        this.setCell(countRow, 0, style1, "合计");//第1列
        if (colIs2) {
            this.setCell(countRow, 1, style1, "");//第2列
        }
        for (int j = 0; j < acctTypeList.length; j++) {
            int col = j + 1 + (colIs2 ? 1 : 0);
            for (Object obj : openAccountJsonArr) {
                JSONObject jsonObject = (JSONObject) obj;
                if ("count".equals((String) jsonObject.get("depositorType"))
                        && acctTypeList[j].equals((String) jsonObject.get("acctType"))) {
                    this.setCell(countRow, col, style2, (Long) jsonObject.get("value"));
                }
            }
        }

        //合并“存款人类别”
        if (isIndividualBusiness || isLinShiAccount || isZhuanYongAccount) {
            String target = (isIndividualBusiness ? "$B" : "$A") + ((isLinShiAccount || isZhuanYongAccount) ? "$2" : "$1");
            this.mergedRegion("$A$1", target, sheet, wb);
        }
        //合并个体工商户
        if (isIndividualBusiness) {
            this.mergedRegion("$A$" + (individualBusinessStart + (rowIs2 ? 3 : 2))
                    , "$A$" + (individualBusinessEnd + (rowIs2 ? 3 : 2)), sheet, wb);
        }
        //合并非个体工商户
        if (colIs2) {
            int i = rowIs2 ? 3 : 2;
            for (; i <= rowIndex + 1; i++) {
                if (i < (individualBusinessStart + (rowIs2 ? 3 : 2)) || i > (individualBusinessEnd + (rowIs2 ? 3 : 2))) {
                    this.mergedRegion("$A$" + i
                            , "$B$" + i, sheet, wb);
                }
            }
        }
        //合并基本户和一般户
        if (isLinShiAccount || isZhuanYongAccount) {
            int i = isIndividualBusiness ? 3 : 2;
            int max = i + jibenYibanNum;
            for (; i < max; i++) {
                this.mergedRegion("$" + PoiExcelUtils.excelColIndexToStr(i) + "$1"
                        , "$" + PoiExcelUtils.excelColIndexToStr(i) + "$2", sheet, wb);
            }
        }
        //合并专用存款账户
        if (isZhuanYongAccount) {
            this.mergedRegion("$" + PoiExcelUtils.excelColIndexToStr(zhuanYongAccountStart + (colIs2 ? 2 : 1)) + "$1"
                    , "$" + PoiExcelUtils.excelColIndexToStr(zhuanYongAccountStart + zhuanYongNum - 1 + (colIs2 ? 2 : 1)) + "$1", sheet, wb);
        }
        //合并临时存款账户
        if (isLinShiAccount) {
            this.mergedRegion("$" + PoiExcelUtils.excelColIndexToStr(linShiAccountStart + (colIs2 ? 2 : 1)) + "$1"
                    , "$" + PoiExcelUtils.excelColIndexToStr(linShiAccountStart + linShiNum - 1 + (colIs2 ? 2 : 1)) + "$1", sheet, wb);
        }
        return wb;
    }

    private void setCell(HSSFRow row, int column, HSSFCellStyle style, Object value) {
        HSSFCell cell = row.createCell(column);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(value == null ? "" : String.valueOf(value)));
    }

    /**
     * 合并单元格
     */
    private void mergedRegion(String ref1, String ref2, HSSFSheet sheet, HSSFWorkbook wb) {
        if (StringUtils.isBlank(ref1) || StringUtils.isBlank(ref2) || ref1.equals(ref2)) {
            return;
        }
        CellRangeAddress cra = CellRangeAddress.valueOf(ref1 + ":" + ref2);
        sheet.addMergedRegion(cra);
        RegionUtil.setBorderBottom(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        RegionUtil.setBorderLeft(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        RegionUtil.setBorderTop(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
        RegionUtil.setBorderRight(HSSFBorderFormatting.BORDER_THIN, cra, sheet, wb);
    }

    /**
     * 获取开户统计结果json中code对应的中文名称
     */
    private String getDepositorTypeAcctTypeName(JSONArray acctTypeJson, String value) {
        for (Object obj : acctTypeJson) {
            JSONObject jsonObject = (JSONObject) obj;
            if (value.equals((String) jsonObject.get("value"))) {
                return (String) jsonObject.get("text");
            }
        }
        return "";
    }

    private boolean isZhuanYongAccount(String[] acctTypeList) {
        for (String value : acctTypeList) {
            if(this.isZhuanYongAccount(value)){
                return true;
            }
        }
        return false;
    }

    private boolean isLinShiAccount(String[] acctTypeList) {
        for (String value : acctTypeList) {
            if(this.isLinShiAccount(value)){
                return true;
            }
        }
        return false;
    }

    private boolean isIndividualBusiness(String[] depositorTypeList) {
        for (String value : depositorTypeList) {
            if(this.isIndividualBusiness(value)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为专用存款账户
     */
    private boolean isZhuanYongAccount(String value) {
        String[] arr = new String[]{"yusuan", "feiyusuan", "teshu"};//预算单位专用存款账户，非预算单位专用存款账户，特殊单位专用存款账户
        return Arrays.asList(arr).contains(value);
    }

    /**
     * 判断是否为临时存款账户
     */
    private boolean isLinShiAccount(String value) {
        String[] arr = new String[]{"linshi", "feilinshi"};//临时机构临时存款账户，非临时机构临时存款账户
        return Arrays.asList(arr).contains(value);
    }

    /**
     * 判断是否为个体工商户
     */
    private boolean isIndividualBusiness(String value) {
        String[] arr = new String[]{"13", "14"};//有字号的个体工商户，无字号的个体工商户
        return Arrays.asList(arr).contains(value);
    }

    private void loopQuery(Long pid, Long organId, AccountStatisticsInfoVo accountStatisticsInfoVo,List<StatisticsInfoPoi> statisticsPoiList){
        List<Map<String, Object>> rows = new ArrayList<>();
        rows = query(pid,organId,accountStatisticsInfoVo).getRows();
        if(CollectionUtils.isNotEmpty(rows)) {
            for(Map<String, Object> map : rows) {
                StatisticsInfoPoi statisticsPoi = new StatisticsInfoPoi();
                if(map.get("name") != null){
                    statisticsPoi.setOrgName(String.valueOf(map.get("name")));
                }
                if(map.get("num1") != null){
                    statisticsPoi.setNormalCount(String.valueOf(map.get("num1")));
                }
                if(map.get("num2") != null){
                    statisticsPoi.setRevokeCount(String.valueOf(map.get("num2")));
                }
                if(map.get("name") != null){
                    statisticsPoi.setSuspendCount(String.valueOf(map.get("num3")));
                }
                statisticsPoiList.add(statisticsPoi);
                if(map.get("hasRows") != null && StringUtils.equals("1",String.valueOf(map.get("hasRows"))) && map.get("id") != null){
                    loopQuery(Long.valueOf(map.get("id").toString()),organId,accountStatisticsInfoVo,statisticsPoiList);
                }
            }

        }
    }

    private void loopQuery2(Long pid, Long organId, String createddatestart, String createddateend, List<StatisticsPoi> statisticsPoiList,String acctType) {
        List<Map<String, Object>> rows = new ArrayList<>();
        rows = this.query(pid, organId, createddatestart, createddateend,acctType).getRows();
        if (CollectionUtils.isNotEmpty(rows)) {
            for (Map<String, Object> map : rows) {
                StatisticsPoi statisticsPoi = new StatisticsPoi();
                if (map.get("name") != null) {//机构名称
                    statisticsPoi.setOrgName(String.valueOf(map.get("name")));
                }
                if (map.get("num1") != null) {//新开户
                    statisticsPoi.setOpenCount(String.valueOf(map.get("num1")));
                }
                if (map.get("num2") != null) {//变更
                    statisticsPoi.setChangeCount(String.valueOf(map.get("num2")));
                }
                if (map.get("num3") != null) {//销户
                    statisticsPoi.setRevokeCount(String.valueOf(map.get("num3")));
                }
                if (map.get("num4") != null) {//久悬
                    statisticsPoi.setSuspendCount(String.valueOf(map.get("num4")));
                }
                statisticsPoiList.add(statisticsPoi);
                if (map.get("hasRows") != null && StringUtils.equals("1", String.valueOf(map.get("hasRows"))) && map.get("id") != null) {
                    loopQuery2(Long.valueOf(map.get("id").toString()), organId, createddatestart, createddateend, statisticsPoiList,acctType);
                }
            }

        }
    }

    private Map<String,Object> buildRow(OrganizationDto organ, String createddatestart, String createddateend,String acctType) {
        long count_acctOpen = 0;
        long count_acctChange = 0;
        long count_acctRevoke = 0;
        long count_acctSuspend = 0;

        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", organ.getId().toString());

        List<OrganizationDto> organChilds = organizationService.searchChild(organ.getId(), "");

        if (CollectionUtils.isNotEmpty(organChilds)) {
            row.put("hasRows", "1");
            row.put("name", organ.getName());
        } else {
            row.put("name", organ.getName());
        }

        List<CompanySyncStatus> pbcSyncStatusList = new ArrayList<>();
        pbcSyncStatusList.add(CompanySyncStatus.buTongBu);
        pbcSyncStatusList.add(CompanySyncStatus.tongBuChengGong);
        List<CompanySyncStatus> eccsSyncStatusList = new ArrayList<>();
        eccsSyncStatusList.add(CompanySyncStatus.buTongBu);
        eccsSyncStatusList.add(CompanySyncStatus.tongBuChengGong);
        List<CompanyAmsCheckStatus> pbcCheckStatusList = new ArrayList<>();
        pbcCheckStatusList.add(CompanyAmsCheckStatus.NoCheck);
        pbcCheckStatusList.add(CompanyAmsCheckStatus.CheckPass);

        count_acctOpen = accountBillsAllService.count(BillType.ACCT_OPEN, organ.getFullId(), createddatestart, createddateend, pbcSyncStatusList, eccsSyncStatusList, pbcCheckStatusList,acctType);
        count_acctChange = accountBillsAllService.count(BillType.ACCT_CHANGE, organ.getFullId(), createddatestart, createddateend, pbcSyncStatusList, eccsSyncStatusList, pbcCheckStatusList,acctType);
        count_acctRevoke = accountBillsAllService.count(BillType.ACCT_REVOKE, organ.getFullId(), createddatestart, createddateend, pbcSyncStatusList, eccsSyncStatusList, pbcCheckStatusList,acctType);
        count_acctSuspend = accountBillsAllService.count(BillType.ACCT_SUSPEND, organ.getFullId(), createddatestart, createddateend, pbcSyncStatusList, eccsSyncStatusList, pbcCheckStatusList,acctType);

        row.put("num1", count_acctOpen);
        row.put("num2", count_acctChange);
        row.put("num3", count_acctRevoke);
        row.put("num4", count_acctSuspend);
        row.put("num5", count_acctOpen+count_acctChange);
        row.put("orgCode", organ.getCode());

        return row;
    }

    private Map<String,Object> buildRow(OrganizationDto organ, AccountStatisticsInfoVo accountStatisticsInfoVo) {
        long count_acctNormal = 0;
        long count_acctRevoke = 0;
        long count_acctSuspend = 0;

        Map<String, Object> row = new HashMap<String, Object>();
        row.put("id", organ.getId().toString());

        List<OrganizationDto> organChilds = organizationService.searchChild(organ.getId(), "");

        if (CollectionUtils.isNotEmpty(organChilds)) {
            row.put("hasRows", "1");
            row.put("name", organ.getName());
        } else {
            row.put("name", organ.getName());
        }
        List<String> string003s = new ArrayList<>();

        if(StringUtils.equals(accountStatisticsInfoVo.getString003(),"1")){//存量数据
            string003s.add("1");
            accountStatisticsInfoVo.setString003s(string003s);
        }else if(StringUtils.equals(accountStatisticsInfoVo.getString003(),"0")){//增量
            string003s.add("");
            string003s.add("0");
            string003s.add(null);
            accountStatisticsInfoVo.setString003s(string003s);
        }
        accountStatisticsInfoVo.setOrganFullId(organ.getFullId());
        accountStatisticsInfoVo.setAccountStatus(AccountStatus.normal);
        count_acctNormal = accountsAllDao.count(new AccountsAllStatisticsInfoSpec(accountStatisticsInfoVo));
        accountStatisticsInfoVo.setAccountStatus(AccountStatus.revoke);
        count_acctRevoke = accountsAllDao.count(new AccountsAllStatisticsInfoSpec(accountStatisticsInfoVo));
        accountStatisticsInfoVo.setAccountStatus(AccountStatus.suspend);
        count_acctSuspend = accountsAllDao.count(new AccountsAllStatisticsInfoSpec(accountStatisticsInfoVo));

        row.put("num1", count_acctNormal);
        row.put("num2", count_acctRevoke);
        row.put("num3", count_acctSuspend);
        row.put("orgCode", organ.getCode());


        return row;
    }

}
