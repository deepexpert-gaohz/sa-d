package com.ideatech.ams.ws.api.service;

/**
 * @author van
 * @date 16:33 2018/5/30
 */
public class DefaultAmsHzPushServiceImpl implements AmsHzPushService {

	/**
	 * 推送人行核准号
	 * @param acctNo
	 * @param accountKey
	 * @return
	 */
	@Override
    public Object push(String acctNo, String accountKey) {
        return null;
    }

	/**
	 * 推送人行核准日期以及人行反馈数据的JSON字符串
	 * @param pbcCheckDate
	 * @param jsonData
	 * @return
	 */
    @Override
	public Object pushAll(String pbcCheckDate , String jsonData) {
		return null;
	}

	/**
	 * 推送批次的人行核准数据，保存到某一个文件夹下面进行读取
	 * @param filePath
	 * @return
	 */
	@Override
	public Object pushCoreFile(String filePath) {
		return null;
	}
}
