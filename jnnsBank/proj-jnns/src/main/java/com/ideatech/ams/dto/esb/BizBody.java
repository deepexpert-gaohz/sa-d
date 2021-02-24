package com.ideatech.ams.dto.esb;

import com.ideatech.ams.domain.SyncCompare;
import lombok.Data;

import java.util.List;

@Data
public class BizBody {

    private String keyWord;
    private String TranTime;
    private String BatchDt;
    private String BatchSeq;
    private String OriBatchDt;
    private String OriBatchSep;
    private List<SyncCompare> acoplist_ARRAY;
    private String qishibis;//起始笔数
    private String chxunbis;//查询笔数（暂定最大5笔）
    private String jiaoyijg;//交易机构
    private String n;
    private String stype;
    private String frid;
    private String id;
    private String userid;



    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }


    private String kaixhubz;//开销户标志

    public String getKaixhubz() {
        return kaixhubz;
    }

    public void setKaixhubz(String kaixhubz) {
        this.kaixhubz = kaixhubz;
    }



    public String getJiaoyirq() {
        return jiaoyirq;
    }

    public void setJiaoyirq(String jiaoyirq) {
        this.jiaoyirq = jiaoyirq;
    }

    private String jiaoyirq;//交易日期


    public String getJiaoyijg() {
        return jiaoyijg;
    }

    public void setJiaoyijg(String jiaoyijg) {
        this.jiaoyijg = jiaoyijg;
    }

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

    public List<SyncCompare> getAcoplist_ARRAY() {
        return acoplist_ARRAY;
    }

    public void setAcoplist_ARRAY(List<SyncCompare> acoplist_ARRAY) {
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
