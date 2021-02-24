package com.ideatech.ams.kyc.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ImgUtil {

	//path:"http://"+ip+"/picp/photoservlet1?Index=0&jsessionid="+s4
	//path2:"http://"+ip+"/picp/photoservlet1;jsessionid="+s4+"?Index=0"
	public void setImage(String path,String image){
		URL url;
		try {
			url = new URL(path);
			URLConnection uc = url.openConnection();
			InputStream is = uc.getInputStream();
			File file = new File(image);
			FileOutputStream out = new FileOutputStream(file);
			int i = 0;
			while ((i = is.read()) != -1) {
				out.write(i);
			}
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 把图片转换为String
	 */
	public static String getImageStr(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		// String imgFile = "d:\\111.jpg";// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 将字符串转为图片
	 * 
	 * @param imgStr
	 * @return
	 */
	public static boolean generateImage(String imgStr, String imgFile)
			throws Exception {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = imgFile;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 将图片转化为二进制数据
	 * @param path
	 * @return
	 */
	public static byte[] toByteArry(String path){
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return data;
	}
}
