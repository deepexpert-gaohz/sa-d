package com.ideatech.ams.apply.cryptography;

import lombok.Data;

import java.util.Date;

/**
 * @Description 影像资料的返回对象
 * @Author wanghongjie
 * @Date 2018/10/21
 **/
@Data
public class CryptoImagesVo {
    /**
     * 记录ID
     */
    private Long id;
    /**
     * 预约编号 : APPLY447389244407676928
     * 易账户生成的预约号，具有唯一性，后续会改造成更具有意义的便于识别的编号
     */
    private String applyid;
    /**
     * 当前图片是第几张
     */
    private Integer curNum;
    /**
     * 图片的总数
     */
    private Integer totalNum;
    /**
     * 影像类别码
     */
    private String doccode;
    /**
     * 影像文件名
     */
    private String filename;
    /**
     * 影像数据(base64)
     */
    private String imageBase64;
    /**
     * 创建时间
     */
    private Date createddate;
}
