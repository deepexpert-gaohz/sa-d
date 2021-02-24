package com.ideatech.ams.compare.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.compare.dto.*;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetailListDto;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultDetails;
import com.ideatech.ams.compare.dto.compareresultdetail.CompareResultFieldDto;
import com.ideatech.ams.compare.entity.CompareField;
import com.ideatech.ams.compare.enums.CompareExportType;
import com.ideatech.ams.system.org.dto.OrganizationDto;
import com.ideatech.ams.system.org.service.OrganizationService;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.lang.Boolean;
import java.util.*;

@Service
//@Transactional
@Slf4j
public class CompareExportServiceImpl implements CompareExportService {
    @Autowired
    private CompareResultService compareResultService;

    @Value("${ams.compare.downPath: /home/weblogic/idea/downPath}")
    private String compareDownPath;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompareTaskService compareTaskService;

    @Autowired
    private CompareRuleFieldsService compareRuleFieldsService;

    @Autowired
    private CompareFieldService compareFieldService;

    @Autowired
    private CompareRuleDataSourceService compareRuleDataSourceService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private CompareDefineService compareDefineService;

    @Override
    public void batchCreateCompareResult(Long taskId) {
        String filePath = compareDownPath + File.separator + taskId;
        FileUtils.deleteQuietly(new File(filePath));

        List<OrganizationDto> organList = organizationService.listAll();
        for (OrganizationDto organ : organList) {
            exportCompareResult(taskId, organ);
            log.info("机构" + organ.getName() + "生成比对结果excel\\txt成功");
        }
    }

    @Override
    public String exportXlsResult(Long taskId, String matchType, Long organId) {
        OrganizationDto organ = organizationService.findById(organId);

        // 先到批量生成的文件夹去查询，机构是否已经生成(比对完成后自动生成)
        String fileName = organ.getCode() + "_" + getTypeByMatchTypeStr(matchType) + ".xls";
        String filePath = compareDownPath + File.separator + taskId + File.separator + "excel" + File.separator + fileName;
        File existFile = new File(filePath);
        if (existFile.exists() && existFile.length() > 0) {
            return filePath;
        }

        // 机构未批量生成、实时生成并喜爱杂
        log.info("未找到批量生成的比对结构，现准备实时查询并下载");

        String organFullId = organ.getFullId();
        Boolean match = getMatchType(matchType);

        CompareTaskDto task = compareTaskService.findById(taskId);

        File dictPath = new File(compareDownPath);
        if (!dictPath.exists()) {
            dictPath.mkdirs();
        }

        File tempFile = new File(compareDownPath, taskId + "-" + organId + "-" + System.currentTimeMillis() + ".xls");

        CompareResultDto condition = new CompareResultDto();
        condition.setCompareTaskId(taskId);
        condition.setMatch(match);
        condition.setOrganFullId(organFullId);

        List<CompareResultDto> compareResultList = compareResultService.findAllByCondition(condition);

        //excel数据填充
        createExcelData(compareResultList, task, tempFile);

        return tempFile.getAbsolutePath();
    }

    @Override
    public String exportTxtResult(Long taskId, String matchType, Long organId) {
        OrganizationDto organ = organizationService.findById(organId);
        // 先到批量生成的文件夹去查询，机构是否已经生成
        String fileName = organ.getCode() + "_" + getTypeByMatchTypeStr(matchType) + ".txt";
        String filePath = compareDownPath + File.separator + taskId + File.separator + "txt" + File.separator + fileName;
        File existFile = new File(filePath);
        if (existFile.exists() && existFile.length() > 0) {
            return filePath;
        }

        String organFullId = organ.getFullId();
        Boolean match = getMatchType(matchType);

        CompareTaskDto task = compareTaskService.findById(taskId);

        File dictPath = new File(compareDownPath);
        if (!dictPath.exists()) {
            dictPath.mkdirs();
        }

        File tempFile = new File(compareDownPath, taskId + "-" + organId + "-" + new Date().getTime() + ".txt");

        CompareResultDto condition = new CompareResultDto();
        condition.setCompareTaskId(taskId);
        condition.setMatch(match);
        condition.setOrganFullId(organFullId);

        List<CompareResultDto> compareResultList = compareResultService.findAllByCondition(condition);

        //txt数据填充
        createTxtData(compareResultList, task, tempFile);

        return tempFile.getAbsolutePath();
    }

