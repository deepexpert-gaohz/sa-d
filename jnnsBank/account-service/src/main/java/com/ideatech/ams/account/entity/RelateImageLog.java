package com.ideatech.ams.account.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 关联企业信息
 *
 *
 * @author RJQ
 */
@Entity
@Table(name = "RELATE_IMAGE_LOG")
@Data
public class RelateImageLog extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 5454155825314635355L;
    public static String baseTableName = "YD_RELATE_IMAGE_LOG";

    /**
     * 账户ID
     */
    @Column(length = 14)
    private Long accountId;

    /**
     * 关联证件类型
     */
    @Column(length = 50)
    private String imageType;

    /**
     * 企业证件名称
     */
    @Column(length = 200)
    private String imageName;

    /**
     * 企业证件号码
     */
    @Column(length = 50)
    private String imageNo;

    /**
     * 企业证件到日期
     */
    @Column(length = 20)
    private String imageDue;
}
