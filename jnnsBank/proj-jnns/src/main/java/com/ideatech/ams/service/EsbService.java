package com.ideatech.ams.service;

import com.ideatech.ams.dto.SyncCompareInfo;
import com.ideatech.ams.dto.esb.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * esb服务类
 *
 * @auther zoulang
 * @create 2018-11-30 2:22 PM
 **/
public interface EsbService {


    Integer getResponsezs(String i);

    boolean getSaveSyncCompareInfo(List<SyncCompareInfo> syncCompareInfoList);

    List<SyncCompareInfo> getResponse(String response, String kaixhubz);

    RequestBody getRequestBody(String kaixhubz, int cishu);


    Map<String, Map<String, String>> getUsernameDianLiang();

    String getResponseCustom(String httpIntefaceUtils);
}
