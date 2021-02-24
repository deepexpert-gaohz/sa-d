package com.ideatech.ams.dto.saic;

import lombok.Data;

@Data
public class OutAccountBase {
    private Long id;
    private Long saicinfoId;
    private String licensedate;
    private String licensekey;
    private String licenseorg;
    private String licensetype;
    private String name;
}
