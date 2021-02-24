package com.ideatech.ams.system.pbc.dto;

import com.ideatech.ams.system.pbc.enums.EAccountStatus;
import com.ideatech.ams.system.pbc.enums.EAccountType;
import lombok.Data;

import java.io.Serializable;

/**
 * 人行账号信息
 * @author liangding
 * @create 2018-05-17 上午12:04
 **/
@Data
public class PbcAccountDto implements Serializable {
    /**
     * 主键
     */
    private Long id;
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
    private EAccountType accountType;
    /**
     * 账号状态
     * <ul>
     *     <li>NEW:未校验</li>
     *     <li>VALID:有效</li>
     *     <li>INVALID:无效</li>
     * </ul>
     */
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
     * @see com.ideatech.ams.system.org.dto.OrganizationDto
     */
    private Long orgId;
}
