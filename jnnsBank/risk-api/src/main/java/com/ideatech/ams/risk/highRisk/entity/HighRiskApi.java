package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description api数据
 * @Author ywz
 * @Date 2019/9/24
 **/
@Entity
@Table(name="risk_high_api")
@SQLDelete(sql = "update yd_risk_high_api set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted = 0")
@Data
public class HighRiskApi extends BaseMaintainablePo {

    /**
     * jiekou id
     */
    private String apiNo;
    /**
     * 接口名
     */
    private String apiName;
    /**
     * 接口地址
     */
    private String apiUrl;

    /**
     * 搜索关键字
     */
    private String keyWord;

    /**
     * 返回的数据类型key
     */
    private String retData;

    private Boolean deleted = Boolean.FALSE;//删除标记
}
