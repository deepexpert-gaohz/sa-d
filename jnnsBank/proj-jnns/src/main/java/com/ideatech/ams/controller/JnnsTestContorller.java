package com.ideatech.ams.controller;

import com.ideatech.ams.readData.service.ReadDataService;
import com.ideatech.ams.service.EtlService;
import com.ideatech.ams.service.JnnsSaicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 一些url方法测试controller
 *
 * @auther zoulang
 * @create 2019-06-15 4:18 PM
 **/
@RequestMapping("/jnnsTest")
@RestController
@Slf4j
public class JnnsTestContorller {

    @Autowired
    EtlService etlService;

    @Autowired
    JnnsSaicTestService jnnsSaicTestService;


    @Autowired
    ReadDataService readDataService;



    /**
     * 测试核心存量数据初始化
     */
    @GetMapping("/etlInitData")
    public void testEtlInitData() {
        etlService.doInitCore();
    }

    /**
     * 测试核心数据T+1更新状态
     */
    @GetMapping("/etlUpdateData")
    public void testEtlUpdateData() {
        etlService.douUpdateCoreData();
    }


    /**
     * 同步核心数据
     */
    @GetMapping("/etlCoreData")
    public void testEtlCodeData() {
        etlService.doCoreData();
    }



    /**
     * 汇法数据
     */
    @GetMapping("/huifa")
    public void testHuifa() {

        jnnsSaicTestService.doQuery();
    }


    /**
     * 变更预警
     */
    @GetMapping("/change")
    public void testChange() {
        log.info("----------------------------变更预警");
        readDataService.readData();
    }



}
