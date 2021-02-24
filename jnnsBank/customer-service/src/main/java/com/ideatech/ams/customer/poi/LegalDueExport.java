package com.ideatech.ams.customer.poi;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jzh
 * @date 2019/4/24.
 */
public class LegalDueExport implements IExcelExport<LegalDuePoi> {

    List<LegalDuePoi> poiList = new ArrayList<>();

    private String title = "法人证件到期提醒";
    String[] headers = {"企业名称","法人（负责人）类型","法人（负责人）姓名","证件类型","证件号码","法人（负责人）证件到期日","是否超期"};

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

    @Override
    public boolean containSpecialField(String filedName) {
        return false;
    }

    @Override
    public String getSpecialFieldValue(String filedName) {
        return null;
    }

    @Override
    public List<LegalDuePoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<LegalDuePoi> data) {
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
