package com.ideatech.ams.annual.service.poi;

import com.ideatech.ams.annual.dto.poi.AnnualCompanyPoi;
import com.ideatech.ams.annual.dto.poi.AnnualCorePoi;
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
public class AnnualCoreRecordExport implements IExcelExport<AnnualCorePoi> {
    List<AnnualCorePoi> poiList = new ArrayList<>();

    private String title = "年检核心模板文件";
    String[] headers = {"账号","企业名称","法人姓名","工商注册号","组织机构代码证","机构代码(行内机构号)","注册资金","注册地址","法人证件种类","法人证件号码","注册币种","国税登记证","地税登记证","经营范围","账户性质"};

    private final static int DATA_BEGIN_ROW = 0;

    int[] autoSizeColumns = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};

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
    public List<AnnualCorePoi> getPoiList() {
        return this.poiList;
    }

    @Override
    public void setPoiList(List<AnnualCorePoi> data) {
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
