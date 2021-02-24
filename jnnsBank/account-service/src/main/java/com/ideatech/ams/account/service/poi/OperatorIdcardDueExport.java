package com.ideatech.ams.account.service.poi;

import com.ideatech.ams.account.dto.poi.OperatorIdcardDuePoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jzh
 * @date 2019/4/24.
 */
public class OperatorIdcardDueExport implements IExcelExport<OperatorIdcardDuePoi> {

    List<OperatorIdcardDuePoi> poiList = new ArrayList<>();

    private String title = "经办人证件到期提醒";
    String[] headers = {"账号","账户名称","人行机构号","开户行","证件类型",
            "证件编号","开户日期","到期日期","是否超期"};

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
    public List<OperatorIdcardDuePoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<OperatorIdcardDuePoi> data) {
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
