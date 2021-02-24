package com.ideatech.ams.customer.poi;

import lombok.Data;

/**
 * @author jzh
 * @date 2019/4/24.
 */
@Data
public class FileDuePoi {

    /**
     * 企业名称
     */
    private String depositorName;

    /**
     * 证明文件1名称
     */
    private String fileType;

    /**
     * 证明文件1编号
     */
    private String fileNo;

    /**
     * 证明文件1设立日期
     */
    private String fileSetupDate;

    /**
     * 证明文件1到期日
     */
    private String fileDue;

    /**
     * 证明文件2名称
     */
    private String fileType2;

    /**
     * 证明文件2编号
     */
    private String fileNo2;

    /**
     * 证明文件2设立日期
     */
    private String fileSetupDate2;

    /**
     * 证明文件2到期日
     */
    private String fileDue2;

    /**
     * 是否超期
     */
    private String isFileDueOver;
}
