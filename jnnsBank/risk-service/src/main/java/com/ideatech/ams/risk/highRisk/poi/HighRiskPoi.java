package com.ideatech.ams.risk.highRisk.poi;


import com.ideatech.ams.risk.highRisk.entity.HighRisk;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class HighRiskPoi implements IExcelExport<HighRisk> {
    List<HighRisk> poiList = new ArrayList<>();

    private String title = "高风险数据";
    String[] headers = {"客户号","企业名称","法人姓名","企业证件种类","企业证件号","法人证件种类","法人证件号码"};

    private final static int DATA_BEGIN_ROW = 0;

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
    public boolean containSpecialField(String filedName) {
        return false;
    }

    @Override
    public String getSpecialFieldValue(String filedName) {
        return null;
    }

    @Override
    public void setPoiList(List<HighRisk> data) {

    }

    @Override
    public Integer getDataBeginRow() {
        return null;
    }

    @Override
    public int getColSize() {
        return 0;
    }




}
