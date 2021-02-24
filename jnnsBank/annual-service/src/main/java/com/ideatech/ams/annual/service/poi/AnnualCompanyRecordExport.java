package com.ideatech.ams.annual.service.poi;

import com.ideatech.ams.annual.dto.poi.AnnualCompanyPoi;
import com.ideatech.ams.kyc.dto.poi.SaicPoi;
import com.ideatech.common.excel.dto.ExcelHeadDto;
import com.ideatech.common.excel.util.service.IExcelExport;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description 年检企业
 * @Author wanghongjie
 * @Date 2018/8/31
 **/

@Data
public class AnnualCompanyRecordExport  implements IExcelExport<AnnualCompanyPoi> {
    List<AnnualCompanyPoi> poiList = new ArrayList<>();

    private String title = "基础信息";
    String[] headers = {"公司名称"};

    private final static int DATA_BEGIN_ROW = 0;

    int[] autoSizeColumns = {0};

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
        return headers.length;
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
    public List<AnnualCompanyPoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<AnnualCompanyPoi> data) {
        this.poiList = data;
    }

    @Override
    public Integer getDataBeginRow() {
        return DATA_BEGIN_ROW;
    }

    @Override
    public int getColSize() {
        return headers.length;
    }

    @Override
    public int[] getAutoSizeColumns() {
        return autoSizeColumns;
    }
}
