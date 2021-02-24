package com.ideatech.ams.system.trace.entity;

import com.ideatech.ams.system.trace.enums.OperateModule;
import com.ideatech.ams.system.trace.enums.OperateType;
import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户操作痕迹
 * @author jzh
 * @date 2019-10-30.
 */

@Entity
@Table(name = "sys_user_trace")
@Data
public class UserTrace extends BaseMaintainablePo {

    /**
     * 用户名
     */
    private String username;

    /**
     * 操作人（机构+中文名）
     */
    private String operateName;

    /**
     * 机构fullId
     */
    private String organFullId;

    /**
     * 操作时间
     */
    private Date operateDate;

    /**
     * 调用类
     */
    private String className;

    /**
     * 调用方法
     */
    private String methodName;

    /**
     * 操作模块
     */
    @Enumerated(EnumType.STRING)
    private OperateModule operateModule;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    private OperateType operateType;

    /**
     * 操作结果
     */
    private Boolean operateResult;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作详情(具体参数)备用
     */
    private String operateDetail;

    /**
     * 异常详情
     */
    @Column(length = 1023)
    private String exceptionDetail;

    /**
     * 请求地址
     */
    private String url;

}
