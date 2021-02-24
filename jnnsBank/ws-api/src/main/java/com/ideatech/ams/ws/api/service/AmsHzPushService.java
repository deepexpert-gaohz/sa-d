package com.ideatech.ams.ws.api.service;

/**
 * @author van
 * @date 16:31 2018/5/30
 */
public interface AmsHzPushService {

    Object push(String acctNo, String accountKey);

	Object pushAll(String pbcCheckDate , String jsonData);

	Object pushCoreFile(String filePath);

}
