package com.ideatech.ams.unifyLogin.socket;

import com.ideatech.ams.unifyLogin.CharsetEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MessageUtil {
	public static String addHead(String body){
		String msgLength = "0";
		try {
			msgLength = String.valueOf(body.getBytes(CharsetEnum.GBK.getCode()).length);
			return StringUtils.rightPad(msgLength, 4, " ") + body;
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
	
	public static Map<String,String> converToMap(String returnMsg){
		Map<String,String> map = new HashMap<String,String>();
		returnMsg = returnMsg.substring(4);
		String[] body = returnMsg.split("\\|");
		for(String s : body){
			String[] arr = s.split("=");
			if(arr.length==1){
				map.put(arr[0], null);
			}else{
				map.put(arr[0], arr[1]);
			}
		}
		return map;
	}
}
