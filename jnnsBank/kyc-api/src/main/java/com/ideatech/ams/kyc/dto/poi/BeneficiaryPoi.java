package com.ideatech.ams.kyc.dto.poi;

import lombok.Data;

/**
 * 最终受益人
 * @author wangqingan
 * @version 20/04/2018 2:39 PM
 */
@Data
public class BeneficiaryPoi {

    /**目标企业*/
    private String company;

    /**受益人名称*/
    private String name;

    /**受益人类型*/
    private String type;

    /**出资比例*/
    private float capitalpercent;

    /**出资额*/
    private String capital;

    /**出资链*/
    private String capitalchain;

    /**最终的理由*/
    private String lastreason;
}
