package com.ideatech.ams.system.area.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * Created by hammer on 2018/2/11.
 */
@Data
public class AreaDto extends BaseMaintainableDto {

    private String areaCode;

    private String areaName;

    private Integer level;

    private String regCode;

    private String zipcode;
}
