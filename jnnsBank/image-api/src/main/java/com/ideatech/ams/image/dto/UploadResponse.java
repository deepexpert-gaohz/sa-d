package com.ideatech.ams.image.dto;

import lombok.Data;

@Data
public class UploadResponse {
    private String name;
    private boolean state;

    private String extra;
}
