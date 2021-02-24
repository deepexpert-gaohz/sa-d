package com.ideatech.ams.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http报文接口工具类
 *
 * @auther ideatech
 * @create 2018-11-30 2:26 PM
 **/
public class HttpIntefaceUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpIntefaceUtils.class);

    /**
     * http请求
     *
     * @param url    请求url
     * @param json 报文信息
     * @return 返回报文信息，若返回则报文异常
     */
/*    public static String httpRequest(String url, String json) {

        long starttime = System.currentTimeMillis();
        DataOutputStream dos = null;
        DataInputStream dis = null;
        ByteArrayOutputStream baos = null;
        try {
            URL httpUrl = new URL(url);
            URLConnection conn = httpUrl.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(125 * 1000);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeUTF(json);
            dos.flush();
            dis = new DataInputStream(conn.getInputStream());
            int size = 0;
            byte[] buffer = new byte[102400];
            baos = new ByteArrayOutputStream();
            while ((size = dis.read(buffer)) != -1) {
                baos.write(buffer, 0, size);
            }
            byte[] rspData = baos.toByteArray();
            log.info("总共耗时:" + (System.currentTimeMillis() - starttime));
            log.info("服务器返回信息：" + new String(rspData, "utf-8"));
            return new String(rspData, "UTF-8");
        } catch (Exception e) {
            log.error("报文请求异常", e);
            return "";
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (dis != null) {
                    dis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    /**
     * http请求核心接口，获取核心对公开户数据
     * @param url 请求URL
     * @param src 请求json
     * @return 核心返回报文信息
     */
    public static String send(String url,String src){
        try{
                    URL urlpost = new URL(url);
                    HttpURLConnection conn=(HttpURLConnection)urlpost.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");//建议采用httppost方式进行交互
                    conn.setRequestProperty("charset", "UTF-8");//字符编码UTF-8，GBK都支持
                    conn.setRequestProperty("Content-Type","application/json");
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(60000);//超时时间设置，默认60秒，业务系统根据自身业务需求设置
                    PrintWriter pw = new PrintWriter(new BufferedOutputStream(conn.getOutputStream()));
                    pw.write(src);
                    pw.flush();
                    pw.close();
                    int resCode=conn.getResponseCode();
                    System.out.println(conn.getResponseCode());
                    StringBuilder sb=new StringBuilder();
                    if(resCode==200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));//字符编码保持一致
                        String line = null;
                        while((line=br.readLine())!=null){
                    sb.append(line);
                }
                String s=sb.toString();
            }else{
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"UTF-8"));//字符编码保持一致
                String line = null;
                while((line=br.readLine())!=null){
                    sb.append(line);
                }
                String s=sb.toString();
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
            return "exception occurred";
        }
    }
}


