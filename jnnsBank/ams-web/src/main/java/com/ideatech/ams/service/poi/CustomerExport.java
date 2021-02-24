package com.ideatech.ams.service.poi;

import com.ideatech.ams.poi.CustomerPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomerExport implements IExcelExport<CustomerPoi> {

    List<CustomerPoi> poiList = new ArrayList<>();

    private String title = "客户基础信息";
    String[] headers = {"ID","客户号","企业名称","机构名称","机构号","迁移机构（内部机构号）"};


    public List<List<ExcelHeadDto>> headerList = new LinkedList<>();

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
    public int[] getAutoSizeColumns() {
        return new int[0];
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
    public List<CustomerPoi> getPoiList() {
        return poiList;
    }

    @Override
    public void setPoiList(List<CustomerPoi> data) {
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
