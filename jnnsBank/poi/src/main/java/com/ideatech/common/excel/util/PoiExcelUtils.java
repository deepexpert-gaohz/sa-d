/**
 * 
 */
package com.ideatech.common.excel.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhailiang
 *
 */
public class PoiExcelUtils {

	private final static String EXCEL2003 = ".xls"; // 2003- 版本的excel
	private final static String EXCEL2007 = ".xlsx"; // 2007+ 版本的excel

	/**
	 * @param file
	 * @param clazz
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readFile(File file, Class<T> clazz, ExcelReadConfig config) throws Exception {
		return readFile(new FileInputStream(file), clazz, config, file.getName());
	}

	/**
	 * @param stream
	 * @param clazz
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readFile(InputStream stream, Class<T> clazz, ExcelReadConfig config, String fileName)
			throws Exception {
		fileName = fileName.toLowerCase();
		Workbook workbook = null;
		List<T> reuslt = new ArrayList<T>();
		try {
			if (fileName.endsWith(EXCEL2003)) {
				workbook = new HSSFWorkbook(stream);
			} else if (fileName.endsWith(EXCEL2007)) {
				workbook = new XSSFWorkbook(stream);
			} else
				return null;
			int sheets = workbook.getNumberOfSheets();// excel多个sheet页面
			for (int i = config.getStartSheetNo(); i < sheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				int rowNum = sheet.getLastRowNum();
				if (rowNum == 0 || StringUtils.isEmpty(sheet.getRow(0).getCell(0).getStringCellValue())) {
					continue;
				}
				for (int j = config.getStartRowNo(); j <= rowNum; j++) {
					T obj = clazz.newInstance();
					Set<Integer> columns = config.getMapping().keySet();
					for (Integer column : columns) {
						String value = getValue(sheet.getRow(j).getCell(column));
						BeanUtils.setProperty(obj, config.getMapping().get(column), value);
					}
					reuslt.add(obj);
				}
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return reuslt;

	}

	public static String getValue(Cell cell) {
		String value = "";
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				value = String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//				value = String.valueOf(cell.getNumericCellValue());
				//start poi对excel数值型处理，会一律将数字转换成double型，需要处理
				double doubleVal = cell.getNumericCellValue();
				long longVal = Math.round(cell.getNumericCellValue());  
			    if (Double.parseDouble(longVal + ".0") == doubleVal)  
			        value = String.valueOf(longVal);  
			    else  
			    	value = String.valueOf(doubleVal); 
			    
			    //end
			} else {
				value = String.valueOf(cell.getStringCellValue());
			}
		}
		return StringUtils.trim(value);

	}

	public static List<String> readFistLine(File file) throws Exception {
		String fileName = file.getName().toLowerCase();
		Workbook workbook = null;
		InputStream stream = null;
		List<String> reuslt = null;
		try {
			stream = new FileInputStream(file);
			if (fileName.endsWith(EXCEL2003)) {
				workbook = new HSSFWorkbook(stream);
			} else if (fileName.endsWith(EXCEL2007)) {
				workbook = new XSSFWorkbook(stream);
			} else
				return null;
			reuslt = new ArrayList<String>();
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(sheet.getFirstRowNum());

			for (Cell cell : row) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				reuslt.add(cell.getStringCellValue().trim());
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return reuslt;
	}

	public static List<String> readFistLine(InputStream s, String filenameExtension) throws Exception {
		Workbook workbook = null;
		if("xls".equalsIgnoreCase(filenameExtension)) {
			workbook = new HSSFWorkbook(s);
		} else if("xlsx".equalsIgnoreCase(filenameExtension)) {
			workbook = new XSSFWorkbook(s);
		}
		
		List<String> reuslt = null;
		try {
			reuslt = new ArrayList<String>();
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(sheet.getFirstRowNum());

			for (Cell cell : row) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				reuslt.add(cell.getStringCellValue().trim());
			}
		} finally {
			IOUtils.closeQuietly(s);
		}
		return reuslt;
	}


	/**
	 * @param stream
	 * @param clazz
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readFile(InputStream stream, Class<T> clazz, ExcelReadConfig config)
			throws Exception {
		Workbook workbook = null;
		List<T> reuslt = new ArrayList<T>();
		try {
			workbook = new HSSFWorkbook(stream);
			int sheets = workbook.getNumberOfSheets();// excel多个sheet页面
			for (int i = config.getStartSheetNo(); i < sheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				int rowNum = sheet.getLastRowNum();
				if (rowNum == 0 || StringUtils.isEmpty(sheet.getRow(0).getCell(0).getStringCellValue())) {
					continue;
				}
				for (int j = config.getStartRowNo(); j <= rowNum; j++) {
					T obj = clazz.newInstance();
					Set<Integer> columns = config.getMapping().keySet();
					for (Integer column : columns) {
						String value = getValue(sheet.getRow(j).getCell(column));
						BeanUtils.setProperty(obj, config.getMapping().get(column), value);
					}
					reuslt.add(obj);
				}
			}
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return reuslt;

	}

	/**
	 * Excel 列编号字母转数字
	 * @param colStr
	 * @param length
	 * @return
	 */
	public static int excelColStrToNum(String colStr, int length) {
		int num = 0;
		int result = 0;
		for(int i = 0; i < length; i++) {
			char ch = colStr.charAt(length - i - 1);
			num = (int)(ch - 'A' + 1) ;
			num *= Math.pow(26, i);
			result += num;
		}
		return result;
	}

	/**
	 * Excel 列编号数字转字母
	 * @param columnIndex
	 * @return
	 */
	public static String excelColIndexToStr(int columnIndex) {
		if (columnIndex <= 0) {
			return null;
		}
		String columnStr = "";
		columnIndex--;
		do {
			if (columnStr.length() > 0) {
				columnIndex--;
			}
			columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
			columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
		} while (columnIndex > 0);
		return columnStr;
	}
}
