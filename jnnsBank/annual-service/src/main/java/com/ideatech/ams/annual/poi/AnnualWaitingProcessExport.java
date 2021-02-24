package com.ideatech.ams.annual.poi;

import com.ideatech.ams.annual.dto.poi.AnnualWaitingProcessPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 年检待处理导出工具类
 * @author jzh
 * @date 2019/4/25.
 */
public class AnnualWaitingProcessExport implements IExcelExport<AnnualWaitingProcessPoi> {

    List<AnnualWaitingProcessPoi> poiList = new ArrayList<>();

    private String title = "年检待处理";
    String[] headers = {"账号","企业名称","人行机构号","网点机构号",
            "单边状态","异常状态","工商状态","数据一致性","处理状态","账户性质"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6,7,8,9};

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
    public List<AnnualWaitingProcessPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<AnnualWaitingProcessPoi> data) {
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
