package com.ideatech.ams.ws.api.service;

import com.alibaba.fastjson.JSON;
import com.ideatech.ams.unifyLogin.CharsetEnum;
import com.ideatech.ams.unifyLogin.socket.ClientThreadExecutor;
import com.ideatech.ams.unifyLogin.socket.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Service("SsoValidationService")
public class DefaultSsoValiadtionService implements SsoValidationService {

    private static final Logger log = LoggerFactory.getLogger(DefaultSsoValiadtionService.class);
    @Value("${socket.ip}")
    private String ip;

    @Value("${socket.port}")
    private String port;

    @Value("${socket.appid}")
    private String appid;

    @Override
    public String getValidUsername(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        log.info("统一登录进入获取名字方法...");
          String ticket = httpServletRequest.getParameter("ticket");
          log.info("统一登陆获取ticket：", ticket);
          if (StringUtils.isNotEmpty(ticket)) {
          log.info("ticket不为空...");
          int urlPort = Integer.parseInt(port);
          String msg = "prcscd=getuser|ticket=" + ticket + "|";
          log.info("发送报文，并获得响应报文...");
          Map<String, String> returnMap = MessageUtil.converToMap(ClientThreadExecutor.send(ip, urlPort, MessageUtil.addHead(msg), CharsetEnum.GBK, 4));
        log.info("统一登陆获取报文：", JSON.toJSONString(returnMap));
        if (returnMap.get("msgcode").equals("0000")) {
        // 有值表示已登录的行员号，无值表示尚未登录
        String user_name = returnMap.get("userid");
        if (StringUtils.isEmpty(user_name)) {
        log.info("统一登陆获取登陆的行员号为空");
        return null;
        }
        log.info("统一登陆获取登陆的行员号：{}", user_name);
        return user_name;
        } else {
        return null;
        }
        }
        log.info("ticket为空...");
        return null;
        }

        }
