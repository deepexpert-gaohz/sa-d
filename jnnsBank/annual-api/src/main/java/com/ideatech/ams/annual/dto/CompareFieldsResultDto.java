package com.ideatech.ams.annual.dto;

import com.ideatech.ams.annual.enums.CompareFieldEnum;
import lombok.Data;

/**
 * Created by jzh on 2019/1/3.
 */
@Data
public class CompareFieldsResultDto {
    private Long id;

    private CompareFieldEnum compareFieldEnum;

    private String fieldName;

    /**
     * 是否启用
     */
    private boolean active;

    private boolean lockFiled;

    private Long taskId;

    private boolean coreActive;

    private boolean coreNullpass;

    private boolean pbcActive;

    private boolean pbcNullpass;

    private boolean saicActive;

    private boolean saicNullpass;
}
