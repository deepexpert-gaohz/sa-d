package com.ideatech.ams.apply.service;

import com.ideatech.ams.apply.dto.ApplyOcrDto;

import java.util.List;

public interface ApplyOcrService {
    List<ApplyOcrDto> getApplyOcrList(String applyId, String docCode);

    void saveApply(ApplyOcrDto applyOcrDto);

    ApplyOcrDto findByFilename(String filename);

    ApplyOcrDto findByFilenameAndApplyid(String filename,String applyid);

    ApplyOcrDto findByApplyidAndCurNum(String applyid,Integer curNum);

    List<ApplyOcrDto> findByApplyidOrderByCurNumAsc(String applyid);
}
