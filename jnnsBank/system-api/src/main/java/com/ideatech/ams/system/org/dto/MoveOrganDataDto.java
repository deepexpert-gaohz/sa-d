package com.ideatech.ams.system.org.dto;

import com.ideatech.common.dto.BaseMaintainableDto;
import lombok.Data;

@Data
public class MoveOrganDataDto extends BaseMaintainableDto {

    private Long id;

    /**
     * 迁移机构
     */
    private Long fromOrgId;

    /**
     * 迁移种类  0：机构迁移  1：数据迁移
     */
    private String moveType;

    /**
     * 迁移数据类型
     * 0:全部迁移  1、部分迁移
     */
    private String moveData;

    /**
     * to迁移机构
     */
    private Long toOrgId;

    /**
     * 是否含下属机构数据
     */
    private String containOrgan;

    /**
     * 到处数据的类型 1、用户  2、客户  3、客户
     */
    private String moveTemplate;

}
