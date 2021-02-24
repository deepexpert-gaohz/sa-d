package com.ideatech.ams.system.annotation.poi;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StatisticeExport implements IExcelExport<StatisticePoi> {

    List<StatisticePoi> poiList = new ArrayList<>();

    private String title = "监控记录";
    String[] headers = {"接口调用类型","账号","校验结果","调用日期","报送类型","传入参数","返回结果","错误信息"};

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
    public List<StatisticePoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<StatisticePoi> data) {
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
