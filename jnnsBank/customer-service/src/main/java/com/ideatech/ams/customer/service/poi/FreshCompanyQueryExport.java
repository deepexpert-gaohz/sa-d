package com.ideatech.ams.customer.service.poi;

import com.ideatech.ams.customer.dto.poi.FreshCompanyPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FreshCompanyQueryExport implements IExcelExport<FreshCompanyPoi> {
    List<FreshCompanyPoi> poiList = new ArrayList<>();

    private String title = "新增企业信息";
    String[] headers = {"公司名称","法人名称","成立日期","企业状态","社会统一信用代码","注册资本","公司地址","省份","市区","地区"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6,7,8,9,10};

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
    public List<FreshCompanyPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<FreshCompanyPoi> data) {
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
