package com.ideatech.ams.customer.poi;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SaicMonitorExport implements IExcelExport<SaicMonitorPoi> {

    List<SaicMonitorPoi> poiList = new ArrayList<>();

    private String title = "工商调用记录";
    String[] headers = {"调用时间","调用机构","调用用户","调用企业名称","工商注册号","调用类型"};

    int[] autoSizeColumns = {0,1,2,3,4,5};

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
    public List<SaicMonitorPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<SaicMonitorPoi> data) {
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
