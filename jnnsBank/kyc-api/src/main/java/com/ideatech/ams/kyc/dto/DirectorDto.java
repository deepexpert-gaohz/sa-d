package com.ideatech.ams.kyc.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class DirectorDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 内部工商ID
     */
    private Long saicinfoId;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 证件到期日
     */
    private String idcarddue;

    /**
     * 证件号码
     */
    private String idcardno;

    /**
     * 证件类型
     */
    private String idcardtype;

    /**
     * 董事姓名
     */
    private String name;

    /**
     * 职位
     */
    private String position;

    /**
     * 性别
     */
    private String sex;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 序列号 来自IDP
     */
    private Integer index;

}