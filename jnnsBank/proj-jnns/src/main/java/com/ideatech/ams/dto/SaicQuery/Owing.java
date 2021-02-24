package com.ideatech.ams.dto.SaicQuery;

import lombok.Data;

/**
 * 欠税名单
 */
@Data
public class Owing {

    private int keyid;
    private int typet;
    private String title;
    private String name;
    private String cid;
    private String organ;
    private String taxtype;
    private String period;
    private String balance;
    private String datatinme;
    private String remark;
}
