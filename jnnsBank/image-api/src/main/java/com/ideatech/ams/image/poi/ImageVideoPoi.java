package com.ideatech.ams.image.poi;

import lombok.Data;

/**
 * @author jzh
 * @date 2020/5/6.
 */

@Data
public class ImageVideoPoi {
    /**
     * 双录编号
     */
    private String recordsNo;
    /**
     * 账号
     */
    private String acctNo;

    /**
     *存款人名称
     */
    private String depositorName;
    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 双录时间
     */
    private String dateTime;
    /**
     * 双录柜员
     */
    private String username;

    /**
     * 双录方式
     */
    private String recordType;
}
