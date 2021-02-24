package com.ideatech.ams.dto.saic;

import lombok.Data;

@Data
public class Changes {
    private String type;
    private String beforeContent;
    private String afterContent;
    private String changeDate;


}
