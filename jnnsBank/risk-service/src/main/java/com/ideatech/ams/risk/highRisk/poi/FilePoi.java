package com.ideatech.ams.risk.highRisk.poi;

import com.ideatech.ams.pbc.utils.BussUtils;
import com.ideatech.ams.risk.highRisk.entity.HighRisk;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FilePoi {

    private static POIFSFileSystem fs;//poi文件流
    private static HSSFWorkbook wb;//获得execl
    private static HSSFRow row;//获得行
    private static HSSFSheet sheet;//获得工作簿
    private static final Logger logger = LoggerFactory.getLogger(FilePoi.class);

    public List<HighRisk> getFileList(InputStream in) {
        List<HighRisk> list = new ArrayList();
        try {
            fs = new POIFSFileSystem(in);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            int rowend = sheet.getLastRowNum();
            for (int i = 1; i <= rowend; i++) {
                HighRisk highRisk = new HighRisk();
                row = sheet.getRow(i);
//                int colNum = row.getPhysicalNumberOfCells();//一行总列数
                int colNum = 7;//一行总列数
                String[] obj = new String[colNum];
                int j = 0;
                while (j < colNum) {
                    obj[j]  = getCellFormatValue(row.getCell(j)).trim();
                    j++;
                }
                highRisk.setCustomerNo(obj[0]);
                highRisk.setDepositorName(obj[1]);
                highRisk.setLegalName(obj[2]);
                highRisk.setDepositorcardType(obj[3]);
                highRisk.setDepositorcardNo(obj[4]);
                highRisk.setLegalIdcardType(obj[5]);
                highRisk.setLegalIdcardNo(obj[6]);
                list.add(highRisk);
            }
        } catch (Exception e) {
            logger.error("高风险数据导入发生异常",e);
        }
       return list;
    }

    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        int p = (int) cell.getNumericCellValue();
                        cellvalue = String.valueOf(p);

                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
}