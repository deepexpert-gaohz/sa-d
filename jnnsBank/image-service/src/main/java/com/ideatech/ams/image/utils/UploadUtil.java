package com.ideatech.ams.image.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ideatech.ams.image.dto.ImageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.sunyard.client.SunEcmClientApi;
import com.sunyard.client.bean.ClientBatchBean;
import com.sunyard.client.bean.ClientBatchFileBean;
import com.sunyard.client.bean.ClientBatchIndexBean;
import com.sunyard.client.bean.ClientFileBean;
import com.sunyard.client.impl.SunEcmClientSocketApiImpl;

/**
 * 影像平台的接口配置
 * @author yang
 *
 */

public class UploadUtil {
	
	String STARTDATE = new SimpleDateFormat("yyyyMMdd").format(new Date());
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//登录
	public String login(ImageConfig cf){
		
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(cf.getIp(), Integer.parseInt(cf.getSocketPort()));
		String resultMsg ="false";
		try {
			 resultMsg = clientApi.login(cf.getUsername(), cf.getPassword());
			 logger.info(("#######登陆返回的信息[" + resultMsg + "]#######"));
		} catch (Exception e) {
			logger.debug(("#######登陆[失败]#######"));
			e.printStackTrace();
		}
		return resultMsg;
	}
	//上传
	public String upload(ImageConfig cf,String path) {
		return upload("jpg",cf,path);
	}
	public String upload(ImageConfig cf,String path,String suffix) {
		return upload(suffix,cf,path);
	}
	private String upload(String suffix,ImageConfig cf,String path){
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(cf.getIp(), Integer.parseInt(cf.getSocketPort()));
		ClientBatchBean clientBatchBean = new ClientBatchBean();
		clientBatchBean.setModelCode(cf.getModelCode());
		clientBatchBean.setUser(cf.getUsername());
		clientBatchBean.setPassWord(cf.getPassword());
		clientBatchBean.setBreakPoint(false);
		clientBatchBean.setOwnMD5(false);


		ClientBatchIndexBean clientBatchIndexBean = new ClientBatchIndexBean();
		clientBatchIndexBean.setAmount("1");

		clientBatchIndexBean.addCustomMap(cf.getSTARTCOLUMN(), STARTDATE);
		//clientBatchIndexBean.addCustomMap("BUSI_SERIAL_NO", "");

		ClientBatchFileBean clientBatchFileBeanA = new ClientBatchFileBean();
		clientBatchFileBeanA.setFilePartName(cf.getFilePartName());

		ClientFileBean fileBean1 = new ClientFileBean();
		fileBean1.setFileName(path);
		fileBean1.setFileFormat(suffix);
		//fileBean1.setFilesize("286764");
		clientBatchFileBeanA.addFile(fileBean1);
		clientBatchBean.setIndex_Object(clientBatchIndexBean);
		clientBatchBean.addDocument_Object(clientBatchFileBeanA);
		String resultMsg="FAIL";
		try {
			resultMsg = clientApi.upload(clientBatchBean, cf.getGroupName());
			logger.info("#######上传返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMsg;
	}
	//登出
	public void logout(ImageConfig cf){
		
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(cf.getIp(), Integer.parseInt(cf.getSocketPort()));
		try {
			String resultMsg = clientApi.logout(cf.getUsername());
			logger.debug("#######登出返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询
	public String query(ImageConfig cf,String batchId) {
		
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(cf.getIp(), Integer.parseInt(cf.getSocketPort()));
		ClientBatchBean clientBatchBean = new ClientBatchBean();
		clientBatchBean.setModelCode(cf.getModelCode());
		clientBatchBean.setUser(cf.getUsername());
		clientBatchBean.setPassWord(cf.getPassword());
		clientBatchBean.setDownLoad(false);
		// clientBatchBean.getIndex_Object().setVersion("1");
		clientBatchBean.getIndex_Object().setContentID(batchId);
		clientBatchBean.getIndex_Object().addCustomMap(cf.getSTARTCOLUMN(),STARTDATE);

		String resultMsg = "";
		try {
			logger.info("影像平台查询时的GroupName："+cf.getGroupName());
			resultMsg = clientApi.queryBatch(clientBatchBean, cf.getGroupName());
			//System.out.println(resultMsg);
			logger.info("#######查询返回的信息[" + resultMsg + "]#######");
			return resultMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return resultMsg;
		}
	}
	//处理上传后的结果
	public String deal(String resultMsg){
		String[] str = resultMsg.split("<<::>>");
		return str[1];
	}
//	public void test(ImageConfig cf){
//		System.out.println(cf.getFilePartName());
//		System.out.println(cf.getGroupName());
//		System.out.println(cf.getIp());
//		System.out.println(cf.getModelCode());
//		System.out.println(cf.getPassword());
//		System.out.println(cf.getServerName());
//		System.out.println(cf.getSocketPort());
//		System.out.println(cf.getSTARTCOLUMN());
//		System.out.println(cf.getUsername());
//		System.out.println(cf.getGroupName());
//	}
}
