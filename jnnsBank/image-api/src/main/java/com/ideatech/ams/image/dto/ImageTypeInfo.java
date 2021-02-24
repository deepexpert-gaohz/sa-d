package com.ideatech.ams.image.dto;


import com.ideatech.ams.image.enums.BillType;
import com.ideatech.ams.image.enums.CompanyAcctType;
import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Data
public class ImageTypeInfo extends BaseMaintainableDto {
    // 每页显示记录数
    private Integer  limit ;
    // 当前页数
    private Integer  offset ;
    private Long id;
    /**
     * 账户类型
     */
    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;
    /**
     * 业务操作类型
     */
    @Enumerated(EnumType.STRING)
    private BillType operateType;
    /**
     * 存款人性质
     */
    private String depositorType;
    private String depositorTypeCode;
    /**
     * 影像种类
     */
    private String imageName;
    /**
     * 是否必选
     * 0 不是必选 ；1 必选
     */
    private String choose;
    private String value;
    private String string01;
    private String string02;
    private String string03;
    private String string04;
    private String string05;
}
