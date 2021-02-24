package com.ideatech.ams.system.org.vo;

import lombok.Data;

/**
 * @Description 同步机构--插入
 * @Author wanghongjie
 * @Date 2019/2/22
 **/
@Data
public class InsertSyncOrg extends SyncOrgVo{
    /**
     * 机构名称
     */
    private String name;
    /**
     * 机构简称
     */
    private String shortName;
    /**
     * 联行号（12位数字，如果不传易账户这边会自动生成12位随机码例如：DCNpQS0bPOUx）
     */
    private String code;
    /**
     * 新机构的上级机构的fullid（如果是第一次新建的就不用传，以后在他下面建新机构需要把父机构fullid传进来）
     */
    private String afterAmsFullid;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 网点地址
     */
    private String address;

    /**
     * 创建名称
     */
    private String crtName;
    /**
     * 创建用户
     */
    private String crtUser;

    /**
     * 组织机构类型 BANK(银行类) 或者AGENCY(中介机构类)
     */
    private String orgType;

    /**
     * 网点是否开放 1为开放 0为不开放
     */
    private String isOpen;
    /**
    /**
     * 网点联系人手机号，可多个用逗号分隔（15012345678，13090868346）
     */
    private String contactList;
    /**
     * 网点电话
     */
    private String telephone;
    /**
     * 网点是否为业务网点 1是 0不是
     */
    private String isOperatingDepartment;

    /**
     * 对外的银行联行号
     */
    private String cnapsCode;

    /**
     * 对外的上级银行联行号
     */
    private String parentCnapsCode;
}
