package com.ideatech.ams.dto;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ideatech.common.dto.ResultDto;
import lombok.Data;

import java.io.Serializable;

@Data


/**
 * 开户报文返回对象
 */
public class JnResultDto<T>  extends ResultDto<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;
    private String acctName;
    private String acctNo;
    private String bankName;
    private String legalName;
    private String accountKey;
    private String openKey;
    private String selectPwd;
    private String depositoryName;
    private T data;

    public JnResultDto() {
    }

    public JnResultDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore
    public boolean isNonBizError() {
        return "UNAUTHORIZED".equals(this.code) || "COMMON_ERROR".equals(this.code);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

}
