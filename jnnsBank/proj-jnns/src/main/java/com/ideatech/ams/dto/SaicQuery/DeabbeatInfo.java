package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;



/**
 * 老赖
 */
@Data
@Entity
@Table(name = "DEABBEAT_INFO")
public class DeabbeatInfo  extends BaseMaintainablePo implements Serializable {

    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 54541121212135342L;
    public static String baseTableName = "YD_DEABBEAT_INFO";

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
    private String court;

    @Column(length = 50)
    private String caseno;

    @Column(length = 500)
    private String context;

    @Column(length = 50)
    private String dateType;



    @Column(length = 100)
    private String datatinme;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String remark;
}
