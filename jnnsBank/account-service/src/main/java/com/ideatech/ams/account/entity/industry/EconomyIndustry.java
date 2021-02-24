package com.ideatech.ams.account.entity.industry;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 省市区
 * @author van
 * @date 11:07 2018/7/5d
 * 
 */
@Data
@Entity
public class EconomyIndustry extends BaseMaintainablePo {
    /**
     * 地区编号
     */
    private String code;
    /**
     * 地区名称
     */
    private String name;
    /**
     * 省市区级别
     */
    private String level;

}
