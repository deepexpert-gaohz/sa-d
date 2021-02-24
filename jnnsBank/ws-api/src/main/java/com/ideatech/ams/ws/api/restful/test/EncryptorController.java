package com.ideatech.ams.ws.api.restful.test;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vantoo
 * @date 2018-12-12 17:35
 */
@RestController
@RequestMapping("/api/test/enc")
public class EncryptorController {

    @Autowired
    StringEncryptor encryptor;

    @GetMapping("/encrypt")
    public Object encrypt(String... value) {
        JSONObject jsonObject = new JSONObject();
        for (String val : value) {
            jsonObject.put(val, encryptor.encrypt(val));
        }
        return jsonObject;
    }

}
