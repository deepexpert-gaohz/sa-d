package com.ideatech.ams.kyc.service.poi;

import java.util.*;

import com.ideatech.ams.kyc.dto.poi.BeneficiaryPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author wangqingan
 * @version 20/04/2018 4:11 PM
 */
public class BeneficiaryRecordExport implements IExcelExport<BeneficiaryPoi> {
    List<BeneficiaryPoi> poiList = new ArrayList<>();

    private String title = "最终受益人信息";
    String[] headers = {"企业名称", "受益人名称", "受益人类型", "出资比例", "出资总额(万元)", "出资链", "结束穿透原因"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6};

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
    public List<BeneficiaryPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<BeneficiaryPoi> data) {
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
