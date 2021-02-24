package com.ideatech.ams.apply.entity;

import com.ideatech.common.entity.BaseMaintainablePo;
import lombok.Data;
import org.springframework.context.MessageSource;

import javax.persistence.*;

/**
 * 历史发送短信
 */
@Table(name = "yd_ezhmessage")
@Entity
@Data
public class EzhMessage extends BaseMaintainablePo {

    public EzhMessage(){

    }

    public EzhMessage(String phone, String message, String type, Boolean checkPass, String errorMessage, String returnValue){

        this.message=message;
        this.type=type;
        this.phone=phone;
        this.checkPass = checkPass;
        this.errorMessage = errorMessage;
        this.returnValue = returnValue;
    }

    /**
     * 通知提醒内容
     */
    @Column(length = 1000)
    private String message;

    /**
     * 通知提醒手机号
     */
    @Column(length = 30)
    private String phone;

    /**
     * 通知提醒类别：1-预约人通知短信,2-柜员通知短信
     */
    @Column(length = 30)
    private String type;

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
     * 接口返回值
     */
    @Column(length = 1000)
    private String returnValue;

    /**
     * 预约编号
     */
    private String applyId;

}