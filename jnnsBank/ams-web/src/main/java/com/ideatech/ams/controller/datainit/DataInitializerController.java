package com.ideatech.ams.controller.datainit;

import com.ideatech.ams.system.Initializer.DataInitializerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author van
 * @date 16:11 2018/7/5
 */

@RestController
@RequestMapping(value = "/dataInit")
@Slf4j
public class DataInitializerController {

    @Autowired
    DataInitializerService dataInitializerService;

    @GetMapping("listUser")
    public Object listUser() {
        return str2Response(dataInitializerService.printUserList().toString(), "listUser.txt");
    }

    @GetMapping("listOrgan")
    public Object listOrgan() {
        return str2Response(dataInitializerService.printOrganList().toString(), "listOrgan.txt");
    }

    @GetMapping("listDictionary")
    public Object listDictionary() {
        return str2Response(dataInitializerService.printDictionaryList().toString(), "listDictionary.txt");
    }

    @GetMapping("listDicOption")
    public Object listDicOption() {
        return str2Response(dataInitializerService.printOptionList().toString(), "listDicOption.txt");
    }

    private ResponseEntity<byte[]> str2Response(String data, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(data.getBytes(), headers, HttpStatus.OK);
    }


}
