package com.ideatech.ams.kyc.service.poi;

import java.util.*;

import com.ideatech.ams.kyc.dto.poi.SaicPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author wangqingan
 * @version 20/04/2018 2:28 PM
 */
public class SaicRecordExport implements IExcelExport<SaicPoi> {
    List<SaicPoi> poiList = new ArrayList<>();

    private String title = "基础信息";
    String[] headers = {"公司名称", "社会统一信用代码", "注册号", "类型", "法定代表人", "注册资本", "成立日期",
            "营业期限起始日期","营业期限终止日期","登记机关","核准日期","登记状态","住所地址","经营范围","注销或吊销日期",
            "董事会人员","监事人员","高管人员"};

    int[] autoSizeColumns = {0,1,2,3,9,12,15,16,17};

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
    public List<SaicPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<SaicPoi> data) {
        this.poiList = data;
    }

	@Override
	public Integer getDataBeginRow() {
		return null;
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
