package com.ideatech.ams.account.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;



@Data
@Entity
public class OrganizationMapPo extends BaseMaintainablePo implements Serializable {


    /**
     * 序列化ID,缓存需要
     */
    private static final long serialVersionUID = 54541121212135342L;

    @Column(length = 50)
    private String organcode;

    //S数字化管理平台需要的字段
    @Column(length = 50)
    private String dmpOrganCode;

}