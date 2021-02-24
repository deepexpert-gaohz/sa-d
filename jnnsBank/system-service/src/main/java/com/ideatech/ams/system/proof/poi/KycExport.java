package com.ideatech.ams.system.proof.poi;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class KycExport  implements IExcelExport<KycPoi> {
    List<KycPoi> poiList = new ArrayList<>();

    private String title = "客户尽调统计明细";
    String[] headers = {"账号","账户名称","账户性质","开户行","是否客户尽调","尽调机构","操作员","尽调时间"};

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
    public List<KycPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<KycPoi> data) {
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
