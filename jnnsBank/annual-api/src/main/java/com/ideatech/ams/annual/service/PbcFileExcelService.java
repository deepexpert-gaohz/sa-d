package com.ideatech.ams.annual.service;

import com.ideatech.ams.annual.dto.PbcAccountExcelInfo;

import java.util.List;

/**
 * @Description 人行下载的服务接口
 * @Author wanghongjie
 * @Date 2018/8/2
 **/
public interface PbcFileExcelService {
    /**
     * 根据人行excel文件路径解析成excel对象
     *
     * @param amsFilePath
     * @return
     * @throws Exception
     */
    List<PbcAccountExcelInfo> getPbcInfoXlsAccounts(String amsFilePath) throws Exception;
}
