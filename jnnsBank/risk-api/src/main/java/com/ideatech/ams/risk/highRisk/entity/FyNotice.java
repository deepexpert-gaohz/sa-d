package com.ideatech.ams.risk.highRisk.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangwz
 * @Description 法院公告
 * @date 2019-10-16 10:56
 */
@Entity
@Table(name = "risk_external_fynotice")
@Data
public class FyNotice extends BaseMaintainablePo {
    /**
     * 关键字name
     */
    private String keyName;
    /**
     * 执行法院
     */
    private String court;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 发布时间
     */
    private String sortTime;
    /**
     * 内容
     */
    private String body;
    /**
     * 相关当事人
     */
    private String relatedParty;
    /**
     * 公告类型
     */
    private String ggType;

}
