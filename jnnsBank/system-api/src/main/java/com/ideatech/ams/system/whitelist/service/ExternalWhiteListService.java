package com.ideatech.ams.system.whitelist.service;

import com.ideatech.ams.system.whitelist.dto.WhiteListDto;

import java.util.List;

/**
 * 外部调用白名单接口service
 */
public interface ExternalWhiteListService {

    void whiteLististSave(List<String> list);

    void whiteLististSave(String name,String organCode);

    WhiteListDto searchWhiteObj(String name, String organCode);

    String searchWhiteList(List<String> list);

    void whiteListDel(String name,String organCode);
}
