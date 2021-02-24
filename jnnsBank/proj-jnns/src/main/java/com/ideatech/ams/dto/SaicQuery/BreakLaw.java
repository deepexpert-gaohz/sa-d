package com.ideatech.ams.dto.SaicQuery;

import lombok.Data;

/**
 * 违法
 */
@Data
public class BreakLaw {

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

}
