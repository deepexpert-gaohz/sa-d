package com.ideatech.ams.customer.service.poi;

import com.ideatech.ams.customer.dto.poi.IllegalQueryPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IllegalQueryExport implements IExcelExport<IllegalQueryPoi> {

    List<IllegalQueryPoi> poiList = new ArrayList<>();

    private String title = "基础信息";
    String[] headers = {"机构号","企业名称","工商状态","注册号","营业执照到期日","营业执照到期状态","是否经营异常","是否严重违法"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6,7};

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
        return 0;
    }

    @Override
    public int[] getAutoSizeColumns() {
        return autoSizeColumns;
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
    public List<IllegalQueryPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<IllegalQueryPoi> data) {
        this.poiList = data;
    }

    @Override
    public Integer getDataBeginRow() {
        return 1;
    }

    @Override
    public int getColSize() {
        return 0;
    }
}
