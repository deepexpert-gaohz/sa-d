package com.ideatech.ams.system.template.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import com.ideatech.common.enums.BillType;
import com.ideatech.common.enums.CompanyAcctType;
import com.ideatech.common.enums.DepositorType;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 打印模板
 * @author liangding
 * @create 2018-07-09 下午3:09
 **/
@Data
@Entity
@Table(name = "sys_template")
@SQLDelete(sql = "update yd_sys_template set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
public class TemplatePo extends BaseMaintainablePo {
    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    private BillType billType;

    /**
     * 存款人类别
     */
    @Enumerated(EnumType.STRING)
    private DepositorType depositorType;
    /**
     * 账户性质
     */
    @Enumerated(EnumType.STRING)
    private CompanyAcctType acctType;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件内容
     */
    @Lob
    private byte[] templaeContent;

    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
