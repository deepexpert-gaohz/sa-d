package com.ideatech.ams.dto.esb.response;

public class RSysHeader {
    private String msgId;
    private String msgDate;
    private String msgTime;
    private String serviceCd;
    private String operation;
    private String clientCd;
    private String serverCd;
    private String bizId;
    private String bizType;
    private String orgCode;
    private String bizResCode;
    private String bizResText;
    private String ver;
    private String authId;
    private String authPara;
    private String authContext;
    private String pingIndex;
    private String pinValue;
    private String resCode;
    private String resText;

    public String getBizResCode() {
        return bizResCode;
    }

    public void setBizResCode(String bizResCode) {
        this.bizResCode = bizResCode;
    }

    public String getBizResText() {
        return bizResText;
    }

    public void setBizResText(String bizResText) {
        this.bizResText = bizResText;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getServiceCd() {
        return serviceCd;
    }

    public void setServiceCd(String serviceCd) {
        this.serviceCd = serviceCd;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getClientCd() {
        return clientCd;
    }

    public void setClientCd(String clientCd) {
        this.clientCd = clientCd;
    }

    public String getServerCd() {
        return serverCd;
    }

    public void setServerCd(String serverCd) {
        this.serverCd = serverCd;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthPara() {
        return authPara;
    }

    public void setAuthPara(String authPara) {
        this.authPara = authPara;
    }

    public String getAuthContext() {
        return authContext;
    }

    public void setAuthContext(String authContext) {
        this.authContext = authContext;
    }

    public String getPingIndex() {
        return pingIndex;
    }

    public void setPingIndex(String pingIndex) {
        this.pingIndex = pingIndex;
    }

    public String getPinValue() {
        return pinValue;
    }

    public void setPinValue(String pinValue) {
        this.pinValue = pinValue;
    }
}
