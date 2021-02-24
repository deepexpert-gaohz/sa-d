package com.ideatech.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ideatech.common.exception.EacException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author wangqingan
 * @version 08/02/2018 4:53 PM
 */
@Component
@Slf4j
public class HttpRequest {

//	private RestTemplate restTemplate = new RestTemplate();
    /**
     * API key
     */
    @Value("${saic.api.key}")
    private String apiKey;

    @Value("${saic.proxy.flag:false}")
    private boolean idpProxyOn;

    @Value("${saic.proxy.ip}")
    private String idpProxyIp;

    @Value("${saic.proxy.port}")
    private int idpProxyPort;

    /**
     * GET 请求
     * @param url
     * @param params
     * @return
     */
    public String getIdpRequest(String url, Map<String,String> params){
        return baseRequest(url,params,"GET");
    }

    /**
     * POST 请求
     * @param url
     * @param params
     * @return
     */
    public String postIdpRequest(String url, Map<String,String> params){
        return baseRequest(url,params,"POST");
    }

    /**
     * GET 请求
     * @param url
     * @param params
     * @param timeOut 超时时间
     * @return
     */
    public String getIdpRequest(String url, Map<String, String> params, Integer timeOut) {
        return baseRequest(url, params, "GET", timeOut);
    }

    /**
     * POST 请求
     * @param url
     * @param params
     * @param timeOut 超时时间
     * @return
     */
    public String postIdpRequest(String url, Map<String, String> params, Integer timeOut) {
        return baseRequest(url, params, "POST", timeOut);
    }


    private String baseRequest(String url, Map<String,String> params, String method) {
        return this.baseRequest(url, params, method, null);
    }

    /**
     * 数据统计请求
     *
     * @param url
     * @param params
     * @param method POST->params json string,GET->params
     * @return
     */
    private String baseRequest(String url, Map<String,String> params, String method, Integer timeOut) {
        try {
            RestTemplate restTemplate = null;
            if (url.startsWith("https") || timeOut == null) {
                restTemplate = getRestTemplate(url);
            } else {
                HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
                httpRequestFactory.setConnectionRequestTimeout(timeOut);
                httpRequestFactory.setConnectTimeout(timeOut);
                httpRequestFactory.setReadTimeout(timeOut);
                restTemplate = new RestTemplate(httpRequestFactory);
            }
        	checkRestTemplate(restTemplate);

            log.info("查询参数param: " + params.toString());
            String resultJson = null;
            if ("GET".equalsIgnoreCase(method)) {
                StringBuilder paramSB = new StringBuilder();
                paramSB.append("?");
                paramSB.append("key=").append(apiKey);
                for (String key : params.keySet()) {
                    paramSB.append("&").append(key).append("=").append(params.get(key));
                }
                String requestUrl = url + paramSB.toString();
                log.debug("请求URL: {}", requestUrl);
                resultJson = restTemplate.getForObject(requestUrl, String.class);
            } else if ("POST".equalsIgnoreCase(method)) {
                MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
                paramMap.add("key", apiKey);
                for (String key : params.keySet()) {
                    paramMap.add(key, params.get(key));
                }
                resultJson = restTemplate.postForObject(url, paramMap, String.class);
            }
            log.info("返回结果resultJson: " + resultJson);

            return processResult(resultJson);
        } catch (Exception e) {
            log.error("数据获取异常:", e);
            if (e instanceof EacException) {
                throw e;
            } else {
                throw new EacException("获取数据超时");
            }
        }
    }

    public void checkRestTemplate(RestTemplate restTemplate) {
    	if (idpProxyOn) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(idpProxyIp, idpProxyPort));
            requestFactory.setProxy(proxy);
            restTemplate.setRequestFactory(requestFactory);
        }
    }

    public RestTemplate getRestTemplate(String url){
        if(url.startsWith("https")){
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
            SSLContext sslContext = null;
            try {
                sslContext = org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();
            } catch (Exception e) {
                log.error("创建SSL链接失败：",e);
            }

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,hostnameVerifier);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            return restTemplate;
        }else{
            return new RestTemplate();
        }
    }

    /**
     * 解析工商获取结果
     *
     * @param resultJson
     * @return
     */
    private String processResult(String resultJson) {
        if (StringUtils.isNotBlank(resultJson)) {
            try {
                JSONObject jsonObject = JSON.parseObject(resultJson);
                /**
                 * IDP 有两种返回格式
                 * 1. {
                 * "status":	返回结果状态
                 * "message":	返回结果消息
                 * "data": 结果
                 * }
                 * 2. {
                 * "status": "success"/"fail"
                 * "code:  对应上面的status
                 * "result": 结果
                 * "reason": 错误消息
                 */

                String code = jsonObject.getString("code");
                if(code != null){
                    //第二种消息格式
                    if(StringUtils.equalsIgnoreCase(code, "000000")){
                        return jsonObject.getString("result");
                    } else {
                        String errorReason = jsonObject.getString("reason");
                        log.error("获取结果失败:{}", errorReason);
                        //针对受益人批量查询，包含字段查询返回错误信息
                        if(StringUtils.equals(errorReason,"数据正在计算，请稍后再试")){
                            return errorReason;
                        }
                    }
                } else {
                    //第一种消息格式
                    String status = jsonObject.getString("status");
                    if(StringUtils.equalsIgnoreCase(status, "000000")){
                        return jsonObject.getString("data");
                    } else if(StringUtils.equalsIgnoreCase(status, "success")){
                        return jsonObject.toString();
                    } else {
                        String errorMessage = jsonObject.getString("message");
                        if (errorMessage==null){
                            errorMessage = jsonObject.getString("reason");
                        }
                        log.error("获取结果失败:", errorMessage);
                        throw new EacException("获取结果失败:" + errorMessage);
                    }
                }
            }  catch (JSONException e) {
                log.error("查询后解析JSON异常:" + e);
                throw new RuntimeException("系统内部异常");
            }
        }
        return null;
    }
}
