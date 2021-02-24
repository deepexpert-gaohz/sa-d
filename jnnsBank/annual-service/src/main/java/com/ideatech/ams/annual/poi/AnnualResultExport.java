package com.ideatech.ams.annual.poi;

import com.ideatech.ams.annual.dto.poi.AnnualResultPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 2018年8月31日09:42:52
 * @author van
 * @date 2018/8/31 9:42
 */
public class AnnualResultExport implements IExcelExport<AnnualResultPoi> {

	List<AnnualResultPoi> poiList = new ArrayList<>();

	private final static int DATA_BEGIN_ROW = 3;

	private final static int MAX_COL_SIZE = 19;

	private String title = "";

	private String[] headers1 = {};

	private String[] headers2 = {"账号", "核心账户信息", "工商信息", "人行账管系统信息", "账户性质","机构号", "单边类型", "异常状态", "是否黑名单", "工商状态", "比对结果",
			"年检结果","处理人"};

	private String[] headers3 = {};

	private int active;

//	int[] autoSizeColumns = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
	int[] autoSizeColumns = {};

	List<List<ExcelHeadDto>> headerList = new LinkedList<>();

	public void setHeaders3(String[] header){
		this.headers3 = header;
	}

	public void setActive(int active){
		this.active = active;
	}

	public void setTitle(String title) {
		this.title = title;
		this.headers1 = new String[]{title};
	}

	@Override
	public List<List<ExcelHeadDto>> getHeader() {
		List<ExcelHeadDto> list = new LinkedList<>();
		for (String header : headers1) {
			list.add(new ExcelHeadDto(header, true, 1, MAX_COL_SIZE));
		}
		headerList.add(list);

		list = new LinkedList<>();
		for (int i=0;i<headers2.length;i++) {
			String header = headers2[i];
			if (i <= 3 && i > 0) {
				list.add(new ExcelHeadDto(header, true, 1, active));
			} else {
				list.add(new ExcelHeadDto(header, true, 2, 1));
			}
		}
		headerList.add(list);

		list = new LinkedList<>();
		for (String header : headers3) {
			list.add(new ExcelHeadDto(header, false));
		}
		headerList.add(list);

		return headerList;
	}

	@Override
	public int getHeaderSize() {
		return headers1.length;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public boolean containSpecialField(String filedName) {
		return false;
	}

	@Override
	public String getSpecialFieldValue(String filedName) {
		return null;
	}

	@Override
	public List<AnnualResultPoi> getPoiList() {
		return this.poiList;
	}

	@Override
	public void setPoiList(List<AnnualResultPoi> data) {
		this.poiList = data;
	}

	@Override
	public Integer getDataBeginRow() {
		return DATA_BEGIN_ROW;
	}

	@Override
	public int getColSize() {
		return MAX_COL_SIZE;
	}

	@Override
	public int[] getAutoSizeColumns() {
		return autoSizeColumns;
	}
}
