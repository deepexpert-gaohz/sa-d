package com.ideatech.ams.annual.poi;

import com.ideatech.ams.annual.dto.poi.AnnualStatisticsPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AnnualStatisticsExport implements IExcelExport<AnnualStatisticsPoi> {

    List<AnnualStatisticsPoi> poiList = new ArrayList<>();

    private String title = "基础信息";
    String[] headers = {"机构","账号总数","通过总数","通过率"};

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
    public List<AnnualStatisticsPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<AnnualStatisticsPoi> data) {
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
