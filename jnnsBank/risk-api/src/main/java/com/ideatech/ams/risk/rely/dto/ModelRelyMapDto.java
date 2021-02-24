package com.ideatech.ams.risk.rely.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModelRelyMapDto {


    @JsonProperty(value = "name")
    private String modelTable;

    private List<ModelRelyChilderDto> children = new ArrayList<>();


}
