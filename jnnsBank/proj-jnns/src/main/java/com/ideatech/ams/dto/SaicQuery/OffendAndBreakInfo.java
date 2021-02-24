package com.ideatech.ams.dto.SaicQuery;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@Entity
public class OffendAndBreakInfo extends BaseMaintainablePo implements Serializable {



    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 54541121212135342L;
    public static String baseTableName = "YD_OffENDANDBREAK_INFO";

    //行政违法
    @Column(length = 50)
    private int keyid;

    @Column(length = 50)
    private int typet;

    @Column(length =255)
    private String title;

    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String cid;

    @Column(length = 50)
    private String organ;

    @Column(length = 50)
    private String caseno;

    @Column(length = 255)
    private String origin;

    @Column(length = 255)
    private String eresult;

    @Column(length = 100)
    private String judegeresult;

    @Column(length = 100)
    private String datatype;

    @Column(length = 100)
    private String datatinme;

    @Column(length = 100)
    private String remark;

//罪犯嫌疑人名称
    @Column(length = 100)
    private String ztitle;

    @Column(length = 100)
    private String zorgan;

    @Column(length = 100)
    private String zcaseno;

    @Column(length = 100)
    private String zorigin;

    @Column(length = 100)
    private String result;

    @Column(length = 100)
    private String zdatatinme;


}
