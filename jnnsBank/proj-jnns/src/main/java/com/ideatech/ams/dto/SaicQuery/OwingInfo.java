package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 欠税名单
 */
@Data
@Entity
@Table(name = "OWING_INFO")
public class OwingInfo  extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 54541121212135342L;
    public static String baseTableName = "YD_OWING_INFO";

    @Column(length = 50)
    private int keyid;

    @Column(length = 50)
    private int typet;

    @Column(length = 50)
    private String title;

    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String cid;

    @Column(length = 255)
    private String organ;

    @Column(length = 50)
    private String taxtype;

    @Column(length = 50)
    private String period;

    @Column(length = 50)
    private String balance;



    @Column(length = 100)
    private String datatinme;



    @Column(length = 100)
    private String remark;
}