    private void exportCompareResult(Long taskId, OrganizationDto organ) {
        CompareResultDto condition = new CompareResultDto();
        condition.setCompareTaskId(taskId);
        condition.setOrganFullId(organ.getFullId());

        CompareTaskDto compareTaskDto = compareTaskService.findById(taskId);

        //所有
        List<CompareResultDto> allList = compareResultService.findAllByConditionNew(condition);
        if(CollectionUtils.isNotEmpty(allList)) {
            createExcel(allList, compareTaskDto, organ, CompareExportType.ALL);
            createTxt(allList, compareTaskDto, organ, CompareExportType.ALL);
        }

        //比对一致
        condition.setMatch(true);
        List<CompareResultDto> sameList = compareResultService.findAllByConditionNew(condition);
        if(CollectionUtils.isNotEmpty(sameList)) {
            createExcel(sameList, compareTaskDto, organ, CompareExportType.MATCH);
            createTxt(sameList, compareTaskDto, organ, CompareExportType.MATCH);
        }

        //比对不一致
        condition.setMatch(false);
        List<CompareResultDto> differList = compareResultService.findAllByConditionNew(condition);
        if(CollectionUtils.isNotEmpty(differList)) {
            createExcel(differList, compareTaskDto, organ, CompareExportType.NOT_MATCH);
            createTxt(differList, compareTaskDto, organ, CompareExportType.NOT_MATCH);
        }

    }

    private Boolean getMatchType(String matchType) {
        if (StringUtils.isNotBlank(matchType)) {
            if("1".equals(matchType)) {
                return true;
            } else if("2".equals(matchType)) {
                return false;
            }
        }

        return null;
    }

    private List<String> writeHeader(CompareTaskDto task, WritableSheet sheet, WritableCellFormat cellFormat) throws WriteException, RowsExceededException {
        int i = 0;
        List<String> fieldsEngList = new ArrayList<>();
        sheet.addCell(new Label(0, 0, "数据源", cellFormat));

        List<CompareRuleFieldsDto> compareRuleFieldsList = compareRuleFieldsService.findByCompareRuleId(task.getCompareRuleId());

        if (CollectionUtils.isNotEmpty(compareRuleFieldsList)) {
            for (CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsList) {
                CompareFieldDto compareFieldDto = compareFieldService.findById(compareRuleFieldsDto.getCompareFieldId());
                sheet.addCell(new Label(i + 1, 0, compareFieldDto.getName(), cellFormat));
                fieldsEngList.add(compareFieldDto.getField());
                i++;
            }
        }

        sheet.addCell(new Label(compareRuleFieldsList.size() + 1, 0, "备注", cellFormat));
        sheet.setColumnView(0, 15);// 设置列宽
        sheet.setColumnView(1, 20);// 设置列宽
        sheet.setColumnView(2, 30);// 设置列宽
        sheet.setColumnView(3, 10);// 设置列宽
        sheet.setColumnView(4, 10);// 设置列宽
        sheet.setColumnView(5, 15);// 设置列宽
        sheet.setRowView(0, 400);// 设置行高

        return fieldsEngList;
    }

    private List<String> getTxtHeader(CompareTaskDto task, List<String> lines) {
        StringBuffer line = new StringBuffer();
        List<String> fieldsEngList = new ArrayList<>();
        line.append("数据源");

        List<CompareRuleFieldsDto> compareRuleFieldsList = compareRuleFieldsService.findByCompareRuleId(task.getCompareRuleId());
        if(CollectionUtils.isNotEmpty(compareRuleFieldsList)) {
            for(CompareRuleFieldsDto compareRuleFieldsDto : compareRuleFieldsList) {
                CompareFieldDto compareFieldDto = compareFieldService.findById(compareRuleFieldsDto.getCompareFieldId());
                line.append("\t\t" + compareFieldDto.getName());
                fieldsEngList.add(compareFieldDto.getField());
            }
        }

        lines.add(line.toString());

        return fieldsEngList;
    }

    private String getTypeByMatchTypeStr(String matchType) {
        if ("0".equals(matchType)) {
            return CompareExportType.ALL.name();
        } else if ("1".equals(matchType)) {
            return CompareExportType.MATCH.name();
        } else {
            return CompareExportType.NOT_MATCH.name();
        }
    }

    /**
     * 按照机构创建excel
     *
     * @param compareResultList
     *            excel生成的比对结果
     * @param task
     *            比对任务
     * @param organ
     *            机构信息
     * @param type
     *            导出比对任务类型
     */
    private void createExcel(List<CompareResultDto> compareResultList, CompareTaskDto task, OrganizationDto organ, CompareExportType type) {
        String filePath = compareDownPath + File.separator + task.getId() + File.separator + "excel";
        File dictPath = new File(filePath);
        if (!dictPath.exists()) {
            dictPath.mkdirs();
        }
        File tempFile = new File(filePath, organ.getCode() + "_" + type.toString() + ".xls");

        //excel数据填充
        createExcelData(compareResultList, task, tempFile);
    }

    /**
     * 按照机构创建txt
     *
     * @param compareResultList
     *            excel生成的比对结果
     * @param task
     *            比对任务
     * @param organ
     *            机构信息
     * @param type
     *            导出比对任务类型
     */
    private void createTxt(List<CompareResultDto> compareResultList, CompareTaskDto task, OrganizationDto organ, CompareExportType type) {
        String filePath = compareDownPath + File.separator + task.getId() + File.separator + "txt" + File.separator;
        File dictPath = new File(filePath);
        if (!dictPath.exists()) {
            dictPath.mkdirs();
        }
        File tempFile = new File(filePath, organ.getCode() + "_" + type.toString() + ".txt");

        //txt数据填充
        createTxtData(compareResultList, task, tempFile);
    }

