package com.ideatech.ams.account.service.poi;

import com.ideatech.ams.account.dto.poi.CompanyAccountPoi;
import com.ideatech.ams.account.dto.poi.OperatorIdcardDuePoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clx
 * @date 2019/4/24.
 */
public class CompanyAccountPoiExport implements IExcelExport<CompanyAccountPoi> {

    List<CompanyAccountPoi> poiList = new ArrayList<>();

    private String title = "账户信息统计列表";
    String[] headers = {"账号","账户名称","账户性质","账户状态","开户行","网点号","本异地标识","开户日期","销户日期"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6,7,8};

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
    public List<CompanyAccountPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<CompanyAccountPoi> data) {
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
