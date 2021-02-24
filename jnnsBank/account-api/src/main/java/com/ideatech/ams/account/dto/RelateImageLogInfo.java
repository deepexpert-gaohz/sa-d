package com.ideatech.ams.account.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

/**
 * 关联企业信息
 *
 * @author RJQ
 */
@Data
public class RelateImageLogInfo extends BaseMaintainableDto {

    private Long id;
    
    private Long accountId;

    /**
     * 关联证件类型
     */
    private String imageType;

    /**
     * 企业证件名称
     */
    private String imageName;

    /**
     * 企业证件号码
     */
    private String imageNo;

    /**
     * 企业证件到日期
     */
    private String imageDue;
}
