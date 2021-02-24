package com.ideatech.ams.kyc.service;

import com.alibaba.fastjson.JSONObject;

public interface EquityShareService {
	public JSONObject getEquityShareTreeJsonObject(String username,Long saicInfoId, String orgfullid);
}
