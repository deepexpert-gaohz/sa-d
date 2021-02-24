package com.ideatech.ams.kyc.service.poi;

import com.ideatech.ams.kyc.dto.poi.BeneficiaryPoi;
import com.ideatech.ams.kyc.dto.poi.SaicHistoryPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangqingan
 * @version 20/04/2018 4:11 PM
 */
public class SaicHistoryPoiExport implements IExcelExport<SaicHistoryPoi> {
    List<SaicHistoryPoi> poiList = new ArrayList<>();

    private String title = "客户尽调历史查询";
    String[] headers = {"查询时间", "企业名称", "尽调机构", "操作员"};

    int[] autoSizeColumns = {0,1,2,3};

	List<List<ExcelHeadDto>> headerList = new LinkedList<>();

	@Override
	public List<List<ExcelHeadDto>> getHeader() {
		List<ExcelHeadDto> list = new LinkedList<>();
		for (String header : headers) {
			list.add(new ExcelHeadDto(header,false));
		}
		headerList.add(list);
		return headerList;
	}

	@Override
    public int getHeaderSize() {
		return headers.length;
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
    public List<SaicHistoryPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<SaicHistoryPoi> data) {
        this.poiList = data;
    }

	@Override
	public Integer getDataBeginRow() {
		return 1;
	}

	@Override
	public int getColSize() {
		return headers.length;
	}

	@Override
    public int[] getAutoSizeColumns() {
        return autoSizeColumns;
    }
}
