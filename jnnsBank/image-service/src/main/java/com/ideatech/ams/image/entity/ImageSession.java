package com.ideatech.ams.image.entity;

import com.ideatech.ams.image.enums.BusinessTypeEnum;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 远程双录 会话信息
 * @author jzh
 * @date 2019-12-10.
 */

@Entity
@Data
public class ImageSession extends BaseMaintainablePo {

    /**
     * 服务端登录名
     */
    private String serverName;

    /**
     * 客户端登录名
     */
    private String clientName;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 企业名称
     */
    private String depositorName;

    /**
     * 法人
     */
    private String legalName;

    /**
     * 法人证件号
     */
    private String legalIdcardNo;

    /**
     * 到期的时间戳(毫米级，ms);
     */
    private Long endTimeStamp;

    /**
     * 关联流水id
     */
    private Long billId;
    /**
     * 预约编号
     */
    private String applyid;
    /**
     * 双录视频来源
     */
    private String source;

    /**
     * 是否指定，是否随机坐席
     * true：随机；false：指定
     */
    private Boolean randomFlag;

    /**
     * 人脸识别结果
     * -1： 未认证；0： 认证中；
     * 1： 认证通过；2： 认证不通过、3 无需认证
     */
    private String faceResult;

    /**
     *  发起实人认证返回的请求id
     */
    private String idpRequestId;

    /**
     * 业务类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private BusinessTypeEnum businessType;

    /**
     * 客户姓名
     */
    @Column(length = 32)
    private String customerName;

    /**
     * 短信内容
     */
    @Column(length = 1000)
    private String message;
}
