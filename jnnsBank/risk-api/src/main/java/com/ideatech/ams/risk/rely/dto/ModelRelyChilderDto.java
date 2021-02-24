package com.ideatech.ams.risk.rely.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModelRelyChilderDto{
    @JsonProperty(value = "name")
    private String relyTable;


}
