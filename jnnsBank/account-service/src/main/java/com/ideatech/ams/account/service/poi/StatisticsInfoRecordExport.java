package com.ideatech.ams.account.service.poi;

import com.ideatech.ams.account.dto.poi.StatisticsInfoPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description 账户信息统计excel导出
 * @Author wanghongjie
 * @Date 2019/1/9
 **/
public class StatisticsInfoRecordExport implements IExcelExport<StatisticsInfoPoi> {
    List<StatisticsInfoPoi> poiList = new ArrayList<>();

    private String title = "账户信息统计";
    String[] headers = {"机构名称","正常账号数量","销户账号数量","久悬账号数量"};

    private final static int DATA_BEGIN_ROW = 0;

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
    public List<StatisticsInfoPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<StatisticsInfoPoi> data) {
        this.poiList = data;
    }

    @Override
    public Integer getDataBeginRow() {
        return DATA_BEGIN_ROW;
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
