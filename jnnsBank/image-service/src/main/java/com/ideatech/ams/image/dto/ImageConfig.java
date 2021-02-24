package com.ideatech.ams.image.dto;

import lombok.Data;

@Data
public class ImageConfig {
    private String ip;
    private String socketPort;
    private String username;
    private String password;
    private String serverName;
    private String groupName;
    private String STARTCOLUMN;
    private String modelCode;
    private String filePartName;
}
