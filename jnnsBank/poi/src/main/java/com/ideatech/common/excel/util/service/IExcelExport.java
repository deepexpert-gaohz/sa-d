package com.ideatech.common.excel.util.service;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

/**
 * @author wangqingan
 * @version 20/04/2018 2:24 PM
 */
public interface IExcelExport<T> {

	/**
     * 获取Excel的Header
     *
     * @return
     */
	List<List<ExcelHeadDto>> getHeader();

    /**
     * 返回Excel的header大小
     *
     * @return
     */
    @Deprecated
    int getHeaderSize();

    /**
     * zi
     * @return
     */
    int[] getAutoSizeColumns();

    /**
     * 导出的标题
     *
     * @return
     */
    String getTitle();

    /**
     * 是否包含 特殊的Field的处理
     *
     * @param filedName
     * @return
     */
    boolean containSpecialField(String filedName);

    /**
     * 获取 特殊的Field的处理后的值
     *
     * @param filedName
     * @return
     */
    String getSpecialFieldValue(String filedName);

    /**
     * 获取数据源
     *
     * @return
     */
    List<T> getPoiList();

    /**
     * 设置数据源
     */
    void setPoiList(List<T> data);

	/**
	 * 行数开始行
	 */
	Integer getDataBeginRow();

	/**
	 * 返回最大列数
	 * @return
	 */
	int getColSize();

}
