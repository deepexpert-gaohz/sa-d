package com.ideatech.common.excel.util;

import com.ideatech.common.enums.EErrorCode;
import com.ideatech.common.exception.BizServiceException;
import com.ideatech.common.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class DataTablesExcelService extends AbstractExcelService {


    @SuppressWarnings({"rawtypes"})
    @Override
    public void buildExcelData(Map<String, Object> map, HSSFWorkbook workbook) {
        List dataList = (List) map.get("data");
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= dataList.size(); i++) {
            HSSFRow row = sheet.createRow(i);
            Object data = dataList.get(i - 1);
            Field[] fields = data.getClass().getDeclaredFields();
//            HSSFCellStyle cellStyle = super.getStyle(workbook);
            int j = 0;
            for (Field field : fields) {
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                HSSFCell cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
//                cell.setCellStyle(cellStyle);
                cell.setCellValue(StringUtils.EMPTY);
                try {
                    if (setCellValue(field, data, cell))
                        j++;
                } catch (Exception e) {
                    log.error("set cell value failed", e);
                    throw new BizServiceException(EErrorCode.COMM_INTERNAL_ERROR);
                }
            }
        }
    }

    /**
     * @param field
     * @param data
     * @param cell
     * @return true if field needs to be put into cell, else return false
     */
    private boolean setCellValue(Field field, Object data, HSSFCell cell)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = data.getClass();
        Class<?> type = field.getType();
        cell.setCellStyle(getStyle(type, cell.getSheet().getWorkbook()));
        Method getter = ReflectionUtil.getGetter(clazz, field);
        if (null == getter)
            return false;
        if (String.class.isAssignableFrom(type)) {
            String fieldValue = (String) getter.invoke(data);
            if (null != fieldValue)
                cell.setCellValue(fieldValue);
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            BigDecimal fieldValue = (BigDecimal) getter.invoke(data);
            if (null != fieldValue)
                cell.setCellValue(fieldValue.doubleValue());
        } else if (Date.class.isAssignableFrom(type)) {
            Date fieldValue = (Date) getter.invoke(data);
            if (null != fieldValue)
                cell.setCellValue(fieldValue);
        } else if (Number.class.isAssignableFrom(type)) {
            String fieldValue = String.valueOf(getter.invoke(data));
            if (null != fieldValue)
                cell.setCellValue(fieldValue);
        } else {
            throw new UnsupportedOperationException("field has getter but type is unknown");
        }
        return true;
    }

}
