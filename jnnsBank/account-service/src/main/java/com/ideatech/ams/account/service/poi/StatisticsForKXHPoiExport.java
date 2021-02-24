package com.ideatech.ams.account.service.poi;

import com.ideatech.ams.account.dto.poi.StatisticsForDatePoi;
import com.ideatech.ams.account.dto.poi.StatisticsForKXHPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clx
 * @date 2019/4/24.
 */
public class StatisticsForKXHPoiExport implements IExcelExport<StatisticsForKXHPoi> {

    List<StatisticsForKXHPoi> poiList = new ArrayList<>();

    private String title = "开销户统计列表";
    String[] headers = {"账号","企业名称","账户性质","操作类型","网点机构号","账管上报状态","信用代码证上报状态","本异地标识","申请日期","申请人","上报操作方式"};

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
    public List<StatisticsForKXHPoi> getPoiList() {
        return poiList;
    }

    @Override
    public void setPoiList(List<StatisticsForKXHPoi> data) {
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
