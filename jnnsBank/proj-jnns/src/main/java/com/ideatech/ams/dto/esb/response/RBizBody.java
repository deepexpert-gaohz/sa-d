package com.ideatech.ams.dto.esb.response;

public class RBizBody {
    private String TranTime;
    private String BatchDt;
    private String BatchSeq;
    private String OriBatchDt;
    private String OriBatchSep;
    private String acoplist_ARRAY;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;

    public String getQishibis() {
        return qishibis;
    }

    public void setQishibis(String qishibis) {
        this.qishibis = qishibis;
    }

    public String getChxunbis() {
        return chxunbis;
    }

    public void setChxunbis(String chxunbis) {
        this.chxunbis = chxunbis;
    }

    public String getJiaoyijg() {
        return jiaoyijg;
    }

    public void setJiaoyijg(String jiaoyijg) {
        this.jiaoyijg = jiaoyijg;
    }

    private String qishibis;//起始笔数
    private String chxunbis;//查询笔数（暂定最大5笔）
    private String jiaoyijg;//交易机构


    public String getAcoplist_ARRAY() {
        return acoplist_ARRAY;
    }

    public void setAcoplist_ARRAY(String acoplist_ARRAY) {
        this.acoplist_ARRAY = acoplist_ARRAY;
    }

    public String getTranTime() {
        return TranTime;
    }

    public void setTranTime(String tranTime) {
        TranTime = tranTime;
    }

    public String getBatchDt() {
        return BatchDt;
    }

    public void setBatchDt(String batchDt) {
        BatchDt = batchDt;
    }

    public String getBatchSeq() {
        return BatchSeq;
    }

    public void setBatchSeq(String batchSeq) {
        BatchSeq = batchSeq;
    }

    public String getOriBatchDt() {
        return OriBatchDt;
    }

    public void setOriBatchDt(String oriBatchDt) {
        OriBatchDt = oriBatchDt;
    }

    public String getOriBatchSep() {
        return OriBatchSep;
    }

    public void setOriBatchSep(String oriBatchSep) {
        OriBatchSep = oriBatchSep;
    }
}
