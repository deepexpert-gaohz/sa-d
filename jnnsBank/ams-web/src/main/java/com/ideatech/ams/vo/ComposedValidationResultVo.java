package com.ideatech.ams.vo;

import com.ideatech.ams.kyc.dto.SaicIdpInfo;
import com.ideatech.ams.pbc.dto.AmsAccountInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liangding
 * @create 2018-08-25 上午12:27
 **/
@Data
public class ComposedValidationResultVo implements Serializable {

    /**
     * 工商校验是否通过
     */
    private Boolean saicCheckPassed;

    /**
     * 工商校验通过未通过的原因
     */
    private String saicValidationMessage;

    /**
     * 工商信息
     */
    private SaicIdpInfo saicIdpInfo;

    /**
     * 人行校验是否通过
     */
    private Boolean pbcCheckPassed;

    /**
     * 人行校验通过未通过的原因
     */
    private String pbcValidationMessage;

    /**
     * 人行信息
     */
    private AmsAccountInfo amsAccountInfo;
    /**
     * 黑名单校验是否通过
     */
    private Boolean blackCheckPassed;

    /**
     * 黑名单校验通过未通过的原因
     */
    private String blackValidationMessage;
}
