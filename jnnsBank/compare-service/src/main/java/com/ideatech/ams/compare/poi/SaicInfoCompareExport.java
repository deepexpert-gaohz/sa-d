package com.ideatech.ams.compare.poi;

import com.ideatech.ams.system.user.poi.SysUserPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jzh on 2019/1/8.
 */
public class SaicInfoCompareExport implements IExcelExport<SaicInfoComparePoi> {

    List<SaicInfoComparePoi> poiList = new ArrayList<>();

    private String title = "工商采集明细";
    String[] headers = {"企业名称"};

    int[] autoSizeColumns = {0};

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
    public List<SaicInfoComparePoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<SaicInfoComparePoi> data) {
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
