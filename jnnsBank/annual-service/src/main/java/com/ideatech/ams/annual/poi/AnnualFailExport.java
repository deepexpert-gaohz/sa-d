package com.ideatech.ams.annual.poi;

import com.ideatech.ams.annual.dto.poi.AnnualFailPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 年检失败导出工具类
 * @author jzh
 * @date 2019/4/25.
 */
public class AnnualFailExport implements IExcelExport<AnnualFailPoi> {

    List<AnnualFailPoi> poiList = new ArrayList<>();

    private String title = "年检失败";
    String[] headers = {"账号","企业名称","人行机构号","网点机构号","异常状态","失败原因","账户性质"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6};

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

    public void setTitle(String title) {
        this.title = title;
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
    public List<AnnualFailPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<AnnualFailPoi> data) {
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