    /**
     * excel数据填充
     * @param compareResultList
     * @param task
     * @param tempFile
     */
    private void createExcelData(List<CompareResultDto> compareResultList, CompareTaskDto task, File tempFile) {
        int rowIndex = 1;
        WritableWorkbook workbook = null;

        try {
            workbook = Workbook.createWorkbook(tempFile);

            int sheetNum = 0;

            WritableSheet sheet = workbook.createSheet("data1", sheetNum);

            WritableCellFormat misMatchCellFormat = new WritableCellFormat();
            misMatchCellFormat.setBackground(Colour.YELLOW2);

            WritableCellFormat cellFormat = new WritableCellFormat();

            //表头列和英文字段名的映射关系
            List<String> fieldsEngList = writeHeader(task, sheet, cellFormat);

            List<CompareDefineDto> compareDefineList = compareDefineService.findByCompareRuleId(task.getCompareRuleId());

            for (CompareResultDto compareResult : compareResultList) {
                JSONObject detailJson = JSONObject.parseObject(compareResult.getDetails());
                JSONObject dataJson = detailJson.getJSONObject("data");
                JSONObject matchJson = detailJson.getJSONObject("match");

                for (int i = 0; i < dataJson.size(); i++) {
//                    String[] colors = StringUtils.splitByWholeSeparatorPreserveAllTokens(matchJson, ",");
                    DataSourceDto dataSourceDto = dataSourceService.findById(compareDefineList.get(i).getDataSourceId());

                    JSONObject filedsJson = dataJson.getJSONObject(dataSourceDto.getName());
                    sheet.addCell(new Label(0, rowIndex, dataSourceDto.getName(), cellFormat));

                    int j = 1;
                    for(String key : fieldsEngList) {
                        sheet.addCell(new Label(j, rowIndex, filedsJson.getString(key), "true".equals(matchJson.getString(key)) ? cellFormat : misMatchCellFormat));
                        j++;
                    }

                    if (StringUtils.isNotBlank(compareResult.getAlone())) {
                        sheet.addCell(new Label(filedsJson.size() + 1, rowIndex, compareResult.getAlone(), cellFormat));
                    } else if(compareResult.getMatch() != null) {
                        if (compareResult.getMatch()) {
                            sheet.addCell(new Label(filedsJson.size() + 1, rowIndex, "", cellFormat));
                        } else if (!compareResult.getMatch()) {
                            sheet.addCell(new Label(filedsJson.size() + 1, rowIndex, "比对不一致", cellFormat));
                        }
                    }

                    rowIndex++;
                }

                sheet.addCell(new Label(0, rowIndex, "", cellFormat));
                rowIndex++;

                if (rowIndex > 0 && (rowIndex % 65536 == 0 || rowIndex % 65535 == 0 || rowIndex % 65534 == 0 || rowIndex % 65533 == 0)) {
                    sheetNum = sheetNum + 1;
                    sheet = workbook.createSheet("data" + (sheetNum + 1), sheetNum);
                    writeHeader(task, sheet, cellFormat);
                    rowIndex = 1;
                }
            }

            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭 Excel 工作薄对象
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * txt数据填充
     * @param compareResultList
     * @param task
     * @param tempFile
     */
    private void createTxtData(List<CompareResultDto> compareResultList, CompareTaskDto task, File tempFile) {
        List<String> lines = new ArrayList();
        String split = "\t";

        List<String> fieldsEngList = getTxtHeader(task, lines);
        List<CompareDefineDto> compareDefineList = compareDefineService.findByCompareRuleId(task.getCompareRuleId());

        for (CompareResultDto compareResult : compareResultList) {
            JSONObject detailJson = JSONObject.parseObject(compareResult.getDetails());
            JSONObject dataJson = detailJson.getJSONObject("data");

            for (int i = 0; i < dataJson.size(); i++) {
                DataSourceDto dataSourceDto = dataSourceService.findById(compareDefineList.get(i).getDataSourceId());
                JSONObject filedsJson = dataJson.getJSONObject(dataSourceDto.getName());

                StringBuffer line = new StringBuffer();
                line.append(dataSourceDto.getName() + split);

                for (String key : fieldsEngList) {
                    line.append(filedsJson.getString(key) + split);
                }
                if (StringUtils.isNotBlank(compareResult.getAlone())) {
                    line.append(compareResult.getAlone() + split);
                } else if (compareResult.getMatch()) {
                    line.append("" + split);
                } else if (!compareResult.getMatch()) {
                    line.append("比对不一致" + split);
                }
                lines.add(line.toString());

            }

            lines.add("\n");
        }

        try {
            FileUtils.writeLines(tempFile, "GBK", lines, null, false);
        } catch (IOException e) {
            log.error("生成导出txt文件异常", e);
        }
    }

}
