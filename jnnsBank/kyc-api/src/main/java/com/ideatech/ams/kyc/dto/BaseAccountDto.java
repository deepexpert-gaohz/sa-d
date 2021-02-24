package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class BaseAccountDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 审批日期
     */
    private String licensedate;

    /**
     * 基本户许可证号
     */
    private String licensekey;

    /**
     * 审批机关
     */
    private String licenseorg;

    /**
     * 许可类型
     */
    private String licensetype;

    /**
     * 单位名称/账户名称
     */
    private String name;

}