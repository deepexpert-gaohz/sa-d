package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author wangqingan
 * @version 12/02/2018 10:50 AM
 */
@Data
public class SupplementQueryDto extends BaseMaintainableDto {

//    @NotNull(message = "补录用户对应工商ID不能为空")
    private Long id;

    @NotEmpty(message="补录用户类型不能为空")
    private String type;

    @NotNull(message = "补录用户对应工商ID不能为空")
    private Long saicInfoId;

}
