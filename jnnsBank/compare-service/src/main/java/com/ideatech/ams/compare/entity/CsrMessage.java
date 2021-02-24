package com.ideatech.ams.compare.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 客户异动短信发送历史记录表
 * @author jzh
 * @date 2019/6/28.
 */

@Data
@Entity
public class CsrMessage extends BaseMaintainablePo {

    public CsrMessage(){

    }

    /**
     * 存款人名称
     */
    private String depositorName;

    /**
     * 通知提醒手机号
     */
    @Column(length = 30)
    private String phone;

    /**
     * 短信发送是否成功
     */
    @Column(length = 30)
    private Boolean checkPass;

    /**
     * 发送失败原因
     */
    @Column(length = 1000)
    private String errorMessage;

    /**
     * 通知提醒内容
     */
    @Column(length = 1000)
    private String message;

    /**
     * 备用
     */
    @Column(length = 30)
    private String type;

}
