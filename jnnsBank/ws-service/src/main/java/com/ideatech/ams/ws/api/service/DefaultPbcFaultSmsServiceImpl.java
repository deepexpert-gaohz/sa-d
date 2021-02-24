package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSONArray;
import com.ideatech.ams.account.service.pbc.PbcFaultSmsService;
import org.springframework.stereotype.Service;

@Service
public class DefaultPbcFaultSmsServiceImpl implements PbcFaultSmsService {
    @Override
    public String sendMessage(JSONArray statisticsJson) {
        return null;
    }

}
