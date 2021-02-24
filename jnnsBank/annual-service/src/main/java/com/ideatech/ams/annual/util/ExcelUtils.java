/**
 * 
 */
package com.ideatech.ams.annual.util;

import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

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
public class ExcelUtils {
	
	/**
	 * @param file
	 * @param clazz
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readFile(File file, Class<T> clazz, ExcelReadConfig config) throws Exception {
		return readFile(new FileInputStream(file), clazz, config);
	}
	
	/**
	 * @param stream
	 * @param clazz
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readFile(InputStream stream, Class<T> clazz, ExcelReadConfig config) throws Exception {
		
		Workbook workbook = null;
		List<T> reuslt = new ArrayList<T>();
		try {
			workbook = Workbook.getWorkbook(stream);
			Sheet sheets[] = workbook.getSheets();// excel多个sheet页面
			for (int i = config.getStartSheetNo(); i < sheets.length; i++) {
				Sheet sheet = sheets[i];
				int rowNum = sheet.getRows();
				if (rowNum == 0 || StringUtils.isEmpty(sheet.getCell(0, 0).getContents())) {
					continue;
				}
				for (int j = config.getStartRowNo(); j < rowNum; j++) {
					boolean isBlankRow = false;
					T obj = clazz.newInstance();
					Set<Integer> columns = config.getMapping().keySet();
					for (Integer column : columns) {
						BeanUtils.setProperty(obj, config.getMapping().get(column), sheet.getCell(column, j).getContents());
						if(sheet.getCell(column, j).getContents()!=null&&!"".equals(sheet.getCell(column, j).getContents())){
							isBlankRow = true;
						}
					}
					if(isBlankRow){
						reuslt.add(obj);
					}
				}
			}
		}catch (Exception e) {
			throw e;
		}finally {
			if(stream != null){
				IOUtils.closeQuietly(stream);
			}
			if(workbook != null){
				workbook.close();
			}
		}
		return reuslt;
		
	}

	public static List<String> readFirstLine(File file) throws Exception {
		Workbook workbook = null;
		InputStream stream = null;
		List<String> reuslt = new ArrayList<String>();
		try {
			stream= new FileInputStream(file);
			workbook = Workbook.getWorkbook(stream);
			Sheet sheet = workbook.getSheets()[0];
			for (int i = 0; i < sheet.getColumns(); i++) {
				if(!StringUtils.isBlank(sheet.getCell(i,0).getContents())){
					reuslt.add(sheet.getCell(i,0).getContents().trim());
				}
			}
		}catch (Exception e) {
			throw new Exception("读取文件"+file.getName()+"失败");
		}finally {
			if(stream != null){
				IOUtils.closeQuietly(stream);
			}
			if(workbook != null){
				workbook.close();
			}
		}
		return reuslt;
	}

}
