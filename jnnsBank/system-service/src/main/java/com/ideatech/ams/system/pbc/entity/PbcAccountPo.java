package com.ideatech.ams.system.pbc.entity;

import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 人行账户信息
 * @author liangding
 * @create 2018-05-16 下午11:02
 **/
@Data
@Entity
@Table(name = "yd_sys_pbc_account")
@SQLDelete(sql = "update yd_sys_pbc_account set yd_deleted=1 where yd_id=? and yd_version_ct=?")
@Where(clause = "yd_deleted=0")
public class PbcAccountPo extends BaseMaintainablePo {
    /**
     * 服务器IP
     */
    private String ip;
    /**
     * 用户名
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 账号类型
     * <ul>
     *     <li>AMS:人行账管系统</li>
     *     <li>PICP:身份联网核查系统</li>
     *     <li>ECCS:机构信用代码系统</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    private EAccountType accountType;
    /**
     * 账号状态
     * <ul>
     *     <li>NEW:未校验</li>
     *     <li>VALID:有效</li>
     *     <li>INVALID:无效</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    private EAccountStatus accountStatus;
    /**
     * 错误原因
     */
    private String errorReason;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 所属机构ID
     * @see com.ideatech.ams.system.org.entity.OrganizationPo
     */
    private Long orgId;
    /**
     * 删除标记
     */
    private Boolean deleted = Boolean.FALSE;
}
