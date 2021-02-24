package com.ideatech.ams.dto.SaicQuery;


import lombok.Data;

/**
 * 罪犯与违法
 */
@Data
public class OffenderAndBreakTLaw {
//行政违法
    private int keyid;
    private int typet;
    private String title;
    private String name;
    private String cid;
    private String organ;
    private String caseno;
    private String origin;
    private String eresult;
    private String judegeresult;
    private String datatype;
    private String datatinme;
    private String remark;

//罪犯嫌疑人名称

    private String ztitle;
    private String zorgan;
    private String zcaseno;
    private String zorigin;
    private String result;
    private String zdatatinme;


}
