package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.ams.image.service.FaceRecognitionService;
import com.ideatech.common.util.DesEncrypter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jzh
 * @date 2019-12-26.
 */
public class DefaultFaceRecognitionServiceImpl implements FaceRecognitionService {


    /**
     * 密钥，系统提供的apikey
     */
    @Value("${ams.image.faceRecognition.key}")
    private String key;

    /**
     * 人脸识别请求地址
     */
    @Value("${ams.image.faceRecognition.url}")
    private String url;

    /**
     * 人脸识别结果查询请求地址
     */
    @Value("${ams.image.faceRecognition.resultUrl}")
    private String resultUrl;

    @Override
    public Map<String,Object> getVerifyUrl(String name, String identificationNumber, String successRedirect, String failRedirect)  {

        //创建实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Map<String,Object> map = new HashMap<>(16);
        try {


            HttpPost httpPost = new HttpPost(url);

            String encryptParam = "name="+name+"&identificationNumber="+identificationNumber+"&successRedirect="+successRedirect+"&failRedirect="+failRedirect;

            //加密示例
            DesEncrypter desEncrypter = new DesEncrypter(key);
            encryptParam = desEncrypter.encrypt(encryptParam);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addTextBody("key", key)
                    .addTextBody("encryptParam", encryptParam)
                    .build();
            httpPost.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(httpPost);

            int code = response.getStatusLine().getStatusCode();
            String info = response.getStatusLine().getReasonPhrase();
            if(code != 200) {
                throw new Exception(String.valueOf(code) + "&" + info);
            }

            HttpEntity resEntity = response.getEntity();

            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(resEntity));
            String status = jsonObject.getString("status");

            // 000000	查询成功 (消息码人脸识别接口提供)
            if ("000000".equals(status)){
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                map.put("realPersonVerifyUrl",dataJsonObject.getString("realPersonVerifyUrl"));
                map.put("idpRequestId",dataJsonObject.getString("idpRequestId"));
            }

            //关闭流
            EntityUtils.consume(resEntity);
            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    @Override
    public Map<String, Object> getVerifyResult(String id) {
        //创建实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Map<String,Object> map = new HashMap<>(16);
        try {
            HttpGet httpGet = new HttpGet(resultUrl+"?key="+key+"&idpRequestId="+id);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            int code = response.getStatusLine().getStatusCode();
            String info = response.getStatusLine().getReasonPhrase();
            if(code != 200) {
                throw new Exception(String.valueOf(code) + "&" + info);
            }

            HttpEntity resEntity = response.getEntity();

            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(resEntity));
            String status = jsonObject.getString("status");

            // 000000	查询成功 (消息码人脸识别接口提供)
            if ("000000".equals(status)){
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                map.put("statusCode",dataJsonObject.getString("statusCode"));
                map.put("auditConclusions",dataJsonObject.getString("auditConclusions"));
                map.put("authorityComparisonScore",dataJsonObject.getString("authorityComparisonScore"));
            }

            //关闭流
            EntityUtils.consume(resEntity);
            response.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}
