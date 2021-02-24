package com.ideatech.ams.service.poi;

import com.ideatech.ams.poi.UserPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserExport implements IExcelExport<UserPoi> {

    List<UserPoi> poiList = new ArrayList<>();

    private String title = "用户基础信息";
    String[] headers = {"中文姓名","用户名","迁移机构（内部机构号）"};

    int[] autoSizeColumns = {0,1,2};

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
    public List<UserPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<UserPoi> data) {
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
