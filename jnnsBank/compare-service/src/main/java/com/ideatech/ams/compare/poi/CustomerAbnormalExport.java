package com.ideatech.ams.compare.poi;

import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jzh
 * @date 2019/6/17.
 */
public class CustomerAbnormalExport implements IExcelExport<CustomerAbnormalPoi> {

    List<CustomerAbnormalPoi> poiList = new ArrayList<>();

    private String title = "客户异动信息";
    String[] headers = {"客户名称","银行机构代码","银行机构名称","严重违法","经营异常","经营到期","工商状态异常","登记信息异动","系统异动时间","处理状态","处理时间","处理人","短信发送状态"};

    int[] autoSizeColumns = {0,1,2,3,4,5,6,7,8,9,10,11,12};

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
    public List<CustomerAbnormalPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<CustomerAbnormalPoi> data) {
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
