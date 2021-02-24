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
public class FileDueExport implements IExcelExport<FileDuePoi> {

    List<FileDuePoi> poiList = new ArrayList<>();

    private String title = "证明文件到期提醒";
    String[] headers = {"企业名称","证明文件1名称","证明文件1编号","证明文件1设立日期","证明文件1到期日",
            "证明文件2名称","证明文件2编号","证明文件2设立日期","证明文件2到期日","是否超期"};

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

    @Override
    public boolean containSpecialField(String filedName) {
        return false;
    }

    @Override
    public String getSpecialFieldValue(String filedName) {
        return null;
    }

    @Override
    public List<FileDuePoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<FileDuePoi> data) {
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
