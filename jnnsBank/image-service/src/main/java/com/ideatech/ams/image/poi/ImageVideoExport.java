package com.ideatech.ams.image.poi;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jzh
 * @date 2020/5/6.
 */
public class ImageVideoExport implements IExcelExport<ImageVideoPoi> {
    List<ImageVideoPoi> poiList = new ArrayList<>();

    private String title = "双录视频";
    String[] headers = {"双录编号","账号","企业名称","法人名称","客户姓名","业务种类","双录时间","双录柜员","双录方式"};

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
    public List<ImageVideoPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<ImageVideoPoi> data) {
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
